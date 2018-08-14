package com.aobei.trainconsole.qimo;

import com.alibaba.fastjson.JSONObject;
import com.aobei.train.model.Business;
import com.aobei.train.model.BusinessExample;
import com.aobei.train.service.BusinessService;
import com.aobei.trainconsole.qimo.qimobean.QimoFiled;
import com.aobei.trainconsole.qimo.qimobean.QimoReceiveRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by mr_bl on 2018/7/4.
 */
@RestController
@RequestMapping(value = {"/qimo"})
public class QimoWorkOrderController {

    private static final Logger logger = LoggerFactory.getLogger(QimoWorkOrderController.class);

    @Autowired
    private BusinessService businessService;

    @RequestMapping("/receiveWorkOrder")
    public String receiveWorkOrder(InputStream inputStream) {
        String returnCode = "400";
        try {
            // 获取JSON 数据
            String workOrdersJson = StreamUtils.copyToString(inputStream, Charset.forName("utf-8"));
            logger.info("Qimo receiveWorkOrder DATA:{}", workOrdersJson);
            // 请求签名校验
            JSONObject jsonObject = JSONObject.parseObject(workOrdersJson);
            QimoReceiveRequestBody receivedWorkOrder = jsonObject.toJavaObject(QimoReceiveRequestBody.class);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //存储工单数据
            Business business = new Business();
            business.setBusiness_id(receivedWorkOrder.getBusinessNumber());
            business.setUser(receivedWorkOrder.getUser());
            business.setCreateTime(sdf.parse(receivedWorkOrder.getCreateTime()));
            business.setAction(receivedWorkOrder.getAction());
            business.setStepName(receivedWorkOrder.getStepName());
            business.setFlowInfo(receivedWorkOrder.getFlowInfo());
            List<QimoFiled> fields = receivedWorkOrder.getFields();
            fields.stream().forEach(n ->{
                if ("订单号".equals(n.getName())){
                    business.setPay_order_id(n.getValue());
                }
                if ("联系方式".equals(n.getName())){
                    business.setCus_phone(n.getValue());
                }
            });
            BusinessExample example = new BusinessExample();
            example.or()
                    .andBusiness_idEqualTo(business.getBusiness_id())
                    .andPay_order_idEqualTo(business.getPay_order_id());
            if(businessService.countByExample(example) < 1){
                businessService.insertSelective(business);
            }
            returnCode = "200";
        } catch (Exception e) {
            logger.error("Qimo receiveWorkOrder ERROR", e);
        }
        return returnCode;
    }


}
