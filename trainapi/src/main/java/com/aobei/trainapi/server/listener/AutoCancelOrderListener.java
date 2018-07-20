package com.aobei.trainapi.server.listener;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aobei.common.boot.OnsMessageListener;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.Customer;
import com.aobei.train.model.Order;
import com.aobei.train.model.ServiceUnit;
import com.aobei.train.model.ServiceUnitExample;
import com.aobei.train.service.*;
import com.aobei.trainapi.configuration.CustomProperties;
import com.aobei.trainapi.schema.Errors;
import com.aobei.trainapi.server.bean.MessageContent;
import com.aobei.trainapi.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import custom.bean.ons.CancelOrderMessage;
import custom.util.ParamsCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Component
public class AutoCancelOrderListener implements OnsMessageListener {
    Logger logger = LoggerFactory.getLogger(AutoCancelOrderListener.class);

    @Autowired
    OrderService orderService;
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    CustomProperties properties;
    @Autowired
    RedisService redisService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ServiceUnitService serviceUnitService;
    @Autowired
    MessageService messageService;
    @Override
    public String getTopic() {
        return properties.getAliyun().getOns().getTopic();
    }

    @Override
    public String getTag() {
        return "cancelOrder";
    }

    @Override
    public boolean isClustering() {
        return true;
    }

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        logger.info("aliOns-AutoCancelOrderListener message{}", message);
        try {
            String json = new String(message.getBody());
            CancelOrderMessage cancelOrderMessage = JSON.parseObject(json, CancelOrderMessage.class);
            cancleOrder(cancelOrderMessage);
        } catch (Exception e) {
            logger.error("aliOns-AutoCancelOrderListener message{},e{}", message, e.getMessage());
        }
        return Action.CommitMessage;


    }


    private boolean cancleOrder(CancelOrderMessage message) {
        logger.info("aliOns-AutoCancelOrderListener:message{}", message);
        String pay_order_id = message.getPay_order_id();
        if (orderService.xCancelOrderWithoutPay(pay_order_id, "支付超时，系统自动取消！")) {
            orderLogService.xInsert("system", 0l, pay_order_id, "【未支付取消】支付超时，系统自动取消！");
            logger.info("aliOns-AutoCancelOrderListener: status{SUCCESS}");
            //清除服务人员时间调度副本
            Set<String> set = redisService.sMembers(pay_order_id);
            if (set!=null){
                redisService.delete(pay_order_id);
                redisService.delete(set);
            }
            //顾客下单自动取消,发送消息
            Order order = orderService.selectByPrimaryKey(pay_order_id);
            Customer customer = customerService.selectByPrimaryKey(order.getUid());
            ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
            serviceUnitExample.or().andPay_order_idEqualTo(pay_order_id).andPidEqualTo(0l);
            ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
            com.aobei.train.model.Message msg = new com.aobei.train.model.Message();
            msg.setId(IdGenerator.generateId());
			msg.setType(1);
			msg.setBis_type(4);
			msg.setUser_id(customer.getUser_id());
			msg.setUid(order.getUid());
			msg.setMsg_title("订单取消通知");
			//因您在{时长}分钟内未支付，您预约的{服务名称}，{预约时间}去{服务地址}的订单已自动取消
			MessageContent.ContentMsg content = new MessageContent.ContentMsg();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String beginString = format.format(serviceUnit.getC_begin_datetime());
            content.setContent("因您在30分钟内未支付,您预约的"+order.getName()+"+,"+beginString+"去"+order.getCus_address()+"的订单已经自动取消");
			content.setMsgtype("native");
			content.setV("1");
			content.setTypes(1);
			content.setTitle("订单取消通知");
			if (content.getContent() != null && !ParamsCheck.checkStrAndLength(content.getContent(),500)){
                Errors._41040.throwError("消息长度过长");
                return false;
            }
			String json = null;
			try {
				json = JacksonUtil.object_to_json(content);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			msg.setMsg_content(json);
			msg.setCreate_datetime(new Date());
			msg.setNotify_datetime(new Date());
			msg.setGroup_id(order.getPay_order_id());
			msg.setSend_type(1);
			msg.setApp_type(4);
			msg.setApp_platform(0);
			messageService.insertSelective(msg);
        }
        return true;
    }
}
