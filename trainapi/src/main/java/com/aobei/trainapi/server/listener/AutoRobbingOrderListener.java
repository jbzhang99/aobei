package com.aobei.trainapi.server.listener;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aobei.common.boot.OnsMessageListener;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainapi.configuration.CustomProperties;
import com.aobei.trainapi.server.ApiOrderService;
import custom.bean.ons.RobbingOrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Component
public class AutoRobbingOrderListener implements OnsMessageListener {

    @Autowired
    CustomProperties properties;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    RobbingService robbingService;
    @Autowired
    ServiceUnitService serviceUnitService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ProSkuService proSkuService;
    @Autowired
    StationService stationService;
    @Autowired
    MetadataService metadataService;
    @Autowired
    ProductService productService;
    @Autowired
    StoreService storeService;
    @Autowired
    ApiOrderService apiOrderService;
    Logger logger = LoggerFactory.getLogger(AutoRobbingOrderListener.class);

    @Override
    public String getTopic() {
        return properties.getAliyun().getOns().getTopic();
    }

    @Override
    public String getTag() {
        return "robbing";
    }

    @Override
    public boolean isClustering() {
        return true;
    }

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {

        logger.info("aliOns-AutoRobbingOrderListener message{}", message);
        try {
            String json = new String(message.getBody());
            RobbingOrderMessage robbingOrderMessage = JSON.parseObject(json, RobbingOrderMessage.class);
            Integer type  = robbingOrderMessage.getType();
            if(type==null || type == 0) {
                prepareRobbingOrder(robbingOrderMessage);
            }else {
                stopRobbindOrder(robbingOrderMessage);
            }
        } catch (Exception e) {
            logger.error("aliOns-AutoRobbingOrderListener message{},e{}", message, e.getMessage());
        }
        return Action.CommitMessage;

    }

    private boolean prepareRobbingOrder(RobbingOrderMessage message) {
        logger.info("aliOns-AutoRobbingOrderListener:message{}", message);
        String pay_order_id = message.getPay_order_id();
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        if (order.getProxyed()==null || order.getProxyed() == 0)
            apiOrderService.startRobbing(order);
        return true;

    }

    private  boolean stopRobbindOrder(RobbingOrderMessage message){
        logger.info("aliOns-AutoRobbingOrderListener:message{}", message);
        String pay_order_id = message.getPay_order_id();
        ServiceUnitExample serviceunitExample  = new ServiceUnitExample();
        serviceunitExample.or()
                .andRobbingEqualTo(1)
                .andPay_order_idEqualTo(pay_order_id)
                .andPidEqualTo(0L);

       ServiceUnit serviceUnit  = singleResult( serviceUnitService.selectByExample(serviceunitExample));
       if (serviceUnit!=null && new Integer(1).equals(serviceUnit.getRobbing())){
           Robbing robbing  = new Robbing();
           robbing.setStatus(0);
           RobbingExample robbingExample  = new RobbingExample();
           robbingExample.or().andServiceunit_idEqualTo(serviceUnit.getServiceunit_id());
           robbingService.updateByExampleSelective(robbing,robbingExample);
           ServiceUnit updateUnit = new ServiceUnit();
           updateUnit.setServiceunit_id(serviceUnit.getServiceunit_id());
           updateUnit.setRobbing(3);
           serviceUnitService.updateByPrimaryKeySelective(updateUnit);
       }
        logger.info("aliOns-AutoRobbingOrderListener:SUCCESS");
       return true;
    }


}
