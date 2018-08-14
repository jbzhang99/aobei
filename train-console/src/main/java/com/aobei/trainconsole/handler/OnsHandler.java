package com.aobei.trainconsole.handler;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.aobei.train.IdGenerator;
import com.aobei.trainconsole.configuration.CustomProperties;
import custom.bean.ons.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OnsHandler {

    @Autowired
    CustomProperties properties;
    @Autowired
    Producer mqProducer;

    public SendResult sendRejectMessage(String pay_order_id, Long partner_id, Long delayTime) {
        RejectOrderMessage rejectOrderMessage = new RejectOrderMessage();
        rejectOrderMessage.setMessageId(IdGenerator.generateId() + "");
        rejectOrderMessage.setPay_order_id(pay_order_id);
        rejectOrderMessage.setPartner_id(partner_id);
        String json = JSON.toJSONString(rejectOrderMessage);
        Message onsMessage = new Message();
        onsMessage.setTag(rejectOrderMessage.getTag());
        onsMessage.setBody(json.getBytes());
        onsMessage.setTopic(properties.getAliyun().getOns().getTopic());
        if (delayTime != null)
            onsMessage.setStartDeliverTime(delayTime);
        return mqProducer.send(onsMessage);
    }

    public SendResult sendCancelMessage(String pay_order_id, Long delayTime) {
        CancelOrderMessage cancelOrderMessage = new CancelOrderMessage();
        cancelOrderMessage.setMessageId(IdGenerator.generateId() + "");
        cancelOrderMessage.setPay_order_id(pay_order_id);
        String json = JSON.toJSONString(cancelOrderMessage);
        Message onsMessage = new Message();
        onsMessage.setTag(cancelOrderMessage.getTag());
        onsMessage.setBody(json.getBytes());
        onsMessage.setTopic(properties.getAliyun().getOns().getTopic());
        if (delayTime != null)
            onsMessage.setStartDeliverTime(delayTime);
        return mqProducer.send(onsMessage);
    }

    public SendResult sendRefundMessage(String pay_order_id, String appid, Long refund_id, Long delayTime) {
        RefundOrderMessage refundOrderMessage = new RefundOrderMessage();
        refundOrderMessage.setMessageId(IdGenerator.generateId() + "");
        refundOrderMessage.setAppid(appid);
        refundOrderMessage.setPay_order_id(pay_order_id);
        refundOrderMessage.setRefund_id(refund_id);
        String json = JSON.toJSONString(refundOrderMessage);
        Message onsMessage = new Message();
        onsMessage.setTag(refundOrderMessage.getTag());
        onsMessage.setBody(json.getBytes());
        onsMessage.setTopic(properties.getAliyun().getOns().getTopic());
        if (delayTime != null)
            onsMessage.setStartDeliverTime(delayTime);
        return mqProducer.send(onsMessage);
    }

    public SendResult sendCompleteMessage(String pay_order_id, Long delayTime) {
        CompleteOrderMessage completeOrderMessage = new CompleteOrderMessage();
        completeOrderMessage.setMessageId(IdGenerator.generateId() + "");
        completeOrderMessage.setPay_order_id(pay_order_id);
        String json = JSON.toJSONString(completeOrderMessage);
        Message onsMessage = new Message();
        onsMessage.setTag(completeOrderMessage.getTag());
        onsMessage.setBody(json.getBytes());
        onsMessage.setTopic(properties.getAliyun().getOns().getTopic());
        if (delayTime != null)
            onsMessage.setStartDeliverTime(delayTime);
        return mqProducer.send(onsMessage);
    }
    public SendResult sendRobbingMessage(String pay_order_id,Integer type,Long delayTime){
        RobbingOrderMessage robbingOrderMessage = new RobbingOrderMessage();
        robbingOrderMessage.setMessageId(IdGenerator.generateId()+"");
        robbingOrderMessage.setPay_order_id(pay_order_id);
        robbingOrderMessage.setType(type);
        String json = JSON.toJSONString(robbingOrderMessage);
        Message onsMessage = new Message();
        onsMessage.setTag(robbingOrderMessage.getTag());
        onsMessage.setBody(json.getBytes());
        onsMessage.setTopic(properties.getAliyun().getOns().getTopic());
        if (delayTime != null)
            onsMessage.setStartDeliverTime(delayTime);
        return mqProducer.send(onsMessage);
    }

}
