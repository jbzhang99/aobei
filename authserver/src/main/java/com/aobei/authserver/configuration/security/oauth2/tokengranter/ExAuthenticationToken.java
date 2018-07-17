package com.aobei.authserver.configuration.security.oauth2.tokengranter;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.aobei.authserver.configuration.security.userdetails.CustomUserDetails;

public class ExAuthenticationToken extends AbstractAuthenticationToken{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CustomUserDetails customUserDetails;

	public ExAuthenticationToken(CustomUserDetails customUserDetails) {
		super(customUserDetails.getAuthorities());
		this.customUserDetails = customUserDetails;
	}
	
	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return customUserDetails;
	}

	
}
