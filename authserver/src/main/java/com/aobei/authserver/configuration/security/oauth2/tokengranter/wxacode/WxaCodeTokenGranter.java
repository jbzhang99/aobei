package com.aobei.authserver.configuration.security.oauth2.tokengranter.wxacode;

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


public class WxaCodeTokenGranter extends AbstractTokenGranter {

	private static final String GRANT_TYPE = "wxa_code";
	
	private WxaAuthorizationCodeServices wxaAuthorizationCodeServices;

	public WxaCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
			AuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory) {
		this(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	}

	protected WxaCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
		super(tokenServices, clientDetailsService, requestFactory, grantType);
	}
	

	@Override
	public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
		// TODO Auto-generated method stub
		return super.grant(grantType, tokenRequest);
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		Map<String, String> parameters = tokenRequest.getRequestParameters();
		String code = parameters.get("code");
		String appid = parameters.get("appid");

		if (code == null) {
			throw new InvalidRequestException("An authorization code must be supplied.");
		}
		
		if (appid == null) {
			throw new InvalidRequestException("An authorization appid must be supplied.");
		}
		
		Authentication authentication = wxaAuthorizationCodeServices.consumeAuthorizationCode(appid, code);
		return new OAuth2Authentication(tokenRequest.createOAuth2Request(client), authentication);
	}

	public WxaAuthorizationCodeServices getWxaAuthorizationCodeServices() {
		return wxaAuthorizationCodeServices;
	}

	public void setWxaAuthorizationCodeServices(WxaAuthorizationCodeServices wxaAuthorizationCodeServices) {
		this.wxaAuthorizationCodeServices = wxaAuthorizationCodeServices;
	}

}
