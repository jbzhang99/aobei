package com.aobei.trainconsole.controller.test;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.StepData;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xk873 on 2018/5/21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class Test {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServiceUnitService serviceUnitService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private FallintoService fallintoService;

    @Autowired
    private BalanceOrderService balanceOrderService;

    @Autowired
    private DeductMoneyService deductMoneyService;

    @Autowired
    private PartnerFallintoService partnerFallintoService;

    @Autowired
    private FallintoRefundService fallintoRefundService;


    @Autowired
    private FallintoCompensationService fallintoCompensationService;

    @Autowired
    private CompensationService compensationService;

    @Autowired
    private FallintoDeductMoneyService fallintoDeductMoneyService;

    @Autowired
    private FallintoFineMoneyService fallintoFineMoneyService;

    @Autowired
    private FineMoneyService fineMoneyService;

    /**
     * 罚款测试
     */
    @org.junit.Test
    public void fineMoney(){
        LocalDate localDate = LocalDate.now().minusMonths(1);
        // 取本月第1天：
        LocalDate firstDayOfThisMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = firstDayOfThisMonth.atStartOfDay(zoneId);
        Date firstDate = Date.from(zdt.toInstant());
        // 取本月第15天
        LocalDate secondDayOfThisMonth = localDate.withDayOfMonth(15);
        LocalDateTime localDateTime = secondDayOfThisMonth.atTime(23, 59, 59);
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date endDate = Date.from(instant);

        FineMoneyExample fineMoneyExample = new FineMoneyExample();
        fineMoneyExample.or().andConfirm_datetimeBetween(firstDate,endDate);
        List<FineMoney> fineMonies = fineMoneyService.selectByExample(fineMoneyExample);
        if(!fineMonies.isEmpty()){
            fineMonies.stream().forEach(fineMoney -> {
                ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
                serviceUnitExample.or().andServiceunit_idEqualTo(fineMoney.getServiceunit_id()).andPay_order_idEqualTo(fineMoney.getPay_order_id());
                ServiceUnit serviceUnit = DataAccessUtils.singleResult(this.serviceUnitService.selectByExample(serviceUnitExample));
                if(serviceUnit!=null){
                    //找出半月结的数据
                    PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
                    partnerFallintoExample.or().andPartner_idEqualTo(serviceUnit.getPartner_id()).andProduct_idEqualTo(serviceUnit.getProduct_id())
                            .andBalance_typeEqualTo(1);
                    PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(this.partnerFallintoService.selectByExample(partnerFallintoExample));
                    if(partnerFallinto!=null){
                        FallintoFineMoneyExample fallintoFineMoneyExample = new FallintoFineMoneyExample();
                        fallintoFineMoneyExample.or().andFine_money_idEqualTo(fineMoney.getFine_money_id()).andPay_order_idEqualTo(fineMoney.getPay_order_id());
                        FallintoFineMoney fallintoFineMoney = DataAccessUtils.singleResult(fallintoFineMoneyService.selectByExample(fallintoFineMoneyExample));
                        if(fallintoFineMoney==null){
                            FallintoFineMoney fm = combine(localDate, fineMoney);
                            this.fallintoFineMoneyService.insert(fm);
                        }
                    }
                }
            });
        }
    }

    public FallintoFineMoney combine(LocalDate localDate,FineMoney fineMoney) {
        //对应订单
        Order order = this.orderService.selectByPrimaryKey(fineMoney.getPay_order_id());
        //对应服务单
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andPay_order_idEqualTo(order.getPay_order_id()).andPidEqualTo(0L);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        //找到对应订单合伙人信息，对应结算策略信息
        Partner partner = partnerService.selectByPrimaryKey(serviceUnit.getPartner_id());

        FallintoFineMoney fallintoFineMoney=new FallintoFineMoney();
        fallintoFineMoney.setFallinto_fine_money_id(IdGenerator.generateId());
        String month = localDate.getMonthValue() < 10 ? "0" + localDate.getMonthValue() : localDate.getMonthValue() + "";
        fallintoFineMoney.setBalance_cycle(localDate.getYear() + month + "01");//结算期
        fallintoFineMoney.setFine_money_id(fineMoney.getFine_money_id());//罚款单号
        fallintoFineMoney.setPay_order_id(order.getPay_order_id());//订单号
        fallintoFineMoney.setServiceunit_id(serviceUnit.getServiceunit_id());//服务单号
        fallintoFineMoney.setOrder_name(order.getName());//订单名称
        fallintoFineMoney.setOrder_create_datetime(order.getCreate_datetime());//订单创建时间
        fallintoFineMoney.setFine_money_create_datetime(fineMoney.getCreate_datetime());//罚款创建时间
        fallintoFineMoney.setFine_confirm_datetime(fineMoney.getConfirm_datetime());//罚款完成时间
        fallintoFineMoney.setPrice_total(order.getPrice_total());//订单总价
        fallintoFineMoney.setPrice_discount(order.getPrice_discount());//优惠金额
        fallintoFineMoney.setPrice_pay(order.getPrice_pay());//实际支付金额
        fallintoFineMoney.setFine_money(fineMoney.getFine_amount());//罚款金额 单位分
        fallintoFineMoney.setPartner_id(serviceUnit.getPartner_id());//合伙人id
        fallintoFineMoney.setPartner_name(partner.getName());//合伙人名称
        fallintoFineMoney.setPartner_level(partner.getLevel());//合伙人级别
        fallintoFineMoney.setCooperation_start(partner.getCooperation_start());//合伙人合作开始时间
        fallintoFineMoney.setCooperation_end(partner.getCooperation_end());//合伙人合作结束时间
        fallintoFineMoney.setStatus(1);//（1 待结算  2 已结算）
        return fallintoFineMoney;
    }

    /**
     * 扣款测试
     */
    @org.junit.Test
    public void deductMoney(){
        LocalDate localDate = LocalDate.now().minusMonths(1);
        // 取本月第1天：
        LocalDate firstDayOfThisMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = firstDayOfThisMonth.atStartOfDay(zoneId);
        Date firstDate = Date.from(zdt.toInstant());
        // 取本月第15天
        LocalDate secondDayOfThisMonth = localDate.withDayOfMonth(15);
        LocalDateTime localDateTime = secondDayOfThisMonth.atTime(23, 59, 59);
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date endDate = Date.from(instant);

        DeductMoneyExample deductMoneyExample = new DeductMoneyExample();
        deductMoneyExample.or().andDeduct_statusEqualTo(2).andConfirm_datetimeBetween(firstDate,endDate);
        List<DeductMoney> deductMoneyList = deductMoneyService.selectByExample(deductMoneyExample);
        if(!deductMoneyList.isEmpty()){
            deductMoneyList.stream().forEach(deductMoney -> {
                ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
                serviceUnitExample.or().andServiceunit_idEqualTo(deductMoney.getServiceunit_id()).andPay_order_idEqualTo(deductMoney.getPay_order_id());
                ServiceUnit serviceUnit = DataAccessUtils.singleResult(this.serviceUnitService.selectByExample(serviceUnitExample));
                if(serviceUnit!=null){
                    //找出半月结的数据
                    PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
                    partnerFallintoExample.or().andPartner_idEqualTo(serviceUnit.getPartner_id()).andProduct_idEqualTo(serviceUnit.getProduct_id())
                            .andBalance_typeEqualTo(1);
                    PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(this.partnerFallintoService.selectByExample(partnerFallintoExample));
                    if(partnerFallinto!=null){
                        FallintoDeductMoneyExample fallintoDeductMoneyExample = new FallintoDeductMoneyExample();
                        fallintoDeductMoneyExample.or().andDeduct_money_idEqualTo(deductMoney.getDeduct_money_id()).andPay_order_idEqualTo(deductMoney.getPay_order_id());
                        FallintoDeductMoney fd = DataAccessUtils.singleResult(fallintoDeductMoneyService.selectByExample(fallintoDeductMoneyExample));
                        if(fd==null){
                            FallintoDeductMoney fallintoDeductMoney = combine(localDate, deductMoney);
                            this.fallintoDeductMoneyService.insert(fallintoDeductMoney);
                        }
                    }
                }
            });
        }
    }

    public FallintoDeductMoney combine(LocalDate localDate,DeductMoney deductMoney) {
        //对应订单
        Order order = this.orderService.selectByPrimaryKey(deductMoney.getPay_order_id());
        //对应服务单
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andPay_order_idEqualTo(order.getPay_order_id()).andPidEqualTo(0L);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        //找到对应订单合伙人信息，对应结算策略信息
        Partner partner = partnerService.selectByPrimaryKey(serviceUnit.getPartner_id());

        FallintoDeductMoney fallintoDeductMoney=new FallintoDeductMoney();
        fallintoDeductMoney.setFallinto_deduct_money_id(IdGenerator.generateId());
        String month = localDate.getMonthValue() < 10 ? "0" + localDate.getMonthValue() : localDate.getMonthValue() + "";
        fallintoDeductMoney.setBalance_cycle(localDate.getYear() + month + "01");//结算期
        fallintoDeductMoney.setDeduct_money_id(deductMoney.getDeduct_money_id());//扣款单号
        fallintoDeductMoney.setPay_order_id(order.getPay_order_id());//订单号
        fallintoDeductMoney.setServiceunit_id(serviceUnit.getServiceunit_id());//服务单号
        fallintoDeductMoney.setOrder_name(order.getName());//订单名称
        fallintoDeductMoney.setOrder_create_datetime(order.getCreate_datetime());//订单创建时间
        fallintoDeductMoney.setDeduct_money_create_datetime(deductMoney.getCreate_datetime());//扣款创建时间
        fallintoDeductMoney.setDeduct_confirm_datetime(deductMoney.getConfirm_datetime());//扣款完成时间
        fallintoDeductMoney.setPrice_total(order.getPrice_total());//订单总价
        fallintoDeductMoney.setPrice_discount(order.getPrice_discount());//优惠金额
        fallintoDeductMoney.setPrice_pay(order.getPrice_pay());//实际支付金额
        fallintoDeductMoney.setDeduct_money(deductMoney.getDeduct_amount());//扣款金额 单位分
        fallintoDeductMoney.setPartner_id(serviceUnit.getPartner_id());//合伙人id
        fallintoDeductMoney.setPartner_name(partner.getName());//合伙人名称
        fallintoDeductMoney.setPartner_level(partner.getLevel());//合伙人级别
        fallintoDeductMoney.setCooperation_start(partner.getCooperation_start());//合伙人合作开始时间
        fallintoDeductMoney.setCooperation_end(partner.getCooperation_end());//合伙人合作结束时间
        fallintoDeductMoney.setStatus(1);//（1 待结算  2 已结算）
        return fallintoDeductMoney;
    }


    /**
     * 赔偿测试
     */
    @org.junit.Test
    public void compensation(){
        LocalDate localDate = LocalDate.now().minusMonths(1);
        // 取本月第1天：
        LocalDate firstDayOfThisMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = firstDayOfThisMonth.atStartOfDay(zoneId);
        Date firstDate = Date.from(zdt.toInstant());
        // 取本月第15天
        LocalDate secondDayOfThisMonth = localDate.withDayOfMonth(15);
        LocalDateTime localDateTime = secondDayOfThisMonth.atTime(23, 59, 59);
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date endDate = Date.from(instant);

        CompensationExample compensationExample = new CompensationExample();
        compensationExample.or().andConfirm_datetimeBetween(firstDate,endDate).andCompensation_statusEqualTo(2);
        List<Compensation> compensations = compensationService.selectByExample(compensationExample);
        if(!compensations.isEmpty()){
            compensations.stream().forEach(compensation -> {
                ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
                serviceUnitExample.or().andPay_order_idEqualTo(compensation.getPay_order_id()).andPartner_idEqualTo(compensation.getPartner_id())
                            .andPidEqualTo(0L);
                ServiceUnit serviceUnit = DataAccessUtils.singleResult(this.serviceUnitService.selectByExample(serviceUnitExample));
                if(serviceUnit!=null){
                    //找出半月结的数据
                    PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
                    partnerFallintoExample.or().andPartner_idEqualTo(serviceUnit.getPartner_id()).andProduct_idEqualTo(serviceUnit.getProduct_id())
                            .andBalance_typeEqualTo(1);
                    PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(this.partnerFallintoService.selectByExample(partnerFallintoExample));
                    if(partnerFallinto!=null){
                        FallintoCompensationExample fallintoCompensationExample = new FallintoCompensationExample();
                        fallintoCompensationExample.or().andCompensation_idEqualTo(compensation.getCompensation_id()).andPay_order_idEqualTo(compensation.getPay_order_id());
                        FallintoCompensation fc = DataAccessUtils.singleResult(fallintoCompensationService.selectByExample(fallintoCompensationExample));
                        if(fc==null){
                            FallintoCompensation fallintoCompensation = combine(localDate, compensation);
                            this.fallintoCompensationService.insert(fallintoCompensation);
                        }
                    }
                }
            });
        }

    }

    public FallintoCompensation combine(LocalDate localDate,Compensation compensation) {
        //对应订单
        Order order = this.orderService.selectByPrimaryKey(compensation.getPay_order_id());
        //对应服务单
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andPay_order_idEqualTo(order.getPay_order_id()).andPidEqualTo(0L);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        //找到对应订单合伙人信息，对应结算策略信息
        Partner partner = partnerService.selectByPrimaryKey(compensation.getPartner_id());

        FallintoCompensation fallintoCompensation=new FallintoCompensation();
        fallintoCompensation.setFallinto_compensation_id(IdGenerator.generateId());
        String month = localDate.getMonthValue() < 10 ? "0" + localDate.getMonthValue() : localDate.getMonthValue() + "";
        fallintoCompensation.setBalance_cycle(localDate.getYear() + month + "01");//结算期
        fallintoCompensation.setCompensation_id(compensation.getCompensation_id());//赔偿单号
        fallintoCompensation.setPay_order_id(order.getPay_order_id());//订单号
        fallintoCompensation.setServiceunit_id(serviceUnit.getServiceunit_id());//服务单号
        fallintoCompensation.setOrder_name(order.getName());//订单名称
        fallintoCompensation.setOrder_create_datetime(order.getCreate_datetime());//订单创建时间
        fallintoCompensation.setCompensation_create_datetime(compensation.getCreate_datetime());//赔偿创建时间
        fallintoCompensation.setCompensation_confirm_datetime(compensation.getConfirm_datetime());//赔偿完成时间
        fallintoCompensation.setPartner_bear_amount(compensation.getPartner_bear_amount());//合伙人赔偿金额
        fallintoCompensation.setPartner_bear_coupon_amount(compensation.getPartner_bear_coupon_amount());//合伙人承担卷额
        fallintoCompensation.setCoupon_id(compensation.getCoupon_id());//优惠卷id
        fallintoCompensation.setCoupon_receive_id(compensation.getCoupon_receive_id());//优惠卷领用id

        fallintoCompensation.setPrice_total(order.getPrice_total());//订单总价
        fallintoCompensation.setPrice_discount(order.getPrice_discount());//优惠金额
        fallintoCompensation.setPrice_pay(order.getPrice_pay());//实际支付金额
        fallintoCompensation.setAmount(compensation.getAmount());//赔偿金额 单位分
        fallintoCompensation.setPartner_id(serviceUnit.getPartner_id());//合伙人id
        fallintoCompensation.setPartner_name(partner.getName());//合伙人名称
        fallintoCompensation.setPartner_level(partner.getLevel());//合伙人级别
        fallintoCompensation.setCooperation_start(partner.getCooperation_start());//合伙人合作开始时间
        fallintoCompensation.setCooperation_end(partner.getCooperation_end());//合伙人合作结束时间
        fallintoCompensation.setStatus(1);//（1 待结算  2 已结算）
        return fallintoCompensation;
    }


    /**
     *退款测试
     */
    @org.junit.Test
    public void refund(){
        LocalDate localDate = LocalDate.now().minusMonths(1);
        // 取本月第1天：
        LocalDate firstDayOfThisMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = firstDayOfThisMonth.atStartOfDay(zoneId);
        Date firstDate = Date.from(zdt.toInstant());
        // 取本月第15天
        LocalDate secondDayOfThisMonth = localDate.withDayOfMonth(15);
        LocalDateTime localDateTime = secondDayOfThisMonth.atTime(23, 59, 59);
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date endDate = Date.from(instant);

        RefundExample refundExample = new RefundExample();
        refundExample.or().andRefund_dateBetween(firstDate, endDate).andStatusEqualTo(2);
        List<Refund> refunds = refundService.selectByExample(refundExample);
        if (!refunds.isEmpty()){
            refunds = refunds.stream().filter(refund -> refund.getFee() < refund.getPrice_pay()).collect(Collectors.toList());
            refunds.stream().forEach(refund -> {
                ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
                serviceUnitExample.or().andPay_order_idEqualTo(refund.getPay_order_id()).andActiveEqualTo(0).andPidEqualTo(0L);
                ServiceUnit serviceUnit = DataAccessUtils.singleResult(this.serviceUnitService.selectByExample(serviceUnitExample));
                if(serviceUnit!=null){
                    //找出半月结的数据
                    PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
                    partnerFallintoExample.or().andPartner_idEqualTo(serviceUnit.getPartner_id()).andProduct_idEqualTo(serviceUnit.getProduct_id())
                            .andBalance_typeEqualTo(1);
                    PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(this.partnerFallintoService.selectByExample(partnerFallintoExample));
                    if(partnerFallinto!=null) {
                        FallintoRefund fallintoRefund = combine(localDate, refund);
                        this.fallintoRefundService.insert(fallintoRefund);
                    }
                }
            });
        }

    }
    public FallintoRefund combine(LocalDate localDate, Refund refund) {
        //对应订单
        Order order = this.orderService.selectByPrimaryKey(refund.getPay_order_id());
        //对应服务单
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andPay_order_idEqualTo(order.getPay_order_id()).andActiveEqualTo(0).andPidEqualTo(0L);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        //找到对应订单合伙人信息，对应结算策略信息
        Partner partner = partnerService.selectByPrimaryKey(refund.getPartner_id());

        FallintoRefund fallintoRefund=new FallintoRefund();
        fallintoRefund.setFallinto_refund_id(IdGenerator.generateId());
        String month = localDate.getMonthValue() < 10 ? "0" + localDate.getMonthValue() : localDate.getMonthValue() + "";
        fallintoRefund.setBalance_cycle(localDate.getYear() + month + "01");//结算期
        fallintoRefund.setRefund_id(refund.getRefund_id());//退款单号
        fallintoRefund.setPay_order_id(order.getPay_order_id());//订单号
        fallintoRefund.setServiceunit_id(serviceUnit.getServiceunit_id());//服务单号
        fallintoRefund.setOrder_name(order.getName());//订单名称
        fallintoRefund.setOrder_create_datetime(order.getCreate_datetime());//订单创建时间
        fallintoRefund.setPrice_total(order.getPrice_total());//订单总价
        fallintoRefund.setPrice_discount(order.getPrice_discount());//优惠金额
        fallintoRefund.setPrice_pay(order.getPrice_pay());//实际支付金额
        fallintoRefund.setPartner_id(serviceUnit.getPartner_id());//合伙人id
        fallintoRefund.setPartner_name(partner.getName());//合伙人名称
        fallintoRefund.setPartner_level(partner.getLevel());//合伙人级别
        fallintoRefund.setCooperation_start(partner.getCooperation_start());//合伙人合作开始时间
        fallintoRefund.setCooperation_end(partner.getCooperation_end());//合伙人合作结束时间
        fallintoRefund.setStatus(1);//（1 待结算  2 已结算）
        fallintoRefund.setPrice_compute(fallintoRefund.getPrice_pay()-refund.getFee());
        return fallintoRefund;
    }


    /**
     * 订单完成测试
     */
    @org.junit.Test
    public void test(){
        LocalDate localDate = LocalDate.now().minusMonths(2);
        //.minusMonths(-1)
        // 取本月第1天：
        /*LocalDate firstDayOfThisMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = firstDayOfThisMonth.atStartOfDay(zoneId);
        Date firstDate = Date.from(zdt.toInstant());*/
        // 取本月最后一天
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate lastDayOfThisMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime localDateTime = lastDayOfThisMonth.atTime(23, 59, 59);
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date endDate = Date.from(instant);
        // 取本月第15天
        LocalDate secondDayOfThisMonth = localDate.withDayOfMonth(16);
        LocalDateTime localDateTime1 = secondDayOfThisMonth.atTime(0, 0, 0);
        Instant instant1 = localDateTime1.atZone(zoneId).toInstant();
        Date startDate = Date.from(instant1);


        //底价结算集合
        List<ServiceUnit> floorPriceList = new ArrayList<>();
        //比例结算集合
        List<ServiceUnit> percentList = new ArrayList<>();
        //单数阶梯结算集合
        List<ServiceUnit> numList = new ArrayList<>();
        //金额阶梯结算集合
        List<ServiceUnit> moneyList = new ArrayList<>();
        //客单价结算集合
        List<ServiceUnit> guestList = new ArrayList<>();

        //找出这月16-月底的服务单
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andFinish_datetimeBetween(startDate,endDate).andActiveEqualTo(1).andPidEqualTo(0L);
        List<ServiceUnit> serviceUnits = this.serviceUnitService.selectByExample(serviceUnitExample);
        if(!serviceUnits.isEmpty()){
            serviceUnits.stream().forEach(serviceUnit -> {
                //根据服务单找到对应订单
                OrderExample orderExample = new OrderExample();
                orderExample.or().andPay_order_idEqualTo(serviceUnit.getPay_order_id()).andStatus_activeEqualTo(5);
                Order order = DataAccessUtils.singleResult(this.orderService.selectByExample(orderExample));
                if(order!=null){
                    //找出半月结的数据
                    PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
                    partnerFallintoExample.or().andPartner_idEqualTo(serviceUnit.getPartner_id()).andProduct_idEqualTo(serviceUnit.getProduct_id())
                    .andBalance_typeEqualTo(1);
                    PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(this.partnerFallintoService.selectByExample(partnerFallintoExample));
                    if(partnerFallinto!=null){
                        //查找该订单有没有正在处理中的
                        //退款
                        RefundExample refundExample = new RefundExample();
                        refundExample.or().andPay_order_idEqualTo(serviceUnit.getPay_order_id())
                                .andStatusEqualTo(1);//状态-->退款中
                        List<Refund> refunds = refundService.selectByExample(refundExample);

                        //赔款
                        CompensationExample compensationExample = new CompensationExample();
                        compensationExample.or().andPay_order_idEqualTo(serviceUnit.getPay_order_id())
                                .andCompensation_statusEqualTo(1);//状态-->待处理
                        List<Compensation> compensations = compensationService.selectByExample(compensationExample);

                        //扣款
                        DeductMoneyExample deductMoneyExample = new DeductMoneyExample();
                        deductMoneyExample.or().andPay_order_idEqualTo(order.getPay_order_id()).andServiceunit_idEqualTo(serviceUnit.getServiceunit_id())
                                .andDeduct_statusEqualTo(1);//状态-->待处理
                        List<DeductMoney> deductMonies = deductMoneyService.selectByExample(deductMoneyExample);

                        //结算
                        BalanceOrderExample balanceOrderExample = new BalanceOrderExample();
                        balanceOrderExample.or().andPay_order_idEqualTo(order.getPay_order_id()).andServiceunit_idEqualTo(serviceUnit.getServiceunit_id());
                        List<BalanceOrder> balanceOrders = balanceOrderService.selectByExample(balanceOrderExample);

                        if (refunds.isEmpty() & compensations.isEmpty() & balanceOrders.isEmpty() & deductMonies.isEmpty()) {
                            //按照结算类型来分
                            Partner partner = partnerService.selectByPrimaryKey(serviceUnit.getPartner_id());
                            Fallinto fallinto = new Fallinto();
                            if (partner != null) {
                                //PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(partnerFallintoService.selectByExample(partnerFallintoExample));
                                if (partnerFallinto != null) {
                                    fallinto = fallintoService.selectByPrimaryKey(partnerFallinto.getFallinto_id());
                                }
                            }
                            switch (fallinto.getFallinto_type()) {
                                case 1://底价结算
                                    floorPriceList.add(serviceUnit);
                                    break;
                                case 2://比例结算
                                    percentList.add(serviceUnit);
                                    break;
                                case 3://单数阶梯
                                    numList.add(serviceUnit);
                                    break;
                                case 4://金额阶梯
                                    moneyList.add(serviceUnit);
                                    break;
                                case 5://客价单结算
                                    guestList.add(serviceUnit);
                                    break;
                            }
                        }
                    }
                }

            });
        }
        //底价
        if (!floorPriceList.isEmpty()) {
            floorPriceList.stream().forEach(serviceUnit -> {
                BalanceOrder balance = combine(localDate, serviceUnit);
                balance.setFallinto_info("底价结算");
                this.balanceOrderService.insert(balance);
            });
        }

        //比例
        if (!percentList.isEmpty()) {
            percentList.stream().forEach(serviceUnit -> {
                BalanceOrder balance = combine(localDate, serviceUnit);
                balance.setFallinto_info("比例结算");
                this.balanceOrderService.insert(balance);
            });
        }

        //单数阶梯
        if (!numList.isEmpty()) {
            Map<String, Map<String, List<ServiceUnit>>> mapList = partnerFallintoMap(numList,3,1);
            mapList.forEach((String k, Map<String, List<ServiceUnit>> v) ->{
               // Map<String, List<ServiceUnit>> stringListMap = mapList.get(k);
                v.forEach((key,val) ->{
                    Fallinto fallinto = this.fallintoService.selectByPrimaryKey(Long.parseLong(key));
                    //List<ServiceUnit> serviceUnitList = stringListMap.get(val);
                    List<ServiceUnit> serviceUnitList = v.get(key);
                    List<StepData> stepData = com.alibaba.fastjson.JSONArray.parseArray(fallinto.getStep_data(), StepData.class);
                    if (fallinto.getStep_type() == 1) {
                        if (stepData.size() != 0) {
                            stepData.stream().forEach(step -> {
                                if (serviceUnitList.size() >=step.getD()) {
                                    serviceUnitList.stream().forEach(serviceUnit -> {
                                        Order order = this.orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
                                        BalanceOrder balance = combine(localDate, serviceUnit);
                                        balance.setPartner_balance_fee(Integer.parseInt(step.getV()));//合伙人订单结算金额
                                        balance.setBalance_fee(order.getPrice_pay()-balance.getPartner_balance_fee());//平台订单结算金额
                                        balance.setFallinto_info(">=" + step.getD() + "底价:" + step.getV());//结算策略 命中说明
                                        this.balanceOrderService.insert(balance);
                                    });
                                }
                            });
                        }
                    } else {
                        if (stepData.size() != 0) {
                            stepData.stream().forEach(step -> {
                                if (serviceUnitList.size() >= step.getD()) {
                                    serviceUnitList.stream().forEach(serviceUnit -> {
                                        Order order = this.orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
                                        BalanceOrder balance = combine(localDate, serviceUnit);
                                        balance.setPartner_balance_fee((int) (order.getPrice_pay() * Double.parseDouble(Integer.parseInt(step.getV())*0.01+"")));//合伙人订单结算金额
                                        balance.setBalance_fee(order.getPrice_pay()-balance.getPartner_balance_fee());//平台订单结算金额
                                        balance.setFallinto_info(">=" + step.getD() + "比例:" + step.getV());//结算策略 命中说明
                                        this.balanceOrderService.insert(balance);
                                    });
                                }
                            });
                        }
                    }

                });
            });


        }

        //金额阶梯
        if (!moneyList.isEmpty()) {
            Map<String, Map<String, List<ServiceUnit>>> mapList = partnerFallintoMap(moneyList,4,1);

            mapList.forEach((String k, Map<String, List<ServiceUnit>> v) ->{
                Map<String, List<ServiceUnit>> stringListMap = mapList.get(k);
                stringListMap.forEach((key,val) ->{
                    Fallinto fallinto = this.fallintoService.selectByPrimaryKey(Long.parseLong(key));
                    List<ServiceUnit> serviceUnitList = stringListMap.get(key);
                    List<StepData> stepData = com.alibaba.fastjson.JSONArray.parseArray(fallinto.getStep_data(), StepData.class);
                    int sumPrice=0;
                    for(ServiceUnit serviceUnit:serviceUnitList){
                        Order order = this.orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
                        sumPrice+=order.getPrice_pay();
                    }
                    if (fallinto.getStep_type() == 1) {
                        if (stepData.size() != 0) {
                            for(StepData step:stepData){
                                if (sumPrice >= step.getD()) {
                                    serviceUnitList.stream().forEach(serviceUnit -> {
                                        Order order = this.orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
                                        BalanceOrder balance = combine(localDate, serviceUnit);
                                        balance.setPartner_balance_fee(Integer.parseInt(step.getV())*100);
                                        balance.setBalance_fee(order.getPrice_pay()-balance.getPartner_balance_fee());//平台订单结算金额
                                        balance.setFallinto_info(">=" + step.getD() + "底价:" + step.getV());//结算策略 命中说明
                                        this.balanceOrderService.insert(balance);
                                    });
                                }
                            }
                        }
                    }else {
                        if (stepData.size() != 0) {
                            for(StepData step:stepData){
                                if (sumPrice >= step.getD()) {
                                    serviceUnitList.stream().forEach(serviceUnit -> {
                                        Order order = this.orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
                                        BalanceOrder balance = combine(localDate, serviceUnit);
                                        balance.setPartner_balance_fee((int) (order.getPrice_pay() * Double.parseDouble(Integer.parseInt(step.getV())*0.01+"")));
                                        balance.setBalance_fee(order.getPrice_pay()-balance.getPartner_balance_fee());//平台订单结算金额
                                        balance.setFallinto_info(">=" + step.getD() + "比例:" + step.getV());//结算策略 命中说明
                                        this.balanceOrderService.insert(balance);
                                    });
                                }
                            }
                        }
                    }
                });
            });
        }

        if (!guestList.isEmpty()) {
            guestList.stream().forEach(serviceUnit -> {
                BalanceOrder balance = combine(localDate, serviceUnit);
                this.balanceOrderService.insert(balance);
            });
        }
    }

    public BalanceOrder combine(LocalDate localDate, ServiceUnit serviceUnit) {
        Order order = this.orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
        //找到对应订单合伙人信息，对应结算策略信息
        Partner partner = partnerService.selectByPrimaryKey(serviceUnit.getPartner_id());
        Fallinto fallinto = new Fallinto();
        if (partner != null) {
            PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
            partnerFallintoExample.or().andPartner_idEqualTo(partner.getPartner_id()).andProduct_idEqualTo(serviceUnit.getProduct_id());
            PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(partnerFallintoService.selectByExample(partnerFallintoExample));
            if (partnerFallinto != null) {
                fallinto = fallintoService.selectByPrimaryKey(partnerFallinto.getFallinto_id());
            }
        }
        BalanceOrder balance = new BalanceOrder();
        balance.setBalance_order_id(IdGenerator.generateId());
        String month = localDate.getMonthValue() < 10 ? "0" + localDate.getMonthValue() : localDate.getMonthValue() + "";
        balance.setBalance_cycle(localDate.getYear() + month + "01");//结算期
        balance.setPay_order_id(order.getPay_order_id());//订单号
        balance.setServiceunit_id(serviceUnit.getServiceunit_id());//服务单号
        balance.setBalance_type(1);//结算类型
        balance.setStatus(1);//（1 待结算  2 已结算）
        balance.setOrder_name(order.getName());//订单名称
        balance.setProduct_id(serviceUnit.getProduct_id());//产品id
        balance.setPsku_id(serviceUnit.getPsku_id());//sku id
        balance.setOrder_create_datetime(order.getCreate_datetime());//订单创建时间
        balance.setWork_finish_datetime(serviceUnit.getWork_4_datetime());//服务人员完成时间
        balance.setFinish_datetime(serviceUnit.getFinish_datetime());//服务单完成时间
        balance.setPrice_total(order.getPrice_total());//订单总价
        balance.setPrice_discount(order.getPrice_discount());//优惠金额
        balance.setPrice_pay(order.getPrice_pay());//实际支付金额
        balance.setDiscount_data(order.getDiscount_data());//优惠数据JSON
        balance.setPartner_id(serviceUnit.getPartner_id());//合伙人id
        balance.setPartner_name(partner.getName());//合伙人名称
        balance.setPartner_level(partner.getLevel());//合伙人级别
        balance.setCooperation_start(partner.getCooperation_start());//合伙人合作开始时间
        balance.setCooperation_end(partner.getCooperation_end());//合伙人合作结束时间
        switch (fallinto.getFallinto_type()) {
            case 1:
                balance.setPartner_balance_fee(fallinto.getFloor_price());//合伙人订单结算金额
                balance.setBalance_fee(order.getPrice_pay()-balance.getPartner_balance_fee());//平台订单结算金额
                break;
            case 2:
                balance.setPartner_balance_fee(order.getPrice_pay() * fallinto.getPercent());//合伙人订单结算金额
                balance.setBalance_fee(order.getPrice_pay()-balance.getPartner_balance_fee());//平台订单结算金额
                break;
            /*case 3:
                balance = Numjudge(num, balance, fallinto, order);
                break;
            case 4:
                balance = Moneyjudge(balance, fallinto, order);
                break;*/
            case 5:
                balance = Moneyjudge(balance, fallinto, order);
                break;
        }
        balance.setFallinto_id(fallinto.getFallinto_id());// 结算策略id
        balance.setFallinto_name(fallinto.getFallinto_name());//结算策略名称
        balance.setCreate_datetime(new Date());
        return balance;
    }

   /* //单数
    public BalanceOrder Numjudge(int num, BalanceOrder balance, Fallinto fallinto, Order order) {
        List<StepData> stepData = com.alibaba.fastjson.JSONArray.parseArray(fallinto.getStep_data(), StepData.class);
        if (fallinto.getStep_type() == 1) {
            if (stepData.size() != 0) {
                stepData.stream().forEach(step -> {
                    if (num >= Integer.parseInt(step.getD())) {
                        balance.setPartner_balance_fee(Integer.parseInt(step.getV()));//合伙人订单结算金额
                        balance.setBalance_fee(order.getPrice_pay()-balance.getPartner_balance_fee());//平台订单结算金额
                        balance.setFallinto_info(">=" + step.getD() + "底价:" + step.getV());//结算策略 命中说明
                    }
                });
            }
        } else {
            if (stepData.size() != 0) {
                stepData.stream().forEach(step -> {
                    if (num >= Integer.parseInt(step.getD())) {
                        balance.setPartner_balance_fee(order.getPrice_pay() * Integer.parseInt(step.getV()));//合伙人订单结算金额
                        balance.setBalance_fee(order.getPrice_pay()-balance.getPartner_balance_fee());//平台订单结算金额
                        balance.setFallinto_info(">=" + step.getD() + "比例:" + step.getV());//结算策略 命中说明
                    }
                });
            }
        }
        return balance;
    }*/

    //金额
    public BalanceOrder Moneyjudge(BalanceOrder balance, Fallinto fallinto, Order order) {

        List<StepData> stepData = com.alibaba.fastjson.JSONArray.parseArray(fallinto.getStep_data(), StepData.class);
        if (fallinto.getStep_type() == 1) {
            if (stepData.size() != 0) {
                stepData.stream().forEach(step -> {
                    if(order.getPrice_pay()>=step.getD()){
                        balance.setPartner_balance_fee(Integer.parseInt(step.getV())*100);
                        balance.setBalance_fee(order.getPrice_pay()-balance.getPartner_balance_fee());//平台订单结算金额
                        balance.setFallinto_info(">=" + step.getD() + "底价:" + step.getV());//结算策略 命中说明
                    }
                });
            }
        } else {
            if (stepData.size() != 0) {
                stepData.stream().forEach(step -> {
                    if(order.getPrice_pay()>=step.getD()){
                        balance.setPartner_balance_fee((int) (order.getPrice_pay() * Double.parseDouble(Integer.parseInt(step.getV())*0.01+"")));
                        balance.setBalance_fee(order.getPrice_pay()-balance.getPartner_balance_fee());//平台订单结算金额
                        balance.setFallinto_info(">=" + step.getD() + "比例:" + step.getV());//结算策略 命中说明
                    }
                });
            }
        }
        return balance;
    }


    public  Map<String,Map<String,List<ServiceUnit>>>  partnerFallintoMap(List<ServiceUnit> list,int fType,int bType){
        //找到是同一个合伙人的订单集合
        Map<String,List<ServiceUnit>> resultMap = new HashMap<>();
        list.stream().forEach(serviceUnit -> {
            if(resultMap.containsKey(serviceUnit.getPartner_id().toString())){
                resultMap.get(serviceUnit.getPartner_id().toString()).add(serviceUnit);
            }else{//map中不存在，新建key，用来存放数据
                List<ServiceUnit> tmpList = new ArrayList<>();
                tmpList.add(serviceUnit);
                resultMap.put(serviceUnit.getPartner_id().toString(), tmpList);
            }
        });

        //同一合伙人同一策略集合封装
        Map<String,Map<String,List<ServiceUnit>>> mapList=new HashMap<>();
        resultMap.forEach((k,v) ->{
            Map<String,List<ServiceUnit>> FallintoMap = new HashMap<>();
            List<ServiceUnit> serviceUnitList = resultMap.get(k);
            PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
            partnerFallintoExample.or().andPartner_idEqualTo(Long.parseLong(k)).andFallinto_typeEqualTo(fType).andBalance_typeEqualTo(bType);
            List<PartnerFallinto> partnerFallintos = this.partnerFallintoService.selectByExample(partnerFallintoExample);
            if(!partnerFallintos.isEmpty()){
                for(PartnerFallinto partnerFallinto:partnerFallintos){
                    if(FallintoMap.containsKey(partnerFallinto.getFallinto_id().toString())){
                        for(ServiceUnit serviceUnit:serviceUnitList){
                            if(serviceUnit.getPartner_id().equals(partnerFallinto.getPartner_id()) && serviceUnit.getProduct_id().equals(partnerFallinto.getProduct_id())){
                                FallintoMap.get(partnerFallinto.getFallinto_id().toString()).add(serviceUnit);
                            }
                        }
                    }else{//map中不存在，新建key，用来存放数据
                        List<ServiceUnit> tmpList = new ArrayList<>();
                        for(ServiceUnit serviceUnit:serviceUnitList){
                            if(serviceUnit.getPartner_id().equals(partnerFallinto.getPartner_id()) && serviceUnit.getProduct_id().equals(partnerFallinto.getProduct_id())){
                                tmpList.add(serviceUnit);
                            }
                        }
                        FallintoMap.put(partnerFallinto.getFallinto_id().toString(), tmpList);
                    }
                }
            }
            mapList.put(k,FallintoMap);
        });
        return mapList;
    }
}