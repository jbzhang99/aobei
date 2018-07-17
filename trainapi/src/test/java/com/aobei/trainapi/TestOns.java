package com.aobei.trainapi;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.*;
import com.aobei.train.IdGenerator;
import com.aobei.trainapi.server.handler.OnsHandler;
import custom.bean.ons.CompleteOrderMessage;
import custom.bean.ons.RobbingOrderMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Properties;


public class TestOns {








    public static void main(String[] args) {
        Properties props = new Properties();
        // 您在 MQ 控制台创建的 Producer ID
        props.put(PropertyKeyConst.ProducerId, "PID_aobei_Producer");
        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
        props.put(PropertyKeyConst.AccessKey, "LTAIYlFXdaXPDOej");
        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
        props.put(PropertyKeyConst.SecretKey, "yKVGJOwWrmmx3wAJG0nupzU46PVZou");
        // 设置 TCP 接入域名（此处以公共云的公网接入为例）
        props.put(PropertyKeyConst.ONSAddr, "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");


        Producer producer = ONSFactory.createProducer(props);
        // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
        producer.start();



//        RobbingOrderMessage robbingOrderMessage = new RobbingOrderMessage();
//        robbingOrderMessage.setPay_order_id("123456");
//        robbingOrderMessage.setMessageId("L98799");
//
//        String json = JSON.toJSONString(robbingOrderMessage);
//        Message onsMessage = new Message();
//        onsMessage.setBody(json.getBytes());
//        onsMessage.setTopic("aobei_topic");
//        onsMessage.setTag("dev_" + robbingOrderMessage.getTag());
//        producer.send(onsMessage);


        String[] strs = new String[]{
                "1530182217",
                "1530143855",
                "1530137949",
                "1530066211",
                "1529911575",
                "1529910431",
                "1529888680",
                "1529848387",
                "1529835850",
                "1529835672",
                "1529834657",
                "1529814276",
                "1529813303",
                "1529808932",
                "1529803041",
                "1529734933",
                "1529719999",
                "1529718415"
        };

        for (int i = 0; i < strs.length; i++) {

            System.out.println(strs[i]);
            CompleteOrderMessage completeOrderMessage = new CompleteOrderMessage();
            completeOrderMessage.setMessageId(IdGenerator.generateId() + "");
            completeOrderMessage.setPay_order_id(strs[i]);
            String json = JSON.toJSONString(completeOrderMessage);
            Message onsMessage = new Message();
            onsMessage.setBody(json.getBytes());
            onsMessage.setTopic("aobei_topic");
            onsMessage.setTag("pro_" + completeOrderMessage.getTag());
            producer.send(onsMessage);
        }
        producer.shutdown();


    }




}



