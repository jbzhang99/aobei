package com.aobei.trainapi.server.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.UsersService;
import com.aobei.train.service.UsersWxService;
import com.aobei.trainapi.configuration.CustomProperties;
import com.aobei.trainapi.server.CustomerApiService;
import com.aobei.trainapi.server.PayService;
import com.aobei.trainapi.server.bean.AliPayClientMap;
import com.aobei.trainapi.util.XMLConverUtil2;
import custom.bean.Constant;
import custom.bean.WxPaymentBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.MchOrderInfoResult;
import weixin.popular.bean.paymch.MchOrderquery;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.util.JsonUtil;
import weixin.popular.util.MapUtil;
import weixin.popular.util.SignatureUtil;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    CustomProperties properties;
    @Autowired
    UsersWxService usersWxService;
    @Autowired
    UsersService usersService;
    @Autowired
    AliPayClientMap aliPayClientMap;
    @Autowired
    CustomerApiService customerApiService;
    Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);


    private static String WX_ERROR_CODE="ORDERPAID";
    /**
     * 微信支付统一下单接口
     *
     * @param customer 顾客信息
     * @param order    订单
     * @return
     */
    @Override
    public UnifiedorderResult wxUnifiedorder(Customer customer, Order order, WxApp wxApp, WxMch wxMch, String path, String openid,String trad_type) {


        String mch_id = wxMch.getMch_id();
        String appid = wxApp.getApp_id();
        String key = wxMch.getMch_key();
        String notify_url = properties.getWx().getPayNotifyUrl().replace("{path}", path);

        try{
            if (StringUtils.isEmpty(openid)) {
                Users users = usersService.selectByPrimaryKey(customer.getUser_id());
                UsersWxExample usersWxExample = new UsersWxExample();
                usersWxExample.or()
                        .andAppidEqualTo(appid)
                        .andWx_idEqualTo(users.getWx_id());
                UsersWx usersWx = singleResult(usersWxService.selectByExample(usersWxExample));
                if (usersWx != null) {
                    openid = usersWx.getOpenid();
                }
            }
        }catch (Exception e){
            logger.error("api-method:wxUnifiedorder error:no openid found openid:{},error:{}",openid,e.getMessage());
        }

        //组装调用接微信接口的字符串
        Unifiedorder unifiedorder = new Unifiedorder();
        unifiedorder.setAppid(appid);
        unifiedorder.setMch_id(mch_id);
        //利用随机主键作为随机字符串
        unifiedorder.setNonce_str(IdGenerator.generateId() + "");
        unifiedorder.setNotify_url(notify_url);
        unifiedorder.setFee_type("CNY");
        unifiedorder.setTrade_type(trad_type);
        unifiedorder.setOut_trade_no(order.getPay_order_id());
        unifiedorder.setTotal_fee(order.getPrice_pay() + "");
        if (StringUtils.isEmpty(order.getName())) {
            order.setName(Constant.SIGN_NAME);
        }
        if (order.getName().getBytes().length > 100) {
            order.setName(order.getName().substring(0, 20));
        }
        unifiedorder.setBody(order.getName());
        unifiedorder.setSign_type("MD5");
        if (!"APP".equals(trad_type))
            unifiedorder.setOpenid(openid);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        unifiedorder.setTime_start(format.format(order.getCreate_datetime()));
        unifiedorder.setTime_expire(format.format(order.getExpire_datetime()));
        logger.info("api-method:wxUnifiedorder param to wx unifiedorder:{}", JSON.toJSON(unifiedorder));
        //调用统一下单接口
        UnifiedorderResult unifiedorderResult =PayMchAPI.payUnifiedorder(unifiedorder, key);

        logger.info("api-method:wxUnifiedorderresult of  unifiedorderResult:{}", JSON.toJSON(unifiedorderResult));
        //当用户再次进行统一下单操作时，微信会返回订单已支付。所以要将我们的订单标记为已支付。这种情况下，微信回调表会为空。
        if(WX_ERROR_CODE.equals(unifiedorderResult.getErr_code())){
            customerApiService.orderPaysuccess(order.getPay_order_id(),1);
            logger.info("api-method:wxUnifiedorder WX_ERROR_CODE ",WX_ERROR_CODE);
        }
        return unifiedorderResult;
    }

    /**
     * 微信支付，订单查询接口
     *
     * @param customer 顾客信息
     * @param order    订单
     * @return
     */
    @Override
    public MchOrderInfoResult wxPayOrderquery(Customer customer, Order order, WxApp wxApp, WxMch wxMch) {
        String wxKey = wxMch.getMch_key();
        String appid = wxApp.getApp_id();
        String mch_id = wxMch.getMch_id();
        MchOrderquery mchOrderquery = new MchOrderquery();
        mchOrderquery.setMch_id(mch_id);
        mchOrderquery.setAppid(appid);
        mchOrderquery.setOut_trade_no(order.getPay_order_id());
        mchOrderquery.setSign_type("MD5");
        mchOrderquery.setNonce_str(IdGenerator.generateId() + "");
        MchOrderInfoResult mchOrderInfoResult = PayMchAPI.payOrderquery(mchOrderquery, wxKey);
        return mchOrderInfoResult;
    }

    @Override
    public WxPaymentBody wxWxPaymentBody(String prepay_id, WxApp wxApp, WxMch wxMch) {
        String wxKey = wxMch.getMch_key();
        String appid = wxApp.getApp_id();
        WxPaymentBody body = new WxPaymentBody();
        body.setAppId(appid);
        body.set_package("prepay_id=" + prepay_id);
        body.setNonceStr(IdGenerator.generateId() + "");
        body.setSignType("MD5");
        body.setTimeStamp(new Date().getTime() / 1000 + "");
        Map<String, String> map = body.toMap();
        String sign = SignatureUtil.generateSign(map, body.getSignType(), wxKey);
        body.setSign(sign);
        return body;
    }

    @Override
    public String aliPaymentBody(Order order, String appId) throws AlipayApiException {
        AlipayClient alipayClient = aliPayClientMap.getClient(appId);
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(order.getName());
        model.setSubject(order.getName());
        model.setOutTradeNo(order.getPay_order_id());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(order.getPrice_pay() / 100D + "");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        String notify_url = properties.getAlipay().getPayNotifyUrl().replace("{path}", appId);
        request.setNotifyUrl(notify_url);
        //这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse aliResponse = alipayClient.sdkExecute(request);
        aliResponse.isSuccess();
        //就是orderString 可以直接给客户端请求，无需再做处理。
        return aliResponse.getBody();
    }

    @Override
    public Order wxPayConfirm() {


        return null;
    }

    @Override
    public Order aliPayConfirm() {
        return null;
    }

    /**
     * 统一下单
     * @param unifiedorder unifiedorder
     * @param key key
     * @return UnifiedorderResult
     */
    @Deprecated
    private   UnifiedorderResult payUnifiedorder(Unifiedorder unifiedorder,String key){

            Map<String,String> map = MapUtil.objectToMap(unifiedorder,"detail");
            //@since 2.8.8 detail 字段签名处理
            if(unifiedorder.getDetail() != null){
                map.put("detail", JsonUtil.toJSONString(unifiedorder.getDetail()));
            }
            if(key != null){
                String sign = SignatureUtil.generateSign(map,unifiedorder.getSign_type(),key);
                unifiedorder.setSign(sign);
            }
            String unifiedorderXML = XMLConverUtil2.convertToXML(unifiedorder);
            HttpUriRequest httpUriRequest = RequestBuilder.post()
                    .setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_XML.toString()))
                    .setUri("https://api.mch.weixin.qq.com/pay/unifiedorder")
                    .setEntity(new StringEntity(unifiedorderXML, Charset.forName("utf-8")))
                    .build();
        try{
            return LocalHttpClient.executeXmlResult(httpUriRequest,UnifiedorderResult.class,unifiedorder.getSign_type(),key);
        }catch (Exception e){
            logger.error("api-method:payUnifiedorder failn error:{}",e.getMessage());
        }

        UnifiedorderResult result = new UnifiedorderResult();
        result.setErr_code("FAIL");
        result.setErr_code_des(unifiedorderXML);
       return result;
    }

}
