package com.aobei.authserver.configuration.security.oauth2;

import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;

@SuppressWarnings("serial")
public class CustomClientAuthenticationException extends ClientAuthenticationException {

	private String oAuth2ErrorCode;

	public CustomClientAuthenticationException(String oAuth2ErrorCode, String msg, String errcode) {
		super(msg);
		this.oAuth2ErrorCode = oAuth2ErrorCode;
		this.addAdditionalInformation("errcode", errcode);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return oAuth2ErrorCode;
	}

}
