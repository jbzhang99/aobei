package com.aobei.trainconsole.configuration.onlinedtask;

import com.aobei.common.boot.RedisIdGenerator;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RefundSecondTask {

	@Autowired
	private OrderService orderService;


	@Autowired
	private ServiceUnitService serviceUnitService;


	@Autowired
	private PartnerService partnerService;

	@Autowired
	private PartnerFallintoService partnerFallintoService;


	@Autowired
	private RefundService  refundService;

	@Autowired
	private FallintoRefundService fallintoRefundService;

    @Autowired
    private StringRedisTemplate redisTemplate;
	/**
	 * 每月2号
	 */
	//@Scheduled(cron ="0 0 0 2 * ?")
	private void extractData() {
        //抽取数据之前先将待结算的变为已结算
        FallintoRefundExample fallintoRefundExample = new FallintoRefundExample();
        fallintoRefundExample.or().andStatusEqualTo(1);
        List<FallintoRefund> fallintoRefunds = this.fallintoRefundService.selectByExample(fallintoRefundExample);
        if(!fallintoRefunds.isEmpty()){
            fallintoRefunds.stream().forEach(fallintoRefund -> {
                fallintoRefund.setStatus(2);
                this.fallintoRefundService.updateByPrimaryKeySelective(fallintoRefund);
            });
        }

        RedisIdGenerator idGenerator = new RedisIdGenerator();
        idGenerator.setRedisTemplate(redisTemplate);
        String date = LocalDate.now().toString();
        long autoIncrId = idGenerator.getAutoIncrNum("RSCT"+date);
        if (autoIncrId != 1){
            return;
        }
        halfMonth();
        totalMonth();
    }

    public void halfMonth(){
        LocalDate localDate = LocalDate.now().minusMonths(1);
        ZoneId zoneId = ZoneId.systemDefault();
        // 取本月最后一天
        LocalDate lastDayOfThisMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime localDateTime = lastDayOfThisMonth.atTime(23, 59, 59);
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date endDate = Date.from(instant);
        // 取本月第16天
        LocalDate secondDayOfThisMonth = localDate.withDayOfMonth(16);
        ZonedDateTime zonedDateTime = secondDayOfThisMonth.atStartOfDay(zoneId);
        Date sixteenDate = Date.from(zonedDateTime.toInstant());

        RefundExample refundExample = new RefundExample();
        refundExample.or().andRefund_dateBetween(sixteenDate, endDate).andStatusEqualTo(2);
        List<Refund> refunds = refundService.selectByExample(refundExample);
        if (!refunds.isEmpty()){
            refunds = refunds.stream().filter(refund -> refund.getFee() < refund.getPrice_pay()).collect(Collectors.toList());
            refunds.stream().forEach(refund -> {
                ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
                serviceUnitExample.or().andPay_order_idEqualTo(refund.getPay_order_id()).andPidEqualTo(0L);
                ServiceUnit serviceUnit = DataAccessUtils.singleResult(this.serviceUnitService.selectByExample(serviceUnitExample));
                if(serviceUnit!=null){
                    //找出半月结的数据
                    PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
                    partnerFallintoExample.or().andPartner_idEqualTo(serviceUnit.getPartner_id()).andProduct_idEqualTo(serviceUnit.getProduct_id())
                            .andBalance_typeEqualTo(1);
                    PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(this.partnerFallintoService.selectByExample(partnerFallintoExample));
                    if(partnerFallinto!=null) {
                        //判断是否结算
                        FallintoRefundExample fallintoRefundExample = new FallintoRefundExample();
                        fallintoRefundExample.or().andRefund_idEqualTo(refund.getRefund_id()).andPay_order_idEqualTo(refund.getPay_order_id());
                        FallintoRefund fd = DataAccessUtils.singleResult(this.fallintoRefundService.selectByExample(fallintoRefundExample));
                        if(fd==null){
                            FallintoRefund fallintoRefund = combine(LocalDate.now(), refund);
                            this.fallintoRefundService.insert(fallintoRefund);
                        }
                    }
                }
            });
        }
    }

    public void  totalMonth(){
        LocalDate localDate = LocalDate.now().minusMonths(1);
        // 取本月第1天：
        LocalDate firstDayOfThisMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = firstDayOfThisMonth.atStartOfDay(zoneId);
        Date firstDate = Date.from(zdt.toInstant());
        // 取本月最后一天
        LocalDate lastDayOfThisMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime localDateTime = lastDayOfThisMonth.atTime(23, 59, 59);
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        Date endDate = Date.from(instant);

        RefundExample refundExample = new RefundExample();
        refundExample.or().andRefund_dateBetween(firstDate, endDate).andStatusEqualTo(2);
        List<Refund> refunds = refundService.selectByExample(refundExample);
        if (!refunds.isEmpty()){
            refunds = refunds.stream().filter(refund -> refund.getFee() < refund.getPrice_pay()).collect(Collectors.toList());
            refunds.stream().forEach(refund -> {
                ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
                serviceUnitExample.or().andPay_order_idEqualTo(refund.getPay_order_id()).andPidEqualTo(0L);
                ServiceUnit serviceUnit = DataAccessUtils.singleResult(this.serviceUnitService.selectByExample(serviceUnitExample));
                if(serviceUnit!=null){
                    //找出半月结的数据
                    PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
                    partnerFallintoExample.or().andPartner_idEqualTo(serviceUnit.getPartner_id()).andProduct_idEqualTo(serviceUnit.getProduct_id())
                            .andBalance_typeEqualTo(2);
                    PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(this.partnerFallintoService.selectByExample(partnerFallintoExample));
                    if(partnerFallinto!=null) {
                        //判断是否结算
                        FallintoRefundExample fallintoRefundExample = new FallintoRefundExample();
                        fallintoRefundExample.or().andRefund_idEqualTo(refund.getRefund_id()).andPay_order_idEqualTo(refund.getPay_order_id());
                        FallintoRefund fd = DataAccessUtils.singleResult(this.fallintoRefundService.selectByExample(fallintoRefundExample));
                        if(fd==null){
                            FallintoRefund fallintoRefund = combine(LocalDate.now(), refund);
                            this.fallintoRefundService.insert(fallintoRefund);
                        }
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
        serviceUnitExample.or().andPay_order_idEqualTo(order.getPay_order_id()).andPidEqualTo(0L);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        //找到对应订单合伙人信息，对应结算策略信息
        Partner partner = partnerService.selectByPrimaryKey(serviceUnit.getPartner_id());

        FallintoRefund fallintoRefund=new FallintoRefund();
        fallintoRefund.setFallinto_refund_id(IdGenerator.generateId());
        String month = localDate.getMonthValue() < 10 ? "0" + localDate.getMonthValue() : localDate.getMonthValue() + "";
        fallintoRefund.setBalance_cycle(localDate.getYear() + month + "02");//结算期
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
}



