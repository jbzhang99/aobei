package com.aobei.authserver.configuration.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderExt extends BCryptPasswordEncoder {

	private static ThreadLocal<Boolean> threadLocal = new ThreadLocal<>();

	/**
	 * 不校验密码，适用于微信code 登录
	 * 
	 * @param noPassword
	 *            不校验密码
	 */
	public static void noPassword(boolean noPassword) {
		threadLocal.set(noPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		Boolean noPassword = threadLocal.get();
		if (noPassword != null && noPassword) {
			return true;
		}
		boolean result = super.matches(rawPassword, encodedPassword);
		if (!result) {
			LoginInterceptor.noLogin();
		}
		return result;
	}

}
