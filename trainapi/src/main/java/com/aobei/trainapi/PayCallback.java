package com.aobei.trainapi;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainapi.configuration.CustomProperties;
import com.aobei.trainapi.server.CustomerApiService;
import com.aobei.trainapi.server.bean.AliPayClientMap;
import com.aobei.trainapi.server.impl.ExpireKeyRedis;
import com.aobei.trainapi.util.AESUtil;
import custom.bean.Status;
import custom.bean.Type;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import weixin.popular.bean.paymch.MchBaseResult;
import weixin.popular.bean.paymch.MchPayNotify;
import weixin.popular.bean.paymch.RefundNotifyReqInfo;
import weixin.popular.bean.paymch.SecapiPayRefundNotify;
import weixin.popular.util.PayUtil;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@RestController
@RequestMapping(value = "/callback/")
public class PayCallback {

    @Autowired
    CustomProperties properties;
    @Autowired
    CustomerApiService apiService;
    @Autowired
    ExpireKeyRedis expireKey;
    @Autowired
    PayWxNotifyService payWxNotifyService;
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    WxMchService wxMchService;
    @Autowired
    WxAppService wxAppService;
    @Autowired
    RefundWxNotifyService refundWxNotifyService;
    @Autowired
    RefundService refundService;
    @Autowired
    OrderService orderService;
    @Autowired
    AliPayService aliPayService;
    @Autowired
    PayAliNotifyService payAliNotifyService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    @RequestMapping(value = "/{path}/wxpay")
    public void wxpayCallBack(@PathVariable String path, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("WXPAY_CALLBACK  params path:{}", path);
        WxAppExample wxAppExample = new WxAppExample();
        wxAppExample.or().andPathEqualTo(path);
        WxApp wxApp = singleResult(wxAppService.selectByExample(wxAppExample));
        WxMch wxMch = wxMchService.selectByPrimaryKey(wxApp.getMch_id());
        String key = wxMch.getMch_key();
        //获取请求数据
        String xmlData = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
        //将XML转为MAP,确保所有字段都参与签名验证
        Map<String, String> mapData = XMLConverUtil.convertToMap(xmlData);
//        if (mapData.size() == 0) {
//            return;
//        }
        //转换数据对象
        MchPayNotify payNotify = XMLConverUtil.convertToObject(MchPayNotify.class, xmlData);
        if (payNotify != null && "SUCCESS".equals(payNotify.getReturn_code())) {
            logger.info("WXPAY_CALLBACK payNotify:{}", payNotify.toString());
            //已处理 去重
            if (!expireKey.exists("WX_PAY_NOTIFY" + payNotify.getTransaction_id())) {
                expireKey.add("WX_PAY_NOTIFY" + payNotify.getTransaction_id(), 60);
                //签名验证
                if (SignatureUtil.validateSign(mapData, key)) {
                    //@since 2.8.5
                    payNotify.buildDynamicField(mapData);
                    apiService.orderPaysuccess(payNotify.getOut_trade_no(), Type.PayType.wxpay.value);
                    //插入支付成功更新为你通知数据
                    PayWxNotify payWxNotify = convertToPayWxNotify(payNotify);
                    payWxNotifyService.upsertSelective(payWxNotify);
                    //payWxNotifyService.insertSelective(payWxNotify);
                    String logText = "通过微信的回调确认订单的支付情况";
                    orderLogService.xInsert("system", 0l, payWxNotify.getOut_trade_no(), logText);
                    logger.info("WXPAY_CALLBACK pay_success");
                } else {
                    logger.info("WXPAY_CALLBACK  pay_fail   msg=签名错误,data：{}", mapData);
                    MchBaseResult baseResult = new MchBaseResult();
                    baseResult.setReturn_code("FAIL");
                    baseResult.setReturn_msg("ERROR");
                    response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
                    return;
                }
            } else {
                logger.info("WXPAY_CALLBACK Repeated access");
            }
        } else {
            logger.info("WXPAY_CALLBACK pay_fail  illegal param:{}", mapData);
            MchBaseResult baseResult = new MchBaseResult();
            baseResult.setReturn_code("FAIL");
            baseResult.setReturn_msg("ERROR");
            response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
            return;
        }
        MchBaseResult baseResult = new MchBaseResult();
        baseResult.setReturn_code("SUCCESS");
        baseResult.setReturn_msg("OK");
        response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
        return;

    }

