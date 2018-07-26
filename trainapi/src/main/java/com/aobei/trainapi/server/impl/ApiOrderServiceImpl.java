package com.aobei.trainapi.server.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.aobei.common.bean.SmsData;
import com.aobei.common.boot.EventPublisher;
import com.aobei.common.boot.event.SmsSendEvent;
import com.aobei.train.IdGenerator;
import com.aobei.train.Roles;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainapi.schema.Errors;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.schema.type.PageResult;
import com.aobei.trainapi.server.ApiOrderService;
import com.aobei.trainapi.server.PayService;
import com.aobei.trainapi.server.bean.AliPayClientMap;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.server.bean.StudentInfo;
import com.aobei.trainapi.server.handler.OnsHandler;
import com.aobei.trainapi.server.handler.PushHandler;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.bean.paymch.MchPayApp;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.util.PayUtil;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Component
public class ApiOrderServiceImpl implements ApiOrderService {
    Logger logger = LoggerFactory.getLogger(ApiOrderServiceImpl.class);
    @Autowired
    OrderService orderService;
    @Autowired
    CouponReceiveService couponReceiveService;
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ProductService productService;
    @Autowired
    ProSkuService proSkuService;
    @Autowired
    CustomerAddressService customerAddressService;
    @Autowired
    MetadataService metadataService;
    @Autowired
    ProductSoleService productSoleService;
    @Autowired
    StoreService storeService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    StationService stationService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    ServiceUnitService serviceUnitService;
    @Autowired
    ServiceunitPersonService serviceunitPersonService;
    @Autowired
    CouponService couponService;
    @Autowired
    CacheReloadHandler cacheReloadHandler;
    @Autowired
    PayService payService;
    @Autowired
    WxMchService wxMchService;
    @Autowired
    WxAppService wxAppService;
    @Autowired
    AliPayClientMap aliPayClientMap;
    @Autowired
    CouponEnvService couponEnvService;
    @Autowired
    EventPublisher publisher;
    @Autowired
    RobbingService robbingService;
    @Autowired
    PushHandler pushHandler;
    @Autowired
    OnsHandler onsHandler;
    @Override
    public void paySuccess(Order order, int paytype) {
        // 只需要更新order表中的订单支付状态表
        order.setStatus_active(Status.OrderStatus.wait_confirm.value);
        if (order.getProxyed().equals(1)) {
            order.setStatus_active(Status.OrderStatus.wait_service.value);

        }

        order.setPay_type(paytype);
        order.setPay_status(Status.PayStatus.payed.value);
        order.setPay_datetime(new Date());
        orderService.updateByPrimaryKeySelective(order);
        // 如果有优惠信息。去查看优惠券使用情况
        if (order.getDiscount_data() != null) {
            CouponReceiveExample couponReceiveExample = new CouponReceiveExample();
            couponReceiveExample.or()
                    .andPay_order_idEqualTo(order.getPay_order_id())
                    .andUidEqualTo(order.getUid())
                    .andStatusEqualTo(3)
                    .andDeletedEqualTo(Status.DeleteStatus.no.value);
            CouponReceive couponReceive = singleResult(couponReceiveService.selectByExample(couponReceiveExample));
            // 确认优惠券的使用
            if (couponReceive != null) {
                couponReceive.setStatus(4);
                couponReceiveService.updateByPrimaryKeySelective(couponReceive);
                //更新缓存
                cacheReloadHandler.couponListReload(order.getUid());
                cacheReloadHandler.userCouponListReload(order.getUid());
            }
        }

        // 添加订单日志
        Customer customer = customerService.selectByPrimaryKey(order.getUid());
        String cname = customer.getName()==null ? customer.getPhone():customer.getName();
        String logText = "用户【" + cname + "】进行了支付操作";
        orderLogService.xInsert(customer.getName(), customer.getUser_id(), order.getPay_order_id(), logText);

    }

