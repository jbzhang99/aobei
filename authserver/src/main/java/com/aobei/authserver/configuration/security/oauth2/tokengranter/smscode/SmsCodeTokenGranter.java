package com.aobei.authserver.configuration.security.oauth2.tokengranter.smscode;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.StringUtils;

import com.aobei.authserver.configuration.security.oauth2.OauthExceptions;

public class SmsCodeTokenGranter extends AbstractTokenGranter {

	private static final String GRANT_TYPE = "sms_code";

	private SmsAuthorizationCodeServices smsAuthorizationCodeServices;

	private boolean testEnv; // 是否为测试环境

	public SmsCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
			AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory) {
		this(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	}

	protected SmsCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
		super(tokenServices, clientDetailsService, requestFactory, grantType);
	}

	@Override
	public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
		return super.grant(grantType, tokenRequest);
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		Map<String, String> parameters = tokenRequest.getRequestParameters();
		String code = parameters.get("code");
		String phone = parameters.get("phone");
		if (!StringUtils.hasText(phone)) {
			throw new InvalidRequestException("请输入手机号");
		} else if (!checkCellphone(phone)) {
			// 手机号 无效
			throw new InvalidRequestException("手机号格式不正确");
		}
		int userGroup = 0;
		if (client.getClientId().matches(".*student.*")) {
			userGroup = 1;
		} else if (client.getClientId().matches(".*teacher.*")) {
			userGroup = 2;
		} else if (client.getClientId().matches(".*partner.*")) {
			userGroup = 3;
		} else if (client.getClientId().matches(".*custom.*")) {
			userGroup = 4;
		}
		Authentication authentication = null;
		if (StringUtils.hasLength(code)) {
			authentication = smsAuthorizationCodeServices.consumeAuthorizationCode(client.getClientId(), userGroup,
					phone, code);
		} else {
			String sended = smsAuthorizationCodeServices.sendCode(client.getClientId(), userGroup, phone);
			if (StringUtils.hasLength(sended)) {
				if (testEnv) {
					OauthExceptions.sms_send_success.throwException("code:" + sended);
				} else {
					OauthExceptions.sms_send_success.throwException();
				}
			}
		}
		return new OAuth2Authentication(tokenRequest.createOAuth2Request(client), authentication);
	}

	public SmsAuthorizationCodeServices getSmsAuthorizationCodeServices() {
		return smsAuthorizationCodeServices;
	}

	public void setSmsAuthorizationCodeServices(SmsAuthorizationCodeServices smsAuthorizationCodeServices) {
		this.smsAuthorizationCodeServices = smsAuthorizationCodeServices;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param cellphone
	 * @return
	 */
	public static boolean checkCellphone(String cellphone) {
		String regex = "^(1[345789][0-9])\\d{8}$";
		return cellphone.matches(regex);
	}

	public void setTestEnv(boolean testEnv) {
		this.testEnv = testEnv;
	}

}
