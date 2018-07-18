package com.aobei.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.Roles;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.mapper.OrderMapper;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import custom.bean.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Service
public class OrderServiceImpl extends MbgServiceSupport<OrderMapper, String, Order, Order, OrderExample> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    ServiceUnitService serviceUnitService;
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    ProductService productService;
    @Autowired
    ProSkuService proSkuService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    PartnerService partnerService;
    @Autowired
    StudentService studentService;
    @Autowired
    CustomerAddressService customerAddressService;
    @Autowired
    ProEvaluateService proEvaluateService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CouponReceiveService couponReceiveService;
    @Autowired
    RefundService refundService;
    @Autowired
    MessageService messageService;
    @Autowired
    UsersService usersService;
    @Autowired
    DataDownloadService dataDownloadService;
    @Autowired
    MetadataService metadataService;
    @Autowired
    RobbingService robbingService;
    @Autowired
    ServiceunitPersonService serviceunitPersonService;
    @Autowired
    OrderService orderService;
    @Autowired
    CompensationService compensationService;
    @Autowired
    CompleteApplyService completeApplyService;
    @Autowired
    VOrderUnitService vOrderUnitService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    CacheReloadHandler cacheReloadHandler;
    @Autowired
    DeductMoneyService deductMoneyService;
    @Autowired
    FineMoneyService fineMoneyService;
    @Autowired
    CouponService couponService;

    @Autowired
    private ProductSoleService productSoleService;

    @Autowired
    private StationService stationService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory) {
        super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(orderMapper);
    }

    /**
     * @param product         产品
     * @param proSku          产品sku
     * @param customerAddress 用户地址
     * @param num             数量
     * @return Order
     */
    @Override
    public Order initOrder(Product product, ProSku proSku, CustomerAddress customerAddress, int num) {


        Order order = new Order();
        String customerId = customerAddress.getCustomer_id().toString();
//        String pay_order_id = IdGenerator.generateId() + "" +
//                customerId.substring(customerId.length() - 6, customerId.length());
//        order.setPay_order_id(pay_order_id);
        order.setName(product.getName() + proSku.getName());
        order.setPrice_total(proSku.getPrice());
        order.setPay_status(StatusConstant.PAY_STATUS_WAITPAY);
        order.setCustomer_address_id(customerAddress.getCustomer_address_id());
        order.setCus_address(customerAddress.getAddress() + customerAddress.getSub_address());
        order.setCus_phone(customerAddress.getPhone());
        order.setCus_username(customerAddress.getUsername());
        order.setCus_province(customerAddress.getProvince());
        order.setCus_city(customerAddress.getCity());
        order.setCus_area(customerAddress.getArea());
        order.setStatus_active(StatusConstant.ORDER_STATUS_WAITPAY);
        order.setUid(customerAddress.getCustomer_id());
        order.setLbs_lat(customerAddress.getLbs_lat());
        order.setLbs_lng(customerAddress.getLbs_lng());
        order.setProxyed(0);
        order.setGroup_tag(IdGenerator.generateId());
        Date create = new Date();
        Metadata metadata = metadataService.selectByPrimaryKey(Constant.MKEY_MAX_PAY_TIMEOUT);
        Integer integer = metadata.getMeta_value() == null ? Constant.ORDER_PAY_TIMEOUT : Integer.parseInt(metadata.getMeta_value());
        Date expire = new Date(create.getTime() + integer * 60 * 1000l);
        order.setCreate_datetime(new Date());
        order.setExpire_datetime(expire);
        return order;
    }

    /**
     * 首先根据角色筛选不同角色需要的订单。
     * TMANAGER：关心所有订单，任何人的订单。
     * PARTNER_USER：合伙人只关心分配给自己和曾经分配给自己的订单
     * STUDENT：服务人员只关心分配到自己的订单
     * CUSTOMER：顾客关心自己所有的订单，包含为支付的。
     *
     * @param roles   用户角色
     * @param example 订单的条件
     * @param page    页码
     * @param size    条数
     * @return
     */
    @Override
    public Page<OrderInfo> orderInfoList(Roles roles, OrderExample example, int page, int size) {
        Page<Order> orderPage = selectByExample(example, page, size);
        List<Order> orders = orderPage.getList();
        List<OrderInfo> orderInfos = new ArrayList<>();

        for (Order order : orders) {
            orderInfos.add(orderInfoDetail(roles, order));
        }

        Page<OrderInfo> pages = new Page<OrderInfo>(orderInfos, orderPage.getTotal(), orderPage.getPageNo(), orderPage.getPageSize());

        return pages;
    }

    //public Page<OrderInfo>


    /**
     * 重载方法，适合从服务单入手查看订单
     *
     * @param roles   用户角色
     * @param example 服务单条件
     * @param page    页码
     * @param size    条数
     * @return
     */
    @Override
    public Page<OrderInfo> orderInfoList(Roles roles, ServiceUnitExample example, int page, int size) {

        Page<ServiceUnit> serviceUnitPage = serviceUnitService.selectByExample(example, page, size);
        List<ServiceUnit> serviceUnits = serviceUnitPage.getList();
        List<OrderInfo> orderInfos = new ArrayList<>();
        for (ServiceUnit serviceUnit : serviceUnits) {
            OrderInfo info  = orderInfoDetail(roles, serviceUnit);
            info.getdServiceStartTime();
            orderInfos.add(info);
        }
        Page<OrderInfo> pages = new Page<OrderInfo>(orderInfos, serviceUnitPage.getTotal(), serviceUnitPage.getPageNo(), serviceUnitPage.getPageSize());
        return pages;
    }

    @Override
    public List<OrderInfo> orderInfoListWithOutPage(Roles roles, ServiceUnitExample example) {
        List<ServiceUnit> serviceUnits = serviceUnitService.selectByExample(example);
        List<OrderInfo> orderInfos = new ArrayList<>();
        for (ServiceUnit serviceUnit : serviceUnits) {
            OrderInfo info  = orderInfoDetail(roles, serviceUnit);
            info.getdServiceStartTime();
            orderInfos.add(info);
        }
        return orderInfos;
    }

    /**
     * @param roles 用户角色
     * @param order 订单
     * @return
     */
    @Override
    public OrderInfo orderInfoDetail(Roles roles, Order order) {
        OrderInfo orderInfo = new OrderInfo(roles);
        orderInfo.setOrder(order);
        //ServiceUnit 对象，每个角色都需要关心
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andPay_order_idEqualTo(order.getPay_order_id()).andPidEqualTo(0l);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        orderInfo.setServiceUnit(serviceUnit);
        orderInfo.setCustomer(customerService.selectByPrimaryKey(order.getUid()));
        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.or().andPay_order_idEqualTo(order.getPay_order_id());
        OrderItem orderItem = DataAccessUtils.singleResult(orderItemService.selectByExample(orderItemExample));
        orderInfo.setOrderItem(orderItem);
        Product product = productService.selectByPrimaryKey(serviceUnit.getProduct_id());
        orderInfo.setProduct(product);
        if (StatusConstant.PAY_STATUS_PAYED.equals(order.getPay_status())) {// 只有已支付的订单才涉及到评价
            ProEvaluateExample proEvaluateExample = new ProEvaluateExample();
            proEvaluateExample.or().andPay_order_idEqualTo(order.getPay_order_id());
            if (proEvaluateService.countByExample(proEvaluateExample) > 0) {
                orderInfo.setEvaluate(true);
            }
        }
        Partner partner = partnerService.selectByPrimaryKey(serviceUnit.getPartner_id());
        orderInfo.setPartner(partner);
        ServiceunitPersonExample serviceunitPersonExample = new ServiceunitPersonExample();
        serviceunitPersonExample.or().andServiceunit_idEqualTo(serviceUnit.getServiceunit_id());
        List<ServiceunitPerson> personList = serviceunitPersonService.selectByExample(serviceunitPersonExample);
//      orderInfo.setStudent(studentService.selectByPrimaryKey(serviceUnit.getStudent_id()));
        orderInfo.setStudents(personList);
        if (personList.size() > 0){
            ServiceunitPerson serviceunitPerson = personList.get(0);
            Student student = studentService.selectByPrimaryKey(serviceunitPerson.getStudent_id());
            orderInfo.setStudent(student);
        }
        orderInfo.setProSku(proSkuService.selectByPrimaryKey(serviceUnit.getPsku_id()));
        RobbingExample robbingExample = new RobbingExample();
        if (partner != null) {
            robbingExample.or().andServiceunit_idEqualTo(serviceUnit.getServiceunit_id()).andPartner_idEqualTo(partner.getPartner_id());
            List<Robbing> robbings = robbingService.selectByExample(robbingExample);
            if (robbings.size() >= 1){
                orderInfo.setRobbing(robbings.get(0));
            }
        }
        return orderInfo;
    }

    /**
     * @param roles       用户角色
     * @param serviceUnit 订单
     * @return
     */
    @Override
    public OrderInfo orderInfoDetail(Roles roles, ServiceUnit serviceUnit) {
        OrderInfo orderInfo = new OrderInfo(roles);
        orderInfo.setServiceUnit(serviceUnit);
        Partner partner = partnerService.selectByPrimaryKey(serviceUnit.getPartner_id());
        orderInfo.setPartner(partner);
        orderInfo.setProduct(productService.selectByPrimaryKey(serviceUnit.getProduct_id()));

        orderInfo.setProSku(proSkuService.selectByPrimaryKey(serviceUnit.getPsku_id()));
        Order order = orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
        orderInfo.setOrder(order);
        RobbingExample robbingExample = new RobbingExample();
        if (partner != null) {
            ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
            serviceUnitExample.or()
                    .andPay_order_idEqualTo(serviceUnit.getPay_order_id())
                    .andPidEqualTo(0l);
            ServiceUnit unit2 = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
            robbingExample.or()
            .andServiceunit_idEqualTo(unit2.getServiceunit_id())
            .andPartner_idEqualTo(partner.getPartner_id())
            .andStatusEqualTo(1);
            List<Robbing> list = robbingService.selectByExample(robbingExample);
            if (list.size() >= 1){
                orderInfo.setRobbing(list.get(0));
            }
            ServiceunitPersonExample serviceunitPersonExample = new ServiceunitPersonExample();
            serviceunitPersonExample.or().andServiceunit_idEqualTo(unit2.getServiceunit_id());
            List<ServiceunitPerson> personList = serviceunitPersonService.selectByExample(serviceunitPersonExample);
            orderInfo.setStudents(personList);
        }

        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.or().andPay_order_idEqualTo(serviceUnit.getPay_order_id());
        OrderItem orderItem = DataAccessUtils.singleResult(orderItemService.selectByExample(orderItemExample));
        orderInfo.setOrderItem(orderItem);
        return orderInfo;
    }

    /**
     * @param order_id 订单号
     * @return
     */
    @Transactional
    @Override
    public boolean xCancelOrderWithoutPay(String order_id, String remark) {

        Order order = selectByPrimaryKey(order_id);
        if (order == null) {
            return false;
        }
        if (StatusConstant.PAY_STATUS_PAYED.equals(order.getPay_status())) {
            return false;
        }
        if (StatusConstant.ORDER_STATUS_CANCLE.equals(order.getStatus_active())) {
            return false;
        }
        OrderExample example = new OrderExample();
        example.or()
                .andPay_order_idEqualTo(order_id);
        order.setStatus_active(StatusConstant.ORDER_STATUS_CANCLE);
        order.setRemark_cancel(remark);
        int count = updateByExampleSelective(order, example);
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        ServiceUnitExample.Criteria criteria = serviceUnitExample.or();
        criteria.andPay_order_idEqualTo(order.getPay_order_id());
        criteria.andPidEqualTo(0l);
        ServiceUnit serviceUnit2 = singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        criteria.andActiveEqualTo(StatusConstant.SERVICEUNIT_STATUS_ACTIVE);
        ServiceUnit serviceUnit = new ServiceUnit();
        serviceUnit.setActive(StatusConstant.SERVICEUNIT_STATUS_UNACTIVE);
        serviceUnitService.updateByExampleSelective(serviceUnit, serviceUnitExample);

        ServiceunitPersonExample serviceunitPersonExample = new ServiceunitPersonExample();
        serviceunitPersonExample.or().andServiceunit_idEqualTo(serviceUnit2.getServiceunit_id());
        List<ServiceunitPerson> personList = serviceunitPersonService.selectByExample(serviceunitPersonExample);
        if(personList.size() != 0){
            for (ServiceunitPerson serviceunitPerson : personList) {
                serviceunitPerson.setStatus_active(StatusConstant.ORDER_STATUS_CANCLE);
                serviceunitPersonService.updateByPrimaryKeySelective(serviceunitPerson);
                cacheReloadHandler.student_order_listReload(serviceunitPerson.getStudent_id());
            }
        }
        cacheReloadHandler.orderDetailReload(order.getUid(),order_id);
        cacheReloadHandler.orderListReload(order.getUid());
        if (count > 0) {
            CouponReceiveExample couponReceiveExample = new CouponReceiveExample();
            couponReceiveExample.or()
                    .andPay_order_idEqualTo(order_id)
                    .andStatusEqualTo(3)
                    .andDeletedEqualTo(Status.DeleteStatus.no.value);

            CouponReceive couponReceive = singleResult(couponReceiveService.selectByExample(couponReceiveExample));
            //回滚优惠券
            if (couponReceive != null) {
                if (couponReceive.getCoupon_env_id() != null && couponReceive.getCoupon_env_id() != 0) {
                    couponReceive.setDeleted(Status.DeleteStatus.yes.value);
                    couponReceive.setStatus(4);
                } else {
                    couponReceive.setPay_order_id(null);
                    couponReceive.setUse_datetime(null);
                    //回滚优惠券。优惠券回滚时如果优惠券已过期，要标记已过期
                    Coupon coupon  = couponService.selectByPrimaryKey(couponReceive.getCoupon_id());
                    if(new Date().after(coupon.getUse_end_datetime())){
                        couponReceive.setStatus(5);
                    }else {
                        couponReceive.setStatus(2);
                    }
                }

                couponReceiveService.updateByPrimaryKeySelective(couponReceive);
            }
            return true;
        }
        return false;
    }

    /**
     * @param customer      顾客
     * @param order         订单
     * @param remark_cancel 备注
     * @return
     */
    @Transactional
    @Override
    public boolean xCancelOrderPayedAndRefund(Customer customer, Order order, String remark_cancel,CancleStrategyMethod cancleStrategyMethod) {
        //更新订单表
        OrderExample orderExample = new OrderExample();
        orderExample.or().andPay_order_idEqualTo(order.getPay_order_id())
                .andUidEqualTo(customer.getCustomer_id());
        order.setStatus_active(Status.OrderStatus.cancel.value);
        order.setRemark_cancel(remark_cancel);
        if (cancleStrategyMethod.getFeePrice() != null){
            order.setR_fee(cancleStrategyMethod.getFeePrice());
        }
        order.setR_datetime(new Date());
        order.setR_status(Status.RefundStatus.wait_refund.value);

        //更新服务单表
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andPay_order_idEqualTo(order.getPay_order_id())
                .andPidEqualTo(0l);
        ServiceUnit serviceUnit = singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        ServiceUnitExample serviceUnitExample1 = new ServiceUnitExample();
        serviceUnitExample1.or().andGroup_tagEqualTo(serviceUnit.getGroup_tag());
        ServiceUnit serviceUnit1 = new ServiceUnit();
        serviceUnit1.setActive(StatusConstant.SERVICEUNIT_STATUS_UNACTIVE);
       
        //插入退款申请表
        Refund refund = new Refund();
        refund.setPay_order_id(order.getPay_order_id());
        refund.setRefund_id(IdGenerator.generateId());
        if (cancleStrategyMethod.getFeePrice() != null){
            refund.setFee(cancleStrategyMethod.getFeePrice());
        }
        refund.setApply_type(1);
        refund.setInfo(remark_cancel);
        refund.setR_user_id(customer.getUser_id());
        refund.setRtype(Type.RefundType.auto.value);
        refund.setStatus(Status.RefundStatus.wait_refund.value);
        refund.setCreate_date(new Date());
        refund.setCuname(order.getCus_username());
        refund.setUphone(order.getCus_phone());
        refund.setPrice_pay(order.getPrice_pay());
        refund.setPrice_total(order.getPrice_total());
        refund.setPartner_id(serviceUnit.getPartner_id());
        refund.setCus_address(order.getCus_address());
        refund.setOrder_name(order.getName());
        refund.setStudent_name(serviceUnit.getStudent_name());


        int count1 = orderService.updateByExampleSelective(order, orderExample);
        int count2 = serviceUnitService.updateByExampleSelective(serviceUnit1, serviceUnitExample1);
        int count3 = refundService.insertSelective(refund);
        if (count1 > 0 && count2 > 0 && count3 > 0)
            return true;
        return false;
    }

    /**
     * @param order_id   订单号
     * @param partner_id 合伙人ID
     * @return
     */
    @Override
    public boolean xRejectOrder(String order_id, Long partner_id, String remark,int regectType) {
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or()
                .andPay_order_idEqualTo(order_id)
                .andPartner_idEqualTo(partner_id)
                .andStatus_activeLessThanOrEqualTo(3)
                .andActiveEqualTo(1);
        List<ServiceUnit> list = serviceUnitService.selectByExample(serviceUnitExample);
        int count = 0;

        for (ServiceUnit unit : list) {
            ServiceUnit updateUnit  = new ServiceUnit();
            updateUnit.setServiceunit_id(unit.getServiceunit_id());
            updateUnit.setP_reject_remark(remark);
            updateUnit.setP_reject_datetime(new Date());
            updateUnit.setStatus_active(6);
            updateUnit.setReject_type(regectType);
            if (unit.getPid() != 0) {
                updateUnit.setActive(0);
            }
            count = serviceUnitService.updateByPrimaryKeySelective(updateUnit);
        }
        if (count > 0)
            return true;
        return false;
    }

    @Override
    @Transactional(timeout = 5)
    public int xCancelOrderBackstage(Order order) {
        // 让服务单状态变成 #无效# 状态
        ServiceUnit serviceUnit = new ServiceUnit();
        serviceUnit.setActive(StatusConstant.SERVICEUNIT_STATUS_UNACTIVE);
        serviceUnit.setStudent_name(null);
        ServiceUnitExample example = new ServiceUnitExample();
        example.or().andPay_order_idEqualTo(order.getPay_order_id());
        serviceUnitService.updateByExampleSelective(serviceUnit, example);

        return this.updateByPrimaryKeySelective(order);
    }

    @Override
    @Transactional(timeout = 5)
    public int applyRefund(String fee, String info, ServiceUnit unit, Users users) {
        String pay_order_id = unit.getPay_order_id();
        Order order_db = this.selectByPrimaryKey(pay_order_id);

        Order order = new Order();
        order.setPay_order_id(pay_order_id);
        order.setR_fee(Integer.valueOf(fee));//生成退款单时再加
        order.setR_status(Status.RefundStatus.wait_refund.value);
        order.setR_datetime(new Date());
        if (order_db.getStatus_active() != 4 && order_db.getStatus_active()!=5) {
            order.setStatus_active(4);
            xCancelOrderBackstage(order);
        } else {
            updateByPrimaryKeySelective(order);
        }

        //插入退款单数据
        Refund refund = new Refund();
        refund.setRefund_id(IdGenerator.generateId());
        refund.setPay_order_id(pay_order_id);
        refund.setFee(Integer.valueOf(fee));
        refund.setInfo(info);
        //refund.setRefund_date(refund_date);//退款日期
        refund.setCreate_date(new Date());
        refund.setStatus(Status.RefundStatus.wait_refund.value);//设置待退款
        refund.setRtype(Type.RefundType.system.value);
        refund.setUser_id(users.getUser_id());
        refund.setApply_type(2);
        refund.setOrder_name(order_db.getName());
        refund.setCuname(order_db.getCus_username());
        refund.setUphone(order_db.getCus_phone());
        refund.setCus_address(order_db.getCus_address());
        refund.setPrice_total(order_db.getPrice_total());
        refund.setPrice_pay(order_db.getPrice_pay());
        refund.setPartner_id(unit.getPartner_id());
        refund.setStudent_name(unit.getStudent_name());

        return refundService.insertSelective(refund);
    }

    @Override
    @Transactional(timeout = 5)
    public int xInsertCompensation(Compensation compensation) {
        if (compensation.getCoupon_id() != null){
            String pay_order_id = compensation.getPay_order_id();
            Order order = this.selectByPrimaryKey(pay_order_id);
            Long uid = order.getUid();
            CouponReceive couponReceive = new CouponReceive();
            couponReceive.setCoupon_receive_id(IdGenerator.generateId());
            couponReceive.setCoupon_id(compensation.getCoupon_id());
            couponReceive.setUid(uid);
            couponReceive.setReceive_datetime(new Date());
            couponReceive.setStatus(2);
            couponReceive.setVerification(0);
            couponReceive.setCreate_datetime(new Date());
            couponReceiveService.insertSelective(couponReceive);
        }
        compensation.setCompensation_id(IdGenerator.generateId());
        compensation.setCreate_datetime(new Date());
        compensation.setCompensation_status(1);
        return compensationService.insertSelective(compensation);
    }

    @Override
    @Transactional(timeout = 5)
    public int xInsertCompleteApply(CompleteApply completeApply) {
        return completeApplyService.insertSelective(completeApply);
    }

    @Override
    @Transactional(timeout = 5)
    public int xUpdateComfirmCompleteApply(CompleteApply completeApply,Users users) {
        Order order = new Order();
        ServiceUnit serviceUnit = new ServiceUnit();
        Order order_db = orderService.selectByPrimaryKey(completeApply.getPay_order_id());
        ServiceUnit unit_db = serviceUnitService.selectByPrimaryKey(completeApply.getServiceunit_id());
        order.setPay_order_id(order_db.getPay_order_id());
        order.setStatus_active(Status.OrderStatus.done.value);
        int i = orderService.updateByPrimaryKeySelective(order);
        serviceUnit.setServiceunit_id(unit_db.getServiceunit_id());
        serviceUnit.setActive(StatusConstant.SERVICEUNIT_STATUS_UNACTIVE);
        serviceUnit.setStatus_active(Status.ServiceStatus.done.value);
        int j = serviceUnitService.updateByPrimaryKeySelective(serviceUnit);
        int k = 0;
        if ( i>0 && j>0){
            CompleteApply upApply = new CompleteApply();
            upApply.setComplete_apply_id(completeApply.getComplete_apply_id());
            upApply.setConfirm_datetime(new Date());
            upApply.setConfirm_operator(users.getUser_id());
            upApply.setApply_status(2);
            k = completeApplyService.updateByPrimaryKeySelective(upApply);
        }
        return k;
    }

    @Override
    @Transactional(timeout = 5)
    public int xUpdateRejectCompleteApply(CompleteApply completeApply,Users users) {
        CompleteApply upApply = new CompleteApply();
        upApply.setApply_status(3);
        upApply.setComplete_apply_id(completeApply.getComplete_apply_id());
        return completeApplyService.updateByPrimaryKeySelective(upApply);
    }

    @Override
    @Transactional(timeout = 5)
    public int xInsertDeductMoney(DeductMoney deductMoney) {
        return deductMoneyService.insertSelective(deductMoney);
    }

    @Override
    @Transactional(timeout = 5)
    public int xUpdateComfirmDeductMoney(DeductMoney deductMoney, Users users) {
        DeductMoney deductMoneyUpdate = new DeductMoney();
        deductMoneyUpdate.setDeduct_status(2);
        deductMoneyUpdate.setDeduct_money_id(deductMoney.getDeduct_money_id());
        deductMoneyUpdate.setConfirm_datetime(new Date());
        deductMoneyUpdate.setConfirm_operator(users.getUser_id());
        return deductMoneyService.updateByPrimaryKeySelective(deductMoneyUpdate);
    }

    @Override
    @Transactional(timeout = 5)
    public int xUpdateRejectDeductMoney(DeductMoney deductMoney, Users users) {
        DeductMoney deductMoneyUpdate = new DeductMoney();
        deductMoneyUpdate.setDeduct_money_id(deductMoney.getDeduct_money_id());
        deductMoneyUpdate.setDeduct_status(3);
        return deductMoneyService.updateByPrimaryKeySelective(deductMoneyUpdate);
    }

    @Override