    /*@Override
    public Station dispatch(Order order, ServiceUnit serviceUnit) {
        Station station = null;
        if (order.getProxyed().equals(0)) {
            station = getStation(order, serviceUnit);
            // 如果找不到任何合伙人，那么这一条订单无法分配了。
            if (station == null) {
                String text = "找不到对应的合伙人和工作站，订单不能正常分配！";
                orderLogService.xInsert("system", 0l, order.getPay_order_id(), text);
            }
        }
        // 更新服务单到等待合伙人确认状态
        ServiceUnit serviceUnit1 = new ServiceUnit();
        serviceUnit1.setActive(1);
        serviceUnit1.setStatus_active(station == null ? 1 : 2);
        if (order.getProxyed().equals(1)) {
            serviceUnit1.setStatus_active(4);
        }
        serviceUnit1.setStation_id(station == null ? null : station.getStation_id());
        serviceUnit1.setPartner_id(station == null ? null : station.getPartner_id());
        ServiceUnitExample example1 = new ServiceUnitExample();
        example1.or()
                .andPay_order_idEqualTo(order.getPay_order_id())
                .andGroup_tagEqualTo(serviceUnit.getGroup_tag());
        serviceUnitService.updateByExampleSelective(serviceUnit1, example1);
        if (order.getProxyed() == 1) {
            ServiceunitPersonExample serviceunitPersonExample = new ServiceunitPersonExample();
            serviceunitPersonExample.or()
                    .andServiceunit_idEqualTo(serviceUnit.getServiceunit_id());
            ServiceunitPerson serviceunitPerson = new ServiceunitPerson();
            serviceunitPerson.setStatus_active(Status.OrderStatus.wait_service.value);
            serviceunitPerson.setServiceunit_id(serviceUnit.getServiceunit_id());
            serviceunitPersonService.updateByExampleSelective(serviceunitPerson, serviceunitPersonExample);

        }

        return station;
    }


    private Station getStation(Order order, ServiceUnit serviceUnit) {

        Product product = productService.selectByPrimaryKey(serviceUnit.getProduct_id());
        ProSku proSku = proSkuService.selectByPrimaryKey(serviceUnit.getPsku_id());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.or()
                .andPay_order_idEqualTo(order.getPay_order_id());
        OrderItem orderItem = singleResult(orderItemService.selectByExample(orderItemExample));
        CustomerAddress customerAddress = customerAddressService.selectByPrimaryKey(order.getCustomer_address_id());
        Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_SEARCH_RADIUS);
        Integer integer = metadata.getMeta_value() == null ? Constant.SEARCH_RADIUS
                : Integer.parseInt(metadata.getMeta_value());
        ProductSoleExample soleExample = new ProductSoleExample();
        soleExample.or().andProduct_idEqualTo(product.getProduct_id());
        List<ProductSole> productSoles = productSoleService.selectByExample(soleExample);
        List<Partner> partners = new ArrayList<>();
        List<Station> stations = new ArrayList<>();
        List<Long> partner_ids = new ArrayList<>();
        //获取到能够提供服务的所有服务站
        if (productSoles.size() != 0) {// 绑定合伙人的产品
            partner_ids = productSoles.stream().map(n -> n.getPartner_id()).collect(Collectors.toList());
            StationExample stationExample = new StationExample();
            stationExample.or().andPartner_idIn(partner_ids)
                    .andCityEqualTo(customerAddress.getCity());
            stations = stationService.selectByExample(stationExample);
        } else {// 未绑定合伙人的产品
            stations = stationService.findNearbyStation(customerAddress, integer);
            stations = stationService.filterByProduct(product, stations);
        }
        partner_ids = stations.stream().map(n -> n.getPartner_id()).collect(Collectors.toList());
        PartnerExample partnerExample = new PartnerExample();
        if (partner_ids.size() > 0) {
            partnerExample.or().andPartner_idIn(partner_ids);
            partnerExample.setOrderByClause(PartnerExample.C.dispatch_priority.name());
            partners = partnerService.selectByExample(partnerExample);
        }
        //按照合伙人分组服务站
        Map<Long, List<Station>> stationMap = groupStationByPartnerId(stations);
        //按照优先级分组合伙人
        Map<Integer, List<Partner>> partnerMap = groupPartnerByPriority(partners);
        Set<Integer> set = partnerMap.keySet();
        for (Integer key : set) {
            List<Station> resultStations = new ArrayList<>();
            for (Partner partner : partnerMap.get(key)) {
                StationExample stationExample = new StationExample();
                stationExample.or()
                        .andPartner_idEqualTo(partner.getPartner_id());

                resultStations.addAll(stationMap.get(partner.getPartner_id()).stream()
                        .filter(t -> storeService.isStationHasStore(
                                t,
                                format.format(serviceUnit.getC_begin_datetime()),
                                product,
                                serviceUnit.getC_begin_datetime(),
                                serviceUnit.getC_end_datetime(),
                                proSku.getBuy_multiple_o2o() == 1 ? orderItem.getNum() : 1))
                        .collect(Collectors.toList()));
            }
            Station station = stationService.randomAStation(resultStations);
            if (station != null) {
                logger.info("api-method:ddispatch:getStation station:{}", station);
                return station;
            }
        }
        return null;

    }


    private Map<Long, List<Station>> groupStationByPartnerId(List<Station> stations) {
        Map<Long, List<Station>> stationMap = new HashMap<>();
        for (Station station1 : stations) {
            List<Station> t = new ArrayList<>();
            if (stationMap.get(station1.getPartner_id()) == null) {
                t.add(station1);
            } else {
                t = stationMap.get(station1.getPartner_id());
                t.add(station1);
            }
            stationMap.put(station1.getPartner_id(), t);
        }
        return stationMap;
    }

    private Map<Integer, List<Partner>> groupPartnerByPriority(List<Partner> partners) {
        Map<Integer, List<Partner>> partnerMap = new LinkedHashMap<>();
        partners.forEach(t -> {
            Integer priority = t.getDispatch_priority();
            List<Partner> tmp = new ArrayList<>();
            if (partnerMap.get(priority) == null) {
                tmp.add(t);
            } else {
                tmp = partnerMap.get(priority);
                tmp.add(t);
            }
            partnerMap.put(priority, tmp);
        });
        return partnerMap;
    }*/

