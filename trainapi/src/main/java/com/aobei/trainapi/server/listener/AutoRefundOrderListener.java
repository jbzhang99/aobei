package com.aobei.trainapi.server.listener;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aobei.common.boot.OnsMessageListener;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainapi.configuration.CustomProperties;
import com.aobei.trainapi.server.bean.AliPayClientMap;
import custom.bean.Status;
import custom.bean.Type;
import custom.bean.ons.RefundOrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.SecapiPayRefund;
import weixin.popular.bean.paymch.SecapiPayRefundResult;

@Component
public class AutoRefundOrderListener implements OnsMessageListener {

    @Autowired
    RedisService redisService;
    @Autowired
    WxAppService wxAppService;
    @Autowired
    WxMchService wxMchService;
    @Autowired
    CustomProperties properties;
    @Autowired
    OrderService orderService;
    @Autowired
    RefundService refundService;
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    AliPayClientMap aliPayClientMap;

    Logger logger = LoggerFactory.getLogger(AutoRefundOrderListener.class);

    @Override
    public String getTopic() {
        return properties.getAliyun().getOns().getTopic();
    }

    @Override
    public String getTag() {

        return "refundOrder";
    }

    @Override
    public boolean isClustering() {
        return true;
    }

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        logger.info(" aliOns-AutoRefundOrderListener message{}", message);
        try {
            String json = new String(message.getBody());
            RefundOrderMessage refundOrderMessage = JSON.parseObject(json, RefundOrderMessage.class);
            String pay_order_id = refundOrderMessage.getPay_order_id();
            Order order = orderService.selectByPrimaryKey(pay_order_id);
            if (order != null && Status.PayStatus.payed.value.equals(order.getPay_status())) {
                if (Type.PayType.wxpay.value.equals(order.getPay_type())) {
                    if (wxRefund(refundOrderMessage))
                        return Action.CommitMessage;
                }
                if (Type.PayType.alipay.value.equals(order.getPay_type())) {
                    if (alipayRefund(refundOrderMessage))
                        return Action.CommitMessage;
                }
                if (Type.PayType.unionPay.value.equals(order.getPay_type())) {
                    if (unionPayRefund(refundOrderMessage))
                        return Action.CommitMessage;
                }
            }
        } catch (Exception e) {
            logger.error("aliOns-AutoRefundOrderListener message{},e{}", message, e.getMessage());
        }
        return Action.CommitMessage;


    }

    private boolean wxRefund(RefundOrderMessage message) {
        logger.info("aliOns-AutoRefundOrderListener:message{}", message.toString());
        String appid = message.getAppid();
        //获取微信相应得基础数据
        WxApp wxApp = wxAppService.selectByPrimaryKey(appid);
        if (wxApp == null) {
            logger.error("aliOns-AutoRefundOrderListener:error{invalid wxApp REQUEST!}");
            return true;
        }
        WxMch wxMch = wxMchService.selectByPrimaryKey(wxApp.getMch_id());
        if (wxMch == null) {
            logger.error("aliOns-AutoRefundOrderListener:error{invalid wxMch REQUEST!}");
            return true;
        }
        //组装退款请求体
        Long refund_id = message.getRefund_id();
        Refund refund = refundService.selectByPrimaryKey(refund_id);
        if (refund == null) {
            logger.error("aliOns-AutoRefundOrderListener:error{invalid refund REQUEST!}");
            return true;
        }
        String notify_url = properties.getWx().getRefundNotifyUrl().replace("{path}", wxApp.getPath());
        SecapiPayRefund secapiPayRefund = new SecapiPayRefund();
        secapiPayRefund.setAppid(wxApp.getApp_id());
        secapiPayRefund.setNotify_url(notify_url);
        secapiPayRefund.setMch_id(wxMch.getMch_id());
        secapiPayRefund.setNonce_str(IdGenerator.generateId() + "");
        secapiPayRefund.setOut_trade_no(refund.getPay_order_id());
        secapiPayRefund.setOut_refund_no(refund.getRefund_id() + "");
        secapiPayRefund.setRefund_fee(refund.getFee());
        secapiPayRefund.setTotal_fee(refund.getPrice_pay());
        secapiPayRefund.setRefund_fee_type("CNY");
        secapiPayRefund.setSign_type("MD5");
        //进行退款请求
        SecapiPayRefundResult result = PayMchAPI.secapiPayRefund(secapiPayRefund, wxMch.getMch_key());
        //处理退款请求结果

        if (result != null && "SUCCESS".equals(result.getResult_code())) {
            Order order = new Order();
            order.setPay_order_id(refund.getPay_order_id());
            order.setR_status(Status.RefundStatus.refunding.value);
            orderService.updateByPrimaryKeySelective(order);
            refund.setStatus(Status.RefundStatus.refunding.value);
            orderLogService.xInsert("system", 0l, refund.getPay_order_id(), "【申请退款】微信接受退款请求！");
            logger.info("aliOns-AutoRefundOrderListener: status{SUCCESS}");
        } else {
            refund.setStatus(Status.RefundStatus.fail.value);
            orderLogService.xInsert("system", 0l, refund.getPay_order_id(), "【申请退款】微信拒绝退款请求！");
            logger.info("aliOns-AutoRefundOrderListener: status{FAIL}");
        }
        RefundExample refundExample = new RefundExample();
        refundExample.or().andStatusEqualTo(0)
                .andRefund_idEqualTo(refund.getRefund_id());
        refundService.updateByExampleSelective(refund,refundExample);
        return true;
    }

    private boolean alipayRefund(RefundOrderMessage message) {
        logger.info("aliOns-AutoRefundOrderListener:message{}", message.toString());
        AlipayClient alipayClient = aliPayClientMap.getClient(message.getAppid());
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        Long refund_id = message.getRefund_id();
        Refund refund = refundService.selectByPrimaryKey(refund_id);
        model.setOutTradeNo(message.getPay_order_id());
        model.setRefundAmount(refund.getFee()/100D+"");
        model.setOutRequestNo(refund.getRefund_id()+"");
        model.setRefundReason(refund.getR_info());
        request.setBizModel(model);

        try {
          AlipayTradeRefundResponse response  =  alipayClient.execute(request);

            if(response.isSuccess()){
                Order order = new Order();
                order.setPay_order_id(refund.getPay_order_id());
                order.setR_status(Status.RefundStatus.refunding.value);
                orderService.updateByPrimaryKeySelective(order);
                refund.setStatus(Status.RefundStatus.refunding.value);
                orderLogService.xInsert("system", 0l, refund.getPay_order_id(), "【申请退款】支付宝接受退款请求！");
                logger.info("aliOns-AutoRefundOrderListener: status SUCCESS  code:{},msg:{}",response.getSubCode(),response.getSubMsg());
            } else {
                refund.setStatus(Status.RefundStatus.fail.value);
                orderLogService.xInsert("system", 0l, refund.getPay_order_id(), "【申请退款】支付宝拒绝退款请求！");
                logger.info("aliOns-AutoRefundOrderListener: status{FAIL}，msg:{}",response.getSubMsg());
            }
            RefundExample refundExample = new RefundExample();
            refundExample.or().andStatusEqualTo(0)
                    .andRefund_idEqualTo(refund.getRefund_id());
            refundService.updateByExample(refund,refundExample);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }


        return true;
    }

    private boolean unionPayRefund(RefundOrderMessage message) {
        return false;
    }
}
