package com.aobei.authserver.configuration.security.oauth2.tokengranter.wxacode;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author liyi
 *
 */
public interface WxaAuthorizationCodeServices {

	Authentication consumeAuthorizationCode(String appid, String code)
			throws OAuth2Exception;
}