    /**
     * 用户领取优惠券
     */
    @Override
    public ApiResponse getCounpons(Customer customer, Long coupon_id) {
        logger.info("api-method:getCounpons:params customer:{},coupon_id:{}", customer, coupon_id);
        ApiResponse response = new ApiResponse();
        Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
        if (coupon.getNum_limit() == 1 && coupon.getNum_able() < 1){
            response.setErrors(Errors._42029);
            return response;
        }
        if (coupon.getReceive_end_datetime().after(new Date()) && coupon.getType()==3) {
            if (coupon.getNum_limit() == 1) {
                CouponExample couponExample = new CouponExample();
                couponExample.or().andCoupon_idEqualTo(coupon_id)
                        .andNum_ableGreaterThan(0);
                Coupon updateCoupon = new Coupon();
                updateCoupon.setNum_able(coupon.getNum_able() - 1);
                int  count  = couponService.updateByExampleSelective(updateCoupon,couponExample);
                if (count==0){
                    response.setErrors(Errors._42029);
                    logger.info("api-method:getCounpons:process  the numAble is out of size");
                    return response;
                }
            }
            CouponReceiveExample couponReceiveExample  =new CouponReceiveExample();
            couponReceiveExample.or()
                                    .andCoupon_idEqualTo(coupon_id)
                                    .andUidEqualTo(customer.getCustomer_id())
                                    .andDeletedEqualTo(0);
            List<CouponReceive> couponReceives = couponReceiveService.selectByExample(couponReceiveExample);
            logger.info("api-method:getCounpons:process couponReceives:{}", couponReceives);
            //目前需求一个优惠券一个用户只可以领取一次
            if (couponReceives.size() == 0){
                CouponReceive couponReceive = new CouponReceive();
                couponReceive.setCoupon_receive_id(IdGenerator.generateId());
                couponReceive.setCoupon_id(coupon_id);
                couponReceive.setUid(customer.getCustomer_id());
                couponReceive.setReceive_datetime(new Date());
                couponReceive.setStatus(2);
                couponReceive.setVerification(0);
                couponReceive.setCreate_datetime(new Date());
                couponReceiveService.insertSelective(couponReceive);
            }else{
                response.setErrors(Errors._42029);
                return response;
            }
        } else {
            response.setErrors(Errors._42029);
            return response;
        }
        cacheReloadHandler.couponListReload(customer.getCustomer_id());
        cacheReloadHandler.userCouponListReload(customer.getCustomer_id());
        response.setMutationResult(new MutationResult());
        return response;
    }

