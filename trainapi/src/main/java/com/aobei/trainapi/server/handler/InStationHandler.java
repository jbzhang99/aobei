package com.aobei.trainapi.server.handler;

import com.alibaba.fastjson.JSON;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainapi.schema.Errors;
import com.aobei.trainapi.server.bean.MessageContent;
import custom.bean.OrderInfo;
import custom.bean.TransmissionContent;
import custom.util.ParamsCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adminL on 2018/7/20.
 */
@Component
public class InStationHandler {

    @Autowired
    private MessageService messageService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServiceUnitService serviceUnitService;

    @Autowired
    private CustomerService customerService;

    /**
     * 待确定变待服务站内消息
     * @param student_id
     * @param pay_order_id
     */
    public void sentToStudentOrder(Long student_id,String pay_order_id){
        Student student = studentService.selectByPrimaryKey(student_id);
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or()
                .andPay_order_idEqualTo(pay_order_id)
                .andPidEqualTo(0l);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        if(serviceUnit==null){
            return;
        }
        if(order==null){
            return;
        }
        if(student==null){
            return;
        }
        Message mes = new Message();
        mes.setId(IdGenerator.generateId());
        mes.setType(2);
        mes.setBis_type(1);
        mes.setUser_id(student.getUser_id()==null ? 0l : student.getUser_id());
        mes.setUid(student.getStudent_id());
        mes.setMsg_title("新订单通知");
        MessageContent.ContentMsg contentMsg = new MessageContent.ContentMsg();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTimeStr = sd.format(serviceUnit.getC_begin_datetime());
        contentMsg.setMsgtype("native");
        contentMsg.setContent("您有一个新的"+order.getName()+"任务，于"+beginTimeStr+"去"+order.getCus_address()+"进行服务");

        Map<String,String> param = new HashMap<>();
        param.put("pay_order_id",order.getPay_order_id());
        param.put("orderStatus","waitService");
        TransmissionContent tContent = new TransmissionContent(TransmissionContent.STUDENT,TransmissionContent.ORDER_DETAIL,param);
        contentMsg.setHref(tContent.getHrefNotEncode());
        contentMsg.setTypes(1);//纯文本
        contentMsg.setNoticeTypes(1);//新订单类型
        contentMsg.setTitle("新订单通知");
        if (contentMsg.getContent() != null && !ParamsCheck.checkStrAndLength(contentMsg.getContent(),500)){
            Errors._41040.throwError("消息长度过长");
        }
        String json = JSON.toJSONString(contentMsg);
        mes.setMsg_content(json);
        mes.setCreate_datetime(new Date());
        mes.setNotify_datetime(new Date());
        mes.setGroup_id(order.getPay_order_id());
        mes.setSend_type(1);//站内类型
        mes.setApp_type(1);//服务人员端
        mes.setApp_platform(0);//所有平台
        messageService.insertSelective(mes);
    }

    /**
     * 取消订单（合伙人端）
     */
    public void sentToPartnerCancleOrder(String pay_order_id,Partner partner){
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or()
                .andPay_order_idEqualTo(pay_order_id)
                .andPidEqualTo(0l);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        if(serviceUnit==null){
            return;
        }
        if(order==null){
            return;
        }
        if(partner==null){
            return;
        }
        Message mes = new Message();
        mes.setId(IdGenerator.generateId());
        mes.setType(2);
        mes.setBis_type(3);//合伙人端
        mes.setUser_id(partner.getUser_id()==null ? 0l : partner.getUser_id());
        mes.setUid(partner.getPartner_id());
        mes.setMsg_title("订单取消通知");
        MessageContent.ContentMsg contentMsg = new MessageContent.ContentMsg();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTimeStr = sd.format(serviceUnit.getC_begin_datetime());
        contentMsg.setMsgtype("native");
        contentMsg.setContent("您于"+beginTimeStr+"进行"+order.getName()+"服务的订单已取消");
        Map<String,String> param = new HashMap<>();
        param.put("pay_order_id",order.getPay_order_id());
        param.put("orderStatus","cancel");
        TransmissionContent tContent = new TransmissionContent(TransmissionContent.PARTNER,TransmissionContent.ORDER_DETAIL,param);
        contentMsg.setHref(tContent.getHrefNotEncode());
        contentMsg.setTypes(1);//纯文本
        contentMsg.setNoticeTypes(3);//订单取消类型
        contentMsg.setTitle("订单取消通知");
        if (contentMsg.getContent() != null && !ParamsCheck.checkStrAndLength(contentMsg.getContent(),500)){
            Errors._41040.throwError("消息长度过长");
        }
        String json = JSON.toJSONString(contentMsg);
        mes.setMsg_content(json);
        mes.setCreate_datetime(new Date());
        mes.setNotify_datetime(new Date());
        mes.setGroup_id(order.getPay_order_id());
        mes.setSend_type(1);//站内类型
        mes.setApp_type(3);//合伙人员端
        mes.setApp_platform(0);//所有平台
        messageService.insertSelective(mes);
    }

