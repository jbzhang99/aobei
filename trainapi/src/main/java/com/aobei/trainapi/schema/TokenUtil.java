package com.aobei.trainapi.schema;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;

@Component
public class TokenUtil {

	private static Logger logger = LoggerFactory.getLogger(TokenUtil.class);

	@Autowired
	private ResourceServerTokenServices tokenServices;

	/**
	 * 获取当前HttpServletRequest
	 * 
	 * @return
	 */
	public HttpServletRequest request() {
		ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return ra.getRequest();
	}

	/**
	 * 获取header中的channel，渠道
	 * @return
	 */
	public String getChannel() {
		String channelString = headerInfomation("channel");
		channelString  = channelString==null?"0":channelString;
		return channelString;
	}

	/**
	 * 获取平台  android 安卓
	 * ios 苹果
	 * wxm 微信小程序
	 * @return
	 */
	public String getPlatform(){
		return headerInfomation("platform");
	}

	/**
	 * 获取设备ID
	 * @return
	 */
	public String getDuuid(){
		return  headerInfomation("Duuid");
	}

	/**
	 * 获取客户端版本
	 * @return
	 */
	public String getClientVersion(){
		return headerInfomation("version");
	}

	/**
	 * 获取设备名称
	 * @return
	 */
	public String getDevice(){
		return headerInfomation("device");
	}
	String headerInfomation(String key){
		return  request().getHeader(key);
	}
	/**
	 * 获取请求 token
	 * 
	 * @return
	 */
	OAuth2AccessToken readAccessToken() {
		OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) SecurityContextHolder
				.getContext().getAuthentication().getDetails();
		return tokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue());
	}

	/**
	 * 获取 token 中body 字段信息
	 * 
	 * @param key
	 * @return
	 */
	String getAdditionalInformation(String key) {
		if ("client_id".equals(key)) {
			try {
				String data = readAccessToken().getValue().split("\\.")[1];
				String jsonData = new String(Base64.getDecoder().decode(data));
				return JSON.parseObject(jsonData).getString("client_id");
			} catch (Exception e) {
				logger.error("", e);
				return null;
			}
		} else {
			Object object = readAccessToken().getAdditionalInformation().get(key);
			if (object != null) {
				return object.toString();
			} else {
				return null;
			}
		}
	}

	/**
	 * 获取uuid
	 * 
	 * @return
	 */
	Long getUuid() {
		String uuid = getAdditionalInformation("uuid");
		if (uuid != null) {
			return Long.valueOf(uuid);
		}
		return null;
	}

	/**
	 * 获取uuid
	 * 
	 * @return
	 */
	String getUserName() {
		return getAdditionalInformation("user_name");
	}

	/**
	 * 获取jti
	 * 
	 * @return
	 */
	String getJti() {
		return getAdditionalInformation("jti");
	}

	/**
	 * 获取client_id
	 * 
	 * @return
	 */
	public String getClientId() {
		return getAdditionalInformation("client_id");
	}

}