    /**
     * 查询可领取优惠券列表(product_id 可以为空)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<CouponResponse> queryConponList(Customer customer, Long product_id, Integer page_index, Integer count) {
        logger.info("api-method:queryConponList:params customer:{},product_id:{},page_index:{},count:{}", customer, product_id,page_index,count);
        List<CouponResponse> couponResponses = new ArrayList<>();
        //第一步：顾虑满足指定商品的优惠券
        List<Coupon> coupons = queryList(page_index, count);
        coupons =  coupons.stream().filter(t->{
            if(t.getNum_able()<=0){
                return  false;
            }
            Condition_type condition = JSON.parseObject(t.getCondition(), Condition_type.class);
            if (t.getCondition_type()==1 || t.getCondition_type()==3){
              return   true;
            }else {
                if(condition.getList_product().contains(product_id)){
                      return  true;
                }
                return false;
            }
        }).collect(Collectors.toList());
        logger.info("api-method:queryConponList:process coupons:{}", coupons);
        //组织返回数据
        coupons.stream().forEach(t->{
            CouponResponse response = new CouponResponse();
            CouponReceiveExample couponReceiveExample = new CouponReceiveExample();
            CouponReceiveExample.Criteria criteria =  couponReceiveExample.or();
            criteria.andCoupon_idEqualTo(t.getCoupon_id());
            criteria.andDeletedEqualTo(0);
            if(customer!=null) {
                criteria.andUidEqualTo(customer.getCustomer_id());
            }else {
                criteria.andUidEqualTo(0L);
            }
            long couponCount = couponReceiveService.countByExample(couponReceiveExample);
            if(couponCount>0){
                response.setStatus(2);
            }else {
                response.setStatus(1);
            }
            Programme_type programme = JSON.parseObject(t.getProgramme(), Programme_type.class);
            int program_type =  t.getProgramme_type();
            switch (program_type) {
                case 1://已固定折扣
                    response.setValue(programme.getValue() * 10);
                    response.setUnit("折");
                    break;
                case 2://减固定价格
                    response.setValue(programme.getValue());
                    response.setUnit("元");
                    break;
            }
            if (new Date().after(t.getUse_end_datetime())) {
                response.setExpire(true);
            }
            response.setCoupon_id(t.getCoupon_id());
            response.setName(t.getName());
            response.setProgramme_type(t.getProgramme_type());
            response.setType(t.getType());
            couponResponses.add(response);
        });
        logger.info("api-method:queryConponList:process couponResponses:{}", couponResponses);
        return couponResponses;
    }



    /**
     * 可领取优惠券列表
     */
    public List<Coupon> queryList(Integer page_index, Integer count) {
        CouponExample couponExample = new CouponExample();
        couponExample.or()
                .andTypeEqualTo(3)
                .andReceive_end_datetimeGreaterThan(new Date())
                .andReceive_start_datetimeLessThan(new Date())
                .andValidEqualTo(1);
        Page<Coupon> page = couponService.selectByExample(couponExample, page_index, count);
        return page.getList();
    }

