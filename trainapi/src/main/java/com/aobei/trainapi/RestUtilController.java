package com.aobei.trainapi;

import com.aobei.common.bean.IGtPushData;
import com.aobei.common.boot.EventPublisher;
import com.aobei.common.boot.event.IGtPushEvent;
import com.aobei.train.Roles;
import com.aobei.train.model.Customer;
import com.aobei.train.model.Order;
import com.aobei.trainapi.server.handler.PushHandler;
import custom.bean.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RestUtilController {

    @Autowired
    PushHandler pushHandler;
    @Autowired
    EventPublisher publisher;

    @RequestMapping("/push")
    public Map<String, String> push(@RequestParam String clientId,
                                    @RequestParam String client,
                                    @RequestParam String phonetype,
                                    @RequestParam(required = false) Boolean bind,
                                    @RequestParam(required = false) String orderId,
                                    @RequestParam(required = false) String target) {
        Map<String, String> map = new HashMap<>();
        IGtPushData data = new IGtPushData();
        data.setCid(clientId);
        data.setType(IGtPushData.BIND);
        Order order = new Order();
        order.setName("这是一个订单");
        if(orderId==null){
            map.put("msg","请输入一个想要跳转orderId");
            return map;
        }

        order.setPay_order_id(orderId);

        OrderInfo info = new OrderInfo(Roles.STUDENT);
        info.setOrder(order);


        try {
            switch (client) {
                case "custom":
                    if (bind) {
                        data.setClient(IGtPushData.Client.customer);
                        data.setAlia("testuser" + phonetype);
                        publisher.publish(new IGtPushEvent(this, data));
                        Thread.sleep(500);
                    }

                    pushHandler.pushOrderMessageToCustomer(order, "testuser" + phonetype);
                    break;
                case "partner":
                    if (bind) {
                        data.setClient(IGtPushData.Client.partner);
                        data.setAlia("testuser" + phonetype);
                        publisher.publish(new IGtPushEvent(this, data));
                        Thread.sleep(500);
                    }
                    if(target==null){
                        pushHandler.pushOrderMessageToPartner(order, "testuser" + phonetype);
                    }else if("homepage".equals(target)){
                        pushHandler.pushRobbingMessageToPartner("testuser" + phonetype);
                    }else {
                        map.put("msg","如果想要发起模拟抢单的推送，target参数请输入 homepage");
                        return map;
                    }

                    break;
//          case "student":
//              data.setClient(IGtPushData.Client.student);
//              data.setAlia("testuser");
//              pushHandler.pushOrderMessageBeforServiceToStudent(info,"testuser");
//              break;
            }
            map.put("code", "success");

        } catch (Exception e) {
            map.put("code", "error");
            e.printStackTrace();
        }


        return map;
    }

}
