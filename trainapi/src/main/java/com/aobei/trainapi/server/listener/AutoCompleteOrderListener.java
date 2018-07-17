package com.aobei.trainapi.server.listener;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aobei.common.boot.OnsMessageListener;
import com.aobei.train.model.*;
import com.aobei.train.service.OrderLogService;
import com.aobei.train.service.OrderService;
import com.aobei.train.service.ServiceUnitService;
import com.aobei.train.service.ServiceunitPersonService;
import com.aobei.trainapi.configuration.CustomProperties;
import custom.bean.Status;
import custom.bean.ons.CompleteOrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AutoCompleteOrderListener implements OnsMessageListener {
    @Autowired
    CustomProperties properties;
    @Autowired
    OrderService orderService;
    @Autowired
    ServiceUnitService serviceUnitService;
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    ServiceunitPersonService serviceunitPersonService;
    Logger logger = LoggerFactory.getLogger(AutoCompleteOrderListener.class);

    @Override
    public String getTopic() {
        return properties.getAliyun().getOns().getTopic();
    }

    @Override
    public String getTag() {
        return "completeOrder";
    }

    @Override
    public boolean isClustering() {
        return true;
    }

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        logger.info("aliOns-AutoCompleteOrderListener message{}", message);
        try {
            String json = new String(message.getBody());
            CompleteOrderMessage completeOrderMessage = JSON.parseObject(json, CompleteOrderMessage.class);
            completeOrder(completeOrderMessage);
        } catch (Exception e) {
            logger.error("aliOns-AutoCompleteOrderListener message{},e{}", message, e.getMessage());
        }
        return Action.CommitMessage;

    }


    private boolean completeOrder(CompleteOrderMessage message) {
        logger.info("aliOns-AutoCompleteOrderListener:message{}", message);
        String pay_order_id = message.getPay_order_id();
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        if (order == null)
            return true;
        ServiceUnitExample example = new ServiceUnitExample();
        example.or().andPay_order_idEqualTo(pay_order_id)
                .andPidEqualTo(0l);
        if (Status.PayStatus.payed.value.equals(order.getPay_status())
                && Status.OrderStatus.wait_service.value.equals(order.getStatus_active())) {
            ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(example));

            ServiceUnitExample example1 = new ServiceUnitExample();
            example1.or().andGroup_tagEqualTo(serviceUnit.getGroup_tag());
            ServiceUnit unit = new ServiceUnit();
            unit.setGroup_tag(serviceUnit.getGroup_tag());
            unit.setStatus_active(Status.OrderStatus.done.value);
            unit.setFinish_datetime(new Date());
            serviceUnitService.updateByExampleSelective(unit,example1);

            order = new Order();
            order.setPay_order_id(pay_order_id);
            order.setStatus_active(Status.OrderStatus.done.value);
            ServiceunitPersonExample serviceunitPersonExample = new ServiceunitPersonExample();
            serviceunitPersonExample.or().andServiceunit_idEqualTo(serviceUnit.getServiceunit_id());
            ServiceunitPerson serviceunitPerson = new ServiceunitPerson();
            serviceunitPerson.setStatus_active(Status.OrderStatus.done.value);
            if (orderService.updateByPrimaryKeySelective(order) > 0) {
                serviceunitPersonService.updateByExampleSelective(serviceunitPerson, serviceunitPersonExample);
                orderLogService.xInsert("system", 0l, pay_order_id, "【订单自动完成】");
                logger.info("aliOns-AutoCompleteOrderListener: status{SUCCESS}");
            } else {
                logger.info("aliOns-AutoCompleteOrderListener: status{FAIL}");
            }
            return true;

        }

        return true;
    }
}