    /**
     * App微信支付，获取吊起支付参数
     *
     * @param customer
     * @param pay_order_id
     * @param path
     * @return
     */
    @Override
    public ApiResponse<MchPayApp> appWxPrePay(Customer customer, String pay_order_id, String path) {
        ApiResponse<MchPayApp> response = new ApiResponse<>();
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        if (order == null) {
            response.setErrors(Errors._41007);
            return response;
        }
        if (order.getExpire_datetime().before(new Date()) || order.getStatus_active() == 4) {
            response.setErrors(Errors._41018);
            return response;
        }
        if (Status.PayStatus.payed.value.equals(order.getPay_status())) {
            response.setErrors(Errors._41043);
            return response;
        }
        WxAppExample wxAppExample = new WxAppExample();
        wxAppExample.or().andPathEqualTo(path);
        WxApp wxApp = singleResult(wxAppService.selectByExample(wxAppExample));
        WxMch wxMch = wxMchService.selectByPrimaryKey(wxApp.getMch_id());
        logger.info("wxapp{},wxmch{}", wxApp, wxMch);
        String prepay_id = null;
        if (prepay_id == null) {
            UnifiedorderResult unifiedorderResult = payService.wxUnifiedorder(customer, order, wxApp, wxMch, path, null, "APP");
            logger.info("the result to unifiedorder msg1{}, msg2{}", unifiedorderResult.getReturn_msg(), unifiedorderResult.getReturn_msg());
            if (StringUtils.equals(unifiedorderResult.getReturn_code(), "FAIL")) {
                response.setErrors(Errors._41019);
                return response;
            }
            prepay_id = unifiedorderResult.getPrepay_id();
        }
        logger.info("api-method:appWxPrePay:process prepay_id:{}", prepay_id);
        MchPayApp mchPayApp = PayUtil.generateMchAppData(prepay_id, wxApp.getApp_id(), wxMch.getMch_id(), wxMch.getMch_key());
        response.setT(mchPayApp);
        return response;
    }

    /**
     *  v2 orderlist
     * @param status
     * @param customerId
     * @param nextId
     * @param preId
     * @param forward
     * @return
     */
    @Override
    public PageResult customerOrderList(String status,Long customerId, String nextId, String preId,boolean forward) {
        logger.info("api-method:params:status:{},customerId:{},nextId:{},preId{},forward:{}",status,customerId,nextId,preId,forward);
        //由后台定义了一页显示的条数。此处也可作为参数传递。理论值不应超过20
        //分页最小值不能小于10条。目前该方法如果条数设置为1或2.或出现数组越界异常。方法不能正常使用
        OrderExample orderExample = new OrderExample();
        OrderExample.Criteria criteria = orderExample.or();
        criteria.andUidEqualTo(customerId);
        // 组装条件
        OrderInfo.OrderStatus orderStatus = OrderInfo.OrderStatus.get(status);
        if (orderStatus != null) {
            switch (orderStatus) {
                case WAIT_PAY:
                    criteria.andPay_statusEqualTo(Status.PayStatus.wait_pay.value);
                    criteria.andStatus_activeEqualTo(Status.OrderStatus.wait_pay.value);
                    break;
                case PAYED:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    criteria.andStatus_activeEqualTo(Status.OrderStatus.wait_confirm.value);
                    break;
                case WAIT_SERVICE:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    List<Integer> list = new ArrayList<>();
                    list.add(Status.OrderStatus.wait_confirm.value);
                    list.add(Status.OrderStatus.wait_service.value);
                    criteria.andStatus_activeIn(list);
                    break;
                case CANCEL:
                    criteria.andStatus_activeEqualTo(Status.OrderStatus.cancel.value);
                    break;
                case DONE:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    criteria.andStatus_activeEqualTo(Status.OrderStatus.done.value);
                    break;
                case WAIT_REFUND:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    criteria.andR_statusEqualTo(Status.RefundStatus.wait_refund.value);
                    break;
                case REFUNDED:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    criteria.andR_statusEqualTo(Status.RefundStatus.refunded.value);
                    break;
                case PART_REFUND:
                    criteria.andPay_statusEqualTo(Status.PayStatus.payed.value);
                    criteria.andR_statusEqualTo(Status.RefundStatus.part_refunded.value);
                    break;
            }
        }else if(!StringUtils.equals("all",status)){
            return  null;
        }


        Long total  = orderService.countByExample(orderExample);

        Long count=10L;
        orderExample.limit(0L,count+1);
        if (StringUtils.isEmpty(nextId)) {
            nextId = Long.MAX_VALUE + "";
        }
        if(StringUtils.isEmpty(preId)){
            preId = "0";
        }
        PageResult page ;
        if (forward) {
            criteria.andPay_order_idLessThan(nextId);
            orderExample.setOrderByClause(OrderExample.C.pay_order_id + " DESC");
            page =  nextPage(orderExample,nextId,count);
        } else  {
            orderExample.setOrderByClause(OrderExample.C.pay_order_id+" ASC");

            criteria.andPay_order_idGreaterThan(preId);
            page = prePage(orderExample,count);
        }
        page.setTotal(total);
        return page;
    }

