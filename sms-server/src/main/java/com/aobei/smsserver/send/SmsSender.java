package com.aobei.smsserver.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aobei.common.bean.SmsData;
import com.aobei.common.sms.SmsAliSender;

@Component
public class SmsSender {

	private static final Logger logger = LoggerFactory.getLogger(SmsSender.class);

	@Autowired
	private SmsAliSender smsAliSender;
	
	public boolean send(SmsData smsData){
		try {
			SendSmsResponse sendSmsResponse = smsAliSender.send(
					smsData.getTemplateCode(), 
					smsData.getSignName(), 
					smsData.getParams(),
					smsData.getPhoneNumber());
			if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
				logger.info("[SMS SEND SUCCESS] PHONES:{} TEMPLATE_CODE:{} SIGN_NAME:{} PARAMS:{} BIZ_ID:{}" , 
						String.join(",", smsData.getPhoneNumber()),
						smsData.getTemplateCode(),
						smsData.getSignName(),
						smsData.getParams() == null ? "" : JSON.toJSONString(smsData.getParams()),
						sendSmsResponse.getBizId());
				return true;
			}else{
				logger.warn("[SMS SEND FAILE] PHONES:{} TEMPLATE_CODE:{} SIGN_NAME:{} PARAMS:{} ERROR_CODE:{} ERROR_MSG:{}" , 
						String.join(",", smsData.getPhoneNumber()),
						smsData.getTemplateCode(),
						smsData.getSignName(),
						smsData.getParams() == null ? "" : JSON.toJSONString(smsData.getParams()),
						sendSmsResponse.getCode(),
						sendSmsResponse.getMessage());
			}
		} catch (Exception e) {
			logger.error("[SMS SEND ERROR] DATA:" + JSON.toJSONString(smsData), e);
		}
		return false;
	}
}
