package com.aobei.common.sms;

import java.util.Map;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;

/**
 * create by renpiming on 2018/2/1
 */
public class SmsAliSender {

	private IAcsClient acsClient;

	public SmsAliSender(IAcsClient acsClient) {
		this.acsClient = acsClient;
	}

	/**
	 * 发送短信
	 * 
	 * @param templateCode
	 * @param signName
	 * @param params
	 * @param phoneNumber 可以多个手机号同时发送
	 * @return
	 * @throws com.aliyuncs.exceptions.ClientException
	 * @throws ServerException
	 * @throws @throws
	 *             Exception
	 */
	public SendSmsResponse send(String templateCode, String signName,Map<String, String> params, String... phoneNumber)
			throws ServerException, ClientException {
		// IAcsClient acsClient = makeAcsClient();
		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(String.join(",", phoneNumber));
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(signName);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templateCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		if (params != null) {
			request.setTemplateParam(JSON.toJSONString(params));
		}
		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");
		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		return sendSmsResponse;
	}

	/**
	 * 短信查询
	 * 
	 * @param phoneNumber
	 *            手机号
	 * @param bizId 
	 * 			发送流水号    可以为空           
	 * @param sendDate
	 *            发送日期 格式 YYYYMMDD
	 * @param pageSize
	 *            页大小
	 * @param currentPage
	 *            当前页码 从1开始计数
	 * @return
	 * @throws Exception
	 */
	public QuerySendDetailsResponse query(String phoneNumber, String bizId, String sendDate, long pageSize, long currentPage)
			throws ServerException, ClientException {
		// 组装请求对象
		QuerySendDetailsRequest request = new QuerySendDetailsRequest();
		// 必填-号码
		request.setPhoneNumber(phoneNumber);
		// 可选-调用发送短信接口时返回的BizId
		request.setBizId(bizId);
		// 必填-短信发送的日期 支持30天内记录查询（可查其中一天的发送数据），格式yyyyMMdd
		request.setSendDate(sendDate);
		// 必填-页大小
		request.setPageSize(pageSize);
		// 必填-当前页码从1开始计数
		request.setCurrentPage(currentPage);
		// hint 此处可能会抛出异常，注意catch
		QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
		return querySendDetailsResponse;
	}

}
