package com.aobei.trainconsole.util;

import com.aobei.common.bean.IGtPushData;
import com.aobei.common.boot.EventPublisher;
import com.aobei.common.boot.event.IGtPushEvent;
import com.aobei.train.model.Order;
import com.gexin.fastjson.JSON;
import custom.bean.OrderInfo;
import custom.bean.TransmissionContent;
import custom.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class PushHandler {

    @Autowired
    EventPublisher publisher;

    /**
     * 用户下单成功后向合伙人发送推送。请合伙人及时确认订单
     *
     * @param order
     * @param alia
     */
    public void pushOrderMessageToPartner(Order order, String alia) {
        //您有一个新订单，请及时确认
        IGtPushData data = new IGtPushData();
        List<String> alias = new ArrayList<>();
        alias.add(alia);
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("新订单通知");
        String text = String.format("您有一张新订单，请及时确认");
        data.setText(text);
        data.setClient(IGtPushData.Client.partner);
        data.setType(IGtPushData.SINGLE);
        data.setSound(IGtPushData.SOUND_NEW_ORDER);
        Map<String, String> params = new HashMap<>();
        params.put("pay_order_id", order.getPay_order_id());
        params.put("orderStatus",OrderInfo.OrderStatus.PAYED.descr);
        TransmissionContent content = new TransmissionContent(TransmissionContent.PARTNER, TransmissionContent.ORDER_DETAIL, params);
        content.setSound(IGtPushData.SOUND_NEW_ORDER);
        content.setTitle("新订单通知");
        content.setText(text);
        data.setTransmissionContent(JSON.toJSONString(content));
        data.setSetTransmissionType(2);
        IGtPushEvent event = new IGtPushEvent(this, data);
        publisher.publish(event);
    }

    /**
     * 订单取消。发送给合伙人。
     */
    public void pushCancelOrderMessageToPartner(Order order, String alia) {
        //您{订单号}的服务订单已取消
        IGtPushData data = new IGtPushData();
        List<String> alias = new ArrayList<>();
        alias.add(alia);
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("订单取消通知");
        String text = String.format("您%s的服务订单已取消",order.getPay_order_id());
        data.setText(text);
        data.setClient(IGtPushData.Client.partner);
        data.setType(IGtPushData.SINGLE);
        Map<String, String> params = new HashMap<>();
        params.put("pay_order_id", order.getPay_order_id());
        params.put("orderStatus",OrderInfo.OrderStatus.CANCEL.descr);
        TransmissionContent content = new TransmissionContent(TransmissionContent.PARTNER, TransmissionContent.ORDER_DETAIL, params);
        content.setTitle("订单取消通知");
        content.setText(text);
        data.setTransmissionContent(JSON.toJSONString(content));
        data.setSetTransmissionType(2);
        IGtPushEvent event = new IGtPushEvent(this, data);
        publisher.publish(event);
    }

    /**
     * 订单取消。发送给服务人员。
     */
    public void pushCancelOrderMessageToStudent(OrderInfo orderInfo, String... alia) {
        //{预约时间}的服务订单已取消
        IGtPushData data = new IGtPushData();
        List<String> alias = Arrays.asList(alia);
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("订单取消通知");
        String text = String.format("%s的服务订单已取消"
                , DateUtil.dateToLocalDateTime(orderInfo.getC_begin_datetime()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        data.setText(text);
        data.setClient(IGtPushData.Client.student);
        data.setType(IGtPushData.GROUP);
        Map<String, String> params = new HashMap<>();
        params.put("pay_order_id", orderInfo.getPay_order_id());
        params.put("orderStatus",OrderInfo.OrderStatus.CANCEL.descr);
        TransmissionContent content = new TransmissionContent(TransmissionContent.STUDENT, TransmissionContent.ORDER_DETAIL, params);
        content.setTitle("订单取消通知");
        content.setText(text);
        data.setTransmissionContent(JSON.toJSONString(content));
        data.setSetTransmissionType(2);
        IGtPushEvent event = new IGtPushEvent(this, data);
        publisher.publish(event);
    }

    /**
     * 订单取消。发送给顾客
     */
    public void pushCancelOrderMessageToCustomer(Order order, String alia) {
        //您{订单号}的订单已取消
        IGtPushData data = new IGtPushData();
        List<String> alias = new ArrayList<>();
        alias.add(alia);
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("订单取消通知");
        String text = String.format("您%s的订单已取消", order.getPay_order_id());
        data.setText(text);
        data.setClient(IGtPushData.Client.customer);
        data.setType(IGtPushData.SINGLE);
        Map<String, String> params = new HashMap<>();
        params.put("pay_order_id", order.getPay_order_id());
        params.put("orderStatus",OrderInfo.OrderStatus.CANCEL.descr);
        TransmissionContent content = new TransmissionContent(TransmissionContent.CUSTOM, TransmissionContent.ORDER_DETAIL, params);
        content.setTitle("订单取消通知");
        content.setText(text);
        data.setTransmissionContent(JSON.toJSONString(content));
        data.setSetTransmissionType(2);
        IGtPushEvent event = new IGtPushEvent(this, data);
        publisher.publish(event);
    }

}
