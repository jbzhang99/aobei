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

@Configuration
public class DeductMoneySecondTask {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ServiceUnitService serviceUnitService;


	@Autowired
	private PartnerService partnerService;

	@Autowired
	private PartnerFallintoService partnerFallintoService;



	@Autowired
	private FallintoDeductMoneyService fallintoDeductMoneyService;

	@Autowired
	private DeductMoneyService deductMoneyService;

    @Autowired
    private StringRedisTemplate redisTemplate;
	/**
	 * 每月2号
	 */
	//@Scheduled(cron ="0 0 0 2 * ?")
	private void extractData() {
        //先将待结算变成已结算
        FallintoDeductMoneyExample fallintoDeductMoneyExample = new FallintoDeductMoneyExample();
        fallintoDeductMoneyExample.or().andStatusEqualTo(1);
        List<FallintoDeductMoney> fallintoDeductMonies = fallintoDeductMoneyService.selectByExample(fallintoDeductMoneyExample);
        if(!fallintoDeductMonies.isEmpty()){
            fallintoDeductMonies.stream().forEach(fallintoDeductMoney -> {
                fallintoDeductMoney.setStatus(2);
                this.fallintoDeductMoneyService.updateByPrimaryKeySelective(fallintoDeductMoney);
            });
        }

        RedisIdGenerator idGenerator = new RedisIdGenerator();
        idGenerator.setRedisTemplate(redisTemplate);
        String date = LocalDate.now().toString();
        long autoIncrId = idGenerator.getAutoIncrNum("DSCT"+date);
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

        DeductMoneyExample deductMoneyExample = new DeductMoneyExample();
        deductMoneyExample.or().andDeduct_statusEqualTo(2).andConfirm_datetimeBetween(sixteenDate,endDate);
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
                            FallintoDeductMoney fallintoDeductMoney = combine(LocalDate.now(), deductMoney);
                            this.fallintoDeductMoneyService.insert(fallintoDeductMoney);
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
                            .andBalance_typeEqualTo(2);
                    PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(this.partnerFallintoService.selectByExample(partnerFallintoExample));
                    if(partnerFallinto!=null){
                        FallintoDeductMoneyExample fallintoDeductMoneyExample = new FallintoDeductMoneyExample();
                        fallintoDeductMoneyExample.or().andDeduct_money_idEqualTo(deductMoney.getDeduct_money_id()).andPay_order_idEqualTo(deductMoney.getPay_order_id());
                        FallintoDeductMoney fd = DataAccessUtils.singleResult(fallintoDeductMoneyService.selectByExample(fallintoDeductMoneyExample));
                        if(fd==null){
                            FallintoDeductMoney fallintoDeductMoney = combine(LocalDate.now(), deductMoney);
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
        fallintoDeductMoney.setBalance_cycle(localDate.getYear() + month + "02");//结算期
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
}



