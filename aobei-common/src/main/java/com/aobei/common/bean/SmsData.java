package com.aobei.common.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class SmsData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5052449609326757325L;

	private String templateCode;
	private Map<String, String> params;
	private String signName;
	private String[] phoneNumber;
	private Long startDeliverTime; // 发送时间 距离 1970 的秒数,不为空时将使用延迟发送
	private long timestamp;

	public void addParam(String key, String value) {
		if (params == null) {
			params = new LinkedHashMap<>();
		}
		params.put(key, value);
	}

	public SmsData() {
		this.timestamp = System.currentTimeMillis();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String[] getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String[] phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getStartDeliverTime() {
		return startDeliverTime;
	}

	public void setStartDeliverTime(Long startDeliverTime) {
		this.startDeliverTime = startDeliverTime;
	}

}