    @Override
    public ApiResponse<String> appAliPrePay(Customer customer, String pay_order_id, String appId) {
        ApiResponse<String> response = new ApiResponse<>();
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        if (order == null) {
            response.setErrors(Errors._41007);
            return response;
        }
        if (order.getExpire_datetime().before(new Date()) || order.getStatus_active() == 4) {
            response.setErrors(Errors._41018);
            return response;
        }
        if (Status.PayStatus.payed.value.equals(order.getPay_status())) {
            response.setErrors(Errors._41043);
            return response;
        }
        try {
           String body =  payService.aliPaymentBody(order,appId);
           response.setT(body);
        } catch (AlipayApiException e) {
            response.setErrors(Errors._41023);
            logger.error("api-method:error code:{},msg:{}",e.getErrCode(),e.getErrMsg());
        }

        return response;
    }

    //查找下一页
    private PageResult nextPage(OrderExample example,String nextId, Long count) {
        PageResult page = new PageResult();
        //倒序排列

        List<Order> orders = orderService.selectByExample(example);
        List<OrderInfo> infos = orders.stream().map(t -> {
            OrderInfo info = orderService.orderInfoDetail(Roles.CUSTOMER, t);
            return info;
        }).collect(Collectors.toList());
        if (infos.size() == count+1) {
            page.setNext_id(infos.get(infos.size() - 2).getPay_order_id());
            infos.remove(infos.size() - 1);
        }
        if (!(Long.MAX_VALUE + "").equals(nextId)) {
            page.setPre_id(infos.get(0).getPay_order_id());
        }
        page.setData(infos);
        page.setCount(infos.size());
        return page;
    }
    //查找上一页
    private PageResult prePage(OrderExample example, Long count) {
        PageResult page = new PageResult();

        List<Order> orders = orderService.selectByExample(example);
        List<OrderInfo> infos = orders.stream().map(t -> {
            OrderInfo info = orderService.orderInfoDetail(Roles.CUSTOMER, t);
            return info;
        }).collect(Collectors.toList());
        //结果需要翻转
        Collections.reverse(infos);
        if (infos.size() == count+1) {
            page.setPre_id(infos.get(1).getPay_order_id());
            infos.remove(0);
        }
        page.setNext_id(infos.get(infos.size() - 1).getPay_order_id());
        page.setData(infos);
        page.setCount(infos.size());

        return page;

    }

    /**
     * 兑换码兑换优惠券
     */
    @Transactional(timeout = 2)
    public ApiResponse exchangeCodeToCoupon(Customer customer,String exchange_code){
        logger.info("api-method:exchangeCodeToCoupon:params customer:{},exchange_code", customer,exchange_code);
        ApiResponse response = new ApiResponse();
        //根据兑换码id查询优惠券
        CouponReceiveExample receiveExample = new CouponReceiveExample();
        receiveExample.or().andExchange_codeEqualTo(exchange_code);
        CouponReceive couponReceive = singleResult(couponReceiveService.selectByExample(receiveExample));
        if (couponReceive == null || couponReceive.getStatus() != 1){
                response.setErrors(Errors._42030);
                return response;
        }
        Coupon coupon = couponService.selectByPrimaryKey(couponReceive.getCoupon_id());
        logger.info("api-method:exchangeCodeToCoupon:process coupon:{}", coupon);
        if (coupon.getReceive_end_datetime().after(new Date()) && coupon.getType()==4) {
                couponReceive.setUid(customer.getCustomer_id());
                couponReceive.setReceive_datetime(new Date());
                couponReceive.setStatus(2);
                couponReceive.setVerification(0);
                couponReceiveService.updateByPrimaryKeySelective(couponReceive);
        } else {
            response.setErrors(Errors._42029);
            return response;
        }
        cacheReloadHandler.couponListReload(customer.getCustomer_id());
        cacheReloadHandler.userCouponListReload(customer.getCustomer_id());
        response.setMutationResult(new MutationResult());
        return response;
    }