    @ResponseBody
    @RequestMapping(value = "/{path}/alipay")
    public String alipayCallBack(@PathVariable String path, HttpServletRequest request, HttpServletResponse response) throws IOException {


        //arams{gmt_create=2018-05-31 18:18:26, charset=utf-8, seller_email=abkj20171010@aobei.com, subject=vbbbbcbv, sign=P7mzvpkn+ZCWCwJQ1zIAvSthJaL3KiufgNj0Bc9hpkIAKvM87wL2YTwnQpDEmF67QpS+U4Fhas5BmAqHVGsDROGczupYjJFXncgY7k+Utn5Te4l4DVsQpdQe72daq8hdtOXlb7OKGfpfWQNyiKizgnd+jQ2u9/+XyC6kJekBPuQAyvz+PIZB7KMHEU2Q3Ufjkif7TJytVpzKhLYgl/1ovjS1mCaCczE+zuVPD5cwj8wbuTHbLrn4+ab1Ruih7vMx5/eFPh7mcO0rVdnhkBm/H+81Q6CnY0q6tovWIDaYpCsxq68d0IESt7Qp/vhGyR4Mss/ZoxA9UVNDlo8104IkYQ==, body=vbbbbcbv, buyer_id=2088412647793195, notify_id=0e69a601269f6246a46be021c135c5chgx, notify_type=trade_status_sync, trade_status=TRADE_CLOSED, app_id=2018052960271202, sign_type=RSA2, seller_id=2088131337265043, gmt_payment=2018-05-31 18:18:28, notify_time=2018-06-01 16:09:43, gmt_refund=2018-06-01 16:09:43.102, out_biz_no=1527764112_1, version=1.0, out_trade_no=1527764112_1, total_amount=0.01, refund_fee=0.01, trade_no=2018053121001004190521059896, auth_app_id=2018052960271202, buyer_logon_id=136****9970, gmt_close=2018-06-01 16:09:43}
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        if (requestParams == null || requestParams.size()==0) {
            logger.error("ALIPAY_CALLBACK error illegal request");
            return "fail";
        }
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        logger.info("ALIPAY_CALLBACK params{}", params);
        //"TRADE_FINISHED"
        //"TRADE_SUCCESS"
        //"TRADE_CLOSED"
        if (!(StringUtils.equals("TRADE_SUCCESS", params.get("trade_status"))
                || StringUtils.equals("TRADE_FINISHED", params.get("trade_status"))
                || StringUtils.equals("TRADE_CLOSED", params.get("trade_status")))) {
            logger.error("ALIPAY_CALLBACK error wrong params {}", params);
            return "fail";
        }
        String appid = params.get("app_id");
        if (!StringUtils.equals(path, appid)) {
            logger.warn("ALIPAY_CALLBACK warn the path isnot correct path:{},appid:{}", path, appid);
        }
        AliPay aliPay = aliPayService.selectByPrimaryKey(path);
        if (aliPay == null) {
            logger.error("ALIPAY_CALLBACK error alipay app not found appid:{}", appid);
            return "fail";
        }
        //组装数数据
        PayAliNotify payAliNotify = convertToPayAliNotify(params);
        if (!expireKey.exists("ALIPAY_NOTIFY" + payAliNotify.getOut_trade_no())) {
            // 添加业务处理标记
            expireKey.add("ALIPAY_NOTIFY" + payAliNotify.getOut_trade_no(), 60);

            try {
                if (AlipaySignature.rsaCheckV1(params, aliPay.getPublic_key(), "utf-8", "RSA2")) {
                    payAliNotifyService.insertSelective(payAliNotify);
                    if(StringUtils.equals("TRADE_SUCCESS", params.get("trade_status"))
                            ||StringUtils.equals("TRADE_FINISHED", params.get("trade_status"))){
                        //进行支付成功操作，如果订单已经被支付过了。支付成功方法中将不进行处理
                        apiService.orderPaysuccess(params.get("out_trade_no"), Type.PayType.alipay.value);
                        logger.info("ALIPAY_CALLBACK pay SUCCESS");
                    }else if(StringUtils.equals("TRADE_CLOSED", params.get("trade_status"))){
                        //TRADE_CLOSED 时 可能是退款操作。如果附件信息中的附加信息能够找到对应的退款单，进进行更新操作
                        Long refund_id  = new Long(payAliNotify.getOut_biz_no());
                        if(refundService.selectByPrimaryKey(refund_id)!=null) {
                            //更新退款申请表
                            Refund refund = new Refund();
                            refund.setStatus(Status.RefundStatus.refunded.value);
                            refund.setRefund_id(refund_id);
                            refund.setRefund_date(new Date());
                            refundService.updateByPrimaryKeySelective(refund);
                            //更新订单表
                            Order order = new Order();
                            order.setPay_order_id(payAliNotify.getOut_trade_no());
                            order.setR_finish_datetime(new Date());
                            order.setR_status(Status.RefundStatus.refunded.value);
                            orderService.updateByPrimaryKeySelective(order);
                            String logText = "通过支付宝的回调确认订单的退款情况";
                            orderLogService.xInsert("system", 0l, payAliNotify.getOut_trade_no(), logText);
                            logger.error("ALIPAY_CALLBACK refund SUCCESS ");
                        }
                    }
                    logger.error("ALIPAY_CALLBACK SUCCESS ");
                }

            } catch (Exception e) {
                logger.error("ALIPAY_CALLBACK  message:{}", e.getMessage());
                return "fail";
            }

        } else {
            logger.info("ALIPAY_CALLBACK  Repeated access");
        }


        return "success";

    }

