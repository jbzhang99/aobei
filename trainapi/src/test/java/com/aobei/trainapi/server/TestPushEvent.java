package com.aobei.trainapi.server;

import com.aobei.common.bean.IGtPushData;
import com.aobei.common.boot.EventPublisher;
import com.aobei.common.boot.event.IGtPushEvent;
import com.aobei.train.model.Order;
import com.aobei.train.model.Partner;
import com.aobei.train.model.Student;
import com.aobei.train.model.StudentExample;
import com.aobei.train.service.OrderService;
import com.aobei.train.service.PartnerService;
import com.aobei.train.service.StudentService;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.server.handler.PushHandler;
import custom.bean.OrderInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPushEvent {

    @Autowired
    EventPublisher publisher;
    @Autowired
    PushHandler pushHandler;
    @Autowired
    OrderService orderService;
    @Autowired
    StudentService studentService;
    @Autowired
    PartnerApiService partnerApiService;
    @Autowired
    PartnerService partnerService;
    @Test
    public void testPushEvent(){

        IGtPushData data = new IGtPushData();
        List<String>  alias = new ArrayList<>();
        alias.add("1067670088150966272");
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("这是标题");
        data.setText("这是内容");
        data.setClient(IGtPushData.Client.customer);
        data.setType(IGtPushData.SINGLE);
        data.setTransmissionContent("这是透传参数");
        IGtPushEvent event  = new IGtPushEvent(this,data);
        publisher.publish(event);




    }

    @Test
    public  void testPushEventBind(){


        IGtPushData data = new IGtPushData();
        data.setType(IGtPushData.BIND);
        data.setAlia("user02");
        data.setClient(IGtPushData.Client.customer);
        data.setCid("10394898888");
        IGtPushEvent event  = new IGtPushEvent(this,data);
        publisher.publish(event);


    }

    @Test
    public void testPushHandler(){
        /*Order order  = orderService.selectByPrimaryKey("1528274898_1");
        pushHandler.pushOrderMessageToPartner(order,"1107667325747568641");*/
        Student student = studentService.selectByPrimaryKey(1135929198610685952l);
        Partner partner = partnerService.selectByPrimaryKey(1135912592396460032l);
        OrderInfo orderInfo = partnerApiService.partner_order_detail(partner, "1535967698_2");
        pushHandler.pushOrderMessageToStudent(orderInfo,String.valueOf(student.getStudent_id()));
    }

}
