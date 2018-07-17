package com.aobei.trainapi.configuration.security.oauth2;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;

public class CustomWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

	private static Map<String, String> ERRCODE_MAP;

	static {
		ERRCODE_MAP = new HashMap<>();
		ERRCODE_MAP.put("default", "10001");
		ERRCODE_MAP.put("sms_send_success", "0");
		ERRCODE_MAP.put(OAuth2Exception.INVALID_REQUEST, "10002");
		ERRCODE_MAP.put(OAuth2Exception.INVALID_CLIENT, "10003");
		ERRCODE_MAP.put(OAuth2Exception.INVALID_GRANT, "10004");
		ERRCODE_MAP.put(OAuth2Exception.INVALID_SCOPE, "10005");
		ERRCODE_MAP.put(OAuth2Exception.INVALID_TOKEN, "10006");
		ERRCODE_MAP.put(OAuth2Exception.UNAUTHORIZED_CLIENT, "10007");
		ERRCODE_MAP.put(OAuth2Exception.UNSUPPORTED_GRANT_TYPE, "10008");
		ERRCODE_MAP.put("no_phone_user", "10009");
	}

	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
		OAuth2Exception responseBody = super.translate(e).getBody();
		if (ERRCODE_MAP.containsKey(responseBody.getOAuth2ErrorCode())) {
			responseBody.addAdditionalInformation("errcode", ERRCODE_MAP.get(responseBody.getOAuth2ErrorCode()));
		} else {
			responseBody.addAdditionalInformation("errcode", ERRCODE_MAP.get("default"));
		}
		return new ResponseEntity<OAuth2Exception>(responseBody, HttpStatus.OK);
	}

}
