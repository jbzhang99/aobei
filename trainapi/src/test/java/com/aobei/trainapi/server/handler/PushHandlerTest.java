package com.aobei.trainapi.server.handler;

import com.aobei.train.Roles;
import com.aobei.train.model.Order;
import com.aobei.train.service.OrderService;
import custom.bean.OrderInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PushHandlerTest {

    @Autowired
    PushHandler handler;
    @Autowired
    OrderService orderService;
    @Test
    public void  testPushCustomer(){
        String alia = "1054072042874716160";
        Order order = orderService.selectByPrimaryKey("1524841594");
        OrderInfo info  = orderService.orderInfoDetail(Roles.CUSTOMER,order);
        handler.pushCancelOrderMessageToCustomer(order,alia);
        handler.pushOrderMessageBeforServiceToCustomer(info,alia);
        handler.pushOrderMessageToCustomer(order,alia);
        handler.pushOrderMessageWhenStudentChangeToCustomer(info,alia);


    }


    @Test
    public  void testPushStudent(){
        String alia = "1054072042874716160";
        Order order = orderService.selectByPrimaryKey("1524841594");
        OrderInfo info  = orderService.orderInfoDetail(Roles.CUSTOMER,order);
        handler.pushCancelOrderMessageToStudent(info,alia);
        handler.pushOrderMessageBeforServiceToStudent(info,alia);
        handler.pushOrderMessageToStudent(info,alia);

    }

    @Test
    public  void testPushPartner(){
        String alia = "1054072042874716160";
        Order order = orderService.selectByPrimaryKey("1524841594");
        OrderInfo info  = orderService.orderInfoDetail(Roles.CUSTOMER,order);
        handler.pushCancelOrderMessageToPartner(order,alia);
        handler.pushOrderMessageToPartner(order,alia);

    }
}
