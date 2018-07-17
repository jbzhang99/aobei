package com.aobei.trainapi.server.bean;

public class AliyunProperties {

	private String publicBucket;
	private String privateBucket;
	private String accessKeyId;
	private String accessKeySecret;
	private String endpoint;
	public String getPublicBucket() {
		return publicBucket;
	}
	public void setPublicBucket(String publicBucket) {
		this.publicBucket = publicBucket;
	}
	public String getPrivateBucket() {
		return privateBucket;
	}
	public void setPrivateBucket(String privateBucket) {
		this.privateBucket = privateBucket;
	}
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("AliyunProperties{");
		sb.append("publicBucket='").append(publicBucket).append('\'');
		sb.append(", privateBucket='").append(privateBucket).append('\'');
		sb.append(", accessKeyId='").append(accessKeyId).append('\'');
		sb.append(", accessKeySecret='").append(accessKeySecret).append('\'');
		sb.append(", endpoint='").append(endpoint).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