    /**
     * 订单取消（学员）
     * @param pay_order_id
     * @param student
     */
    public void sentToStudentCancleOrder(String pay_order_id,Student student){
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or()
                .andPay_order_idEqualTo(pay_order_id)
                .andPidEqualTo(0l);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        if(serviceUnit==null){
            return;
        }
        if(order==null){
            return;
        }
        if(student==null){
            return;
        }
        Message mes = new Message();
        mes.setId(IdGenerator.generateId());
        mes.setType(2);
        mes.setBis_type(1);//学员端
        mes.setUser_id(student.getUser_id()==null ? 0l : student.getUser_id());
        mes.setUid(student.getStudent_id());
        mes.setMsg_title("订单取消通知");
        MessageContent.ContentMsg contentMsg = new MessageContent.ContentMsg();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTimeStr = sd.format(serviceUnit.getC_begin_datetime());
        contentMsg.setMsgtype("native");
        contentMsg.setContent("您于"+beginTimeStr+"去"+order.getCus_address()+"进行"+order.getName()+"服务的订单已取消");
        Map<String,String> param = new HashMap<>();
        param.put("pay_order_id",order.getPay_order_id());
        param.put("orderStatus","cancel");
        TransmissionContent tContent = new TransmissionContent(TransmissionContent.STUDENT,TransmissionContent.ORDER_DETAIL,param);
        contentMsg.setHref(tContent.getHrefNotEncode());
        contentMsg.setTypes(1);//纯文本
        contentMsg.setNoticeTypes(3);//订单取消类型
        contentMsg.setTitle("订单取消通知");
        if (contentMsg.getContent() != null && !ParamsCheck.checkStrAndLength(contentMsg.getContent(),500)){
            Errors._41040.throwError("消息长度过长");
        }
        String json = JSON.toJSONString(contentMsg);
        mes.setMsg_content(json);
        mes.setCreate_datetime(new Date());
        mes.setNotify_datetime(new Date());
        mes.setGroup_id(order.getPay_order_id());
        mes.setSend_type(1);//站内类型
        mes.setApp_type(1);//服务人员端
        mes.setApp_platform(0);//所有平台
        messageService.insertSelective(mes);
    }

    /**
     * 前一天八点 学员端展示
     * @param pay_order_id
     * @param student
     */
    public void sentToStudentRemindService(String pay_order_id,Student student){
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or()
                .andPay_order_idEqualTo(pay_order_id)
                .andPidEqualTo(0l);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        if(serviceUnit==null){
            return;
        }
        if(order==null){
            return;
        }
        if(student==null){
            return;
        }
        Message mes = new Message();
        mes.setId(IdGenerator.generateId());
        mes.setType(1);//系统消息
        mes.setBis_type(1);//学员端
        mes.setUser_id(student.getUser_id()==null ? 0l : student.getUser_id());
        mes.setUid(student.getStudent_id());
        mes.setMsg_title("服务时间提醒");
        MessageContent.ContentMsg contentMsg = new MessageContent.ContentMsg();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTimeStr = sd.format(serviceUnit.getC_begin_datetime());
        contentMsg.setMsgtype("native");
        contentMsg.setContent("您于"+beginTimeStr+"进行"+order.getName()+"服务");
        Map<String,String> param = new HashMap<>();
        param.put("pay_order_id",order.getPay_order_id());
        param.put("orderStatus","waitService");
        TransmissionContent tContent = new TransmissionContent(TransmissionContent.STUDENT,TransmissionContent.ORDER_DETAIL,param);
        contentMsg.setHref(tContent.getHrefNotEncode());
        contentMsg.setTypes(1);//纯文本
        //contentMsg.setNoticeTypes(3);//订单取消类型************************************
        contentMsg.setTitle("服务时间提醒");
        if (contentMsg.getContent() != null && !ParamsCheck.checkStrAndLength(contentMsg.getContent(),500)){
            Errors._41040.throwError("消息长度过长");
        }
        String json = JSON.toJSONString(contentMsg);
        mes.setMsg_content(json);
        mes.setCreate_datetime(new Date());
        mes.setNotify_datetime(getBefourHourDate(8,serviceUnit.getC_begin_datetime()));//服务开始前一天8：00
        mes.setGroup_id(order.getPay_order_id());
        mes.setSend_type(1);//站内类型
        mes.setApp_type(1);//服务人员端
        mes.setApp_platform(0);//所有平台
        messageService.insertSelective(mes);
    }