    @Transactional
    @RequestMapping("/{path}/wxrefund")
    public void wxRefundCallBack(@PathVariable String path, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("WXPAY_REFUND_CALLBACK params:{}", path);
        WxAppExample wxAppExample = new WxAppExample();
        wxAppExample.or().andPathEqualTo(path);
        WxApp wxApp = singleResult(wxAppService.selectByExample(wxAppExample));
        WxMch wxMch = wxMchService.selectByPrimaryKey(wxApp.getMch_id());
        String key = wxMch.getMch_key();
        String xmlData = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
        logger.error("WXPAY_REFUND_CALLBACK params xmlData:{}", xmlData);
        // 转换数据对象
        SecapiPayRefundNotify refundNotify = XMLConverUtil.convertToObject(SecapiPayRefundNotify.class, xmlData);
        // 退款通知成功
        if (refundNotify != null && "SUCCESS".equals(refundNotify.getReturn_code())) {
            // 解密数据 req_info
            RefundNotifyReqInfo refundNotifyReqInfo = PayUtil.decryptRefundNotifyReqInfo(refundNotify.getReq_info(), key);
            if (refundNotifyReqInfo == null) {
                logger.error("WXPAY_REFUND_CALLBACK refund fail refundNotifyReqInfo {}", refundNotifyReqInfo);
                MchBaseResult baseResult = new MchBaseResult();
                baseResult.setReturn_code("FAIL");
                baseResult.setReturn_msg("ERROR");
                response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
                return;
            }

            // 业务处理标记检查
            if (!expireKey.exists("WX_REFUND_NOTIFY" + refundNotifyReqInfo.getRefund_id())) {
                // 添加业务处理标记
                expireKey.add("WX_REFUND_NOTIFY" + refundNotifyReqInfo.getRefund_id(), 20);
                RefundWxNotify notify = convertToRefundWxNotify(refundNotifyReqInfo, wxApp.getApp_id(), wxMch.getMch_id());
                refundWxNotifyService.upsertSelective(notify);
                //更新退款申请表
                Refund refund = new Refund();
                refund.setStatus(Status.RefundStatus.refunded.value);
                refund.setRefund_id(Long.parseLong(refundNotifyReqInfo.getOut_refund_no()));
                refund.setRefund_date(new Date());
                refundService.updateByPrimaryKeySelective(refund);
                //更新订单表
                Order order = new Order();
                order.setPay_order_id(refundNotifyReqInfo.getOut_trade_no());
                order.setR_finish_datetime(new Date());
                order.setR_status(Status.RefundStatus.refunded.value);
                orderService.updateByPrimaryKeySelective(order);

                String logText = "通过微信的回调确认订单的退款情况";
                orderLogService.xInsert("system", 0l, notify.getOut_trade_no(), logText);
                logger.info("WXPAY_REFUND_CALLBACK success refundNotifyReqInfo:{}", refundNotifyReqInfo.toString());
            } else {
                logger.info(" WXPAY_REFUND_CALLBACK Repeated access");
            }
        } else {
            logger.error("WXPAY_REFUND_CALLBACK refund fail refundNotify:{}", refundNotify);
            MchBaseResult baseResult = new MchBaseResult();
            baseResult.setReturn_code("FAIL");
            baseResult.setReturn_msg("ERROR");
            response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
            return;
        }
        MchBaseResult baseResult = new MchBaseResult();
        baseResult.setReturn_code("SUCCESS");
        baseResult.setReturn_msg("OK");
        response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
        return;

    }


