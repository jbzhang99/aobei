package com.aobei.authserver.configuration.security.oauth2.tokengranter.wxmcode;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author liyi
 *
 */
public interface WxmAuthorizationCodeServices {

	Authentication consumeAuthorizationCode(String appid, String code,String userinfo)
			throws OAuth2Exception;
}
