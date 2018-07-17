package com.aobei.authserver.configuration.security.oauth2.tokengranter.wxmcode;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class WxmCodeTokenGranter extends AbstractTokenGranter {

	private static Logger logger = LoggerFactory.getLogger(WxmCodeTokenGranter.class);

	private static final String GRANT_TYPE = "wxm_code";

	private WxmAuthorizationCodeServices wxmAuthorizationCodeServices;

	public WxmCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
			AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory) {
		this(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	}

	protected WxmCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
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
		String appid = parameters.get("appid");
		String code = parameters.get("code");
		String userinfo = parameters.get("userinfo");
		logger.debug("appid:{} code:{} userinfo:{}", appid, code, userinfo);
		if (code == null) {
			throw new InvalidRequestException("An authorization code must be supplied.");
		}

		if (appid == null) {
			throw new InvalidRequestException("An authorization appid must be supplied.");
		}

		Authentication authentication = wxmAuthorizationCodeServices.consumeAuthorizationCode(appid, code, userinfo);
		return new OAuth2Authentication(tokenRequest.createOAuth2Request(client), authentication);
	}

	public WxmAuthorizationCodeServices getWxmAuthorizationCodeServices() {
		return wxmAuthorizationCodeServices;
	}

	public void setWxmAuthorizationCodeServices(WxmAuthorizationCodeServices wxmAuthorizationCodeServices) {
		this.wxmAuthorizationCodeServices = wxmAuthorizationCodeServices;
	}

}
