package com.aobei.trainapi.server;


import com.alipay.api.AlipayApiException;
import com.aobei.train.model.Customer;
import com.aobei.train.model.Order;
import com.aobei.train.model.WxApp;
import com.aobei.train.model.WxMch;
import custom.bean.WxPaymentBody;
import weixin.popular.bean.paymch.MchOrderInfoResult;
import weixin.popular.bean.paymch.UnifiedorderResult;

public interface PayService {

    /**
     * 微信统一下单接口
     * @param customer 顾客信息
     * @param order 订单
     * @return  微信返回的数据
     */
    UnifiedorderResult wxUnifiedorder(Customer customer , Order order, WxApp wxApp, WxMch wxMch,String path,String openid,String trad_type);

    /**
     * 微信订单查询接口
     * @param customer 顾客信息
     * @param order 订单
     * @return  微信返回的数据
     */
    MchOrderInfoResult wxPayOrderquery(Customer customer,Order order,WxApp wxApp, WxMch wxMch);

    /**
     * 获取小程序调起微信支付请求字段
     * @param prepay_id 统一下单接口的prepayid
     * @return
     */
    WxPaymentBody wxWxPaymentBody(String prepay_id,WxApp wxApp, WxMch wxMch);


    String aliPaymentBody(Order order,String appid) throws AlipayApiException;

    String aliWapPayBody(Order order ,String appid) throws AlipayApiException;
    Order wxPayConfirm();
    Order aliPayConfirm();


}
