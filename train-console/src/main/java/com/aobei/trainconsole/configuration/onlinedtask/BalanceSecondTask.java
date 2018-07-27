package com.aobei.trainconsole.configuration.onlinedtask;

import com.alibaba.fastjson.JSONArray;
import com.aobei.common.boot.RedisIdGenerator;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import custom.bean.StepData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Configuration
public class BalanceSecondTask {

	@Autowired
	private OrderService orderService;

	@Autowired
	private RefundService refundService;


	@Autowired
	private ServiceUnitService serviceUnitService;

	@Autowired
	private CompensationService compensationService;

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private PartnerFallintoService partnerFallintoService;

	@Autowired
	private FallintoService fallintoService;

	@Autowired
	private BalanceOrderService balanceOrderService;

	@Autowired
	private DeductMoneyService deductMoneyService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderItemService orderItemService;
    /**
     * 每月2号
     */
	//@Scheduled(cron ="0 01 16 27 * ?")
	private void extractData(){
       // 没有挂起的数据自动处理为“已结算”“挂起”的数据进入下一个结算月数据
        BalanceOrderExample balanceOrderExample = new BalanceOrderExample();
        balanceOrderExample.or().andStatusEqualTo(1);
        List<BalanceOrder> balanceOrders = this.balanceOrderService.selectByExample(balanceOrderExample);
        if(!balanceOrders.isEmpty()){
            balanceOrders.stream().forEach(balanceOrder -> {
                balanceOrder.setStatus(2);
                balanceOrder.setBalance_datetime(new Date());
                this.balanceOrderService.updateByPrimaryKeySelective(balanceOrder);
            });
        }

        RedisIdGenerator idGenerator = new RedisIdGenerator();
        idGenerator.setRedisTemplate(redisTemplate);
        String date = LocalDate.now().toString();
        long autoIncrId = idGenerator.getAutoIncrNum("BSCT"+date);
        if (autoIncrId != 1){
            return;
        }
        totalMonth();
        halfMonth();

    }


