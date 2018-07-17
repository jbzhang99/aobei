package com.aobei.trainapi.server.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsHandlerTest {

    @Autowired
    SmsHandler handler;
    String phone = "18600203527";
    @Test
    public void sendCode() {
        handler.sendCode("1234",phone);
    }

    @Test
    public void sendToPattnerWhenCustomerPayed() {
        handler.sendToPattnerWhenCustomerPayed("订单1",phone);
    }

    @Test
    public void sendToCustomerWhenOrderCancel() {
        handler.sendToCustomerWhenOrderCancel("订单2","2018-05-02","北京大兴区",phone);
    }

    @Test
    public void sendToPartnerWhenOrderCancel() {
        handler.sendToPartnerWhenOrderCancel("订单3","2018-05-02","小明","北京大兴区",phone);
    }

    @Test
    public void sendToWorkWhenOrderCancel() {
        handler.sendToWorkWhenOrderCancel("订单4","小强","18600203527","2018-05-02","北京大兴区",phone);
    }

    @Test
    public  void all(){
        sendCode();
        sendToPattnerWhenCustomerPayed();
        sendToPartnerWhenOrderCancel();
        sendToCustomerWhenOrderCancel();
        sendToWorkWhenOrderCancel();
    }
}