    /**
     * 前一天九点整  顾客端展示
     * @param orderInfo
     */
    public void sentToCustomerRemindService(OrderInfo orderInfo,String pay_order_id,Student student){
        Customer customer = orderInfo.getCustomer();
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or()
                .andPay_order_idEqualTo(pay_order_id)
                .andPidEqualTo(0l);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        if(serviceUnit==null){
            return;
        }
        if(order==null){
            return;
        }
        if(student==null){
            return;
        }
        if(customer==null){
            return;
        }
        Message mes = new Message();
        mes.setId(IdGenerator.generateId());
        mes.setType(1);//系统消息
        mes.setBis_type(4);//顾客端
        mes.setUser_id(customer.getUser_id());
        mes.setUid(customer.getCustomer_id());
        mes.setMsg_title("服务时间提醒");
        MessageContent.ContentMsg contentMsg = new MessageContent.ContentMsg();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTimeStr = sd.format(serviceUnit.getC_begin_datetime());
        contentMsg.setMsgtype("native");
        contentMsg.setContent("服务人员"+order.getName()+"电话"+student.getPhone()+"将于"+beginTimeStr+"为您进行服务，请安排好您的时间等待服务人员上门为您服务");
        Map<String,String> param = new HashMap<>();
        param.put("pay_order_id",order.getPay_order_id());
        param.put("orderStatus","waitService");
        TransmissionContent tContent = new TransmissionContent(TransmissionContent.CUSTOM,TransmissionContent.ORDER_DETAIL,param);
        contentMsg.setHref(tContent.getHrefNotEncode());
        contentMsg.setTypes(1);//纯文本
        //contentMsg.setNoticeTypes(3);//订单取消类型************************************
        contentMsg.setTitle("服务时间提醒");
        if (contentMsg.getContent() != null && !ParamsCheck.checkStrAndLength(contentMsg.getContent(),500)){
            Errors._41040.throwError("消息长度过长");
        }
        String json = JSON.toJSONString(contentMsg);
        mes.setMsg_content(json);
        mes.setCreate_datetime(new Date());
        mes.setNotify_datetime(getBefourHourDate(9,serviceUnit.getC_begin_datetime()));//服务开始前一天9：00
        mes.setGroup_id(order.getPay_order_id());
        mes.setSend_type(1);//站内类型
        mes.setApp_type(4);//顾客端
        mes.setApp_platform(0);//所有平台
        messageService.insertSelective(mes);
    }

