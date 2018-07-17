package com.aobei.cas.applicationevent;

import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginEvent(Object source, String username, String ip, String xRealIp, String userAgent, String duuid) {
		super(source);
		this.username = username;
		this.ip = ip;
		this.xRealIP = xRealIp;
		this.userAgent = userAgent;
		this.duuid = duuid;
	}

	private String ip;

	private String xRealIP;

	private String username;

	private String userAgent;

	private String duuid;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getxRealIP() {
		return xRealIP;
	}

	public void setxRealIP(String xRealIP) {
		this.xRealIP = xRealIP;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDuuid() {
		return duuid;
	}

	public void setDuuid(String duuid) {
		this.duuid = duuid;
	}

}
