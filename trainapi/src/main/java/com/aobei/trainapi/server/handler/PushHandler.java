package com.aobei.trainapi.server.handler;

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
     * 发起抢单是发送给可以抢单的合伙人
     * @param alia
     */
    public void pushRobbingMessageToPartner(String... alia){
        //您有一个新订单，请及时抢单
        IGtPushData data = new IGtPushData();
        List<String> alias = Arrays.asList(alia);
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("抢单通知");
        data.setText("您有一张新订单，请及时抢单");
        data.setSound(IGtPushData.SOUND_NEW_ORDER);
        data.setClient(IGtPushData.Client.partner);
        data.setType(IGtPushData.GROUP);
        Map<String, String> params = new HashMap<>();
        params.put("position", "2");
        TransmissionContent content = new TransmissionContent(TransmissionContent.PARTNER, TransmissionContent.HOME_PAGE, params);
        content.setSound(IGtPushData.SOUND_NEW_ORDER);
        content.setTitle("抢单通知");
        content.setText("您有一张新订单，请及时抢单");
        data.setTransmissionContent(JSON.toJSONString(content));
        data.setSetTransmissionType(2);
        IGtPushEvent event = new IGtPushEvent(this, data);
        publisher.publish(event);
    }
    /**
     * 合伙人派单后。向指定的服务人员发送
     *
     * @param orderInfo
     * @param alia
     */
    public void pushOrderMessageToStudent(OrderInfo orderInfo, String... alia) {
        //{预约时间}新服务订单，请及时查看
        IGtPushData data = new IGtPushData();
        List<String> alias = new ArrayList<>();
        Arrays.asList(alia);
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("新订单通知");
        String text = String.format("%s新服务订单，请及时查看"
                , DateUtil.dateToLocalDateTime(orderInfo.getC_begin_datetime()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        data.setText(text);
        data.setClient(IGtPushData.Client.student);
        data.setType(IGtPushData.GROUP);
        Map<String, String> params = new HashMap<>();
        params.put("pay_order_id", orderInfo.getPay_order_id());
        params.put("orderStatus",OrderInfo.OrderStatus.WAIT_SERVICE.descr);
        TransmissionContent content = new TransmissionContent(TransmissionContent.STUDENT, TransmissionContent.ORDER_DETAIL, params);
        content.setTitle("新订单通知");
        content.setText(text);
        data.setTransmissionContent(JSON.toJSONString(content));
        data.setSetTransmissionType(2);
        IGtPushEvent event = new IGtPushEvent(this, data);
        publisher.publish(event);

    }

    /**
     * 合伙人派单后。告知顾客服务即将开始
     */
    public void pushOrderMessageToCustomer(Order order, String alia) {
        //您的{服务名称}订单已确认
        IGtPushData data = new IGtPushData();
        List<String> alias = new ArrayList<>();
        alias.add(alia);
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("订单确认通知");
        String text = String.format("您的%s订单已确认", order.getName());
        data.setText(text);
        data.setClient(IGtPushData.Client.customer);
        data.setType(IGtPushData.SINGLE);
        Map<String, String> params = new HashMap<>();
        params.put("pay_order_id", order.getPay_order_id());
        params.put("orderStatus",OrderInfo.OrderStatus.WAIT_SERVICE.descr);
        TransmissionContent content = new TransmissionContent(TransmissionContent.CUSTOM, TransmissionContent.ORDER_DETAIL, params);
        content.setTitle("订单确认通知");
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
        String text = String.format("您的服务订单%s已取消",order.getPay_order_id());
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

    /**
     * 服务开始时间前一天，晚8：00发送给服务人员
     */
    public void pushOrderMessageBeforServiceToStudent(OrderInfo orderInfo, String... alia) {
        //【请您于{预约时间}进行{服务名称}服务
        IGtPushData data = new IGtPushData();
        List<String> alias = Arrays.asList(alia);
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("服务时间提醒");
        String text = String.format("请您于%s进行%s服务"
                , DateUtil.dateToLocalDateTime(orderInfo.getC_begin_datetime()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                , orderInfo.getName()
        );
        data.setText(text);
        data.setClient(IGtPushData.Client.student);
        data.setType(IGtPushData.GROUP);
        Map<String, String> params = new HashMap<>();
        params.put("pay_order_id", orderInfo.getPay_order_id());
        params.put("orderStatus",OrderInfo.OrderStatus.WAIT_SERVICE.descr);
        TransmissionContent content = new TransmissionContent(TransmissionContent.STUDENT, TransmissionContent.ORDER_DETAIL, params);
        content.setTitle("服务时间提醒");
        content.setText(text);
        data.setTransmissionContent(JSON.toJSONString(content));
        data.setSetTransmissionType(2);
        IGtPushEvent event = new IGtPushEvent(this, data);
        publisher.publish(event);
    }

    /**
     * 服务开始时间前一天，早9：00 发送个顾客
     */
    public void pushOrderMessageBeforServiceToCustomer(OrderInfo orderInfo, String alia) {
        //{预约时间}将为您进行服务，请安排好您的时间等待服务人员上门为您服务
        IGtPushData data = new IGtPushData();
        List<String> alias = Arrays.asList(alia);
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("服务时间提醒");
        String text = String.format("%s将为您进行服务，请安排好您的时间等待服务人员上门为您服务"
                , DateUtil.dateToLocalDateTime(orderInfo.getC_begin_datetime()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        data.setText(text);
        data.setClient(IGtPushData.Client.customer);
        data.setType(IGtPushData.SINGLE);
        Map<String, String> params = new HashMap<>();
        params.put("pay_order_id", orderInfo.getPay_order_id());
        params.put("orderStatus",OrderInfo.OrderStatus.WAIT_SERVICE.descr);
        TransmissionContent content = new TransmissionContent(TransmissionContent.CUSTOM, TransmissionContent.ORDER_DETAIL, params);
        content.setTitle("服务时间提醒");
        content.setText(text);
        data.setTransmissionContent(JSON.toJSONString(content));
        data.setSetTransmissionType(2);
        IGtPushEvent event = new IGtPushEvent(this, data);
        publisher.publish(event);
    }

    /**
     * 服务人员进行变更，发送给顾客
     */
    public void pushOrderMessageWhenStudentChangeToCustomer(OrderInfo orderInfo, String alia) {
        //您{服务名称}的订单由服务人员{服务人员名}为您进行服务
        IGtPushData data = new IGtPushData();
        List<String> alias = Arrays.asList(alia);
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("服务变更通知");
        String text = String.format("您%s的订单由服务人员%s为您进行服务"
                , orderInfo.getName()
                , orderInfo.getStudent_name()
        );
        data.setText(text);
        data.setClient(IGtPushData.Client.customer);
        data.setType(IGtPushData.SINGLE);
        Map<String, String> params = new HashMap<>();
        params.put("pay_order_id", orderInfo.getPay_order_id());
        params.put("orderStatus",OrderInfo.OrderStatus.WAIT_SERVICE.descr);
        TransmissionContent content = new TransmissionContent(TransmissionContent.CUSTOM, TransmissionContent.ORDER_DETAIL, params);
        content.setTitle("服务变更通知");
        content.setText(text);
        data.setTransmissionContent(JSON.toJSONString(content));
        data.setSetTransmissionType(2);
        IGtPushEvent event = new IGtPushEvent(this, data);
        publisher.publish(event);
    }


    /**
     * 获得一张优惠券后，发送给顾客
     */
    public void pushOrderMessageWhenGetAcoupon(String couponName,String alia) {
        //【浦尔家】您的专属优惠{优惠券名称}来了，快去看看吧
        IGtPushData data = new IGtPushData();
        List<String> alias = Arrays.asList(alia);
        data.setAlias(alias);
        data.setStyle(IGtPushData.Style0);
        data.setTitle("优惠活动");
        String text = String.format("您的专属优惠%s来了，快去看看吧",couponName);
        data.setText(text);
        data.setClient(IGtPushData.Client.customer);
        data.setType(IGtPushData.SINGLE);
        IGtPushEvent event = new IGtPushEvent(this, data);
        /**
        Map<String, String> params = new HashMap<>();
        params.put("product_id","");
        TransmissionContent content = new TransmissionContent(TransmissionContent.CUSTOM, TransmissionContent.PRODUCT_DETAIL, params);
         content.setTitle("优惠活动");
         content.setText(text);
         data.setTransmissionContent(JSON.toJSONString(content));
        data.setSetTransmissionType(2);
         **/
        publisher.publish(event);
    }
}