    //半月结
    public void  halfMonth(){
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
        // 取本月第16天
        LocalDate secondDayOfThisMonth = localDate.withDayOfMonth(16);
        ZonedDateTime zonedDateTime = secondDayOfThisMonth.atStartOfDay(zoneId);
        Date sixteenDate = Date.from(zonedDateTime.toInstant());


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
        serviceUnitExample.or().andFinish_datetimeBetween(sixteenDate,endDate).andActiveEqualTo(1).andPidEqualTo(0L);
        List<ServiceUnit> serviceUnits = this.serviceUnitService.selectByExample(serviceUnitExample);

        ServiceUnitExample serviceUnitExamples = new ServiceUnitExample();
        serviceUnitExamples.or().andFinish_datetimeLessThan(firstDate).andActiveEqualTo(1).andPidEqualTo(0L);
        List<ServiceUnit> serviceUnitLists = this.serviceUnitService.selectByExample(serviceUnitExamples);
        serviceUnitLists.stream().forEach(serviceUnit -> {
            BalanceOrderExample balanceOrderExample = new BalanceOrderExample();
            balanceOrderExample.or().andPay_order_idEqualTo(serviceUnit.getPay_order_id()).andServiceunit_idEqualTo(serviceUnit.getServiceunit_id());
            List<BalanceOrder> balanceOrders = this.balanceOrderService.selectByExample(balanceOrderExample);
            if(balanceOrders.isEmpty()){
                serviceUnits.add(serviceUnit);
            }
        });
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
                    List<PartnerFallinto> partnerFallintos = this.partnerFallintoService.selectByExample(partnerFallintoExample);
                    if(!partnerFallintos.isEmpty()){
                        packge(serviceUnit,order,partnerFallintoExample,floorPriceList,percentList,numList,moneyList,guestList);
                    }
                }

            });
        }
        ladderBalance(floorPriceList,percentList,numList,moneyList,guestList,localDate,1);
    }

    //整月结
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

        //找出这月1-月底的服务单
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or().andFinish_datetimeBetween(firstDate,endDate).andActiveEqualTo(1).andPidEqualTo(0L);
        List<ServiceUnit> serviceUnits = this.serviceUnitService.selectByExample(serviceUnitExample);

        //找出之前漏掉的数据
        ServiceUnitExample serviceUnitExamples = new ServiceUnitExample();
        serviceUnitExamples.or().andFinish_datetimeLessThan(firstDate).andActiveEqualTo(1).andPidEqualTo(0L);
        List<ServiceUnit> serviceUnitLists = this.serviceUnitService.selectByExample(serviceUnitExamples);
        serviceUnitLists.stream().forEach(serviceUnit -> {
            BalanceOrderExample balanceOrderExample = new BalanceOrderExample();
            balanceOrderExample.or().andPay_order_idEqualTo(serviceUnit.getPay_order_id()).andServiceunit_idEqualTo(serviceUnit.getServiceunit_id());
            List<BalanceOrder> balanceOrders = this.balanceOrderService.selectByExample(balanceOrderExample);
            if(balanceOrders.isEmpty()){
                serviceUnits.add(serviceUnit);
            }
        });

        if(!serviceUnits.isEmpty()){
            serviceUnits.stream().forEach(serviceUnit -> {
                //根据服务单找到对应订单
                OrderExample orderExample = new OrderExample();
                orderExample.or().andPay_order_idEqualTo(serviceUnit.getPay_order_id()).andStatus_activeEqualTo(5);
                Order order = DataAccessUtils.singleResult(this.orderService.selectByExample(orderExample));
                if(order!=null){
                    //找出整月结的数据
                    PartnerFallintoExample partnerFallintoExample = new PartnerFallintoExample();
                    partnerFallintoExample.or().andPartner_idEqualTo(serviceUnit.getPartner_id()).andProduct_idEqualTo(serviceUnit.getProduct_id())
                            .andBalance_typeEqualTo(2);
                    PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(this.partnerFallintoService.selectByExample(partnerFallintoExample));
                    if(partnerFallinto!=null){
                        packge(serviceUnit,order,partnerFallintoExample,floorPriceList,percentList,numList,moneyList,guestList);
                    }
                }

            });
        }

        ladderBalance(floorPriceList,percentList,numList,moneyList,guestList,localDate,2);
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
        switch (fallinto.getFallinto_type()) {
            case 1:
                OrderItemExample orderItemExample = new OrderItemExample();
                orderItemExample.or().andPay_order_idEqualTo(order.getPay_order_id());
                OrderItem orderItem = DataAccessUtils.singleResult(orderItemService.selectByExample(orderItemExample));
                balance=crateBalance(balance,fallinto, order,partner,localDate,serviceUnit);
                balance.setPartner_balance_fee(fallinto.getFloor_price()*orderItem.getNum());//合伙人订单结算金额
                balance.setBalance_fee(order.getPrice_total()-balance.getPartner_balance_fee());//平台订单结算金额
                break;
            case 2:
                balance = crateBalance(balance, fallinto, order, partner, localDate, serviceUnit);
                balance.setPartner_balance_fee((int) (order.getPrice_total() * Double.parseDouble(fallinto.getPercent()*0.01+"")));//合伙人订单结算金额
                balance.setBalance_fee(order.getPrice_total()-balance.getPartner_balance_fee());//平台订单结算金额
                break;
            case 3:
                balance = crateBalance(balance, fallinto, order, partner, localDate, serviceUnit);
                break;
            case 4:
                balance = crateBalance(balance, fallinto, order, partner, localDate, serviceUnit);
                break;
            case 5:
                balance = Moneyjudge(balance, fallinto, order,partner,localDate,serviceUnit);
                break;
        }
        return balance;
    }


    /**
     * 按客单价阶梯结算
     * @param balance
     * @param fallinto
     * @param order
     * @param partner
     * @param localDate
     * @param serviceUnit
     * @return
     */
    public BalanceOrder Moneyjudge(BalanceOrder balance, Fallinto fallinto, Order order,Partner partner,LocalDate localDate, ServiceUnit serviceUnit) {
        List<StepData> stepData = JSONArray.parseArray(fallinto.getStep_data(), StepData.class);
        List<StepData> newStepDataList=new ArrayList<>();
        if (fallinto.getStep_type() == 1) {
            if (stepData.size() != 0) {
                stepData.stream().forEach(step -> {
                    if(order.getPrice_total()>=step.getD()){
                        newStepDataList.add(step);
                    }
                });
            }
            if(!newStepDataList.isEmpty()){
                StepData sd = newStepDataList.get(newStepDataList.size() - 1);
                balance=crateBalance(balance,fallinto, order,partner,localDate,serviceUnit);
                balance.setPartner_balance_fee(Integer.parseInt(sd.getV())*100);
                balance.setBalance_fee(order.getPrice_total()-balance.getPartner_balance_fee());//平台订单结算金额
                balance.setFallinto_info(">=" + sd.getD() + "底价:" + sd.getV());//结算策略 命中说明
            }
        } else {
            if (stepData.size() != 0) {
                stepData.stream().forEach(step -> {
                    if(order.getPrice_total()>=step.getD()){
                        newStepDataList.add(step);

                    }
                });
            }
            if(!newStepDataList.isEmpty()){
                StepData sd = newStepDataList.get(newStepDataList.size() - 1);
                balance=crateBalance(balance,fallinto, order,partner,localDate,serviceUnit);
                balance.setPartner_balance_fee((int) (order.getPrice_total() * Double.parseDouble(Integer.parseInt(sd.getV())*0.01+"")));
                balance.setBalance_fee(order.getPrice_total()-balance.getPartner_balance_fee());//平台订单结算金额
                balance.setFallinto_info(">=" + sd.getD() + "比例:" + sd.getV());//结算策略 命中说明
            }
        }
        return balance;
    }

    /**
     * 根据策略进行数据封装
     * @param serviceUnit
     * @param order
     * @param partnerFallintoExample
     * @param floorPriceList
     * @param percentList
     * @param numList
     * @param moneyList
     * @param guestList
     */
    public void packge(ServiceUnit serviceUnit,Order order,PartnerFallintoExample partnerFallintoExample,
                      List<ServiceUnit> floorPriceList,List<ServiceUnit> percentList,List<ServiceUnit> numList,
                      List<ServiceUnit> moneyList,List<ServiceUnit> guestList){
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
                PartnerFallinto partnerFallinto = DataAccessUtils.singleResult(partnerFallintoService.selectByExample(partnerFallintoExample));
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



    /**
     * 进行封装数据（同一合伙人不同策略的不同产品）
     * @param list
     * @param fType
     * @param bType
     * @return
     */
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


    /**
     * 根据每个结算类型  进行对应策略结算
     * @param floorPriceList
     * @param percentList
     * @param numList
     * @param moneyList
     * @param guestList
     * @param localDate
     * @param bType
     */
    public void ladderBalance(List<ServiceUnit> floorPriceList,List<ServiceUnit> percentList,List<ServiceUnit> numList,
                              List<ServiceUnit> moneyList,List<ServiceUnit> guestList,LocalDate localDate,int bType){
        //底价
        if (!floorPriceList.isEmpty()) {
            floorPriceList.stream().forEach(serviceUnit -> {
                BalanceOrder balance = combine(LocalDate.now(), serviceUnit);
                balance.setFallinto_info("底价结算");
                this.balanceOrderService.insert(balance);
            });
        }

        //比例
        if (!percentList.isEmpty()) {
            percentList.stream().forEach(serviceUnit -> {
                BalanceOrder balance = combine(LocalDate.now(), serviceUnit);
                balance.setFallinto_info("比例结算");
                this.balanceOrderService.insert(balance);
            });
        }

        //单数阶梯
        if (!numList.isEmpty()) {
            Map<String, Map<String, List<ServiceUnit>>> mapList = partnerFallintoMap(numList,3,bType);
            mapList.forEach((String k, Map<String, List<ServiceUnit>> v) ->{
                Map<String, List<ServiceUnit>> stringListMap = mapList.get(k);
                stringListMap.forEach((key,val) ->{
                    List<StepData> newStepDataList=new ArrayList<>();
                    Fallinto fallinto = this.fallintoService.selectByPrimaryKey(Long.parseLong(key));
                    List<ServiceUnit> serviceUnitList = stringListMap.get(key);
                    List<StepData> stepData = com.alibaba.fastjson.JSONArray.parseArray(fallinto.getStep_data(), StepData.class);
                    if (fallinto.getStep_type() == 1) {
                        if (stepData.size() != 0) {
                            stepData.stream().forEach(step -> {
                                if (serviceUnitList.size() >= step.getD()) {
                                    newStepDataList.add(step);
                                }
                            });
                        }
                        if(!newStepDataList.isEmpty()){
                            StepData sd = newStepDataList.get(newStepDataList.size() - 1);
                            serviceUnitList.stream().forEach(serviceUnit -> {
                                Order order = this.orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
                                BalanceOrder balance = combine(LocalDate.now(), serviceUnit);
                                balance.setPartner_balance_fee(Integer.parseInt(sd.getV())*100);//合伙人订单结算金额
                                balance.setBalance_fee(order.getPrice_total()-balance.getPartner_balance_fee());//平台订单结算金额
                                balance.setFallinto_info(">=" + sd.getD() + "底价:" + sd.getV());//结算策略 命中说明
                                this.balanceOrderService.insert(balance);
                            });
                        }
                    } else {
                        if (stepData.size() != 0) {
                            stepData.stream().forEach(step -> {
                                if (serviceUnitList.size() >= step.getD()) {
                                    newStepDataList.add(step);
                                }
                            });
                        }
                        if(!newStepDataList.isEmpty()){
                            StepData step = newStepDataList.get(newStepDataList.size() - 1);
                            serviceUnitList.stream().forEach(serviceUnit -> {
                                Order order = this.orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
                                BalanceOrder balance = combine(LocalDate.now(), serviceUnit);
                                balance.setPartner_balance_fee((int) (order.getPrice_total() * Double.parseDouble(Integer.parseInt(step.getV())*0.01+"")));//合伙人订单结算金额
                                balance.setBalance_fee(order.getPrice_total()-balance.getPartner_balance_fee());//平台订单结算金额
                                balance.setFallinto_info(">=" + step.getD() + "比例:" + step.getV());//结算策略 命中说明
                                this.balanceOrderService.insert(balance);
                            });
                        }
                    }

                });
            });


        }

        //金额阶梯
        if (!moneyList.isEmpty()) {
            Map<String, Map<String, List<ServiceUnit>>> mapList = partnerFallintoMap(moneyList,4,bType);
            List<StepData> newStepDataList=new ArrayList<>();
            mapList.forEach((String k, Map<String, List<ServiceUnit>> v) ->{
                v.forEach((key,val) ->{
                    Fallinto fallinto = this.fallintoService.selectByPrimaryKey(Long.parseLong(key));
                    List<ServiceUnit> serviceUnitList = v.get(key);
                    List<StepData> stepData = com.alibaba.fastjson.JSONArray.parseArray(fallinto.getStep_data(), StepData.class);
                    int sumPrice=0;
                    for(ServiceUnit serviceUnit:serviceUnitList){
                        Order order = this.orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
                        sumPrice+=order.getPrice_total();
                    }
                    if (fallinto.getStep_type() == 1) {
                        if (stepData.size() != 0) {
                            for(StepData step:stepData){
                                if (sumPrice >= step.getD()) {
                                    newStepDataList.add(step);
                                }
                            }
                        }
                        if(!newStepDataList.isEmpty()){
                            StepData step = newStepDataList.get(newStepDataList.size() - 1);
                            serviceUnitList.stream().forEach(serviceUnit -> {
                                Order order = this.orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
                                BalanceOrder balance = combine(LocalDate.now(), serviceUnit);
                                balance.setPartner_balance_fee(Integer.parseInt(step.getV())*100);
                                balance.setBalance_fee(order.getPrice_total()-balance.getPartner_balance_fee());//平台订单结算金额
                                balance.setFallinto_info(">=" + step.getD() + "底价:" + step.getV());//结算策略 命中说明
                                this.balanceOrderService.insert(balance);
                            });
                        }
                    }else {
                        if (stepData.size() != 0) {
                            for(StepData step:stepData){
                                if (sumPrice >=step.getD()) {
                                    newStepDataList.add(step);
                                }
                            }
                        }
                        if(!newStepDataList.isEmpty()){
                            StepData step = newStepDataList.get(newStepDataList.size() - 1);
                            serviceUnitList.stream().forEach(serviceUnit -> {
                                Order order = this.orderService.selectByPrimaryKey(serviceUnit.getPay_order_id());
                                BalanceOrder balance = combine(LocalDate.now(), serviceUnit);
                                balance.setPartner_balance_fee((int) (order.getPrice_total() * Double.parseDouble(Integer.parseInt(step.getV())*0.01+"")));
                                balance.setBalance_fee(order.getPrice_total()-balance.getPartner_balance_fee());//平台订单结算金额
                                balance.setFallinto_info(">=" + step.getD() + "比例:" + step.getV());//结算策略 命中说明
                                this.balanceOrderService.insert(balance);
                            });
                        }
                    }
                });
            });
        }

        if (!guestList.isEmpty()) {
            guestList.stream().forEach(serviceUnit -> {
                BalanceOrder balance = combine(LocalDate.now(), serviceUnit);
                if(balance.getBalance_order_id() !=null){
                    this.balanceOrderService.insert(balance);
                }

            });
        }

    }

    /**
     * 创建结算数据
     */
    public BalanceOrder crateBalance(BalanceOrder balance, Fallinto fallinto, Order order,Partner partner,LocalDate localDate, ServiceUnit serviceUnit){
        balance.setBalance_order_id(IdGenerator.generateId());
        String month = localDate.getMonthValue() < 10 ? "0" + localDate.getMonthValue() : localDate.getMonthValue() + "";
        balance.setBalance_cycle(localDate.getYear() + month + "02");//结算期
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
        balance.setFallinto_id(fallinto.getFallinto_id());// 结算策略id
        balance.setFallinto_name(fallinto.getFallinto_name());//结算策略名称
        balance.setCreate_datetime(new Date());
        return balance;
    }
}



