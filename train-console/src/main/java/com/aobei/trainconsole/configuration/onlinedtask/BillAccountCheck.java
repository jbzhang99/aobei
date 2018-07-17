package com.aobei.trainconsole.configuration.onlinedtask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.aobei.common.boot.RedisIdGenerator;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainconsole.util.FileUtil;
import com.aobei.trainconsole.util.JacksonUtil;
import com.opencsv.bean.CsvToBeanBuilder;
import custom.bean.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.DownloadbillResult;
import weixin.popular.bean.paymch.MchDownloadbill;
import weixin.popular.util.MapUtil;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

/**
 * Created by mr_bl on 2018/6/21.
 */
@Configuration
public class BillAccountCheck {

    @Autowired
    private WxAppService wxAppService;

    @Autowired
    private WxMchService wxMchService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayWxNotifyService payWxNotifyService;

    @Autowired
    private RefundWxNotifyService refundWxNotifyService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private BillBatchService billBatchService;

    @Autowired
    private BillService billService;

    @Autowired
    private Environment env;

    private String profile;

    @Autowired
    private AliPayService aliPayService;

    @Autowired
    private PayAliNotifyService payAliNotifyService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    //写死的alipay------------appid
    private static String appid = "2018052960271202";
    private static String serverURL = "https://openapi.alipay.com/gateway.do";
    private static String format = "json";
    private static String signType = "RSA2";
    private static String charset = "UTF-8";

    @PostConstruct
    public void initProfile(){
        profile  = env.getActiveProfiles()[0];
    }


