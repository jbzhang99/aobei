package com.aobei.trainconsole.qimo;

import com.alibaba.fastjson.JSONObject;
import com.aobei.trainconsole.util.QimoServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by mr_bl on 2018/7/4.
 */
@RestController
@RequestMapping(value = {"/qimo"})
public class QimoWorkOrderController {

    private static final Logger logger = LoggerFactory.getLogger(QimoWorkOrderController.class);

    @PostMapping("/receiveWorkOrder")
    public String receiveWorkOrder(@RequestHeader("Authorization") String authorization,
                                   InputStream inputStream,
                                   @RequestParam String sig) {
        String returnCode = "400";
        try {
            logger.info("authorization:{} sig:{}", authorization, sig);
            // 获取JSON 数据
            String workOrdersJson = StreamUtils.copyToString(inputStream, Charset.forName("utf-8"));
            logger.info("Qimo receiveWorkOrder DATA:{}", workOrdersJson);
            // 请求签名校验
            if (QimoServer.sigValidate(authorization, sig)) {
                JSONObject jsonObject = JSONObject.parseObject(workOrdersJson);

                returnCode = "200";
            }
        } catch (Exception e) {
            logger.error("Qimo receiveWorkOrder ERROR", e);
        }
        return returnCode;
    }


}
