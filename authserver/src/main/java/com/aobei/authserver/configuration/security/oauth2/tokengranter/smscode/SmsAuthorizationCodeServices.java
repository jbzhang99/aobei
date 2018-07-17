package com.aobei.authserver.configuration.security.oauth2.tokengranter.smscode;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author liyi
 *
 */
public interface SmsAuthorizationCodeServices {

	/**
	 * 通过code 换取用户信息
	 * @param clientId
	 * @param userGroup
	 * @param phone
	 * @param code
	 * @return
	 */
	Authentication consumeAuthorizationCode(String clientId,int userGroup,String phone, String code)
			throws OAuth2Exception;
	
	/**
	 * 发送手机号
	 * @param clientId
	 * @param userGroup
	 * @param phone
	 * @return
	 */
	String sendCode(String clientId,int userGroup,String phone)
			throws OAuth2Exception;
	
}