    private PayAliNotify convertToPayAliNotify(Map<String, String> params) {
        PayAliNotify payAliNotify = new PayAliNotify();
        payAliNotify.setApp_id(params.get("app_id"));
        payAliNotify.setBody(params.get("body"));
        payAliNotify.setBuyer_id(params.get("buyer_id"));
        payAliNotify.setBuyer_logon_id(params.get("buyer_logon_id"));

        payAliNotify.setBuyer_pay_amount(multiply(params.get("buyer_pay_amount"), "100"));

        payAliNotify.setCharset(params.get("charset"));
        payAliNotify.setFund_bill_list(params.get("fund_bill_list"));
        payAliNotify.setGmt_close(params.get("gmt_close"));
        payAliNotify.setGmt_create(params.get("gmt_create"));
        payAliNotify.setGmt_payment(params.get("gmt_payment"));
        payAliNotify.setGmt_refund(params.get("gmt_refund"));
        payAliNotify.setInvoice_amount(multiply(params.get("invoice_amount"), "100"));

        payAliNotify.setNotify_id(params.get("notify_id"));
        payAliNotify.setNotify_time(params.get("notify_time"));
        payAliNotify.setNotify_type(params.get("notify_type"));
        payAliNotify.setOut_biz_no(params.get("out_biz_no"));

        payAliNotify.setOut_trade_no(params.get("out_trade_no"));
        payAliNotify.setPoint_amount(multiply(params.get("point_amount"), "100"));
        payAliNotify.setPassback_params(params.get("passback_params"));
        payAliNotify.setReceipt_amount(multiply(params.get("receipt_amount"), "100"));
        payAliNotify.setRefund_fee(multiply(params.get("refund_fee"), "100"));

        payAliNotify.setSeller_email(params.get("seller_email"));
        payAliNotify.setSeller_id(params.get("seller_id"));
        payAliNotify.setSign(params.get("sign"));
        payAliNotify.setSign_type(params.get("sign_type"));
        payAliNotify.setSubject(params.get("subject"));


        System.out.println(params.get("total_amount"));
        System.out.println(StringUtils.isNumeric(params.get("total_amount")));
        payAliNotify.setTotal_amount(multiply(params.get("total_amount"), "100"));
        payAliNotify.setTrade_no(params.get("trade_no"));
        payAliNotify.setTrade_status(params.get("trade_status"));
        payAliNotify.setVersion(params.get("version"));
        payAliNotify.setVoucher_detail_list(params.get("voucher_detail_list"));

        return payAliNotify;
    }


    private PayWxNotify convertToPayWxNotify(MchPayNotify payNotify) {
        PayWxNotify payWxNotify = new PayWxNotify();
        payWxNotify.setAppid(payNotify.getAppid());
        payWxNotify.setAttach(payNotify.getAttach());
        payWxNotify.setBank_type(payNotify.getBank_type());
        //payWxNotify.setBank_billno(null);
        // payWxNotify.setData_issubscribe(null);
        payWxNotify.setData_openid(payNotify.getOpenid());
        //payWxNotify.setDiscount(null);
        payWxNotify.setFee_type(payNotify.getFee_type());
        //payWxNotify.setInput_charset(null);
        //payWxNotify.setNotify_id(null);
        payWxNotify.setOut_trade_no(payNotify.getOut_trade_no());
        payWxNotify.setPartner(payNotify.getMch_id());
        //payWxNotify.setProduct_fee(null);
        payWxNotify.setSign_type(payNotify.getSign_type());
        payWxNotify.setSign(payNotify.getSign());
        payWxNotify.setTransaction_id(payNotify.getTransaction_id());
        payWxNotify.setTrade_type(payNotify.getTrade_type());
        //payWxNotify.setTrade_mode(null);
        // payWxNotify.setTransport_fee(null);
        payWxNotify.setTrade_state(0);//trade_state,"SUCCESS"
        payWxNotify.setTime_end(payNotify.getTime_end());
        payWxNotify.setTotal_fee(payNotify.getTotal_fee());
        return payWxNotify;

    }

