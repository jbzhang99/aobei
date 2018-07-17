package com.aobei.authserver.configuration.security.oauth2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.aobei.authserver.configuration.security.userdetails.CustomUserDetails;

public class JWTTokenEnhancer implements TokenEnhancer {
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if (authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
			String roles = "";
			@SuppressWarnings("unchecked")
			List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) customUserDetails.getAuthorities();
			for (GrantedAuthority grantedAuthority : grantedAuthorities) {
				roles = roles.concat(" " + grantedAuthority.getAuthority());
			}
			roles = roles.trim();

			Map<String, Object> additionalInfo = new HashMap<>();
			additionalInfo.put("uuid", customUserDetails.getUserid());
			additionalInfo.put("role", roles);
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		}
		return accessToken;
	}
}
