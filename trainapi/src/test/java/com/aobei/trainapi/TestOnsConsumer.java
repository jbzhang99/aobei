package com.aobei.trainapi;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.*;
import custom.bean.ons.RobbingOrderMessage;

import java.util.Properties;


public class TestOnsConsumer {








    public static void main(String[] args) {
        Properties props = new Properties();
        // 您在 MQ 控制台创建的 Producer ID
        props.put(PropertyKeyConst.ConsumerId, "CID_T_dev_robbing");
        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
        props.put(PropertyKeyConst.AccessKey, "LTAIYlFXdaXPDOej");
        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
        props.put(PropertyKeyConst.SecretKey, "yKVGJOwWrmmx3wAJG0nupzU46PVZou");
        // 设置 TCP 接入域名（此处以公共云的公网接入为例）
        props.put(PropertyKeyConst.ONSAddr, "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        Consumer consumer = ONSFactory.createConsumer(props);
        consumer.subscribe("aobei_topic", "dev_robbingOrder", new MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
                System.out.println("Receive: " + message);
                return Action.CommitMessage;
            }
        });
        consumer.start();
        while (true){

        }
    }




}



