package com.aobei.trainapi.testcommon;


import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aobei.common.bean.SmsData;
import com.aobei.common.boot.EventPublisher;
import com.aobei.common.boot.event.SmsSendEvent;
import com.aobei.trainapi.configuration.CustomProperties;
import custom.bean.Constant;
import custom.bean.ons.CancelOrderMessage;
import custom.bean.ons.RefundOrderMessage;
import custom.bean.ons.RejectOrderMessage;

import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CommonTest {

    @Autowired
    EventPublisher publisher;
    @Autowired
    Producer producer;
    @Autowired
    CustomProperties properties;
    @Test
    public void testSms(){


        SmsData data  = new SmsData();
        data.setSignName(Constant.SIGN_NAME);
        String[] phone = new String[]{"18600203527"};
        data.setPhoneNumber(phone);
        data.setTemplateCode(Constant.SEND_VERIFICATION_CODE);
        data.addParam("code","3459");
        SmsSendEvent event  = new SmsSendEvent(this,data);
        publisher.publish(event);
    }

    @Test
    public void testDelaySms(){

        SmsData data  = new SmsData();
        data.setSignName(Constant.SIGN_NAME);
        String[] phone = new String[]{"18600203527"};
        data.setPhoneNumber(phone);
        data.setTemplateCode(Constant.SEND_VERIFICATION_CODE);
        data.addParam("code","3459");
        Long delay  = System.currentTimeMillis()+10000;
        data.setStartDeliverTime(delay);
        SmsSendEvent event  = new SmsSendEvent(this,data);
        publisher.publish(event);
    }

    @Test
    public void testMq(){
        Message message1  = new Message();
        message1.setTopic(properties.getAliyun().getOns().getTopic());
        message1.setTag("cancelOrder");
        message1.setBody(JSON.toJSONBytes(new CancelOrderMessage()));
        Message message2  = new Message();
        message2.setTopic(properties.getAliyun().getOns().getTopic());
        message2.setTag("rejectOrder");
        message2.setBody(JSON.toJSONBytes(new RejectOrderMessage()));
        Message message3  = new Message();
        message3.setTopic(properties.getAliyun().getOns().getTopic());
        message3.setTag("refundOrder");
        message3.setBody(JSON.toJSONBytes(new RefundOrderMessage()));
        System.out.println(producer.send(message1).toString());
        System.out.println(producer.send(message2).toString());
        System.out.println(producer.send(message2).toString());

    }
    public static void main(String[] args) {
		SimpleDateFormat sdf =new SimpleDateFormat("HH:mm");
	}

}