    @Override
    public ApiResponse<OrderPrice> recalculatePriceV2(Customer customer, Long psku_id, Long coupon_receive_id, Integer num) {
        logger.info("api-method:recalculatePriceV2:params customer:{},psku_id:{},coupon_receive_id:{},num:{}", customer, psku_id, coupon_receive_id, num);
        ApiResponse<OrderPrice> response = new ApiResponse<>();
        ProSku proSku = proSkuService.selectByPrimaryKey(psku_id);
        if (proSku == null) {
            response.setErrors(Errors._41006);
            return response;
        }
        if (num==null || num == 0) {
            if (proSku.getBuy_limit() == 1) {
              num  =  proSku.getBuy_multiple_min();
            }else {
               num = 1;
            }
        }
        Integer totalPrice = proSku.getPrice()*num;
        Integer payPrice = totalPrice;
        OrderPrice orderPrice = new OrderPrice();
        orderPrice.setTotalPrice(totalPrice);

        // 临时处理首单优惠的方案
        DiscountData discountData = couponEnvService.xDiscountPolicy(customer, proSku, num);
        if (discountData.isDiscount()) {
            payPrice = discountData.getPayPrice();
        }else if (coupon_receive_id != null && coupon_receive_id != 0) {
            discountData = couponService.recalculatePrice(customer, coupon_receive_id, proSku, num);
            if (discountData.isDiscount()) {
                payPrice = discountData.getPayPrice();
            }
        }
        orderPrice.setPayPrice(payPrice);
        orderPrice.setDiscountPrice(totalPrice - payPrice);
        response.setT(orderPrice);
        return response;
    }

    //发起抢单方法
    @Override
    public void startRobbing(Order order) {
        //如果订单不存在，或者订单已经在待服务状态了。不在进行抢单操作
        if (order == null
                || !Status.OrderStatus.wait_confirm.value.equals(order.getStatus_active())
                || order.getProxyed()==1)
            return;

        List<Integer> status = new ArrayList<>();
        status.add(Status.ServiceStatus.wait_partner_confirm.value);
        status.add(Status.ServiceStatus.reject.value);
        ServiceUnitExample example = new ServiceUnitExample();
        example.or().andPay_order_idEqualTo(order.getPay_order_id())
                .andActiveEqualTo(Status.ServiceUnitActive.active.value)
                .andStatus_activeIn(status)
                .andPidEqualTo(0l);
        ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(example));
        RobbingExample robbingExample = new RobbingExample();
        robbingExample.or().andServiceunit_idEqualTo(serviceUnit.getServiceunit_id());
        Long count = robbingService.countByExample(robbingExample);
        if (count > 0) {
            return;
        }

        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.or().andPay_order_idEqualTo(order.getPay_order_id());

