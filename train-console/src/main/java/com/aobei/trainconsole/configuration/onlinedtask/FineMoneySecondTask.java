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
public class FineMoneySecondTask {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ServiceUnitService serviceUnitService;


	@Autowired
	private PartnerService partnerService;

	@Autowired
	private PartnerFallintoService partnerFallintoService;


    @Autowired
    private FineMoneyService fineMoneyService;

    @Autowired
    private FallintoFineMoneyService fallintoFineMoneyService;

    @Autowired
    private StringRedisTemplate redisTemplate;
	/**
	 * 每月2号
	 */
	//@Scheduled(cron ="0 0 0 2 * ?")
	private void extractData() {
        FallintoFineMoneyExample fallintoFineMoneyExample = new FallintoFineMoneyExample();
        fallintoFineMoneyExample.or().andStatusEqualTo(1);
        List<FallintoFineMoney> fallintoFineMonies = fallintoFineMoneyService.selectByExample(fallintoFineMoneyExample);
        if(!fallintoFineMonies.isEmpty()){
            fallintoFineMonies.stream().forEach(fallintoFineMoney -> {
                fallintoFineMoney.setStatus(2);
                this.fallintoFineMoneyService.updateByPrimaryKeySelective(fallintoFineMoney);
            });
        }

        RedisIdGenerator idGenerator = new RedisIdGenerator();
        idGenerator.setRedisTemplate(redisTemplate);
        String date = LocalDate.now().toString();
        long autoIncrId = idGenerator.getAutoIncrNum("FSCT"+date);
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

        FineMoneyExample fineMoneyExample = new FineMoneyExample();
        fineMoneyExample.or().andConfirm_datetimeBetween(sixteenDate,endDate);
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
                            FallintoFineMoney fm = combine(LocalDate.now(), fineMoney);
                            this.fallintoFineMoneyService.insert(fm);
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
                            .andBalance_typeEqualTo(2);
                    PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(this.partnerFallintoService.selectByExample(partnerFallintoExample));
                    if(partnerFallinto!=null){
                        FallintoFineMoneyExample fallintoFineMoneyExample = new FallintoFineMoneyExample();
                        fallintoFineMoneyExample.or().andFine_money_idEqualTo(fineMoney.getFine_money_id()).andPay_order_idEqualTo(fineMoney.getPay_order_id());
                        FallintoFineMoney fallintoFineMoney = DataAccessUtils.singleResult(fallintoFineMoneyService.selectByExample(fallintoFineMoneyExample));
                        if(fallintoFineMoney==null){
                            FallintoFineMoney fm = combine(LocalDate.now(), fineMoney);
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
        fallintoFineMoney.setBalance_cycle(localDate.getYear() + month + "02");//结算期
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
}



