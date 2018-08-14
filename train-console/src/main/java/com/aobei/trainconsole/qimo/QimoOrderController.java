package com.aobei.trainconsole.qimo;

import com.alibaba.fastjson.JSON;
import com.aobei.train.Roles;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainconsole.qimo.qimobean.QimoFiled;
import com.aobei.trainconsole.qimo.qimobean.QimoGenerateRequestBody;
import com.aobei.trainconsole.util.QimoServer;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.OrderInfo;
import custom.bean.Status;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mr_bl on 2018/7/4.
 */
@Controller
@RequestMapping(value = {"/qimo"})
public class QimoOrderController {

    private static final Logger logger = LoggerFactory.getLogger(QimoOrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServiceunitPersonService serviceunitPersonService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ServiceUnitService serviceUnitService;

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private BusinessService businessService;

    /**
     *
     * @param originCallNo 主叫号码
     * @param originCalledNo 被叫号码
     * @param queue 来单进入技能组编号
     * @param offeringTime 来电进入系统的时间
     * @param callerProvince 主叫号码省份
     * @param callerCity 主叫号码城市
     * @param callSheetId 通话记录的ID
     * @param Agent 接听座席工号
     * @param RingTime 座席响铃时间,是时间戳格式
     * @param queueName 来电进入技能组名称
     * @param ivrkey 来电,在系统流转ivr时,按过的按键,每个按键以-分割
     * @param loginName 接听座席工号
     * @param name 客户名称
     * @param title 职位
     * @param phone 电话(如果配置多个电话会以","分割)
     * @param email 邮箱(如果配置多个邮箱会以","分割)
     * @param weixin 微信(如果配置多个微信会以","分割)
     * @param province 省
     * @param city 市
     * @param note 备注
     * @param web 公司网址
     * @param displayName 处理坐席姓名
     * @param exten 处理坐席工号
     * @param tabType tab页对接模块类型：normal(来电弹屏)、call（通话记录）、webchat（在线客服）、customer（客户模块）、business（工单）
     * @return
     */
    @GetMapping("/getOrders")
    public String receiveQimoRequest(Model model,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(defaultValue = "1") Integer p,
                                     @RequestParam(defaultValue = "10") Integer ps,
                                     @RequestParam(required = false) String originCallNo,
                                     @RequestParam(required = false) String originCalledNo,
                                     @RequestParam(required = false) String queue,
                                     @RequestParam(required = false) String offeringTime,
                                     @RequestParam(required = false) String callerProvince,
                                     @RequestParam(required = false) String callerCity,
                                     @RequestParam(required = false) String callSheetId,
                                     @RequestParam(required = false) String Agent,
                                     @RequestParam(required = false) String RingTime,
                                     @RequestParam(required = false) String queueName,
                                     @RequestParam(required = false) String ivrkey,
                                     @RequestParam(required = false) String loginName,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String title,
                                     @RequestParam(required = false) String phone,
                                     @RequestParam(required = false) String email,
                                     @RequestParam(required = false) String weixin,
                                     @RequestParam(required = false) String province,
                                     @RequestParam(required = false) String city,
                                     @RequestParam(required = false) String note,
                                     @RequestParam(required = false) String web,
                                     @RequestParam(required = false) String displayName,
                                     @RequestParam(required = false) String exten,
                                     @RequestParam(required = false) String tabType,
                                     @RequestParam(required = false) String uname,
                                     @RequestParam(required = false) String pay_order_id,
                                     @RequestParam(required = false) String c_begin_datetime,
                                     @RequestParam(required = false) String c_end_datetime,
                                     @RequestParam(required = false) String cuname,
                                     @RequestParam(required = false) String student_name,
                                     @RequestParam(required = false) Integer statu,
                                     @RequestParam(required = false) String qs_create_time,
                                     @RequestParam(required = false) String qe_create_time,
                                     @RequestParam(required = false) String qs_pay_time,
                                     @RequestParam(required = false) String qe_pay_time
                                     ){
        logger.info("qimo tab query:{}", request.getQueryString());
        logger.info("[receiveQimoRequest getOrders] Agent is {}",Agent);
        if ("".equals(originCallNo) || originCallNo == null){
            originCallNo = "";
        }
        SimpleDateFormat sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //订单条件
        OrderExample orderExample = new OrderExample();
        orderExample.setOrderByClause(OrderExample.C.create_datetime + " desc");
        OrderExample.Criteria orderOr = orderExample.or();
        OrderExample.Criteria orderOrAndCustomer = orderExample.or();
        //服务单条件
        ServiceUnitExample serviceUnitExample = new ServiceUnitExample();
        ServiceUnitExample.Criteria unitOr = serviceUnitExample.or();
        //顾客条件
        CustomerExample customerExample = new CustomerExample();
        CustomerExample.Criteria customerOr = customerExample.or();
        //服务人员单的条件
        ServiceunitPersonExample serviceunitPersonExample = new ServiceunitPersonExample();
        ServiceunitPersonExample.Criteria unitPersonOr = serviceunitPersonExample.or();

        if (!("".equals(cuname)) && cuname != null) {
            cuname = cuname.trim();
            orderOr.andCus_usernameLike("%" + cuname + "%");
            orderOrAndCustomer.andCus_usernameLike("%" + cuname + "%");
            customerOr.andNameLike("%" + cuname + "%");
            model.addAttribute("cuname", cuname);
        }

        if (!("".equals(student_name)) && student_name != null) {
            student_name = student_name.trim();
            unitPersonOr.andStudent_nameLike("%" + student_name + "%");
            List<Long> unit_ids = serviceunitPersonService
                    .selectByExample(serviceunitPersonExample)
                    .stream()
                    .map(n -> n.getServiceunit_id())
                    .collect(Collectors.toList());
            if (unit_ids.size() > 0 ){
                unitOr.andServiceunit_idIn(unit_ids);
            }else{
                unitOr.andServiceunit_idEqualTo(0l);
            }
            model.addAttribute("student_name", student_name);
        }

        if (!("".equals(originCallNo)) && originCallNo != null) {
            originCallNo = originCallNo.trim();
            customerOr.andPhoneLike("%" + originCallNo + "%");
            orderOr.andCus_phoneLike("%" + originCallNo + "%");
            orderOrAndCustomer.andCus_phoneLike("%" + originCallNo + "%");
            model.addAttribute("originCallNo", originCallNo);
        }
        List<Long> customer_ids = customerService
                .selectByExample(customerExample)
                .stream()
                .map(n -> n.getCustomer_id())
                .collect(Collectors.toList());

        if (!("".equals(c_begin_datetime)) && c_begin_datetime != null) {
            try {
                unitOr.andC_begin_datetimeGreaterThanOrEqualTo(sdfhms.parse(c_begin_datetime + " 00:00:00"));
                model.addAttribute("c_begin_datetime", c_begin_datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!("".equals(c_end_datetime)) && c_end_datetime != null) {
            try {
                unitOr.andC_end_datetimeLessThanOrEqualTo(sdfhms.parse(c_end_datetime + " 23:59:59"));
                model.addAttribute("c_end_datetime", c_end_datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (statu != null) {
            if (statu < 10) {
                if(statu.equals(Status.OrderStatus.wait_confirm.value)) {
                    unitOr.andStatus_activeNotEqualTo(Status.ServiceStatus.reject.value);
                }
                if(statu.equals(Status.OrderStatus.cancel.value)) {
                    orderOr.andR_statusIn(Arrays.asList(new Integer[]{-1,0,1,3}));
                    orderOrAndCustomer.andR_statusIn(Arrays.asList(new Integer[]{-1,0,1,3}));
                }
                orderOr.andStatus_activeEqualTo(statu);
                orderOrAndCustomer.andStatus_activeEqualTo(statu);
            } else {
                Integer o_statu = statu / 10;
                if (statu < 30) {
                    Integer s_statu = statu % 10;
                    unitOr.andStatus_activeEqualTo(s_statu);
                    orderOr.andStatus_activeEqualTo(o_statu);
                    orderOrAndCustomer.andStatus_activeEqualTo(o_statu);
                } else {
                    Integer r_statu = statu % 10;
                    orderOr.andStatus_activeEqualTo(o_statu);
                    orderOr.andR_statusEqualTo(r_statu);
                    orderOrAndCustomer.andStatus_activeEqualTo(o_statu);
                    orderOrAndCustomer.andR_statusEqualTo(r_statu);
                }
            }
            model.addAttribute("statu", statu);
        }
        if (!("".equals(uname)) && uname != null) {
            uname = uname.trim();
            orderOr.andNameLike("%" + uname + "%");
            orderOrAndCustomer.andNameLike("%" + uname + "%");
            model.addAttribute("uname", uname);
        }
        if (!("".equals(pay_order_id)) && pay_order_id != null) {
            pay_order_id = pay_order_id.trim();
            orderOr.andPay_order_idEqualTo(pay_order_id);
            orderOrAndCustomer.andPay_order_idEqualTo(pay_order_id);
            model.addAttribute("pay_order_id", pay_order_id);
        }
        if (!("".equals(qe_create_time)) && qe_create_time != null) {
            try {
                orderOr.andCreate_datetimeBetween(sdfhms.parse(qs_create_time + " 00:00:00"),
                        sdfhms.parse(qe_create_time + " 23:59:59"));
                orderOrAndCustomer.andCreate_datetimeBetween(sdfhms.parse(qs_create_time + " 00:00:00"),
                        sdfhms.parse(qe_create_time + " 23:59:59"));
                model.addAttribute("qs_create_time",qs_create_time);
                model.addAttribute("qe_create_time",qe_create_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!("".equals(qe_pay_time)) && qe_pay_time != null) {
            try {
                orderOr.andPay_datetimeBetween(sdfhms.parse(qs_pay_time + " 00:00:00"),
                        sdfhms.parse(qe_pay_time + " 23:59:59"));
                orderOrAndCustomer.andPay_datetimeBetween(sdfhms.parse(qs_pay_time + " 00:00:00"),
                        sdfhms.parse(qe_pay_time + " 23:59:59"));
                model.addAttribute("qs_pay_time",qs_pay_time);
                model.addAttribute("qe_pay_time",qe_pay_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (customer_ids.size() > 0){
            orderOrAndCustomer.andUidIn(customer_ids);
        }else{
            orderOrAndCustomer.andUidEqualTo(0l);
        }

        if (unitOr.getCriteria().size() > 0){
            unitOr.andPidEqualTo(0l);
            if (customer_ids.size() > 0){
                unitOr.andCustomer_idIn(customer_ids);
            }else{
                unitOr.andCustomer_idEqualTo(0l);
            }
            List<String> payOrderIds = serviceUnitService
                    .selectByExample(serviceUnitExample)
                    .stream()
                    .map(n -> n.getPay_order_id())
                    .collect(Collectors.toList());
            orderOr.andPay_order_idIn(payOrderIds);
            orderOrAndCustomer.andPay_order_idIn(payOrderIds);
        }
        response.setHeader("X-Frame-Options","ALLOW-FROM http://kf7.7moor.com");
        Page<OrderInfo> page = orderService.orderInfoList(Roles.TMANAGER, orderExample, p, ps);
        List<OrderInfo> orderInfos = page.getList();
        model.addAttribute("page",page);
        model.addAttribute("orderInfos",orderInfos);
        model.addAttribute("agent",Agent);
        return "qimo/qimo_orders";
    }

    @ResponseBody
    @RequestMapping(value = {"/getOrderDetail"}, method = RequestMethod.POST)
    public Object getOrderDetail(String pay_order_id,HttpServletResponse response){
        if ("".equals(pay_order_id) || pay_order_id == null)
            return null;
        // 查询日志
        OrderLogExample orderLogExample = new OrderLogExample();
        orderLogExample.or().andPay_order_idEqualTo(pay_order_id);
        List<OrderLog> logs = orderLogService.selectByExample(orderLogExample);
        Order order = orderService.selectByPrimaryKey(pay_order_id);
        response.setHeader("X-Frame-Options","ALLOW-FROM http://kf7.7moor.com");
        Map<String,Object> map = new HashMap<>();
        map.put("data",orderService.orderInfoDetail(Roles.TMANAGER, order));
        map.put("logs",logs);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = {"/queryBusiness"}, method = RequestMethod.POST)
    public Object queryBusiness(String pay_order_id,HttpServletResponse response){
        if ("".equals(pay_order_id) || pay_order_id == null)
            return null;
        BusinessExample example = new BusinessExample();
        example.or().andPay_order_idEqualTo(pay_order_id);
        List<Business> businesses = businessService.selectByExample(example);
        return businesses;
    }

    @ResponseBody
    @RequestMapping(value = {"/generateWorkOrder"} , method = RequestMethod.POST)
    public Object generateWorkOrder(String pay_order_id,String customer_phone,String agent,HttpServletResponse servletResponse){
        logger.info("[generateWorkOrder generateWorkOrder] Agent is {}",agent);
        QimoGenerateRequestBody requestBody = new QimoGenerateRequestBody();
        requestBody.set_id("");
        requestBody.setCustomerId("");
        requestBody.setTargetUser(agent);
        requestBody.setCreateUser(agent);
        requestBody.setFlowName("工单记录");
        requestBody.setComment("");
        requestBody.setStepName("业务编辑");
        requestBody.setAction("");
        requestBody.setPriority("1");
            List<QimoFiled> filds = new ArrayList<>();
            QimoFiled qimoFiledRadio = new QimoFiled();
            qimoFiledRadio.setName("性别");
            qimoFiledRadio.setType("radio");
            qimoFiledRadio.setValue("男，女");
            filds.add(qimoFiledRadio);

            QimoFiled qimoFiledLink = new QimoFiled();
            qimoFiledLink.setName("联系方式");
            qimoFiledLink.setType("single");
            qimoFiledLink.setValue(customer_phone);
            filds.add(qimoFiledLink);

            QimoFiled qimoFiledOrderNo = new QimoFiled();
            qimoFiledOrderNo.setName("订单号");
            qimoFiledOrderNo.setType("single");
            qimoFiledOrderNo.setValue(pay_order_id);
            filds.add(qimoFiledOrderNo);

            QimoFiled qimoFiledWorkOrderType = new QimoFiled();
            qimoFiledWorkOrderType.setName("工单类型");
            qimoFiledWorkOrderType.setType("dropdown");
            qimoFiledWorkOrderType.setValue("");
            filds.add(qimoFiledWorkOrderType);

            QimoFiled qimoFiledAskOne = new QimoFiled();
            qimoFiledAskOne.setName("咨询事项");
            qimoFiledAskOne.setType("dropdown");
            qimoFiledAskOne.setValue("");
            filds.add(qimoFiledAskOne);

            QimoFiled qimoFiledAskTwo = new QimoFiled();
            qimoFiledAskTwo.setName("咨询事项二级");
            qimoFiledAskTwo.setType("dropdown");
            qimoFiledAskTwo.setValue("");
            filds.add(qimoFiledAskTwo);

            QimoFiled qimoFiledAskThree = new QimoFiled();
            qimoFiledAskThree.setName("咨询事项三级");
            qimoFiledAskThree.setType("dropdown");
            qimoFiledAskThree.setValue("");
            filds.add(qimoFiledAskThree);

            QimoFiled qimoFiledServerDetail = new QimoFiled();
            qimoFiledServerDetail.setName("服务详情");
            qimoFiledServerDetail.setType("multi");
            qimoFiledServerDetail.setValue("");
            filds.add(qimoFiledServerDetail);

            QimoFiled qimoFiledRemark = new QimoFiled();
            qimoFiledRemark.setName("备注");
            qimoFiledRemark.setType("multi");
            qimoFiledRemark.setValue("");
            filds.add(qimoFiledRemark);
        requestBody.setFields(filds);
        servletResponse.setHeader("X-Frame-Options","ALLOW-FROM http://kf7.7moor.com");

        String requestBodyJson = JSON.toJSONString(requestBody, true);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            response = QimoServer.handleBusiness(requestBodyJson);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf8");
            logger.info("the response is : " + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Map<String,String> map = new HashMap<>();
        if (result!= null && result.contains("200")){
            map.put("msg","生成工单成功！");
        }else{
            map.put("msg","生成工单失败！");
        }
        return map;
    }
}