        OrderItem orderItem = singleResult(orderItemService.selectByExample(orderItemExample));
        ProSku proSku = proSkuService.selectByPrimaryKey(orderItem.getPsku_id());
        Product product = productService.selectByPrimaryKey(orderItem.getProduct_id());
        if(product.getSole()==1){
            return;
        }
        Integer o2o = proSku.getBuy_multiple_o2o();

        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setLbs_lng(order.getLbs_lng());
        customerAddress.setLbs_lat(order.getLbs_lat());
        customerAddress.setCity(order.getCus_city());
        Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_SEARCH_RADIUS);
        Integer ratius = metadata.getMeta_value() == null ? Constant.SEARCH_RADIUS : Integer.parseInt(metadata.getMeta_value());
        metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_ROBBING_TIMEOUT);
        Integer robbingtimeout = metadata.getMeta_value() == null ? 30 : Integer.parseInt(metadata.getMeta_value());
        List<Station> stations = stationService.findNearByStation(customerAddress, ratius, product);

        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = format.format(serviceUnit.getC_begin_datetime());
        Set<Long> partnerSet = new HashSet<>();
        stations.stream().forEach(t ->
        {
            boolean isRobbing = false;
            int num = orderItem.getNum();
            if (o2o != 1) {
                num = 1;
            }
            Date now = new Date();
            Date expire = new Date(now.getTime() + robbingtimeout * 60 * 1000);
            if (storeService.isStationHasStore(t, dateTime,
                    product, serviceUnit.getC_begin_datetime(),
                    serviceUnit.getC_end_datetime(), num)) {
                Robbing robbing = new Robbing();

                robbing.setRobbing_id(IdGenerator.generateId());
                robbing.setCreate_datetime(now);
                robbing.setExpire_datetime(expire);
                robbing.setPartner_id(t.getPartner_id());
                robbing.setServiceunit_id(serviceUnit.getServiceunit_id());
                robbing.setActived(Status.RobbingActive.unactive.value);
                robbing.setStatus(Status.ValidStatus.valid.value);
                robbing.setUnique_key(serviceUnit.getServiceunit_id()+"_"+t.getPartner_id());
                try {
                    if (!partnerSet.contains(t.getPartner_id())) {
                        robbingService.insert(robbing);
                        isRobbing = true;
                        partnerSet.add(t.getPartner_id());
                    }
                }catch (Exception e ){
                    logger.error("ERROR startRobbing fail  ",e);
                }

            }
            if (isRobbing) {
                ServiceUnit updateUnit  = new ServiceUnit();
                updateUnit.setServiceunit_id(serviceUnit.getServiceunit_id());
                updateUnit.setRobbing(1);
                serviceUnitService.updateByPrimaryKeySelective(updateUnit);
                //发送抢单短信
                SmsData data = new SmsData();
                data.setSignName(Constant.SIGN_NAME);
                data.setTemplateCode(Constant.SEND_TO_PARTNER_WHEN_CUSTOMER_PAYED);
                data.addParam("product_name", order.getName());
                data.setPhoneNumber(new String[]{t.getPhone()});
                SmsSendEvent event = new SmsSendEvent(this, data);
                publisher.publish(event);
                //推送消息 抢单 通知到合伙人
                pushHandler.pushRobbingMessageToPartner(t.getPartner_id().toString());
                onsHandler.sendRobbingMessage(order.getPay_order_id(),1,expire.getTime());
            }
        });
    }

    /**
     * 服务人员重新计算订单价格
     * @param studentInfo
     * @param psku_id
     * @param num
     * @return
     */
    @Override
    public ApiResponse<OrderPrice> studentRecalculatePrice(StudentInfo studentInfo, Long psku_id, Integer num) {
        logger.info("api-method:studentRecalculatePrice:params studentInfo:{},psku_id:{},num:{}", studentInfo, psku_id, num);
        ApiResponse<OrderPrice> response = new ApiResponse<>();
        ProSku proSku = proSkuService.selectByPrimaryKey(psku_id);
        if (proSku == null) {
            response.setErrors(Errors._41006);
            return response;
        }
        if (num==null || num == 0) {
            if (proSku.getBuy_limit() == 1) {
                num  =  proSku.getBuy_multiple_min();
            }else {
                num = 1;
            }
        }
        Integer totalPrice = proSku.getPrice() * num;
        Integer payPrice = totalPrice;
        OrderPrice orderPrice = new OrderPrice();
        orderPrice.setTotalPrice(totalPrice);
        orderPrice.setPayPrice(payPrice);
        orderPrice.setDiscountPrice(0);
        response.setT(orderPrice);
        return response;
    }


}