    //@Scheduled(cron = "0 0 10 * * ?")
//	@Scheduled(initialDelay = 2000, fixedDelay = 24*60*60*100)
    private void wx_account_check(){
        RedisIdGenerator idGenerator = new RedisIdGenerator();
        idGenerator.setRedisTemplate(redisTemplate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format_hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int mouth = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String bill_date = year + (mouth > 9 ? mouth +"" : "0"+mouth) +((day-1) > 9 ? (day-1) +"" : "0"+ (day-1));
        String today_date = year + (mouth > 9 ? mouth +"" : "0"+mouth) +(day > 9 ? day +"" : "0"+ day);

        long autoIncrId = idGenerator.getAutoIncrNum("WXC"+today_date);
        if (autoIncrId != 1){
            return;
        }

        Calendar todayCalendar = Calendar.getInstance();
        Calendar predayCalendar = Calendar.getInstance();
        todayCalendar.set(year,mouth-1,day,0,0,0);
        Date today = todayCalendar.getTime();
        predayCalendar.set(year,mouth-1,day-1,0,0,0);
        Date preday = predayCalendar.getTime();

        String path_jsapi = "wx_m_custom";
        String path_app = "wx_app_custom";
        /**
         * 数据源1 wx-bill
         */
        //小程序的对账单
        WxDownloadbill bill_jsapi = getbill(bill_date,path_jsapi);
        //微信app的对账单
        WxDownloadbill bill_app = getbill(bill_date,path_app);

        WxDownloadbill getbill = new WxDownloadbill();
        getbill.setGetDatetime(date);
        List<TradeOrder> list = new ArrayList<>();

        double total_trade_amount = 0.0;
        double total_refund_amount = 0.0;
        double total_corporate_red_envelope_refund_amount = 0.0;
        double total_service_charge = 0.0;
        if (bill_jsapi != null){
            list.addAll(bill_jsapi.getTradeOrders());
            total_trade_amount += Double.valueOf(bill_jsapi.getTotal_trade_amount());
            total_refund_amount += Double.valueOf(bill_jsapi.getTotal_refund_amount());
            total_corporate_red_envelope_refund_amount += Double.valueOf(bill_jsapi.getTotal_corporate_red_envelope_refund_amount());
            total_service_charge += Double.valueOf(bill_jsapi.getTotal_service_charge());
        }
        if (bill_app != null){
            list.addAll(bill_app.getTradeOrders());
            total_trade_amount += Double.valueOf(bill_app.getTotal_trade_amount());
            total_refund_amount += Double.valueOf(bill_app.getTotal_refund_amount());
            total_corporate_red_envelope_refund_amount += Double.valueOf(bill_app.getTotal_corporate_red_envelope_refund_amount());
            total_service_charge += Double.valueOf(bill_app.getTotal_service_charge());
        }

        getbill.setTradeOrders(list);
        getbill.setTotal_trade_amount(total_trade_amount + "");
        getbill.setTotal_trade_num(list.size()+"");
        getbill.setTotal_refund_amount(total_refund_amount + "");
        getbill.setTotal_corporate_red_envelope_refund_amount(total_corporate_red_envelope_refund_amount + "");
        getbill.setTotal_service_charge(total_service_charge + "");

        /**
         * 数据源2：
         * 		拿到前一天的商家已支付订单数据
         */
        OrderExample orderExample = new OrderExample();
        orderExample.or().andPay_statusEqualTo(1).andPay_datetimeBetween(preday,today).andPay_typeEqualTo(1);
        List<String> order_ids = orderService.selectByExample(orderExample)
                .stream().map(n -> n.getPay_order_id()).collect(Collectors.toList());
        PayWxNotifyExample example = new PayWxNotifyExample();
        PayWxNotifyExample.Criteria or = example.or();
        or.andTrade_stateEqualTo(0);
        if (order_ids.size() > 0 ){
            or.andOut_trade_noIn(order_ids);
        }else{
            or.andOut_trade_noEqualTo("0");
        }
        List<PayWxNotify> orders = payWxNotifyService.selectByExample(example);
        /**
         * 数据源2：
         * 		拿到前一天的商家已退款的退款单数据
         */
        RefundWxNotifyExample example1 = new RefundWxNotifyExample();
        example1.or().andRefund_statusEqualTo("SUCCESS").andSuccess_timeBetween(format_hms.format(preday),format_hms.format(today));
        List<RefundWxNotify> refunds = refundWxNotifyService.selectByExample(example1);
        /**
         * 数据源2：
         * 		缓冲列表单
         */
        BillExample billExample = new BillExample();
        billExample.or().andBill_statusEqualTo(2)
                .andBuffer_sizeIsNotNull()
                .andBuffer_sizeBetween(0,2)
                .andBill_typeEqualTo(1)
                .andPay_typeEqualTo(1);
        List<Bill> bills = billService.selectByExample(billExample);

        String formated = format.format(date);
        Date parse = null;
        try {
            parse = format.parse(formated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**
         * 为生产批次表的属性初始化
         */
        int my_total_fee = 0;//我方总金额   单位分
        int my_total_refund_fee = 0;//我方总退款金额
        int status_count_1 = 0;//对账正常 的总数
        int status_count_2 = 0;//对账长账 总数
        int status_count_3 = 0;//对账短账 总数
        int status_count_4 = 0;//对账支付金额不一致总数
        int status_count_5 = 0;//对账退款金额不一致总数
        for (PayWxNotify payWxNotify : orders) {
            my_total_fee = my_total_fee + payWxNotify.getTotal_fee();
        }
        for (RefundWxNotify notify : refunds) {
            my_total_refund_fee = my_total_refund_fee + notify.getRefund_fee();
        }

        /**
         * 生产批次表
         */
        BillBatch billBatch = new BillBatch();
        billBatch.setBill_batch_id(today_date+"Q01"+"P001");
        billBatch.setPay_type(1);
        billBatch.setBill_datetime(date);
        billBatch.setBill_date(parse);
        billBatch.setStart_datetime(date);//对账时间范围 开始

        billBatch.setMy_total_orders(orders.size()+refunds.size());//我方总单数
        List<TradeOrder> tradeOrders = new ArrayList<>();
        if (getbill != null){
            Integer p_total_orders = Integer.valueOf(getbill.getTotal_trade_num());
            int p_total_fee = (int) (Double.valueOf(getbill.getTotal_trade_amount()) * 100);
            int p_total_service_charge = (int) (Double.valueOf(getbill.getTotal_service_charge()) * 100);
            int p_total_refund_fee = (int) (Double.valueOf(getbill.getTotal_refund_amount()) * 100);
            billBatch.setP_total_orders(p_total_orders);//第三方平台总订单数
            billBatch.setP_total_fee(p_total_fee);//第三方平台总金额  单位分
            billBatch.setP_total_service_charge(p_total_service_charge);//第三方平台总手续费
            billBatch.setP_total_refund_fee(p_total_refund_fee);//第三方平台总退款金额
            tradeOrders = getbill.getTradeOrders();
        } else {
            billBatch.setP_total_orders(0);//第三方平台总订单数
            billBatch.setP_total_fee(0);//第三方平台总金额  单位分
            billBatch.setP_total_service_charge(0);//第三方平台总手续费
            billBatch.setP_total_refund_fee(0);//第三方平台总退款金额
        }

        /**
         * ******************************************************************************************
         * 	pays_bill_map,refunds_bill_map缓冲单     pays_map，refunds_map 当天单  					*
         * ******************************************************************************************
         */
        Map<String, Bill> pays_bill_map = new HashMap<>();
        Map<String, Bill> refunds_bill_map = new HashMap<>();
        Map<String, PayWxNotify> pays_map = orders.stream().collect(Collectors.toMap(PayWxNotify::getTransaction_id, Function.identity()));
        Map<String, RefundWxNotify> refunds_map = refunds.stream().collect(Collectors.toMap(RefundWxNotify::getTransaction_id, Function.identity()));
        if (bills.size() > 0){
            List<Bill> pays_bills = bills.stream().filter(n -> n.getOrder_create_datetime() != null).collect(Collectors.toList());
            pays_bill_map = pays_bills.stream().collect(Collectors.toMap(Bill::getTransaction_id,Function.identity()));
            List<Bill> refunds_bills = bills.stream().filter(n -> n.getOrder_create_datetime() == null).collect(Collectors.toList());
            refunds_bill_map = refunds_bills.stream().collect(Collectors.toMap(Bill::getTransaction_id,Function.identity()));
        }

        boolean ispay = false;
        boolean isnormal = false;

        if (tradeOrders.size() > 0){
            /**
             * 拿到的数据拆分
             * 		wx_pays_map -- 支付单
             * 		wx_refunds_map  -- 退款单
             */
            List<TradeOrder> pay_tra_orders = tradeOrders.stream().filter(n -> "SUCCESS".equals(n.getTrade_status())).collect(Collectors.toList());
            Map<String, TradeOrder> wx_pays_map = pay_tra_orders.stream().collect(Collectors.toMap(TradeOrder::getWx_pay_order_id, Function.identity()));
            List<TradeOrder> refund_tra_orders = tradeOrders.stream().filter(n -> "REFUND".equals(n.getTrade_status())).collect(Collectors.toList());
            Map<String, TradeOrder> wx_refunds_map = refund_tra_orders.stream().collect(Collectors.toMap(TradeOrder::getWx_pay_order_id, Function.identity()));

            Iterator<Map.Entry<String, TradeOrder>> wx_iterator = wx_pays_map.entrySet().iterator();
            List<BillTemp> normal_list = new ArrayList<>();
            while (wx_iterator.hasNext()){
                Map.Entry<String, TradeOrder> wx_entry = wx_iterator.next();
                String wxkey = wx_entry.getKey();
                Iterator<Map.Entry<String, PayWxNotify>> iterator = pays_map.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, PayWxNotify> entry = iterator.next();
                    String key = entry.getKey();
                    if (wxkey.equals(key)){
                        TradeOrder tradeOrder = wx_entry.getValue();
                        PayWxNotify wxNotify = entry.getValue();
                        BillTemp temp = new BillTemp();
                        temp.setTradeOrder(tradeOrder);
                        temp.setWxNotify(wxNotify);
                        normal_list.add(temp);
                        wx_iterator.remove();
                        iterator.remove();
                    }
                }
            }

            Iterator<Map.Entry<String, TradeOrder>> wx_refund_iterator = wx_refunds_map.entrySet().iterator();
            while (wx_refund_iterator.hasNext()){
                Map.Entry<String, TradeOrder> wx_refund_entry = wx_refund_iterator.next();
                Iterator<Map.Entry<String, RefundWxNotify>> refund_iterator = refunds_map.entrySet().iterator();
                while (refund_iterator.hasNext()){
                    Map.Entry<String, RefundWxNotify> refund_entry = refund_iterator.next();
                    if (wx_refund_entry.getKey().equals(refund_entry.getKey())){
                        TradeOrder tradeOrder = wx_refund_entry.getValue();
                        RefundWxNotify refundWxNotify = refund_entry.getValue();
                        BillTemp temp = new BillTemp();
                        temp.setTradeOrder(tradeOrder);
                        temp.setRefundWxNotify(refundWxNotify);
                        normal_list.add(temp);
                        wx_refund_iterator.remove();
                        refund_iterator.remove();
                    }
                }
            }

            //支付单   进缓存----长帐
            if (pays_map.size() > 0){
                Iterator<Map.Entry<String, PayWxNotify>> iterator2 = pays_map.entrySet().iterator();
                while (iterator2.hasNext()){
                    status_count_2 += 1;
                    Map.Entry<String, PayWxNotify> entry = iterator2.next();
                    PayWxNotify wxNotify = entry.getValue();
                    Order order = orderService.selectByPrimaryKey(wxNotify.getOut_trade_no());
                    ispay = true;
                    insertCacheBill(order,billBatch,wxNotify,null,null,ispay, null);
                }
            }
            //退款单 进缓存-----长帐
            if (refunds_map.size() > 0){
                Iterator<Map.Entry<String, RefundWxNotify>> iterator2 = refunds_map.entrySet().iterator();
                while (iterator2.hasNext()){
                    status_count_2 += 1;
                    Map.Entry<String, RefundWxNotify> entry = iterator2.next();
                    RefundWxNotify refundWxNotify = entry.getValue();
                    Refund refund = refundService.selectByPrimaryKey(Long.valueOf(refundWxNotify.getOut_refund_no()));
                    ispay = false;
                    insertCacheBill(null,billBatch,null,refund,refundWxNotify,ispay, null);
                }
            }

            //正常的
            for (BillTemp n : normal_list) {
                //对退款单
                if (n.getRefundWxNotify() != null) {
                    ispay = false;
                    RefundWxNotify refundWxNotify = n.getRefundWxNotify();
                    Refund refund = refundService.selectByPrimaryKey(Long.valueOf(refundWxNotify.getOut_refund_no()));
                    TradeOrder tradeOrder = n.getTradeOrder();
                    if (refundWxNotify.getRefund_fee().equals((int)(Double.valueOf(tradeOrder.getRefund_amount()) * 100))) {
                        status_count_1 += 1;
                        isnormal = true;
                        insertNormalBill(tradeOrder,billBatch,refund,refundWxNotify,null,null,ispay,isnormal, null, null);
                    } else {
                        //对账金额不一致
                        status_count_5 += 1;
                        isnormal = false;
                        insertNormalBill(tradeOrder,billBatch,refund,refundWxNotify,null,null,ispay,isnormal, null, null);
                    }
                }
                //对支付单
                if (n.getWxNotify() != null) {
                    ispay = true;
                    PayWxNotify wxNotify = n.getWxNotify();
                    Order order = orderService.selectByPrimaryKey(wxNotify.getOut_trade_no());
                    TradeOrder tradeOrder = n.getTradeOrder();
                    if (wxNotify.getTotal_fee().equals((int)(Double.valueOf(tradeOrder.getTotal_amount()) * 100))) {
                        status_count_1 += 1;
                        isnormal = true;
                        insertNormalBill(tradeOrder,billBatch,null,null,order,wxNotify,ispay,isnormal, null, null);
                    } else {
                        status_count_4 += 1;
                        //对账金额不一致
                        isnormal = false;
                        insertNormalBill(tradeOrder,billBatch,null,null,order,wxNotify,ispay,isnormal, null, null);
                    }
                }
            }

            //对完正常的还有剩下            对缓存
            if (wx_pays_map.size() > 0){
                ispay = true;
                Iterator<Map.Entry<String, TradeOrder>> iterator2 = wx_pays_map.entrySet().iterator();
                while(iterator2.hasNext()){
                    Map.Entry<String, TradeOrder> wx_entry = iterator2.next();
                    Iterator<Map.Entry<String, Bill>> iterator1 = pays_bill_map.entrySet().iterator();
                    while (iterator1.hasNext()){
                        Map.Entry<String, Bill> billEntry = iterator1.next();
                        if (wx_entry.getKey().equals(billEntry.getKey())){
                            TradeOrder tradeOrder = wx_entry.getValue();
                            Bill bill = billEntry.getValue();
                            if (bill.getPrice_pay().equals((int)(Double.valueOf(tradeOrder.getTotal_amount())*100))){
                                isnormal = true;
                                updateCacheBill(bill,billBatch,tradeOrder,ispay,isnormal, null);
                            } else {
                                isnormal = false;
                                updateCacheBill(bill,billBatch,tradeOrder,ispay,isnormal, null);
                            }
                            iterator1.remove();
                            iterator2.remove();
                        }
                    }

                }
            }
            //对完正常的还有剩下            对缓存
            if (wx_refunds_map.size() > 0){
                ispay = false;
                Iterator<Map.Entry<String, TradeOrder>> iterator2 = wx_refunds_map.entrySet().iterator();
                while(iterator2.hasNext()){
                    Map.Entry<String, TradeOrder> r_entry = iterator2.next();
                    Iterator<Map.Entry<String, Bill>> iterator1 = refunds_bill_map.entrySet().iterator();
                    while(iterator1.hasNext()){
                        Map.Entry<String, Bill> billEntry = iterator1.next();
                        if (r_entry.getKey().equals(billEntry.getKey())){
                            TradeOrder tradeOrder = r_entry.getValue();
                            Bill bill = billEntry.getValue();
                            if (bill.getR_fee().equals((int)(Double.valueOf(tradeOrder.getRefund_amount())*100))){
                                isnormal = true;
                                updateCacheBill(bill,billBatch,tradeOrder,ispay,isnormal, null);
                            } else {
                                isnormal = false;
                                updateCacheBill(bill,billBatch,tradeOrder,ispay,isnormal, null);
                            }
                            iterator1.remove();
                            iterator2.remove();
                        }
                    }
                }
            }
            //进差错
            if (wx_pays_map.size() > 0){
                ispay = true;
                Iterator<Map.Entry<String, TradeOrder>> iterators = wx_pays_map.entrySet().iterator();
                while(iterators.hasNext()){
                    status_count_3 += 1;
                    Map.Entry<String, TradeOrder> entry = iterators.next();
                    TradeOrder tradeOrder = entry.getValue();
                    insertErrorBill(tradeOrder,billBatch,ispay, null);
                }
            }
            if (wx_refunds_map.size() > 0){
                ispay = false;
                Iterator<Map.Entry<String, TradeOrder>> iterators = wx_refunds_map.entrySet().iterator();
                while (iterators.hasNext()){
                    status_count_3 += 1;
                    Map.Entry<String, TradeOrder> entry = iterators.next();
                    TradeOrder tradeOrder = entry.getValue();
                    insertErrorBill(tradeOrder,billBatch,ispay, null);
                }
            }
        }else{
            billBatch.setError_info("当天没有生成的对账单！");//第三方平台返回错误信息
        }

        //缓存单减次数
        if (pays_bill_map.size() > 0){
            Iterator<Map.Entry<String, Bill>> iterators = pays_bill_map.entrySet().iterator();
            updateBufferSize(iterators,new Date());
        }
        if (refunds_bill_map.size() > 0){
            Iterator<Map.Entry<String, Bill>> iterators = refunds_bill_map.entrySet().iterator();
            updateBufferSize(iterators,new Date());
        }

        billBatch.setMy_total_fee(my_total_fee);//我方总金额   单位分
        billBatch.setMy_total_refund_fee(my_total_refund_fee);//我方总退款金额
        billBatch.setStatus_count_1(status_count_1);//对账正常 的总数
        billBatch.setStatus_count_2(status_count_2);//对账长账 总数
        billBatch.setStatus_count_3(status_count_3);//对账短账 总数
        billBatch.setStatus_count_4(status_count_4);//对账支付金额不一致总数
        billBatch.setStatus_count_5(status_count_5);//对账退款金额不一致总数
        billBatch.setException_info("");//程序异常信息
        billBatch.setEnd_datetime(new Date());//对账时间范围 结束
        billBatchService.insertSelective(billBatch);
    }

    //@Scheduled(cron = "0 0 7 * * ?")
    //@Scheduled(initialDelay = 2000, fixedDelay = 24*60*60*100)
    private void ali_account_check(){
        RedisIdGenerator idGenerator = new RedisIdGenerator();
        idGenerator.setRedisTemplate(redisTemplate);
        SimpleDateFormat format_hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 1. 获取当前日期(年月日)
        LocalDate localDate = LocalDate.now();
        // 2. 前一天
        LocalDate yesterday = localDate.minusDays(1L);

        long autoIncrId = idGenerator.getAutoIncrNum("ALC"+localDate.toString());
        if (autoIncrId != 1){
            return;
        }

        /**
         * 获取支付宝返回的对账单数据
         */
        AliDownloadbill alibill = getAlibill(yesterday.toString());

        Date today = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date preday = Date.from(yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant());
        /**
         * 查询前一天的所有的支付宝交易单(包含退款)
         */
        PayAliNotifyExample example = new PayAliNotifyExample();
        String pre = format_hms.format(preday);
        String tod = format_hms.format(today);
        example.or().andNotify_timeGreaterThanOrEqualTo(pre).andNotify_timeLessThanOrEqualTo(tod);
        List<PayAliNotify> payAliNotifies = payAliNotifyService.selectByExample(example);

        List<PayAliNotify> orders = payAliNotifies
                .stream().filter(n -> n.getOut_biz_no() == null).collect(Collectors.toList());
        Map<String, PayAliNotify> pays_map = orders.stream()
                .collect(Collectors.toMap(PayAliNotify::getTrade_no, Function.identity()));

        List<PayAliNotify> refunds = payAliNotifies
                .stream().filter(n -> n.getOut_biz_no() != null).collect(Collectors.toList());
        Map<String, PayAliNotify> refunds_map = refunds.stream()
                .collect(Collectors.toMap(PayAliNotify::getTrade_no, Function.identity()));

        /**
         * **************************************************************
         * 	pays_bill_map,refunds_bill_map缓冲单      					*
         * **************************************************************
         */
        BillExample billExample = new BillExample();
        billExample.or().andBill_statusEqualTo(2)
                .andBuffer_sizeIsNotNull()
                .andBuffer_sizeBetween(0,2)
                .andBill_typeEqualTo(1)
                .andPay_typeEqualTo(2);
        List<Bill> bills = billService.selectByExample(billExample);
        Map<String, Bill> pays_bill_map = new HashMap<>();
        Map<String, Bill> refunds_bill_map = new HashMap<>();
        if (bills.size() > 0){
            pays_bill_map = bills
                    .stream().filter(n -> n.getOrder_create_datetime() != null)
                    .collect(Collectors.toMap(Bill::getTransaction_id,Function.identity()));
            refunds_bill_map = bills
                    .stream().filter(n -> n.getOrder_create_datetime() == null)
                    .collect(Collectors.toMap(Bill::getTransaction_id,Function.identity()));
        }

        /**
         * 为生产批次表的属性初始化
         */
        int my_total_fee = 0;//我方总金额   单位分
        int my_total_refund_fee = 0;//我方总退款金额
        int status_count_1 = 0;//对账正常 的总数
        int status_count_2 = 0;//对账长账 总数
        int status_count_3 = 0;//对账短账 总数
        int status_count_4 = 0;//对账支付金额不一致总数
        int status_count_5 = 0;//对账退款金额不一致总数
        for (PayAliNotify notify : orders) {
            my_total_fee = my_total_fee + notify.getTotal_amount();
        }
        for (PayAliNotify notify : refunds) {
            my_total_refund_fee = my_total_refund_fee + notify.getRefund_fee();
        }

        /**
         * 生产批次表
         */
        BillBatch billBatch = new BillBatch();
        billBatch.setBill_batch_id(localDate.toString().replace("-","") + "Q02" + "P002");
        billBatch.setPay_type(2);
        billBatch.setBill_datetime(new Date());
        billBatch.setBill_date(today);
        billBatch.setStart_datetime(new Date());//对账时间范围 开始

        billBatch.setMy_total_orders(orders.size()+refunds.size());//我方总单数
        if (alibill != null){
            Integer p_total_orders = Integer.valueOf(alibill.getTotal_trade_num());
            int p_total_fee = (int) (Double.valueOf(alibill.getTotal_trade_amount()) * 100);
            int p_total_service_charge = (int) (Double.valueOf(alibill.getTotal_service_charge()) * 100);
            int p_total_refund_fee = (int) (Double.valueOf(alibill.getTotal_refund_amount()) * 100);
            billBatch.setP_total_orders(p_total_orders);//第三方平台总订单数
            billBatch.setP_total_fee(p_total_fee);//第三方平台总金额  单位分
            billBatch.setP_total_service_charge(p_total_service_charge);//第三方平台总手续费
            billBatch.setP_total_refund_fee(p_total_refund_fee);//第三方平台总退款金额
        } else {
            billBatch.setP_total_orders(0);//第三方平台总订单数
            billBatch.setP_total_fee(0);//第三方平台总金额  单位分
            billBatch.setP_total_service_charge(0);//第三方平台总手续费
            billBatch.setP_total_refund_fee(0);//第三方平台总退款金额
        }

        boolean ispay = false;
        boolean isnormal = false;

        List<AliTradeOrder> tradeOrders = alibill.getTradeOrders();
        if (tradeOrders.size() > 0){
            Map<String, AliTradeOrder> ali_pays_map = tradeOrders
                    .stream().filter(n -> "交易".equals(n.getTrade_type()))
                    .collect(Collectors.toMap(AliTradeOrder::getAli_pay_order_id, Function.identity()));
            Map<String, AliTradeOrder> ali_refunds_map = tradeOrders
                    .stream().filter(n -> "退款".equals(n.getTrade_type()))
                    .collect(Collectors.toMap(AliTradeOrder::getAli_pay_order_id, Function.identity()));

            Iterator<Map.Entry<String, AliTradeOrder>> ali_iterator = ali_pays_map.entrySet().iterator();
            List<BillTemp> normal_list = new ArrayList<>();
            while (ali_iterator.hasNext()){
                Map.Entry<String, AliTradeOrder> ali_entry = ali_iterator.next();
                String alikey = ali_entry.getKey();
                Iterator<Map.Entry<String, PayAliNotify>> iterator = pays_map.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, PayAliNotify> entry = iterator.next();
                    String key = entry.getKey();
                    if (alikey.equals(key)){
                        AliTradeOrder tradeOrder = ali_entry.getValue();
                        PayAliNotify notify = entry.getValue();
                        BillTemp temp = new BillTemp();
                        temp.setAliTradeOrder(tradeOrder);
                        temp.setPayAliNotify(notify);
                        normal_list.add(temp);
                        ali_iterator.remove();
                        iterator.remove();
                    }
                }
            }

            Iterator<Map.Entry<String, AliTradeOrder>> ali_refund_iterator = ali_refunds_map.entrySet().iterator();
            while (ali_refund_iterator.hasNext()){
                Map.Entry<String, AliTradeOrder> ali_refund_entry = ali_refund_iterator.next();
                Iterator<Map.Entry<String, PayAliNotify>> refund_iterator = refunds_map.entrySet().iterator();
                while (refund_iterator.hasNext()){
                    Map.Entry<String, PayAliNotify> refund_entry = refund_iterator.next();
                    if (ali_refund_entry.getKey().equals(refund_entry.getKey())){
                        AliTradeOrder tradeOrder = ali_refund_entry.getValue();
                        PayAliNotify notify = refund_entry.getValue();
                        BillTemp temp = new BillTemp();
                        temp.setAliTradeOrder(tradeOrder);
                        temp.setRefundAliNotify(notify);
                        normal_list.add(temp);
                        ali_refund_iterator.remove();
                        refund_iterator.remove();
                    }
                }
            }

            //支付单   进缓存----长帐
            if (pays_map.size() > 0){
                Iterator<Map.Entry<String, PayAliNotify>> iteratorRemain = pays_map.entrySet().iterator();
                while (iteratorRemain.hasNext()){
                    status_count_2 += 1;
                    Map.Entry<String, PayAliNotify> entry = iteratorRemain.next();
                    PayAliNotify notify = entry.getValue();
                    Order order = orderService.selectByPrimaryKey(notify.getOut_trade_no());
                    ispay = true;
                    insertCacheBill(order,billBatch,null,null,null,ispay,notify);
                }
            }
            //退款单 进缓存-----长帐
            if (refunds_map.size() > 0){
                Iterator<Map.Entry<String, PayAliNotify>> iteratorRemain = refunds_map.entrySet().iterator();
                while (iteratorRemain.hasNext()){
                    status_count_2 += 1;
                    Map.Entry<String, PayAliNotify> entry = iteratorRemain
                            .next();
                    PayAliNotify refundNotify = entry.getValue();
                    Refund refund = refundService.selectByPrimaryKey(Long.valueOf(refundNotify.getOut_biz_no()));
                    ispay = false;
                    insertCacheBill(null,billBatch,null,refund,null,ispay,refundNotify);
                }
            }

            //正常的
            for (BillTemp n : normal_list) {
                //对退款单
                if (n.getRefundAliNotify() != null) {
                    ispay = false;
                    PayAliNotify refundAliNotify = n.getRefundAliNotify();
                    Refund refund = refundService.selectByPrimaryKey(Long.valueOf(refundAliNotify.getOut_biz_no()));
                    AliTradeOrder aliTradeOrder = n.getAliTradeOrder();
                    if (refundAliNotify.getRefund_fee().equals((int)(Double.valueOf(aliTradeOrder.getTotal_amount().replace("-","")) * 100))) {
                        status_count_1 += 1;
                        isnormal = true;
                        insertNormalBill(null,billBatch,refund,null,null,null,ispay,isnormal,aliTradeOrder,refundAliNotify);
                    } else {
                        //对账金额不一致
                        status_count_5 += 1;
                        isnormal = false;
                        insertNormalBill(null,billBatch,refund,null,null,null,ispay,isnormal,aliTradeOrder,refundAliNotify);
                    }
                }
                //对支付单
                if (n.getPayAliNotify() != null) {
                    ispay = true;
                    PayAliNotify payAliNotify = n.getPayAliNotify();
                    Order order = orderService.selectByPrimaryKey(payAliNotify.getOut_trade_no());
                    AliTradeOrder aliTradeOrder = n.getAliTradeOrder();
                    if (payAliNotify.getTotal_amount().equals((int)(Double.valueOf(aliTradeOrder.getTotal_amount()) * 100))) {
                        status_count_1 += 1;
                        isnormal = true;
                        insertNormalBill(null,billBatch,null,null,order,null,ispay,isnormal,aliTradeOrder,payAliNotify);
                    } else {
                        status_count_4 += 1;
                        //对账金额不一致
                        isnormal = false;
                        insertNormalBill(null,billBatch,null,null,order,null,ispay,isnormal,aliTradeOrder,payAliNotify);
                    }
                }
            }

            //对完正常的还有剩下            对缓存
            if (ali_pays_map.size() > 0){
                ispay = true;
                Iterator<Map.Entry<String, AliTradeOrder>> iteratorRemain = ali_pays_map.entrySet().iterator();
                while(iteratorRemain.hasNext()){
                    Map.Entry<String, AliTradeOrder> ali_entry = iteratorRemain.next();
                    Iterator<Map.Entry<String, Bill>> iteratorBillRemain = pays_bill_map.entrySet().iterator();
                    while (iteratorBillRemain.hasNext()){
                        Map.Entry<String, Bill> billEntry = iteratorBillRemain.next();
                        if (ali_entry.getKey().equals(billEntry.getKey())){
                            AliTradeOrder tradeOrder = ali_entry.getValue();
                            Bill bill = billEntry.getValue();
                            if (bill.getPrice_pay().equals((int)(Double.valueOf(tradeOrder.getTotal_amount())*100))){
                                isnormal = true;
                                updateCacheBill(bill,billBatch,null,ispay,isnormal,tradeOrder);
                            } else {
                                isnormal = false;
                                updateCacheBill(bill,billBatch,null,ispay,isnormal,tradeOrder);
                            }
                            iteratorBillRemain.remove();
                            iteratorRemain.remove();
                        }
                    }

                }
            }
            //对完正常的还有剩下            对缓存
            if (ali_refunds_map.size() > 0){
                ispay = false;
                Iterator<Map.Entry<String, AliTradeOrder>> iteratorRemain = ali_refunds_map.entrySet().iterator();
                while(iteratorRemain.hasNext()){
                    Map.Entry<String, AliTradeOrder> r_entry = iteratorRemain.next();
                    Iterator<Map.Entry<String, Bill>> iteratorBillRemain = refunds_bill_map.entrySet().iterator();
                    while(iteratorBillRemain.hasNext()){
                        Map.Entry<String, Bill> billEntry = iteratorBillRemain.next();
                        if (r_entry.getKey().equals(billEntry.getKey())){
                            AliTradeOrder tradeOrder = r_entry.getValue();
                            Bill bill = billEntry.getValue();
                            if (bill.getR_fee().equals((int)(Double.valueOf(tradeOrder.getTotal_amount().replace("-",""))*100))){
                                isnormal = true;
                                updateCacheBill(bill,billBatch,null,ispay,isnormal,tradeOrder);
                            } else {
                                isnormal = false;
                                updateCacheBill(bill,billBatch,null,ispay,isnormal,tradeOrder);
                            }
                            iteratorBillRemain.remove();
                            iteratorRemain.remove();
                        }
                    }
                }
            }

            //进差错
            if (ali_pays_map.size() > 0){
                ispay = true;
                Iterator<Map.Entry<String, AliTradeOrder>> iterators = ali_pays_map.entrySet().iterator();
                while(iterators.hasNext()){
                    status_count_3 += 1;
                    Map.Entry<String, AliTradeOrder> entry = iterators.next();
                    AliTradeOrder tradeOrder = entry.getValue();
                    insertErrorBill(null,billBatch,ispay,tradeOrder);
                }
            }
            if (ali_refunds_map.size() > 0){
                ispay = false;
                Iterator<Map.Entry<String, AliTradeOrder>> iterators = ali_refunds_map.entrySet().iterator();
                while (iterators.hasNext()){
                    status_count_3 += 1;
                    Map.Entry<String, AliTradeOrder> entry = iterators.next();
                    AliTradeOrder tradeOrder = entry.getValue();
                    insertErrorBill(null,billBatch,ispay,tradeOrder);
                }
            }

        } else {
            billBatch.setError_info("当天没有生成的对账单！");//第三方平台返回错误信息
        }

        //缓存单减次数
        if (pays_bill_map.size() > 0){
            Iterator<Map.Entry<String, Bill>> iterators = pays_bill_map.entrySet().iterator();
            updateBufferSize(iterators,new Date());
        }
        if (refunds_bill_map.size() > 0){
            Iterator<Map.Entry<String, Bill>> iterators = refunds_bill_map.entrySet().iterator();
            updateBufferSize(iterators,new Date());
        }

        billBatch.setMy_total_fee(my_total_fee);//我方总金额   单位分
        billBatch.setMy_total_refund_fee(my_total_refund_fee);//我方总退款金额
        billBatch.setStatus_count_1(status_count_1);//对账正常 的总数
        billBatch.setStatus_count_2(status_count_2);//对账长账 总数
        billBatch.setStatus_count_3(status_count_3);//对账短账 总数
        billBatch.setStatus_count_4(status_count_4);//对账支付金额不一致总数
        billBatch.setStatus_count_5(status_count_5);//对账退款金额不一致总数
        billBatch.setException_info("");//程序异常信息
        billBatch.setEnd_datetime(new Date());//对账时间范围 结束
        billBatchService.insertSelective(billBatch);
    }

    /**
     * 获取当天前一天的wx对账单数据
     * @param bill_date
     * @return
     */
    public WxDownloadbill getbill(String bill_date,String path){
        WxAppExample wxAppExample = new WxAppExample();
        wxAppExample.or().andPathEqualTo(path);
        WxApp wxApp = singleResult(wxAppService.selectByExample(wxAppExample));
        WxMch wxMch = wxMchService.selectByPrimaryKey(wxApp.getMch_id());
        String wxKey = wxMch.getMch_key();
        String appid = wxApp.getApp_id();
        String mch_id = wxMch.getMch_id();
        MchDownloadbill mchDownloadbill = new MchDownloadbill();
        mchDownloadbill.setAppid(appid);
        mchDownloadbill.setMch_id(mch_id);
        mchDownloadbill.setNonce_str(IdGenerator.generateId() + "");
        mchDownloadbill.setSign_type("MD5");
        Map<String,String> map = MapUtil.objectToMap(mchDownloadbill);
        mchDownloadbill.setBill_date(bill_date);//前一天
        mchDownloadbill.setBill_type("ALL");
        DownloadbillResult result = PayMchAPI.payDownloadbill(mchDownloadbill, wxKey);
        String return_msg = result.getReturn_msg();
        /**
         * 数据源1：
         */
        WxDownloadbill bill = null;
        if (!"No Bill Exist".equals(return_msg) && result != null && result.getData() != null){
            String data = result.getData();
            String s = data.replace("`", "");
            System.out.println(s);
            //截取对账单分条数据
            String billStr = StringUtils.substringBeforeLast(s, "总交易单数");
            //截取对账单汇总数据
            String summary = StringUtils.substringAfterLast(s, "%\r\n");
            //此处特殊处理为解决交易时间封装不上的问题
            billStr = billStr.replaceAll ("\r\n","\r\nnone,");
            billStr = billStr.replace ("交易时间","none,交易时间");
            billStr = StringUtils.substringBeforeLast(billStr,"\r\n");

            List<TradeOrder> tradeOrders = new CsvToBeanBuilder(new StringReader(billStr))
                    .withType(TradeOrder.class).build().parse();

            List<WxDownloadbill> bills = new CsvToBeanBuilder(new StringReader(summary))
                    .withType(WxDownloadbill.class).build().parse();
            bill = DataAccessUtils.singleResult (bills);
            if(bill != null){
                bill.setTradeOrders(tradeOrders);
                bill.setGetDatetime(new Date());
            }
        }
        double total_trade_amount = 0.0;
        double total_refund_amount = 0.0;
        double total_corporate_red_envelope_refund_amount = 0.0;
        double total_service_charge = 0.0;
        if (bill != null){
            List<TradeOrder> tradeOrders = null;
            switch (profile){
                case "dev":
                    tradeOrders = bill.getTradeOrders().stream()
                            .filter(n -> n.getPay_order_id().contains("_1")
                                    && !"PROCESSING".equals(n.getRefund_status())).collect(Collectors.toList());
                    break;
                case "test":
                    tradeOrders = bill.getTradeOrders().stream()
                            .filter(n -> n.getPay_order_id().contains("_2")
                                    && !"PROCESSING".equals(n.getRefund_status())).collect(Collectors.toList());
                    break;
                default:
                    tradeOrders = bill.getTradeOrders().stream()
                            .filter(n -> n.getPay_order_id().contains("\\d+")
                                    && !"PROCESSING".equals(n.getRefund_status())).collect(Collectors.toList());
                    break;
            }
            bill.setTradeOrders(tradeOrders);
            bill.setTotal_trade_num(tradeOrders.size()+"");
            for (TradeOrder n : tradeOrders) {
                total_trade_amount += Double.valueOf(n.getTotal_amount());
                total_refund_amount += Double.valueOf(n.getRefund_amount());
                total_corporate_red_envelope_refund_amount += Double.valueOf(n.getCorporate_red_envelope_refund_amount());
                total_service_charge += Double.valueOf(n.getService_charge());
            }
            bill.setTotal_trade_amount(total_trade_amount+"");
            bill.setTotal_refund_amount(total_refund_amount+"");
            bill.setTotal_corporate_red_envelope_refund_amount(total_corporate_red_envelope_refund_amount+"");
            bill.setTotal_service_charge(total_service_charge+"");
        }
        return bill;
    }

    /**
     * 获取当天前一天的支付宝对账单数据
     * @param bill_date
     * @return
     */
    public AliDownloadbill getAlibill(String bill_date){
        AliPay aliPay = aliPayService.selectByPrimaryKey(appid);
        AlipayClient alipayClient =
                new DefaultAlipayClient(serverURL, appid, aliPay.getPrivate_key(),
                        format, charset, aliPay.getPublic_key(), signType);
        AlipayDataDataserviceBillDownloadurlQueryRequest request =
                new AlipayDataDataserviceBillDownloadurlQueryRequest();
        JSONObject json = new JSONObject();
        json.put("bill_type","trade");
        json.put("bill_date",bill_date);
        request.setBizContent(json.toString());
        AlipayDataDataserviceBillDownloadurlQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        AliDownloadbill bill = new AliDownloadbill();
        bill.setGetDatetime(new Date());
        double total_trade_amount = 0.0;
        double total_refund_amount = 0.0;
        double total_corporate_red_envelope_refund_amount = 0.0;
        double total_service_charge = 0.0;

        List<AliTradeOrder> aliTradeOrders = new ArrayList<>();
        if(response.isSuccess()){
            List<AliTradeOrder> tradeOrders = new ArrayList<>();
            String billDownloadUrl = response.getBillDownloadUrl();
            //获取配置的下载后的zip文件保存路径
            String filePath = env.getProperty("file.zip");
            //获取配置的zip文件解压路径
            String upzipPath = env.getProperty("file.unzip");
            try {
                FileUtil.downloadNet(billDownloadUrl,filePath + "bill.zip");
                FileUtil.unZip(filePath + "bill.zip",upzipPath);
                File[] fs = new File(upzipPath).listFiles();
                for (File file : fs) {
                    //不包含汇总的
                    if (!file.getAbsolutePath().contains("汇总")) {
                        BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(file), "GBK"));
                        String line = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        int count=0;
                        while (( line = br.readLine () ) != null) {
                            if(count++ >= 4){
                                if (line.contains("#-----")) {
                                    break;
                                }
                                stringBuilder.append(line.replace("\t",""));
                                stringBuilder.append("\n");
                            }
                        }
                        System.out.println(stringBuilder.toString());
                        tradeOrders = new CsvToBeanBuilder(new StringReader(stringBuilder.toString()))
                                .withType(AliTradeOrder.class).build().parse();
                        br.close();
                    }
                }
                // 插入成功， 删除csv 文件
                for (File file : fs) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (profile){
                case "dev":
                    aliTradeOrders = tradeOrders.stream()
                            .filter(n -> n.getPay_order_id().contains("_1")).collect(Collectors.toList());
                    break;
                case "test":
                    aliTradeOrders = tradeOrders.stream()
                            .filter(n -> n.getPay_order_id().contains("_2")).collect(Collectors.toList());
                    break;
                default:
                    aliTradeOrders = tradeOrders.stream()
                            .filter(n -> n.getPay_order_id().contains("\\d+")).collect(Collectors.toList());
                    break;
            }
            for (AliTradeOrder n : aliTradeOrders) {
                if (n.getMerchants_get_amount().contains("-")){
                    total_refund_amount += Double.valueOf(n.getTotal_amount().replace("-",""));
                }else{
                    total_trade_amount += Double.valueOf(n.getTotal_amount());
                }
                //暂时写  商家红包消费金额（元）
                total_corporate_red_envelope_refund_amount += Double.valueOf(n.getMerchants_red_envelope());
                total_service_charge += Double.valueOf(n.getService_charge());
            }
        }else{
            System.out.println("调用失败");
        }
        bill.setTradeOrders(aliTradeOrders);
        bill.setTotal_trade_num(aliTradeOrders.size()+"");
        bill.setTotal_trade_amount(total_trade_amount+"");
        bill.setTotal_refund_amount(total_refund_amount+"");
        bill.setTotal_corporate_red_envelope_refund_amount(total_corporate_red_envelope_refund_amount+"");
        bill.setTotal_service_charge(total_service_charge+"");

        return bill;
    }

