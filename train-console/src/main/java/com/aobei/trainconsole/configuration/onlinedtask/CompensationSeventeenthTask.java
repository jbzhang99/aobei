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
public class CompensationSeventeenthTask {

	@Autowired
	private OrderService orderService;


	@Autowired
	private ServiceUnitService serviceUnitService;

	@Autowired
	private CompensationService compensationService;

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private PartnerFallintoService partnerFallintoService;

	@Autowired
	private FallintoCompensationService fallintoCompensationService;

    @Autowired
    private StringRedisTemplate redisTemplate;
	/**
	 * 每月17号
	 */
	//@Scheduled(cron ="0 0 0 17 * ?")
	private void extractData() {

        FallintoCompensationExample fallintoCompensationExamples = new FallintoCompensationExample();
        fallintoCompensationExamples.or().andStatusEqualTo(1);
        List<FallintoCompensation> fallintoCompensationList = this.fallintoCompensationService.selectByExample(fallintoCompensationExamples);
        if(!fallintoCompensationList.isEmpty()){
            fallintoCompensationList.stream().forEach(fallintoCompensation -> {
                fallintoCompensation.setStatus(2);
                this.fallintoCompensationService.updateByPrimaryKeySelective(fallintoCompensation);
            });
        }

        RedisIdGenerator idGenerator = new RedisIdGenerator();
        idGenerator.setRedisTemplate(redisTemplate);
        String date = LocalDate.now().toString();
        long autoIncrId = idGenerator.getAutoIncrNum("CSVT"+date);
        if (autoIncrId != 1){
            return;
        }

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

        //CompensationExample compensationExamples = new CompensationExample();
       // compensationExamples.or().andConfirm_datetimeLessThan(firstDate).andCompensation_statusEqualTo(2);
        List<Compensation> compensations = compensationService.selectByExample(compensationExample);
        //compensations.addAll(compensationService.selectByExample(compensationExamples));
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
        fallintoCompensation.setBalance_cycle(localDate.getYear() + month + "17");//结算期
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
}



