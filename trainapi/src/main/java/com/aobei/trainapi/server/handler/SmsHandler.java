package com.aobei.trainapi.server.handler;

import com.aobei.common.bean.SmsData;
import com.aobei.common.boot.EventPublisher;
import com.aobei.common.boot.event.SmsSendEvent;
import custom.bean.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SmsHandler {
    Logger logger = LoggerFactory.getLogger(SmsHandler.class);
    @Autowired
    EventPublisher publisher;

    public void sendCode(String code, String... phone) {
        logger.info("eventSMS:sendCode phone:{},code:{}", phone, code);
        SmsData data = new SmsData();
        data.setSignName(Constant.SIGN_NAME);
        data.setPhoneNumber(phone);
        data.setTemplateCode(Constant.SEND_VERIFICATION_CODE);
        data.addParam("code", code);
        publish(data);
    }

    public void sendToPattnerWhenCustomerPayed(String orderName, String... phone) {
        SmsData data = new SmsData();
        data.setSignName(Constant.SIGN_NAME);
        data.setTemplateCode(Constant.SEND_TO_PARTNER_WHEN_CUSTOMER_PAYED);
        data.addParam("product_name", orderName);
        data.setPhoneNumber(phone);
        publish(data);
    }

    public void sendToCustomerWhenOrderCancel(String orderName, String beginTime, String address, String... phone) {
        SmsData data = new SmsData();
        data.setSignName(Constant.SIGN_NAME);
        data.setTemplateCode(Constant.SEND_TO_CUSTOMER_WHEN_ORDER_CANCEL);
        data.addParam("product_name", orderName);
        data.addParam("c_begin_datetime", beginTime);
        if (address.length() > 20) {
            address = address.substring(0, 20);
        }
        data.addParam("cus_address", address);
        data.setPhoneNumber(phone);
        publish(data);
    }

    public void sendToPartnerWhenOrderCancel(String orderName, String beginTime, String userName, String address, String... phone) {
        SmsData data = new SmsData();
        data.setSignName(Constant.SIGN_NAME);
        data.setTemplateCode(Constant.SEND_TO_PARTNER_WHEN_ORDER_CANCEL);
        data.addParam("product_name", orderName);
        data.addParam("c_begin_datetime", beginTime);
        data.addParam("cus_username", userName);
        if (address.length() > 20) {
            address = address.substring(0, 20);
        }
        data.addParam("cus_address", address);
        data.setPhoneNumber(phone);
        publish(data);
    }

    public void sendToWorkWhenOrderCancel(String orderName, String username,String userPhone,String beginTime, String address, String... phone) {

       // 您于${c_begin_datetime}去${cus_address}，为${cus_username}，手机号：${cus_phone}，进行${product_name}服务的订单已取消。
        SmsData data = new SmsData();
        data.setSignName(Constant.SIGN_NAME);
        data.setTemplateCode(Constant.SEND_TO_WORK_WHEN_ORDER_CANCEL);
        data.addParam("cus_phone",userPhone);
        data.addParam("cus_username",username);
        data.addParam("product_name", orderName);
        data.addParam("c_begin_datetime", beginTime);
        if (address.length() > 20) {
            address = address.substring(0, 20);
        }
        data.addParam("cus_address", address);
        data.setPhoneNumber(phone);
        publish(data);
    }

    public void sendToWorkWhenOrderAssign(String product_name, String cus_username, String address, String c_begin_datetime, String cus_phone, String... phone) {
        SmsData data = new SmsData();
        data.setPhoneNumber(phone);
        data.setSignName(Constant.SIGN_NAME);
        data.setTemplateCode(Constant.SEND_TO_WORK_WHEN_ORDER_ASSIGN);
        data.addParam("product_name", product_name);
        data.addParam("cus_username", cus_username);
        if (address.length() > 20) {
            address = address.substring(0, 20);
        }
        data.addParam("cus_address", address);
        data.addParam("c_begin_datetime", c_begin_datetime);
        data.addParam("cus_phone", cus_phone);
        publish(data);
    }

    public void sendToCustomerWhenOrderAlter(String product_name, String worker_name, String c_begin_datetime, String address, String... phone) {
        SmsData data = new SmsData();
        data.setPhoneNumber(phone);
        data.setSignName(Constant.SIGN_NAME);
        data.setTemplateCode(Constant.SEND_TO_CUSTOMER_WHEN_ORDER_ALTER);
        data.addParam("product_name", product_name);
        data.addParam("worker_name", worker_name);
        data.addParam("c_begin_datetime", c_begin_datetime);
        if (address.length() > 20) {
            address = address.substring(0, 20);
        }
        data.addParam("cus_address", address);
        publish(data);
    }

    public void sendToCustomerWhenOrderAssignWork(String product_name, String worker_name, String c_begin_datetime, String address, String... phone) {
        SmsData data = new SmsData();
        data.setPhoneNumber(phone);
        data.setSignName(Constant.SIGN_NAME);
        data.setTemplateCode(Constant.SEND_TO_CUSTOMER_WHEN_ORDER_ASSIGN_WORK);
        data.addParam("product_name", product_name);
        data.addParam("worker_name", worker_name);
        data.addParam("c_begin_datetime", c_begin_datetime);
        if (address.length() > 20) {
            address = address.substring(0, 20);
        }
        data.addParam("cus_address", address);

        publish(data);
    }


    private void publish(SmsData data) {
        SmsSendEvent event = new SmsSendEvent(this, data);
        publisher.publish(event);
    }
}