    /**
     * 缓存单次数刷新
     * @param iterators
     * @param date
     */
    public void updateBufferSize(Iterator<Map.Entry<String, Bill>> iterators,Date date){
        while(iterators.hasNext()){
            Map.Entry<String, Bill> entry = iterators.next();
            Bill bill = entry.getValue();
            String log = bill.getRebill_log();
            List<BillLog> logs = null;
            try {
                logs = JacksonUtil.json_to_object(log, List.class, BillLog.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BillLog l = new BillLog();
            l.setBill_status(2);
            l.setBill_datetime(date);
            logs.add(l);
            String jsonString = JSONArray.toJSONString(logs);
            bill.setRebill_log(jsonString);
            //当对账次数变为0时，变为手动对账
            if ((bill.getBuffer_size()-1) == 0){
                bill.setBill_type(2);
            }
            bill.setBuffer_size(bill.getBuffer_size()-1);
            bill.setCreate_datetime(new Date());
            billService.xUpdate(bill);
        }
    }

    /**
     * 插入缓存单
     * @param order
     * @param billBatch
     * @param wxNotify
     * @param refund
     * @param refundWxNotify
     * @param ispay
     * @param aliNotify
     */
    public void insertCacheBill(Order order, BillBatch billBatch, PayWxNotify wxNotify, Refund refund, RefundWxNotify refundWxNotify, boolean ispay, PayAliNotify aliNotify){
        SimpleDateFormat format_hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //支付单   进缓存----长帐
        Bill y_bill = new Bill();
        y_bill.setBill_id(IdGenerator.generateId());
        y_bill.setBill_batch_id(billBatch.getBill_batch_id());
        y_bill.setBill_type(1);//平账类型 1 自动  2 手动
        y_bill.setBill_status(2);//平账状态 1 正常 2 长账（我们有wx没有）  3 短账（wx有我们没有） 4 支付金额不一致  5 退款金额不一致
        y_bill.setCreate_datetime(new Date());//创建时间
        y_bill.setBuffer_size(2);//对账缓冲次数  0-2 最大为2（一般对过一次没对上的才进缓冲，最多三次，所以最大为2）
        List<BillLog> logs = new ArrayList<>();
        BillLog log = new BillLog();
        log.setBill_datetime(new Date());
        log.setBill_status(2);
        logs.add(log);
        String jsonString = JSONArray.toJSONString(logs);
        y_bill.setRebill_log(jsonString);
        y_bill.setPay_type(billBatch.getPay_type());

        if (ispay){
            //支付单   进缓存----长帐
            y_bill.setPay_order_id(order.getPay_order_id());
            if (wxNotify != null){
                y_bill.setTransaction_id(wxNotify.getTransaction_id());//第三方交易流水号
                y_bill.setPrice_pay(wxNotify.getTotal_fee());
            } else if(aliNotify != null) {
                y_bill.setTransaction_id(aliNotify.getTrade_no());
                y_bill.setPrice_pay(aliNotify.getTotal_amount());
            }
            y_bill.setTransaction_status(order.getPay_status());//0 未支付 1 已支付
            y_bill.setOrder_create_datetime(order.getCreate_datetime());
            y_bill.setPay_datetime(order.getPay_datetime());
            y_bill.setPrice_total(order.getPrice_total());
            y_bill.setPrice_discount(order.getPrice_discount());
            y_bill.setPay_status(order.getPay_status());
        }else{
            //退款单 进缓存-----长帐
            y_bill.setPay_order_id(refund.getPay_order_id());
            if (refundWxNotify != null){
                y_bill.setTransaction_id(refundWxNotify.getTransaction_id());//第三方交易流水号
                y_bill.setRefund_id_three(refundWxNotify.getRefund_id());//第三方平台退款单号
                y_bill.setR_fee(refundWxNotify.getRefund_fee());
                try {
                    y_bill.setR_finish_datetime(format_hms.parse(refundWxNotify.getSuccess_time()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (aliNotify != null) {
                y_bill.setTransaction_id(aliNotify.getTrade_no());//第三方交易流水号
                y_bill.setRefund_id_three(aliNotify.getTrade_no());//第三方平台退款单号
                y_bill.setR_fee(aliNotify.getRefund_fee());
                try {
                    y_bill.setR_finish_datetime(format_hms.parse(aliNotify.getGmt_close()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            y_bill.setRefund_id(refund.getRefund_id());
            y_bill.setR_status(refund.getStatus());
            y_bill.setR_datetime(refund.getRefund_date());
        }
        billService.xInsert(y_bill);
    }

    /**
     * 插入汇总单
     * @param tradeOrder
     * @param billBatch
     * @param refund
     * @param refundWxNotify
     * @param order
     * @param wxNotify
     * @param ispay
     * @param isnormal
     * @param aliTradeOrder
     * @param aliNotify
     */
    public void insertNormalBill(TradeOrder tradeOrder, BillBatch billBatch, Refund refund,
                                 RefundWxNotify refundWxNotify, Order order, PayWxNotify wxNotify,
                                 boolean ispay, boolean isnormal, AliTradeOrder aliTradeOrder, PayAliNotify aliNotify){
        SimpleDateFormat format_hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //正常的  退款 对上
        Bill y_bill = new Bill();
        y_bill.setBill_id(IdGenerator.generateId());
        y_bill.setBill_batch_id(billBatch.getBill_batch_id());
        if (tradeOrder != null){
            y_bill.setPay_order_id(tradeOrder.getPay_order_id());
            y_bill.setTransaction_id(tradeOrder.getWx_pay_order_id());//第三方交易流水号
            y_bill.setTrade_type(tradeOrder.getTrade_type());
            y_bill.setUser_price_pay((int)(Double.valueOf(tradeOrder.getTotal_amount()) * 100));
            y_bill.setCurrency_type(tradeOrder.getCurrency_type());
            y_bill.setCorporate_red_envelope_amount((int)(Double.valueOf(tradeOrder.getCorporate_red_envelope_amount()) * 100));
            y_bill.setCorporate_red_envelope_refund_amount((int)(Double.valueOf(tradeOrder.getCorporate_red_envelope_refund_amount()) * 100));
            try {
                y_bill.setTransaction_datetime(format_hms.parse(tradeOrder.getTrade_date_time()));//第三方交易时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
            y_bill.setBank_name(tradeOrder.getPay_bank());//用户付款银行名称
            y_bill.setService_charge((int) (Double.valueOf(tradeOrder.getService_charge()) * 100));//第三方平台手续费   单位分
            y_bill.setRate((int) (Double.valueOf(tradeOrder.getRate().replace("%", "")) * 10));//扣费费率  千分之几
        }else if(aliTradeOrder != null){
            y_bill.setPay_order_id(aliTradeOrder.getPay_order_id());
            y_bill.setTransaction_id(aliTradeOrder.getAli_pay_order_id());//第三方交易流水号
            y_bill.setTrade_type(aliTradeOrder.getTrade_type());
            y_bill.setUser_price_pay((int)(Double.valueOf(aliTradeOrder.getTotal_amount().replace("-","")) * 100));
            y_bill.setCorporate_red_envelope_amount((int)(Double.valueOf(aliTradeOrder.getMerchants_red_envelope().replace("-","")) * 100));
            try {
                y_bill.setTransaction_datetime(format_hms.parse(aliTradeOrder.getFinish_datetime()));//第三方交易时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
            y_bill.setService_charge((int) (Double.valueOf(aliTradeOrder.getService_charge()) * 100));//第三方平台手续费   单位分
            //y_bill.setCurrency_type("");//货币种类
            //y_bill.setCorporate_red_envelope_refund_amount((int)(Double.valueOf(aliTradeOrder.getCorporate_red_envelope_refund_amount()) * 100));
            //y_bill.setBank_name(aliTradeOrder.getPay_bank());//用户付款银行名称
            //y_bill.setRate((int) (Double.valueOf(aliTradeOrder.getRate().replace("%", "")) * 10));//扣费费率  千分之几
        }
        y_bill.setCreate_datetime(new Date());//创建时间
        y_bill.setPay_type(billBatch.getPay_type());
        if (ispay){
            if (tradeOrder != null){
                y_bill.setTransaction_status("SUCCESS".equals(tradeOrder.getTrade_status()) ? 1 : 0);//0 未支付 1 已支付
                y_bill.setTransaction_fee((int) (Double.valueOf(tradeOrder.getTotal_amount()) * 100));//第三方交易金额
            }else if(aliTradeOrder != null){
                y_bill.setTransaction_status((!"".equals(aliTradeOrder.getFinish_datetime()) || aliTradeOrder.getFinish_datetime() != null) ? 1 : 0);//0 未支付 1 已支付
                y_bill.setTransaction_fee((int) (Double.valueOf(aliTradeOrder.getTotal_amount()) * 100));//第三方交易金额
            }
            y_bill.setOrder_create_datetime(order.getCreate_datetime());
            y_bill.setPay_datetime(order.getPay_datetime());
            y_bill.setPrice_total(order.getPrice_total());
            y_bill.setPrice_discount(order.getPrice_discount());
            int fee = 0;
            if (wxNotify != null){
                fee = wxNotify.getTotal_fee();
                y_bill.setPrice_pay(fee);
            }else if (aliNotify != null){
                fee = aliNotify.getTotal_amount();
                y_bill.setPrice_pay(fee);
            }
            y_bill.setPay_status(order.getPay_status());
            if (isnormal){
                //对上的
                y_bill.setBill_fee(fee);//平账金额  单位分
                y_bill.setBill_type(1);//平账类型 1 自动  2 手动
                y_bill.setBill_status(1);//平账状态 1 正常 2 长账（我们有wx没有）  3 短账（wx有我们没有） 4 支付金额不一致  5 退款金额不一致
            } else {
                //金额不一致的
                y_bill.setBill_type(2);//平账类型 1 自动  2 手动
                y_bill.setBill_status(4);//平账状态 1 正常 2 长账（我们有wx没有）  3 短账（wx有我们没有） 4 支付金额不一致  5 退款金额不一致
            }
        }else{
            if (tradeOrder != null){
                y_bill.setTransaction_status("REFUND".equals(tradeOrder.getTrade_status()) ? 1 : 0);//0 未支付 1 已支付
                y_bill.setRefund_id_three(tradeOrder.getWx_refund_id());//第三方平台退款单号
                y_bill.setRefund_fee((int) (Double.valueOf(tradeOrder.getRefund_amount()) * 100));//第三方平台退款金额 单位分
            }else if(aliTradeOrder != null){
                y_bill.setTransaction_status((!"".equals(aliTradeOrder.getFinish_datetime()) || aliTradeOrder.getFinish_datetime() != null) ? 1 : 0);//0 未支付 1 已支付
                y_bill.setRefund_id_three(aliTradeOrder.getAli_pay_order_id());//第三方平台退款单号
                y_bill.setRefund_fee((int) (Double.valueOf(aliTradeOrder.getTotal_amount().replace("-","")) * 100));//第三方平台退款金额 单位分
            }
            y_bill.setRefund_id(refund.getRefund_id());
            y_bill.setR_status(refund.getStatus());
            y_bill.setR_datetime(refund.getRefund_date());
            int r_fee = 0;
            if (refundWxNotify != null){
                r_fee = refundWxNotify.getRefund_fee();
                y_bill.setR_fee(r_fee);
                try {
                    y_bill.setR_finish_datetime(format_hms.parse(refundWxNotify.getSuccess_time()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(aliNotify != null){
                r_fee = aliNotify.getRefund_fee();
                y_bill.setR_fee(r_fee);
                try {
                    y_bill.setR_finish_datetime(format_hms.parse(aliNotify.getGmt_close()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (isnormal){
                //对上
                y_bill.setBill_fee(r_fee);//平账金额  单位分
                y_bill.setBill_type(1);//平账类型 1 自动  2 手动
                y_bill.setBill_status(1);//平账状态 1 正常 2 长账（我们有wx没有）  3 短账（wx有我们没有） 4 支付金额不一致  5 退款金额不一致
            } else {
                //正常的 对退款单 金额不一致
                y_bill.setBill_type(2);//平账类型 1 自动  2 手动
                y_bill.setBill_status(5);//平账状态 1 正常 2 长账（我们有wx没有）  3 短账（wx有我们没有） 4 支付金额不一致  5 退款金额不一致
            }
        }
        billService.xInsert(y_bill);
    }

    /**
     * 对账缓存单并更新缓存单
     * @param bill
     * @param billBatch
     * @param tradeOrder
     * @param ispay
     * @param isnormal
     * @param aliTradeOrder
     */
    public void updateCacheBill(Bill bill, BillBatch billBatch, TradeOrder tradeOrder, boolean ispay, boolean isnormal, AliTradeOrder aliTradeOrder){
        SimpleDateFormat format_hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bill.setBill_batch_id(billBatch.getBill_batch_id());
        int transaction_fee = 0;
        int refund_fee = 0;
        if (tradeOrder != null){
            try {
                bill.setTransaction_datetime(format_hms.parse(tradeOrder.getTrade_date_time()));//第三方交易时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bill.setBank_name(tradeOrder.getPay_bank());//用户付款银行名称
            bill.setService_charge((int)(Double.valueOf(tradeOrder.getService_charge())*100));//第三方平台手续费   单位分
            bill.setRate((int)(Double.valueOf(tradeOrder.getRate().replace("%",""))*10));//扣费费率  千分之几
            bill.setTrade_type(tradeOrder.getTrade_type());
            bill.setUser_price_pay((int)(Double.valueOf(tradeOrder.getTotal_amount()) * 100));
            bill.setCurrency_type(tradeOrder.getCurrency_type());
            bill.setCorporate_red_envelope_amount((int)(Double.valueOf(tradeOrder.getCorporate_red_envelope_amount()) * 100));
            bill.setCorporate_red_envelope_refund_amount((int)(Double.valueOf(tradeOrder.getCorporate_red_envelope_refund_amount()) * 100));
        }else if(aliTradeOrder != null){
            try {
                bill.setTransaction_datetime(format_hms.parse(aliTradeOrder.getFinish_datetime()));//第三方交易时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bill.setService_charge((int) (Double.valueOf(aliTradeOrder.getService_charge()) * 100));//第三方平台手续费   单位分
            bill.setTrade_type(aliTradeOrder.getTrade_type());
            bill.setUser_price_pay((int)(Double.valueOf(aliTradeOrder.getTotal_amount().replace("-","")) * 100));
            bill.setCorporate_red_envelope_amount((int)(Double.valueOf(aliTradeOrder.getMerchants_red_envelope().replace("-","")) * 100));
            //bill.setCurrency_type("");//货币种类
            //bill.setCorporate_red_envelope_refund_amount((int)(Double.valueOf(aliTradeOrder.getCorporate_red_envelope_refund_amount()) * 100));
            //bill.setBank_name(aliTradeOrder.getPay_bank());//用户付款银行名称
            //bill.setRate((int) (Double.valueOf(aliTradeOrder.getRate().replace("%", "")) * 10));//扣费费率  千分之几
        }
        bill.setCreate_datetime(new Date());//创建时间
        bill.setBuffer_size(-1);//对账缓冲次数  0-2 最大为2（一般对过一次没对上的才进缓冲，最多三次，所以最大为2）
        if (ispay) {
            if (tradeOrder != null){
                transaction_fee = (int)(Double.valueOf(tradeOrder.getTotal_amount())*100);
            }else if(aliTradeOrder != null){
                transaction_fee = (int)(Double.valueOf(aliTradeOrder.getTotal_amount())*100);
            }
            //对完正常的还有剩下   对缓存   对缓存中支付单   对上
            bill.setTransaction_fee(transaction_fee);//第三方交易金额
            if(isnormal){
                //对上的
                bill.setBill_fee(bill.getPrice_pay());//平账金额  单位分
                bill.setBill_status(1);//平账状态 1 正常 2 长账（我们有wx没有）  3 短账（wx有我们没有） 4 支付金额不一致  5 退款金额不一致
            } else {
                //没对上的
                bill.setBill_type(2);//平账类型 1 自动  2 手动
                //bill.setBill_status(4);//平账状态 1 正常 2 长账（我们有wx没有）  3 短账（wx有我们没有） 4 支付金额不一致  5 退款金额不一致
            }
        }else{
            if (tradeOrder != null){
                refund_fee = (int)(Double.valueOf(tradeOrder.getRefund_amount())*100);
            }else if(aliTradeOrder != null){
                refund_fee = (int)(Double.valueOf(aliTradeOrder.getTotal_amount().replace("-",""))*100);
            }
            //对完正常的还有剩下   对缓存   对缓存中退款
            bill.setRefund_fee(refund_fee);//第三方平台退款金额 单位分
            if (isnormal){
                //对上
                bill.setBill_fee(bill.getR_fee());//平账金额  单位分
                bill.setBill_status(1);//平账状态 1 正常 2 长账（我们有wx没有）  3 短账（wx有我们没有） 4 支付金额不一致  5 退款金额不一致
            } else {
                //没对上
                bill.setBill_type(2);//平账类型 1 自动  2 手动
                //bill.setBill_status(5);//平账状态 1 正常 2 长账（我们有wx没有）  3 短账（wx有我们没有） 4 支付金额不一致  5 退款金额不一致
            }
        }
        billService.xUpdate(bill);
    }

    /**
     * 插入当天差错单
     * @param tradeOrder
     * @param billBatch
     * @param ispay
     * @param aliTradeOrder
     */
    public void insertErrorBill(TradeOrder tradeOrder, BillBatch billBatch, boolean ispay, AliTradeOrder aliTradeOrder){
        SimpleDateFormat format_hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Bill y_bill = new Bill();
        y_bill.setBill_id(IdGenerator.generateId());
        y_bill.setBill_batch_id(billBatch.getBill_batch_id());
        y_bill.setPay_type(billBatch.getPay_type());
        y_bill.setBill_type(2);//平账类型 1 自动  2 手动
        y_bill.setBill_status(3);//平账状态 1 正常 2 长账（我们有wx没有）  3 短账（wx有我们没有） 4 支付金额不一致  5 退款金额不一致
        y_bill.setCreate_datetime(new Date());//创建时间
        if (tradeOrder != null){
            y_bill.setPay_order_id(tradeOrder.getPay_order_id());
            y_bill.setTransaction_id(tradeOrder.getWx_pay_order_id());//第三方交易流水号
            try {
                y_bill.setTransaction_datetime(format_hms.parse(tradeOrder.getTrade_date_time()));//第三方交易时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
            y_bill.setBank_name(tradeOrder.getPay_bank());//用户付款银行名称
            y_bill.setService_charge((int)(Double.valueOf(tradeOrder.getService_charge())*100));//第三方平台手续费   单位分
            y_bill.setRate((int)(Double.valueOf(tradeOrder.getRate().replace("%",""))*10));//扣费费率  千分之几
            y_bill.setTrade_type(tradeOrder.getTrade_type());
            y_bill.setUser_price_pay((int)(Double.valueOf(tradeOrder.getTotal_amount()) * 100));
            y_bill.setCurrency_type(tradeOrder.getCurrency_type());
            y_bill.setCorporate_red_envelope_amount((int)(Double.valueOf(tradeOrder.getCorporate_red_envelope_amount()) * 100));
            y_bill.setCorporate_red_envelope_refund_amount((int)(Double.valueOf(tradeOrder.getCorporate_red_envelope_refund_amount()) * 100));
        }else if(aliTradeOrder != null){
            y_bill.setPay_order_id(aliTradeOrder.getPay_order_id());
            y_bill.setTransaction_id(aliTradeOrder.getAli_pay_order_id());//第三方交易流水号
            try {
                y_bill.setTransaction_datetime(format_hms.parse(aliTradeOrder.getFinish_datetime()));//第三方交易时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
            y_bill.setTrade_type(aliTradeOrder.getTrade_type());
            y_bill.setService_charge((int) (Double.valueOf(aliTradeOrder.getService_charge()) * 100));//第三方平台手续费   单位分
            y_bill.setUser_price_pay((int)(Double.valueOf(aliTradeOrder.getTotal_amount().replace("-","")) * 100));
            y_bill.setCorporate_red_envelope_amount((int)(Double.valueOf(aliTradeOrder.getMerchants_red_envelope().replace("-","")) * 100));
            //y_bill.setCurrency_type("");//货币种类
            //y_bill.setCorporate_red_envelope_refund_amount((int)(Double.valueOf(aliTradeOrder.getCorporate_red_envelope_refund_amount()) * 100));
            //y_bill.setBank_name(aliTradeOrder.getPay_bank());//用户付款银行名称
            //y_bill.setRate((int) (Double.valueOf(aliTradeOrder.getRate().replace("%", "")) * 10));//扣费费率  千分之几
        }
        if (ispay){
            if (tradeOrder != null){
                y_bill.setTransaction_status("SUCCESS".equals(tradeOrder.getTrade_status()) ? 1 : 0);//0 未支付 1 已支付
                y_bill.setTransaction_fee((int) (Double.valueOf(tradeOrder.getTotal_amount()) * 100));//第三方交易金额
            }else if(aliTradeOrder != null){
                y_bill.setTransaction_status((!"".equals(aliTradeOrder.getFinish_datetime()) || aliTradeOrder.getFinish_datetime() != null) ? 1 : 0);//0 未支付 1 已支付
                y_bill.setTransaction_fee((int) (Double.valueOf(aliTradeOrder.getTotal_amount()) * 100));//第三方交易金额
            }
        } else {
            //当天正常和缓存都对了的wx还剩下 进差错   退款单
            if (tradeOrder != null){
                y_bill.setTransaction_status("SUCCESS".equals(tradeOrder.getTrade_status()) || "REFUND".equals(tradeOrder.getTrade_status()) ? 1 : 0);//0 未支付 1 已支付
                y_bill.setRefund_id_three(tradeOrder.getWx_refund_id());//第三方平台退款单号
                y_bill.setRefund_fee((int)(Double.valueOf(tradeOrder.getRefund_amount())*100));//第三方平台退款金额 单位分
            }else if(aliTradeOrder != null){
                y_bill.setTransaction_status((!"".equals(aliTradeOrder.getFinish_datetime()) || aliTradeOrder.getFinish_datetime() != null) ? 1 : 0);//0 未支付 1 已支付
                y_bill.setRefund_id_three(aliTradeOrder.getAli_pay_order_id());//第三方平台退款单号
                y_bill.setRefund_fee((int) (Double.valueOf(aliTradeOrder.getTotal_amount().replace("-","")) * 100));//第三方平台退款金额 单位分
            }

        }
        billService.xInsert(y_bill);
    }
}