//    @Transactional(timeout = 10)
    public int changeOrder(String name, Long partner_id, Long station_id, String change_intro, Order order,
                           Date c_begin_datetime, Date c_end_datetime, ServiceUnit unit) {
        //所有unit先拒单
        ServiceUnit upUnit = new ServiceUnit();
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andPay_order_idEqualTo(order.getPay_order_id());
        upUnit.setActive(0);
        upUnit.setReject_type(2);
        upUnit.setStatus_active(6);
        serviceUnitService.updateByExampleSelective(upUnit,serviceUnitExample);

        UsersExample usersExample = new UsersExample();
        usersExample.or().andUsernameEqualTo(name);
        Users users = DataAccessUtils.singleResult(usersService.selectByExample(usersExample));
        orderLogService.xInsert(name, users.getUser_id(), order.getPay_order_id(),
                "用户【" + name + "】为客户进行了订单变更，变更说明：" + change_intro);

        long group_tag = IdGenerator.generateId();

        Remark remark = new Remark();
        remark.setD(new Date());
        remark.setOperator_name(name);
        remark.setRemark("用户【" + name + "】为客户进行了订单变更，变更说明：" + change_intro);
        remark.setUser_id(users.getUser_id());
        List<Remark> s = new ArrayList<>();
        s.add(remark);
        String json = JSONArray.toJSONString(s);

        ServiceUnitExample suex = new ServiceUnitExample();
        suex.or()
                .andPay_order_idEqualTo(order.getPay_order_id())
                .andPidNotEqualTo(0l)
                .andPartner_idEqualTo(partner_id);
        /** 传上来的  *partner_id*  对应的子单 */
        ServiceUnit sub_unit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(suex));

        if (sub_unit != null){
            sub_unit.setStation_id(station_id);
            sub_unit.setActive(1);
            sub_unit.setRemark(json);
            if (c_begin_datetime != null){
                sub_unit.setC_begin_datetime(c_begin_datetime);
            }
            if (c_end_datetime != null){
                sub_unit.setC_end_datetime(c_end_datetime);
            }
            sub_unit.setPid(unit.getServiceunit_id());
            sub_unit.setGroup_tag(group_tag);
            sub_unit.setStatus_active(2);
            sub_unit.setStudent_name(null);
            sub_unit.setReject_type(null);
            sub_unit.setP_assign_datetime(new Date());
            sub_unit.setP_confirm_datetime(null);
            sub_unit.setP_reject_remark(null);
            sub_unit.setP_reject_datetime(null);
            sub_unit.setP2s_assign_datetime(null);
            sub_unit.setWork_status(null);
            sub_unit.setWork_1_datetime(null);
            sub_unit.setWork_2_datetime(null);
            sub_unit.setWork_3_datetime(null);
            sub_unit.setWork_4_datetime(null);
            sub_unit.setWork_remark(null);
            ServiceUnitExample subup = new ServiceUnitExample();
            subup.or().andServiceunit_idEqualTo(sub_unit.getServiceunit_id());
            serviceUnitService.updateByExampleSelective(sub_unit,subup);
        }else{
            ServiceUnit newPar = new ServiceUnit();
            BeanUtils.copyProperties(unit,newPar);
            newPar.setServiceunit_id(IdGenerator.generateId());
            newPar.setPartner_id(partner_id);
            newPar.setStation_id(station_id);
            newPar.setActive(1);
            newPar.setRemark(json);
            if (c_begin_datetime != null){
                newPar.setC_begin_datetime(c_begin_datetime);
            }
            if (c_end_datetime != null){
                newPar.setC_end_datetime(c_end_datetime);
            }
            newPar.setPid(unit.getServiceunit_id());
            newPar.setGroup_tag(group_tag);
            newPar.setStatus_active(2);
            newPar.setStudent_name(null);
            newPar.setReject_type(null);
            newPar.setReject_type(null);
            newPar.setP_assign_datetime(new Date());
            newPar.setP_confirm_datetime(null);
            newPar.setP_reject_remark(null);
            newPar.setP_reject_datetime(null);
            newPar.setP2s_assign_datetime(null);
            newPar.setWork_status(null);
            newPar.setWork_1_datetime(null);
            newPar.setWork_2_datetime(null);
            newPar.setWork_3_datetime(null);
            newPar.setWork_4_datetime(null);
            newPar.setWork_remark(null);
            serviceUnitService.insertSelective(newPar);
        }
        unit.setPartner_id(partner_id);
        unit.setStation_id(station_id);
        unit.setActive(1);
        unit.setRemark(json);
        if (c_begin_datetime != null){
            unit.setC_begin_datetime(c_begin_datetime);
        }
        if (c_end_datetime != null){
            unit.setC_end_datetime(c_end_datetime);
        }
        unit.setGroup_tag(group_tag);
        unit.setStatus_active(2);
        unit.setStudent_name(null);
        unit.setReject_type(null);
        unit.setReject_type(null);
        unit.setP_assign_datetime(new Date());
        unit.setP_confirm_datetime(null);
        unit.setP_reject_remark(null);
        unit.setP_reject_datetime(null);
        unit.setP2s_assign_datetime(null);
        unit.setWork_status(null);
        unit.setWork_1_datetime(null);
        unit.setWork_2_datetime(null);
        unit.setWork_3_datetime(null);
        unit.setWork_4_datetime(null);
        unit.setWork_remark(null);
        ServiceUnitExample unitup = new ServiceUnitExample();
        unitup.or().andServiceunit_idEqualTo(unit.getServiceunit_id());
        int i = serviceUnitService.updateByExample(unit,unitup);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 新订单通知，2018-1-2 12:00 建国饭店1203.张先生，电话12312341234
        Message msg = new Message();
        msg.setId(IdGenerator.generateId());
        msg.setType(2);
        msg.setBis_type(3);
        msg.setUser_id(partner_id);
        msg.setUid(partner_id);
        msg.setMsg_title("订单通知");
        msg.setMsg_content(
                "你好，您有一条新的订单通知，于" + sdf.format(unit.getC_begin_datetime()) + " " + order.getCus_address() + "为"
                        + order.getCus_username() + "提供" + order.getName() + "服务，联系电话：" + order.getCus_phone());
        msg.setCreate_datetime(new Date());
        msg.setNotify_datetime(new Date(unit.getC_begin_datetime().getTime() - 3600 * 24 * 3 * 1000l));
        msg.setGroup_id(order.getPay_order_id() + "-" + partner_id + "-" + unit.getServiceunit_id());
        messageService.insertSelective(msg);
        return i;
    }

    @Override
    @Transactional
    public <T> T generateDownloadTaskAndPottingParam(String str, String username, long id, Class<T> clazz) {
        // 生成excel数据下载任务
        DataDownload dataDownload = new DataDownload();
        dataDownload.setData_download_id(id);
        Date date = new Date();
        dataDownload.setCreate_datetime(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int mouth = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String name = null;
        if (clazz.isInstance(new VOrderUnitExample())) {
            name = year + "-" + mouth + "-" + day + " 订单列表下载";
        } else if (clazz.isInstance(new RefundExample())) {
            name = year + "-" + mouth + "-" + day + " 退款订单列表下载";
        } else if (clazz.isInstance(new CompensationExample())){
            name = year + "-" + mouth + "-" + day + " 赔偿单列表下载";
        } else if (clazz.isInstance(new BillExample())){
            name = year + "-" + mouth + "-" + day + " 对账汇总列表下载";
        }else if(clazz.isInstance(new BalanceOrderExample())){
            name = year + "-" + mouth + "-" + day + " 订单完成结算单列表下载";
        }else if(clazz.isInstance(new FallintoRefundExample())){
            name = year + "-" + mouth + "-" + day + " 退款结算单列表下载";
        }else if(clazz.isInstance(new FallintoCompensationExample())){
            name = year + "-" + mouth + "-" + day + " 赔款结算单列表下载";
        }else if(clazz.isInstance(new FallintoDeductMoneyExample())){
            name = year + "-" + mouth + "-" + day + " 扣款结算单列表下载";
        }else if(clazz.isInstance(new FallintoFineMoneyExample())){
            name = year + "-" + mouth + "-" + day + " 罚款结算单列表下载";
        }else if(clazz.isInstance(new ServiceUnitExample())){
            name = year + "-" + mouth + "-" + day + " 拒单列表下载";
        }
        dataDownload.setName(name);
        dataDownload.setType(1);
        dataDownload.setParams(str);
        dataDownload.setStatus(0);
        UsersExample usersExample = new UsersExample();
        usersExample.or().andUsernameEqualTo(username);
        Users users = DataAccessUtils.singleResult(usersService.selectByExample(usersExample));
        dataDownload.setUser_id(users.getUser_id());
        dataDownloadService.insertSelective(dataDownload);

        // 将字符串json数据转换成json对象
        JSONObject resultJson = JSONObject.parseObject(str);
        // 将json对象按照map进行封装
        Map<String, Object> map = resultJson;

        if (clazz.isInstance(new VOrderUnitExample())) {
            // 获取查询参数
            String uname = (String) map.get("uname");
            String pay_order_id = (String) map.get("pay_order_id");
            String begin = (String) map.get("c_begin_datetime");
            String end = (String) map.get("c_end_datetime");
            Date c_begin_datetime = null;
            Date c_end_datetime = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (!("".equals(end))) {
                try {
                    c_begin_datetime = sdf.parse(begin + " 00:00:00");
                    c_end_datetime = sdf.parse(end + " 23:59:59");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String qs_create = (String)map.get("qs_create_time");
            String qe_create = (String)map.get("qe_create_time");
            Date qs_create_time = null;
            Date qe_create_time = null;
            String qs_pay = (String)map.get("qs_pay_time");
            String qe_pay = (String)map.get("qe_pay_time");
            if (!("".equals(qs_create)) && !("".equals(qe_create))){
                try {
                    qs_create_time = sdf.parse(qs_create + " 00:00:00");
                    qe_create_time = sdf.parse(qe_create + " 23:59:59");
                }catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Date qs_pay_time = null;
            Date qe_pay_time = null;
            if (!("".equals(qs_pay)) && !("".equals(qe_pay))){
                try {
                    qs_pay_time = sdf.parse(qs_pay  + " 00:00:00");
                    qe_pay_time = sdf.parse(qe_pay  + " 23:59:59");
                }catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String cuname = (String) map.get("cuname");
            String uphone = (String) map.get("uphone");
            String student_name = (String) map.get("student_name");
            String par_id = (String) map.get("partner_id");
            Long partner_id = null;
            if (!("".equals(par_id))) {
                partner_id = Long.valueOf(par_id);
            }
            String sta = (String) map.get("statu");
            Integer statu = null;
            if (!("".equals(sta))) {
                statu = Integer.valueOf(sta);
            }

            VOrderUnitExample orderUnitExample = new VOrderUnitExample();
            orderUnitExample.setOrderByClause(VOrderUnitExample.C.create_datetime + " desc");
            VOrderUnitExample.Criteria or = orderUnitExample.or();
            or.andPay_order_idLessThan(Integer.MAX_VALUE + "");
            if (statu != null) {
                if (statu < 10) {
                    if (statu == 2) {
                        or.andStatus_activeNotEqualTo(6);
                    }
                    if (statu == 4) {
                        or.andR_statusIn(Arrays.asList(new Integer[]{-1,0,1,3}));
                    }
                    or.andO_status_activeEqualTo(statu);
                } else {
                    Integer o_statu = statu / 10;
                    if (statu < 30) {
                        Integer s_statu = statu % 10;
                        or.andStatus_activeEqualTo(s_statu);
                        or.andO_status_activeEqualTo(o_statu);
                    } else {
                        Integer r_statu = statu % 10;
                        or.andO_status_activeEqualTo(o_statu);
                        or.andR_statusEqualTo(r_statu);
                    }
                }
            }
            if (!("".equals(uname)) && uname != null) {
                uname = uname.trim();
                or.andUnameLike("%" + uname + "%");
            }
            if (!("".equals(pay_order_id)) && pay_order_id != null) {
                pay_order_id = pay_order_id.trim();
                or.andPay_order_idEqualTo(pay_order_id);
            }
            if (!("".equals(cuname)) && cuname != null) {
                cuname = cuname.trim();
                or.andCus_usernameLike("%" + cuname + "%");
            }
            if (!("".equals(uphone)) && uphone != null) {
                uphone = uphone.trim();
                or.andCus_phoneLike("%" + uphone + "%");
            }

            if (c_begin_datetime != null) {
                or.andC_begin_datetimeGreaterThanOrEqualTo(c_begin_datetime);
            }
            if (c_end_datetime != null) {
                or.andC_end_datetimeLessThanOrEqualTo(c_end_datetime);
            }
            if (!("".equals(student_name)) && student_name != null) {
                student_name = student_name.trim();
                or.andStudent_nameLike("%" + student_name + "%");
            }
            if (partner_id != null) {
                or.andPartner_idEqualTo(partner_id);
            }
            if (qs_create_time != null && qe_create_time != null){
                or.andCreate_datetimeBetween(qs_create_time,qe_create_time);
            }
            if (qs_pay_time != null && qe_pay_time != null){
                or.andPay_datetimeBetween(qs_pay_time,qe_pay_time);
            }
            return (T) orderUnitExample;
        } else if (clazz.isInstance(new RefundExample())) {
            // 获取查询参数
            String pay_order_id = (String) map.get("pay_order_id");
            String begin = (String) map.get("begin_date");
            String end = (String) map.get("end_date");
            Date begin_date = null;
            Date end_date = null;
            if (!("".equals(end))) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    begin_date = sdf.parse(begin);
                    end_date = sdf.parse(end);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String cuname = (String) map.get("cuname");
            String uphone = (String) map.get("uphone");
            String student_name = (String) map.get("student_name");
            String par_id = (String) map.get("partner_id");
            Long partner_id = null;
            if (!("".equals(par_id))) {
                partner_id = Long.valueOf(par_id);
            }
            String sta = (String) map.get("status");
            Integer status = null;
            if (!("".equals(sta))) {
                status = Integer.valueOf(sta);
            }

            RefundExample refundExample = new RefundExample();
            refundExample.setOrderByClause(RefundExample.C.create_date + " desc");
            RefundExample.Criteria or = refundExample.or();

            //拼接查询参数
            if (status != null) {
                or.andStatusEqualTo(status);
            }
            if (!("".equals(pay_order_id)) && pay_order_id != null) {
                pay_order_id = pay_order_id.trim();
                or.andPay_order_idEqualTo(pay_order_id);
            }
            if (!("".equals(cuname)) && cuname != null) {
                cuname = cuname.trim();
                or.andCunameLike("%" + cuname + "%");
            }
            if (!("".equals(uphone)) && uphone != null) {
                uphone = uphone.trim();
                or.andUphoneLike("%" + uphone + "%");
            }

            if (begin_date != null) {
                or.andCreate_dateGreaterThanOrEqualTo(begin_date);
            }
            if (end_date != null) {
                or.andCreate_dateLessThanOrEqualTo(end_date);
            }
            if (!("".equals(student_name)) && student_name != null) {
                student_name = student_name.trim();
                or.andStudent_nameLike("%" + student_name + "%");
            }
            if (partner_id != null) {
                or.andPartner_idEqualTo(partner_id);
            }
            return (T) refundExample;
        } else if (clazz.isInstance(new CompensationExample())) {
            // 获取查询参数
            String pay_order_id = (String) map.get("pay_order_id");
            String begin = (String) map.get("q_begin_datetime");
            String end = (String) map.get("q_end_datetime");
            Date q_begin_datetime = null;
            Date q_end_datetime = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (!("".equals(end))) {
                try {
                    q_begin_datetime = sdf.parse(begin);
                    q_end_datetime = sdf.parse(end);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String cuname = (String) map.get("cuname");
            String uphone = (String) map.get("uphone");
            String student_name = (String) map.get("student_name");
            String par_id = (String) map.get("partner_id");
            Long partner_id = null;
            if (!("".equals(par_id))) {
                partner_id = Long.valueOf(par_id);
            }
            String sta = (String) map.get("compensation_status");
            Integer compensation_status = null;
            if (!("".equals(sta))) {
                compensation_status = Integer.valueOf(sta);
            }

            VOrderUnitExample orderUnitExample = new VOrderUnitExample();
            orderUnitExample.setOrderByClause(VOrderUnitExample.C.create_datetime + " desc");
            //查询条件的对象
            VOrderUnitExample.Criteria or = orderUnitExample.or();
            or.andPay_order_idLessThan(Integer.MAX_VALUE + "");
            //拼接查询参数
            if (!("".equals(pay_order_id)) && pay_order_id != null) {
                pay_order_id = pay_order_id.trim();
                or.andPay_order_idEqualTo(pay_order_id);
            }
            if (!("".equals(cuname)) && cuname != null) {
                cuname = cuname.trim();
                or.andCus_usernameLike("%" + cuname + "%");
            }
            if (!("".equals(uphone)) && uphone != null) {
                uphone = uphone.trim();
                or.andCus_phoneLike("%" + uphone + "%");
            }
            if (!("".equals(student_name)) && student_name != null) {
                student_name = student_name.trim();
                or.andStudent_nameLike("%" + student_name + "%");
            }
            if (partner_id != null) {
                or.andPartner_idEqualTo(partner_id);
            }

            CompensationExample compensationExample = new CompensationExample();
            compensationExample.setOrderByClause(CompensationExample.C.create_datetime + " desc");
            CompensationExample.Criteria criteria = compensationExample.or();
            if (q_begin_datetime != null && q_end_datetime != null) {
                criteria.andCreate_datetimeBetween(q_begin_datetime,q_end_datetime);
            }
            if (partner_id != null) {
                criteria.andPartner_idEqualTo(partner_id);
            }
            if (compensation_status != null) {
                criteria.andCompensation_statusEqualTo(compensation_status);
            }
            if (!("".equals(pay_order_id)) && pay_order_id != null) {
                pay_order_id = pay_order_id.trim();
                criteria.andPay_order_idEqualTo(pay_order_id);
            }

            List<VOrderUnit> orderInfos = new ArrayList<>();
            List<String> order_ids = new ArrayList<>();
            if (or.getCriteria().size() > 1){
                orderInfos = vOrderUnitService.selectByExample(orderUnitExample);
                if (orderInfos.size() > 0){
                    order_ids = orderInfos.stream().map(n -> n.getPay_order_id()).collect(Collectors.toList());
                    criteria.andPay_order_idIn(order_ids);
                }else{
                    criteria.andPay_order_idIsNull();
                }
            }
            return (T)compensationExample;
        }else if (clazz.isInstance(new BillExample())) {
            String bill_batch_id = (String)map.get("bill_batch_id");
            String pay_order_id = (String)map.get("pay_order_id");
            Date s_transaction_datetime = null;
            Date e_transaction_datetime = null;
            Date qs_create_time = null;
            Date qe_create_time = null;
            String s_transaction = (String)map.get("s_transaction_datetime");
            String e_transaction = (String)map.get("e_transaction_datetime");
            String qs_create = (String)map.get("qs_create_time");
            String qe_create = (String)map.get("qe_create_time");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (!("".equals(e_transaction))) {
                try {
                    s_transaction_datetime = sdf.parse(s_transaction);
                    e_transaction_datetime = sdf.parse(e_transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (!("".equals(qe_create))) {
                try {
                    qs_create_time = sdf.parse(qs_create);
                    qe_create_time = sdf.parse(qe_create);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String type = (String) map.get("bill_type");
            Integer bill_type = null;
            if (!("".equals(type))) {
                bill_type = Integer.valueOf(type);
            }

            BillExample billExample = new BillExample();
            billExample.setOrderByClause(BillExample.C.create_datetime + " desc");
            BillExample.Criteria or = billExample.or();
            or.andBill_statusEqualTo(1);
            if (bill_batch_id != null && !"".equals(bill_batch_id)){
                or.andBill_batch_idEqualTo(bill_batch_id);
            }
            if (pay_order_id != null && !"".equals(pay_order_id)){
                or.andPay_order_idEqualTo(pay_order_id);
            }
            if (bill_type != null){
                or.andBill_typeEqualTo(bill_type);
            }
            if (s_transaction_datetime != null && e_transaction_datetime != null){
                or.andTransaction_datetimeBetween(s_transaction_datetime,e_transaction_datetime);
            }
            if (qs_create_time != null && qe_create_time != null){
                or.andCreate_datetimeBetween(qs_create_time,qe_create_time);
            }
            return (T)billExample;
        }else if(clazz.isInstance(new BalanceOrderExample())){
            BalanceOrderExample balanceExample = new BalanceOrderExample();
           /* BalanceOrderExample.Criteria or = balanceExample.or();
            if(map!=null){
                String balance_cycle = (String) map.get("balance_cycle");//结算期模糊检索条件
                if(!"".equals(balance_cycle)){
                    or.andBalance_cycleEqualTo(balance_cycle);
                }
            }*/

            return (T)balanceExample;
        }else if(clazz.isInstance(new FallintoRefundExample())) {
            FallintoRefundExample fallintoRefundExample = new FallintoRefundExample();
//            FallintoRefundExample.Criteria or = fallintoRefundExample.or();
//            if (map != null){
//                String balance_cycle = (String) map.get("balance_cycle");//结算期模糊检索条件
//                if (!"".equals(balance_cycle)) {
//                    or.andBalance_cycleEqualTo(balance_cycle);
//                }
//            }
            return (T)fallintoRefundExample;
        }else if(clazz.isInstance(new FallintoCompensationExample())) {
            FallintoCompensationExample fallintoCompensationExample = new FallintoCompensationExample();
            return (T)fallintoCompensationExample;
        }else if(clazz.isInstance(new FallintoDeductMoneyExample())) {
            FallintoDeductMoneyExample fallintoDeductMoneyExample = new FallintoDeductMoneyExample();
            return (T)fallintoDeductMoneyExample;
        }else if(clazz.isInstance(new FallintoFineMoneyExample())) {
            FallintoFineMoneyExample fallintoFineMoneyExample = new FallintoFineMoneyExample();
            return (T)fallintoFineMoneyExample;
        }else if(clazz.isInstance(new ServiceUnitExample())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String qs_create = (String)map.get("qs_create_time");
            String qe_create = (String)map.get("qe_create_time");
            Date qs_create_time = null;
            Date qe_create_time = null;
            String qs_pay = (String)map.get("qs_pay_time");
            String qe_pay = (String)map.get("qe_pay_time");
            if (!("".equals(qs_create)) && !("".equals(qe_create))){
                try {
                    qs_create_time = sdf.parse(qs_create + " 00:00:00");
                    qe_create_time = sdf.parse(qe_create + " 23:59:59");
                }catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Date qs_pay_time = null;
            Date qe_pay_time = null;
            if (!("".equals(qs_pay)) && !("".equals(qe_pay))){
                try {
                    qs_pay_time = sdf.parse(qs_pay  + " 00:00:00");
                    qe_pay_time = sdf.parse(qe_pay  + " 23:59:59");
                }catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String par_id = (String) map.get("partner_id");
            Long partner_id = null;
            if (!("".equals(par_id))) {
                partner_id = Long.valueOf(par_id);
            }
            OrderExample orderExample = new OrderExample();
            OrderExample.Criteria or = orderExample.or();
            if (!("".equals(qe_create_time)) && qe_create_time != null) {
                or.andCreate_datetimeBetween(qs_create_time,qe_create_time);
            }
            if (!("".equals(qe_pay_time)) && qe_pay_time != null) {
                or.andPay_datetimeBetween(qs_pay_time,qe_pay_time);
            }
            ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
            ServiceUnitExample.Criteria criteria = serviceUnitExample.or();
            criteria.andStatus_activeEqualTo(6).andPidNotEqualTo(0l).andActiveEqualTo(0);
            if (or.getCriteria().size() > 0){
                List<Order> orders = orderService.selectByExample(orderExample);
                List<String> orderids = orders.stream().map(n -> n.getPay_order_id()).collect(Collectors.toList());
                if (orderids.size() > 0){
                    criteria.andPay_order_idIn(orderids);
                }
            }
            if (partner_id != null) {
                criteria.andPartner_idEqualTo(partner_id);
            }
            return (T)serviceUnitExample;
        }else {
            return null;
        }
    }

    @Override
    @Transactional(timeout = 5)
    public int xInsertFineMoney(FineMoney fineMoney) {
        return fineMoneyService.insertSelective(fineMoney);
    }

    @Override
    @Transactional(timeout = 5)
    public int xUpdateComfirmFineMoney(FineMoney fineMoney, Users users) {
        FineMoney fineMoneyup = new FineMoney();
        fineMoneyup.setFine_money_id(fineMoney.getFine_money_id());
        fineMoneyup.setFine_status(2);
        fineMoneyup.setConfirm_datetime(new Date());
        fineMoneyup.setConfirm_operator(users.getUser_id());
        return fineMoneyService.updateByPrimaryKeySelective(fineMoneyup);
    }

    @Override
    @Transactional(timeout = 5)
    public int xUpdateRejectFineMoney(FineMoney fineMoney, Users users) {
        FineMoney fineMoneyup = new FineMoney();
        fineMoneyup.setFine_money_id(fineMoney.getFine_money_id());
        fineMoneyup.setFine_status(3);
        return fineMoneyService.updateByPrimaryKeySelective(fineMoneyup);
    }

    @Override
    @Transactional
    public Station dispatchOrder(Order order, ServiceUnit serviceUnit) {
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
        ServiceUnit upUnit = new ServiceUnit();
        upUnit.setActive(1);
        upUnit.setStatus_active(station == null ? 1 : 2);
        if (order.getProxyed().equals(1)) {
            upUnit.setStatus_active(4);
        }
        upUnit.setStation_id(station == null ? null : station.getStation_id());
        upUnit.setPartner_id(station == null ? null : station.getPartner_id());
        ServiceUnitExample example = new ServiceUnitExample();
        example.or()
                .andPay_order_idEqualTo(order.getPay_order_id())
                .andGroup_tagEqualTo(serviceUnit.getGroup_tag());
        serviceUnitService.updateByExampleSelective(upUnit, example);
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
            StationExample.Criteria criteria = stationExample.or();
            criteria.andPartner_idIn(partner_ids);
            if (customerAddress.getCity() != null){
                criteria.andCityEqualTo(customerAddress.getCity());
            }
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
    }

}