    /**
     * 服务变更通知（人员，时间变更） 顾客
     * @param customer_id
     * @param student_ids    新服务人员id
     * @param pay_order_id   订单号
     */
    public void sentToCustomerChangeOrder(Long customer_id,List<Long> student_ids,String pay_order_id){
        Customer customer = customerService.selectByPrimaryKey(customer_id);
        Student student = studentService.selectByPrimaryKey(student_ids.get(0));
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        if(order==null){
            return;
        }
        if(student==null){
            return;
        }
        if(customer==null){
            return;
        }
        Message mes = new Message();
        mes.setId(IdGenerator.generateId());
        mes.setType(2);
        mes.setBis_type(4);//顾客端
        mes.setUser_id(customer.getUser_id());
        mes.setUid(customer.getCustomer_id());
        mes.setMsg_title("服务变更通知");
        MessageContent.ContentMsg contentMsg = new MessageContent.ContentMsg();
        /*SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTimeStr = sd.format(orderInfo.getC_begin_datetime());*/
        contentMsg.setMsgtype("native");
        contentMsg.setContent("您"+order.getName()+"的订单由服务人员"+student.getName()+"为您进行服务");
        Map<String,String> param = new HashMap<>();
        param.put("pay_order_id",order.getPay_order_id());
        param.put("orderStatus","waitService");
        TransmissionContent tContent = new TransmissionContent(TransmissionContent.CUSTOM,TransmissionContent.ORDER_DETAIL,param);
        contentMsg.setHref(tContent.getHrefNotEncode());
        contentMsg.setTypes(1);//纯文本
        contentMsg.setNoticeTypes(2);//订单变更通知
        contentMsg.setTitle("服务变更通知");
        if (contentMsg.getContent() != null && !ParamsCheck.checkStrAndLength(contentMsg.getContent(),500)){
            Errors._41040.throwError("消息长度过长");
        }
        String json = JSON.toJSONString(contentMsg);
        mes.setMsg_content(json);
        mes.setCreate_datetime(new Date());
        mes.setNotify_datetime(new Date());//预设通知时间
        mes.setGroup_id(order.getPay_order_id());
        mes.setSend_type(1);//站内类型
        mes.setApp_type(4);//顾客端
        mes.setApp_platform(0);//所有平台
        messageService.insertSelective(mes);
    }

    /**
     * 服务变更通知（时间变更） 学员
     * @param pay_order_id
     * @param student
     */
    public void sentToStuentChangeOrder(String pay_order_id,Student student){
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        serviceUnitExample.or()
                .andPay_order_idEqualTo(pay_order_id)
                .andPidEqualTo(0l);
        ServiceUnit serviceUnit = DataAccessUtils.singleResult(serviceUnitService.selectByExample(serviceUnitExample));
        if(serviceUnit==null){
            return;
        }
        if(order==null){
            return;
        }
        if(student==null){
            return;
        }
        Message mes = new Message();
        mes.setId(IdGenerator.generateId());
        mes.setType(2);
        mes.setBis_type(1);//学员端
        mes.setUser_id(student.getUser_id()==null ? 0l : student.getUser_id());
        mes.setUid(student.getStudent_id());
        mes.setMsg_title("服务变更通知");
        MessageContent.ContentMsg contentMsg = new MessageContent.ContentMsg();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTimeStr = sd.format(serviceUnit.getC_begin_datetime());
        contentMsg.setMsgtype("native");
        contentMsg.setContent("您的"+order.getName()+"订单服务时间变更，请于"+beginTimeStr+"，去"+order.getCus_address()+"为进行服务服务");
        Map<String,String> param = new HashMap<>();
        param.put("pay_order_id",order.getPay_order_id());
        param.put("orderStatus","waitService");
        TransmissionContent tContent = new TransmissionContent(TransmissionContent.CUSTOM,TransmissionContent.ORDER_DETAIL,param);
        contentMsg.setHref(tContent.getHrefNotEncode());
        contentMsg.setTypes(1);//纯文本
        contentMsg.setNoticeTypes(2);//订单取消类型
        contentMsg.setTitle("服务变更通知");
        if (contentMsg.getContent() != null && !ParamsCheck.checkStrAndLength(contentMsg.getContent(),500)){
            Errors._41040.throwError("消息长度过长");
        }
        String json = JSON.toJSONString(contentMsg);
        mes.setMsg_content(json);
        mes.setCreate_datetime(new Date());
        mes.setNotify_datetime(new Date());//预设通知时间
        mes.setGroup_id(order.getPay_order_id());
        mes.setSend_type(1);//站内类型
        mes.setApp_type(1);//学员端
        mes.setApp_platform(0);//所有平台
        messageService.insertSelective(mes);
    }



    //转化为入参时间的前一天hour点整
    private Date getBefourHourDate(int hour,Date date){
        Instant instant = new Date().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        LocalDateTime localDateTime1 = localDateTime.minusDays(1).withHour(hour).withMinute(0).withSecond(0);
        ZonedDateTime zonedDateTime = localDateTime1.atZone(zoneId);
        Date date_time = Date.from(zonedDateTime.toInstant());
        return date_time;
    }

}
