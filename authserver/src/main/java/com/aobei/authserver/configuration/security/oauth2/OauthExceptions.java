package com.aobei.authserver.configuration.security.oauth2;

public enum OauthExceptions {

	/**
	 * 短信发送成功
	 */
	sms_send_success("短信发送成功", 0),
	/**
	 * 短信发送无对应手机号用户信息
	 */
	sms_send_nouser("没有对应手机号用户信息", 10009),
	/**
	 * 短信发送次数限制
	 */
	sms_send_limit("短信发送次数限制", 10010),
	/**
	 * 短信code 验证次数限制
	 */
	sms_code_limit("短信验证次数限制", 10011),
	/**
	 * 短信code 过期
	 */
	sms_code_expire("短信验证码已过期", 10012),
	/**
	 * 短信code 不存在
	 */
	sms_code_notexists("短信验证码不存在", 10013);
	

	OauthExceptions(String msg, Integer errcode) {
		this.msg = msg;
		this.errcode = errcode;
	}

	private String msg;
	private Integer errcode;

	public void throwException() {
		throw new CustomClientAuthenticationException(this.name(), this.msg, this.errcode.toString());
	}
	
	public void throwException(String append) {
		throw new CustomClientAuthenticationException(this.name(), this.msg + " " + append, this.errcode.toString());
	}

}
