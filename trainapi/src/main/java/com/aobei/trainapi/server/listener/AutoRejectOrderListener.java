package com.aobei.trainapi.server.listener;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aobei.common.boot.OnsMessageListener;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.model.ServiceUnitExample;
import com.aobei.train.service.OrderLogService;
import com.aobei.train.service.OrderService;
import com.aobei.train.service.ServiceUnitService;
import com.aobei.trainapi.configuration.CustomProperties;
import custom.bean.ons.RejectOrderMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoRejectOrderListener implements OnsMessageListener {
    Logger logger = LoggerFactory.getLogger(AutoRejectOrderListener.class);

    @Autowired
    ServiceUnitService serviceUnitService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    CustomProperties properties;
    @Autowired
    CacheReloadHandler cacheReloadHandler;
    @Override
    public String getTopic() {
        return properties.getAliyun().getOns().getTopic();
    }

    @Override
    public String getTag() {

        return "rejectOrder";
    }

    @Override
    public boolean isClustering() {
        return true;
    }

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        logger.info("aliOns-AutoRejectOrderListener message{}", message);
        try {
            String json = new String(message.getBody());
            RejectOrderMessage rejectOrderMessage = JSON.parseObject(json, RejectOrderMessage.class);
            rejectOrder(rejectOrderMessage);
        } catch (Exception e) {
            logger.error("aliOns-AutoRejectOrderListener message{},e{}", message, e.getMessage());
        }
        return Action.CommitMessage;

    }


    private boolean rejectOrder(RejectOrderMessage message) {
        logger.info("aliOns-AutoRejectOrderListener:message{}", message);
        String pay_order_id = message.getPay_order_id();
        Long partner_id = message.getPartner_id();
        if (partner_id == null || StringUtils.isEmpty(pay_order_id)) {
            return true;
        }
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or()
                .andPay_order_idEqualTo(pay_order_id)
                .andPartner_idEqualTo(partner_id)
                .andStatus_activeEqualTo(2)
                .andPidNotEqualTo(0l);
        if (serviceUnitService.countByExample(serviceUnitExample) > 0) {
            if (orderService.xRejectOrder(pay_order_id, partner_id, "超出接单时间限制[进行自动拒单]",0)) {
                //添加订单变更日志
                orderLogService.xInsert("system", 0l, pay_order_id, " 【系统自动】自动拒单，原因是:30 分钟没有接单[进行自动拒单]");
                logger.info("aliOns-AutoRejectOrderListener: status{SUCCESS}");
                cacheReloadHandler.partner_order_listReload(partner_id);
                cacheReloadHandler.partner_order_detailReload(pay_order_id);
            } else {
                logger.info("aliOns-AutoRejectOrderListener: status{FAIL}");
            }
            return true;
        }

        return true;
    }
}
