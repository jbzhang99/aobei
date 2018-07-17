package com.aobei.authserver.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {

	private String oauth2key;
	
	private String oauth2publicKey;

	public String getOauth2key() {
		return oauth2key;
	}

	public void setOauth2key(String oauth2key) {
		this.oauth2key = oauth2key;
	}

	public String getOauth2publicKey() {
		return oauth2publicKey;
	}

	public void setOauth2publicKey(String oauth2publicKey) {
		this.oauth2publicKey = oauth2publicKey;
	}
	
}
