package com.aobei.trainconsole.configuration.onlinedtask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aobei.common.boot.RedisIdGenerator;
import com.aobei.train.IdGenerator;
import com.aobei.train.Roles;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainconsole.util.HttpAddressUtil;
import com.aobei.trainconsole.util.JdAuthTokenUtil;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.ItemInfo;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.OrderListResult;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.OrderSearchInfo;
import com.jd.open.api.sdk.domain.order.OrderQueryJsfService.UserInfo;
import com.jd.open.api.sdk.request.order.PopOrderSearchRequest;
import com.jd.open.api.sdk.response.order.PopOrderSearchResponse;
import custom.bean.JdToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by mr_bl on 2018/6/26.
 */
@Configuration
public class JdOrderGrab {

    private static final Logger logger = LoggerFactory.getLogger(JdOrderGrab.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String client_id = "F67A59B071284E359D8D12F1E4951567";

    private static final String client_secret = "ebd674ee3a134862b7b1061bf95e4969";

    private static final String server_url = "http://jdapi.aobei.com/routerjson";

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private JdOrderService jdOrderService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerAddressService customerAddressService;

    @Autowired
    private ServiceUnitService serviceUnitService;

    @Autowired
    private ProSkuService proSkuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisIdGenerator redisIdGenerator;

    @Autowired
    private Environment env;

    private String profile;

    @PostConstruct
    public void initProfile(){
        profile  = env.getActiveProfiles()[0];
    }

    /**
     * 定时抓取订单数据
     */
    @Scheduled(cron = "0 0/10 * * * ?")
//    @Scheduled(initialDelay = 2000, fixedDelay = 24*60*60*100)
    private void getJdOrder() {
        //保证单实例运行
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        RedisIdGenerator idGenerator = new RedisIdGenerator();
        idGenerator.setRedisTemplate(redisTemplate);
        long autoIncrId = idGenerator.getAutoIncrNum("JdOrderGrab"+sdf.format(new Date()));
        if (autoIncrId != 1){
            return;
        }

        JdToken jdToken = getToken();
        if (jdToken != null){
            JdClient client= new DefaultJdClient(server_url,jdToken.getAccess_token(),client_id,client_secret);
            PopOrderSearchRequest request=new PopOrderSearchRequest();
            Calendar now = Calendar.getInstance();
            Date nowTime = now.getTime();
            now.add(Calendar.MINUTE,-10);
            Date before10m = now.getTime();
            String endDate = sdf.format(nowTime) + ":00";
            String startDate = sdf.format(before10m) + ":00";
            logger.info("[OrderGrab] query time scope startDate is {},endDate is {}",startDate,endDate);
            request.setStartDate(startDate);
            request.setEndDate(endDate);
            request.setOrderState( "WAIT_SELLER_STOCK_OUT");
            request.setOptionalFields( "orderId," +  //	订单id
                    "venderId" +   //	商家id
                    "payType," +  //	支付方式（1货到付款, 2邮局汇款, 3自提, 4在线支付, 5公司转账, 6银行卡转账）
                    "orderTotalPrice," +  //	订单总金额。总金额=订单金额（不减优惠，不加运费服务费税费）
                    "orderSellerPrice," +  //	订单货款金额（订单总金额-商家优惠金额）
                    "orderPayment," +  //	用户应付金额。应付款=货款-用户优惠-余额+运费+税费+服务费。
                    "sellerDiscount," +  //	商家优惠金额
                    "orderState," +  //	1）WAIT_SELLER_STOCK_OUT 等待出库 2）WAIT_GOODS_RECEIVE_CONFIRM 等待确认收货 3）WAIT_SELLER_DELIVERY等待发货
                                            // （只适用于海外购商家，含义为'等待境内发货'标签下的订单,非海外购商家无需使用） 4) POP_ORDER_PAUSE POP暂停 5）FINISHED_L 完成
                                            //  6）TRADE_CANCELED 取消 7）LOCKED 已锁定 8）WAIT_SEND_CODE 等待发码（LOC订单特有状态）
                    "orderRemark," +  //	买家下单时订单备注
                    "orderStartTime," +  //		下单时间
                    "orderEndTime," +  //	结单时间 如返回信息为“0001-01-01 00:00:00”和“1970-01-01 00:00:00”，可认为此订单为未完成状态。
                    "consigneeInfo," +  //		收货人基本信息
                    "itemInfoList," +  //		商品详细信息
                    "couponDetailList," +  //	优惠详细信息
                    "venderRemark," +  //	商家订单备注（不大于500字符） 可选字段，需要在输入参数optional_fields中写入才能返回
                    "paymentConfirmTime," +  //	付款确认时间 如果没有付款时间 默认返回0001-01-01 00:00:00 可选字段，需要在输入参数optional_fields中写入才能返回
                    "modified");  //	订单更新时间
            request.setPage( "1" );
            request.setPageSize( "10" );
            request.setSortType( 1 );
            request.setDateType( 1 );
            try {
                PopOrderSearchResponse response = client.execute(request);
                if (response != null){
                    if ("".equals(response.getCode()) || response.getCode() == null){
                        logger.error("[OrderGrab] the response is not valid");
                    }else if ("0".equals(response.getCode())){
                        //System.out.println(JSON.toJSONString(response,true));
                        OrderListResult orderListResult = response.getSearchorderinfoResult();
                        if (orderListResult != null){
                            List<OrderSearchInfo> orderList
                                    = orderListResult.getOrderInfoList();
                            System.out.println(JSON.toJSONString(orderList,true));
                            if (orderList.size() > 0){
                                orderList.stream().forEach(n -> {
                                    UserInfo userInfo = n.getConsigneeInfo();
                                    CustomerExample customerExample = new CustomerExample();
                                    customerExample.or().andPhoneEqualTo(userInfo.getMobile());
                                    Customer customer = DataAccessUtils.singleResult(customerService.selectByExample(customerExample));
                                    if (customer == null) {
                                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                                        //新建user、customer
                                        Users users = new Users();
                                        users.setUser_id(IdGenerator.generateId());
                                        users.setUsername("JD" + userInfo.getFullname());
                                        users.setPassword(encoder.encode("123456"));
                                        users.setStatus((byte) 1);
                                        users.setRoles(Roles.CUSTOMER.roleName());
                                        users.setCreate_datetime(new Date());
                                        usersService.insertSelective(users);
                                        customer = new Customer();
                                        customer.setCustomer_id(IdGenerator.generateId());
                                        customer.setName(userInfo.getFullname());
                                        customer.setPhone(userInfo.getMobile());
                                        customer.setCreate_datetime(new Date());
                                        customer.setLocked(0);
                                        customer.setUser_id(users.getUser_id());
                                        customerService.insertSelective(customer);
                                    }
                                    insertCustomerAddress(customer, userInfo);
                                    JdOrder jdOrder = new JdOrder();
                                    BeanUtils.copyProperties(n, jdOrder);
                                    jdOrder.setPay_order_jd_id(n.getOrderId());
                                    jdOrder.setItemInfoList(JSON.toJSONString(n.getItemInfoList()));
                                    jdOrder.setCouponDetailList(JSON.toJSONString(n.getCouponDetailList()));
                                    jdOrder.setCustomer_id(customer.getCustomer_id());
                                    JdOrder queryLocal = jdOrderService.selectByPrimaryKey(n.getOrderId());
                                    if (queryLocal == null){
                                        jdOrderService.insertSelective(jdOrder);
                                    } else if (!jdOrder.getModified().equals(queryLocal.getModified())){
                                        jdOrderService.updateByPrimaryKeySelective(jdOrder);
                                    }
                                    OrderExample orderExample = new OrderExample();
                                    orderExample.or().andOut_order_idEqualTo(jdOrder.getPay_order_jd_id());
                                    if (queryLocal != null && orderService.countByExample(orderExample) > 0) {
                                        //已生成京东订单 、查看对应状态变更
                                        if (!jdOrder.getModified().equals(queryLocal.getModified())){
                                            //完成单，标记订单已完成和服务单已完成
                                            if (jdOrder.getOrderState().equals("FINISHED_L")){
                                                logger.info("[OrderGrab] update order to finished");
                                                List<Order> orders = orderService.selectByExample(orderExample);
                                                orders.stream().forEach(m ->{
                                                    Order upOrder = new Order();
                                                    upOrder.setPay_order_id(m.getPay_order_id());
                                                    upOrder.setStatus_active(5);
                                                    orderService.updateByPrimaryKeySelective(upOrder);

                                                    ServiceUnit upUnit = new ServiceUnit();
                                                    upUnit.setActive(0);
                                                    upUnit.setStatus_active(5);
                                                    upUnit.setFinish_datetime(new Date());
                                                    ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
                                                    serviceUnitExample.or().andPay_order_idEqualTo(m.getPay_order_id()).andActiveEqualTo(1);
                                                    serviceUnitService.updateByExampleSelective(upUnit,serviceUnitExample);

                                                    orderLogService.xInsert("SYSTEM", 0l, m.getPay_order_id(),
                                                            "用户[SYSTEM]将京东订单["+jdOrder.getPay_order_jd_id()+"],本地订单号["+m.getPay_order_id()+"]置为完成状态！");
                                                });
                                            }
                                        }
                                    } else {
                                        //生成本平台对应的京东订单
                                        initOrderByJdOrder(n,customer);
                                    }
                                });
                            }else{
                                //无订单数据
                                logger.info("[OrderGrab] the orderList's size is zero");
                            }
                        }else{
                            logger.error("[OrderGrab] the orderListResult is null");
                        }
                    }
                } else {
                    logger.error("[OrderGrab] the response is null");
                }
            } catch (JdException e) {
                logger.error(e.getErrCode()+"---------"+e.getMessage());
            }
        } else {
            logger.error("[OrderGrab] token获取失败！无法抓取订单数据");
        }
    }

    public JdToken getToken(){
        JdToken jdToken = null;
        String jd_token = redisTemplate.opsForValue().get("JD_Token");
        if (jd_token == null || "".equals(jd_token)){
            String refresh_token = redisTemplate.opsForValue().get("JD_Refresh_Token");
            try {
                jdToken = JdAuthTokenUtil.getTokenByRefreshToken(refresh_token);
                if (jdToken != null){
                    org.json.JSONObject jsonObject = new org.json.JSONObject(jdToken);
                    String json = jsonObject.toString();
                    redisTemplate.opsForValue().set("JD_Token",json);
                    redisTemplate.expire("JD_Token",354, TimeUnit.DAYS);
                    String refreshToken = jdToken.getRefresh_token();
                    redisTemplate.opsForValue().set("JD_Refresh_Token",refreshToken);
                }
            } catch (IOException e) {
                logger.error("refresh token failed");
            }
        } else {
            jdToken = JSONObject.parseObject(jd_token, JdToken.class);
        }
        return jdToken;
    }

    /**
     * 插入用户地址
     * @param customer
     * @param userInfo
     */
    public void insertCustomerAddress(Customer customer,UserInfo userInfo){
        CustomerAddressExample customerAddressExample = new CustomerAddressExample();
        customerAddressExample.or().andAddressEqualTo(userInfo.getFullAddress());
        if (customerAddressService.countByExample(customerAddressExample) > 0){
            return;
        }
        String lat = "";
        String lng = "";
        try {
            Map<String, String> addressMap = HttpAddressUtil.coordinate_GaoDe(userInfo.getFullAddress(), "");
            if(addressMap!=null){
                //经度
                lng = addressMap.get("lng_b");
                //纬度
                lat = addressMap.get("lat_b");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setCustomer_address_id(IdGenerator.generateId());
        customerAddress.setCustomer_id(customer.getCustomer_id());
        customerAddress.setUsername(userInfo.getFullname());
        customerAddress.setPhone(userInfo.getMobile());
        customerAddress.setAddress(userInfo.getFullAddress());
        customerAddress.setLbs_lat(lat);
        customerAddress.setLbs_lng(lng);
        customerAddress.setSub_address("");
        customerAddress.setCreate_datetime(new Date());
        customerAddress.setDefault_address(0);
        customerAddress.setLast_used(2);
        customerAddress.setDeleted(0);
        customerAddressService.insertSelective(customerAddress);
    }

    /**
     * 根据京东订单生成对应的local订单
     * @param orderSearchInfo
     */
    public void initOrderByJdOrder(OrderSearchInfo orderSearchInfo,Customer customer){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ItemInfo> itemInfoList = orderSearchInfo.getItemInfoList();
        UserInfo userInfo = orderSearchInfo.getConsigneeInfo();
        CustomerAddressExample customerAddressExample = new CustomerAddressExample();
        customerAddressExample.or()
                .andAddressEqualTo(userInfo.getFullAddress())
                .andCustomer_idEqualTo(customer.getCustomer_id());
        CustomerAddress customerAddress = DataAccessUtils.singleResult(customerAddressService.selectByExample(customerAddressExample));
        itemInfoList.stream().forEach(n ->{
            String skuId = n.getOuterSkuId();
            String nItemTotal = n.getItemTotal();
            String jdPrice = n.getJdPrice();
            if (!"".equals(skuId) && skuId != null){
                Order order = new Order();
                ProSku proSku = proSkuService.selectByPrimaryKey(Long.valueOf(skuId));
                if (proSku == null){
                    return;
                }
                Product product = productService.selectByPrimaryKey(proSku.getProduct_id());
                String pay_order_id = redisIdGenerator.generatorId("pay_order_id", 1000) + "";
                if ("dev".equals(profile)) {
                    pay_order_id = pay_order_id + "_1";
                } else if ("test".equals(profile)) {
                    pay_order_id = pay_order_id + "_2";
                }
                order.setPay_order_id(pay_order_id);

                logger.info("[OrderGrab initOrderByJdOrder] the local pay_order_id is {},out_order_id is {}",pay_order_id,orderSearchInfo.getOrderId());

                order.setName(product.getName()+proSku.getName());
                order.setUid(customer.getCustomer_id());
                order.setChannel("JD-001");
                order.setClient_id("eb_custom");
                order.setPrice_total((int)(Double.valueOf(jdPrice)*100*(Integer.valueOf(nItemTotal))));
                order.setPrice_discount(0);
                order.setPrice_pay((int)(Double.valueOf(jdPrice)*100*(Integer.valueOf(nItemTotal))));
                //order.setDiscount_data();
                order.setPay_type(3);
                order.setPay_status(1);
                order.setCreate_datetime(new Date());
                try {
                    order.setPay_datetime("".equals(orderSearchInfo.getPaymentConfirmTime())
                            || orderSearchInfo.getPaymentConfirmTime() == null ?
                            null : sdf.parse(orderSearchInfo.getPaymentConfirmTime()) );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                order.setCus_username(customerAddress.getUsername());
                order.setCustomer_address_id(customerAddress.getCustomer_address_id());
                order.setCus_phone(customerAddress.getPhone());
                order.setCus_address(customerAddress.getAddress());
                order.setLbs_lat(customerAddress.getLbs_lat());
                order.setLbs_lng(customerAddress.getLbs_lng());
                order.setRemark(orderSearchInfo.getOrderRemark());
                order.setStatus_active(2);
                order.setProxyed(0);
                order.setOut_order_id(orderSearchInfo.getOrderId());
                orderService.insertSelective(order);

                OrderItem orderItem = new OrderItem();
                orderItem.setPay_order_item_id(IdGenerator.generateId());
                orderItem.setPay_order_id(pay_order_id);
                orderItem.setProduct_id(product.getProduct_id());
                orderItem.setPsku_id(proSku.getPsku_id());
                orderItem.setPrice((int)(Double.valueOf(jdPrice)*100));
                orderItem.setNum(Integer.valueOf(nItemTotal));
                orderItemService.insert(orderItem);

                long groupTag = IdGenerator.generateId();
                ServiceUnit pUnit = new ServiceUnit();
                long punitId = IdGenerator.generateId();
                pUnit.setServiceunit_id(punitId);
                pUnit.setCustomer_id(customer.getCustomer_id());
                pUnit.setPay_order_id(pay_order_id);
                pUnit.setProduct_id(product.getProduct_id());
                pUnit.setPsku_id(proSku.getPsku_id());
                pUnit.setActive(0);
                pUnit.setPid(0l);
                pUnit.setGroup_tag(groupTag);
                pUnit.setStatus_active(1);
                serviceUnitService.insertSelective(pUnit);
                ServiceUnit subUnit = new ServiceUnit();
                BeanUtils.copyProperties(pUnit,subUnit);
                subUnit.setServiceunit_id(IdGenerator.generateId());
                subUnit.setPid(punitId);
                serviceUnitService.insertSelective(subUnit);

                orderLogService.xInsert("SYSTEM", 0l, pay_order_id,
                        "用户[SYSTEM]为用户"+customerAddress.getUsername()+"生成了京东订单["+orderSearchInfo.getOrderId()+"],本地订单号["+pay_order_id+"]。");
            }
        });

    }
}