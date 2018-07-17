package com.aobei.trainconsole.qimo;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mr_bl on 2018/7/4.
 */
@RestController
@RequestMapping(value = {"/qimo"})
public class QimoWorkOrderController {

    private static final Logger logger = LoggerFactory.getLogger(QimoWorkOrderController.class);

    @ResponseBody
    @RequestMapping(value = {"/receiveWorkOrder"} , method = RequestMethod.GET ,
                    consumes = "application/json")
    public Object receiveWorkOrder(String workOrdersJson){
        try {
            JSONObject jsonObject = JSONObject.parseObject(workOrdersJson);
        } catch (Exception e) {
            return "";
        }
        return "xxxxx";
    }

}