    private RefundWxNotify convertToRefundWxNotify(RefundNotifyReqInfo refundNotifyReqInfo, String appid, String mch_id) {
        RefundWxNotify notify = new RefundWxNotify();
        notify.setRefund_id(refundNotifyReqInfo.getRefund_id());
        notify.setAppid(appid);
        notify.setMch_id(mch_id);
        notify.setOut_refund_no(refundNotifyReqInfo.getOut_refund_no());
        notify.setOut_trade_no(refundNotifyReqInfo.getOut_trade_no());
        notify.setTransaction_id(refundNotifyReqInfo.getTransaction_id());
        notify.setTotal_fee(refundNotifyReqInfo.getTotal_fee());
        notify.setRefund_fee(refundNotifyReqInfo.getRefund_fee());
        notify.setSettlement_refund_fee(refundNotifyReqInfo.getSettlement_refund_fee());
        notify.setSettlement_total_fee(refundNotifyReqInfo.getSettlement_total_fee());
        notify.setRefund_recv_accout(refundNotifyReqInfo.getRefund_recv_accout());
        notify.setRefund_account(refundNotifyReqInfo.getRefund_account());
        notify.setRefund_request_source(refundNotifyReqInfo.getRefund_request_source());
        notify.setRefund_status(refundNotifyReqInfo.getRefund_status());
        notify.setSuccess_time(refundNotifyReqInfo.getSuccess_time());
        return notify;
    }

    private Integer multiply(String v1, String v2) {

        try {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            Integer result = b1.multiply(b2).intValue();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws DocumentException {


//        BigDecimal b1 = new BigDecimal("0aaa.93908");
//        BigDecimal b2 = new BigDecimal("100");
//        System.out.println(b1.multiply(b2).intValue());
//
        Map<String,String> params = new HashMap<>();
        params.put("total_amount","0.01");
        PayCallback callback = new PayCallback();
        callback.convertToPayAliNotify(params);


//        Boolean strResult = "0000.1000".matches("^([0-9]*[.]?[0-9]*)$");
//        System.out.println(strResult);
//        File file = new File("wxfile.txt");
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Map<String, String> map = new HashMap<>()?
//        map.put("return_code", "SUCCESS");
//        map.put("return_msg", "OK");
//        //map.put("return_msg","签名失败");
//        //map.put("return_msg","参数格式校验错误");
//        String xml = "<xml>" +
//                "<appid><![CDATA[wx2421b1c4370ec43b]]></appid>" +
//                "<attach><![CDATA[支付测试]]></attach>" +
//                "<bank_type><![CDATA[CFT]]></bank_type>" +
//                "<fee_type><![CDATA[CNY]]></fee_type>" +
//                "<is_subscribe><![CDATA[Y]]></is_subscribe>" +
//                "<mch_id><![CDATA[10000100]]></mch_id>" +
//                "<nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>" +
//                "<openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>" +
//                "<out_trade_no><![CDATA[1409811653]]></out_trade_no>" +
//                "<result_code><![CDATA[SUCCESS]]></result_code>" +
//                "<return_code><![CDATA[SUCCESS]]></return_code>" +
//                "<sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>" +
//                "<sub_mch_id><![CDATA[10000100]]></sub_mch_id>" +
//                "<time_end><![CDATA[20140903131540]]></time_end>" +
//                "<total_fee>1</total_fee>" +
//                "<coupon_fee><![CDATA[10]]></coupon_fee>" +
//                "<coupon_count><![CDATA[1]]></coupon_count>" +
//                "<coupon_type><![CDATA[CASH]]></coupon_type>" +
//                "<coupon_id><![CDATA[10000]]></coupon_id>" +
//                "<coupon_fee><![CDATA[100]]></coupon_fee>" +
//                "<trade_type><![CDATA[JSAPI]]></trade_type>" +
//                "<transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>" +
//                "<test><test1>test1</test1><test2>test2</test2></test>" +
//                "</xml>";
//        PayCallback callback = new PayCallback();
//        Map<String, Object> res = callback.xmlToMap(xml);
//        System.out.println(res);

    }


    private Map<String, Object> xmlToMap(String xml) throws DocumentException {
        Document doc = DocumentHelper.parseText(xml);
        Element element = doc.getRootElement();
        return element(element);
    }

    private Map<String, Object> element(Element element) {
        Map<String, Object> map = new HashMap<>();
        Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()) {
            Element subElement = iterator.next();
            if (subElement.elements().size() > 0) {
                map.put(subElement.getQName().getName(), element(subElement));
            } else {
                map.put(subElement.getQName().getName(), subElement.getData());
            }
        }
        return map;
    }


}

