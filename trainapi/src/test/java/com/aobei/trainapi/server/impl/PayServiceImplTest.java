package com.aobei.trainapi.server.impl;

import com.alipay.api.AlipayApiException;
import com.aobei.train.model.*;
import com.aobei.train.service.CustomerService;
import com.aobei.train.service.OrderService;
import com.aobei.train.service.WxAppService;
import com.aobei.train.service.WxMchService;
import com.aobei.trainapi.server.PayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import weixin.popular.bean.paymch.MchOrderInfoResult;
import weixin.popular.bean.paymch.UnifiedorderResult;

import static org.junit.Assert.*;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PayServiceImplTest {

    @Autowired
    PayService payService;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderService orderService;
    @Autowired
    WxAppService wxAppService;
    @Autowired
    WxMchService wxMchService;
    static String path = "wx_m_customer";
    @Test
    public void wxUnifiedorder() {
        WxAppExample wxAppExample = new WxAppExample();
        wxAppExample.or().andPathEqualTo(path);
        WxApp wxApp = singleResult(wxAppService.selectByExample(wxAppExample));
        WxMch wxMch = wxMchService.selectByPrimaryKey(wxApp.getMch_id());
        String pay_order_id = "1070054184611987456716160";
        Long uid = 1054072042874716160l;
        Customer customer = customerService.selectByPrimaryKey(uid);
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        UnifiedorderResult unifiedorderResult = payService.wxUnifiedorder(customer, order,wxApp,wxMch,path,"opneid","JSAPI");
        unifiedorderResult.getErr_code();
    }

    @Test
    public void wxPayOrderquery() {
        WxAppExample wxAppExample = new WxAppExample();
        wxAppExample.or().andPathEqualTo(path);
        WxApp wxApp = singleResult(wxAppService.selectByExample(wxAppExample));
        WxMch wxMch = wxMchService.selectByPrimaryKey(wxApp.getMch_id());
        String pay_order_id = "1070054184611987456716160";
        Long uid = 1054072042874716160l;
        Customer customer = customerService.selectByPrimaryKey(uid);
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        MchOrderInfoResult mchOrderInfoResult = payService.wxPayOrderquery(customer, order,wxApp,wxMch);

    }
    @Test
    public void  aliWapPay(){

        String pay_order_id = "1525949654";
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        try {
            String  body  = payService.aliWapPayBody(order,"2018052960271202");
            System.out.println(body);

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

    }
}