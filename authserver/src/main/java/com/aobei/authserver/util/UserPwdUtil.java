package com.aobei.authserver.util;

import java.util.Random;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.ConfigurableHashService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;

public class UserPwdUtil {

	private static String staticSalt = "private_salt_aobeikeji_nxt";
	private static SecureRandomNumberGenerator srng = new SecureRandomNumberGenerator();

	public static UserPwd userPwd(String password) {
		String salt = srng.nextBytes(new Random().nextInt(29) + 1).toHex();
		int numIterations = new Random().nextInt(29) + 1;
		String saltpwd = digestEncodedPassword(password, salt, numIterations);
		return new UserPwd(saltpwd, salt, numIterations);
	}

	public static String digestEncodedPassword(String encodedPassword, String dynaSalt,
			Integer numIterations) {
		final ConfigurableHashService hashService = new DefaultHashService();

		hashService.setPrivateSalt(ByteSource.Util.bytes(staticSalt));
		hashService.setHashAlgorithmName("SHA-512");

		hashService.setHashIterations(numIterations);
		final HashRequest request = new HashRequest.Builder().setSalt(dynaSalt).setSource(encodedPassword).build();
		return hashService.computeHash(request).toHex();
	}
	
	public static class UserPwd {

		private String password;

		private String salt;

		private int numIterations;

		public UserPwd() {
			super();
		}

		public UserPwd(String password, String salt, int numIterations) {
			super();
			this.password = password;
			this.salt = salt;
			this.numIterations = numIterations;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getSalt() {
			return salt;
		}

		public void setSalt(String salt) {
			this.salt = salt;
		}

		public int getNumIterations() {
			return numIterations;
		}

		public void setNumIterations(int numIterations) {
			this.numIterations = numIterations;
		}

	}
}
