package com.aobei.authserver.configuration.security.oauth2.tokengranter.smscode;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.aliyun.openservices.shade.io.netty.util.internal.ConcurrentSet;
import com.aobei.authserver.configuration.security.BCryptPasswordEncoderExt;
import com.aobei.authserver.configuration.security.LoginInterceptor;
import com.aobei.authserver.configuration.security.oauth2.OauthExceptions;
import com.aobei.authserver.configuration.security.oauth2.tokengranter.ExAuthenticationToken;
import com.aobei.authserver.configuration.security.userdetails.CustomUserDetails;
import com.aobei.authserver.model.PhoneUser;
import com.aobei.authserver.model.User;
import com.aobei.authserver.repository.UserRepository;
import com.aobei.common.bean.SmsData;
import com.aobei.common.boot.EventPublisher;
import com.aobei.common.boot.event.SmsSendEvent;

@Component
public class DefaultSmsAuthorizationCodeServices implements SmsAuthorizationCodeServices {

	private static Logger logger = LoggerFactory.getLogger(DefaultSmsAuthorizationCodeServices.class);

	private static String TOKEN_KEYPRE = "AUTH_TOKEN_SMS";

	private static String PHONE_KEYPRE = "AUTH_PHONE_SMS";

	private static String IP_KEYPRE = "AUTH_IP_SMS";

	private ValueOperations<String, Object> opsValue;

	private ListOperations<String, Object> opsList;

	private int timeout = 5; // 短信有效期 分钟

	private int errorRetryTimeout = 2; // 短信重试计算周期 分钟

	private int errorRetry = 5; // 短信重试周期内次数限制
	
	private int storeTimeout = 30;	// code保存时间
	
	private static Set<String> whitelist;	// 白名单用户
	
	private String whiteCode = "123456";	// 白名单验证码
	
	static{
		whitelist = new ConcurrentSet<>();
		whitelist.add("13512345678");
		whitelist.add("13612345678");
	}

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private UserRepository userRepository;

	@Resource(name = "passwordEncoder")
	private BCryptPasswordEncoderExt passwordEncoder;

	@Autowired
	EventPublisher eventPublisher;

	@PostConstruct
	private void init() {
		opsValue = redisTemplate.opsForValue();
		opsList = redisTemplate.opsForList();
	}

	@Override
	public Authentication consumeAuthorizationCode(String clientId, int userGroup, String phone, String code)
			throws OAuth2Exception {
		// 判断是否为白名单用户
		boolean whiteUser = whitelist.contains(phone);
		// 判断是否请求限制
		if (phoneLimit(clientId, phone)) {
			OauthExceptions.sms_code_limit.throwException();
		}
		String key = redisKey(clientId, phone, code);
		Long expire = redisTemplate.getExpire(key);
		if (expire != null && expire > 0 || whiteUser && whiteCode.equals(code)) {
			if (expire < 60 * storeTimeout && !whiteUser) {
				OauthExceptions.sms_code_expire.throwException();
			}
			// 根据手机号获取用户信息
			PhoneUser phoneUser = userRepository.findUuidByPhone(userGroup, phone);
			Long user_id = null;
			if (phoneUser == null || phoneUser.getUser_id() == null) {
				// 添加用户
				int channel = (Integer)RequestContextHolder.getRequestAttributes().getAttribute("channel", 0);
				user_id = userRepository.createUuidByPhone(userGroup, phone, channel, phoneUser == null);
			}else{
				user_id = phoneUser.getUser_id();
			}
			User user = userRepository.findById(user_id);
			redisTemplate.delete(key);
			LoginInterceptor.storeLoginLog(user.getUser_id());
			return new ExAuthenticationToken(new CustomUserDetails(user));
		}
		OauthExceptions.sms_code_notexists.throwException();
		return null;
	}

	@Override
	public String sendCode(String clientId, int userGroup, String phone) throws OAuth2Exception {
		if(userGroup < 4){
			PhoneUser phoneUser = userRepository.findUuidByPhone(userGroup, phone); 
			if(phoneUser == null) { 
				OauthExceptions.sms_send_nouser.throwException();
			}
		}
		 
		if (ipLimit()) {
			OauthExceptions.sms_send_limit.throwException();
		}
		String code = RandomStringUtils.randomNumeric(6);

		// 保存code
		storeCode(clientId, phone, code);
		// 发送短信码
		sendSmsCode(phone, code);
		return code;
	}

	/**
	 * 判断手机号code 尝试次数是否限制 <br>
	 * 两分钟内5次
	 * 
	 * @param client_id
	 * @param phone
	 * @return
	 */
	private boolean phoneLimit(String client_id, String phone) {
		String key = String.format("%s:%s:%s", PHONE_KEYPRE, client_id, phone);
		Long size = opsList.size(key);
		if (size >= errorRetry) {
			Integer v = (Integer) opsList.index(key, 0);
			if (v < System.currentTimeMillis() / 1000) {
				opsList.remove(key, 1, v);
			} else {
				return true;
			}
		}
		opsList.rightPush(key, System.currentTimeMillis() / 1000 + 60 * errorRetryTimeout);
		redisTemplate.expire(key, errorRetryTimeout * 5, TimeUnit.MINUTES);
		return false;
	}

	/**
	 * IP 限制  <br>
	 * 同一IP 10 分钟内 40 次
	 * 
	 * @return
	 */
	private boolean ipLimit() {
		String ip = RequestContextHolder.getRequestAttributes().getAttribute("IP", 0).toString();
		String key = String.format("%s:%s", IP_KEYPRE, ip);
		Long size = opsList.size(key);
		if (size >= errorRetry * 8) {
			Integer v = (Integer) opsList.index(key, 0);
			if (v < System.currentTimeMillis() / 1000) {
				opsList.remove(key, 1, v);
			} else {
				return true;
			}
		}
		opsList.rightPush(key, System.currentTimeMillis() / 1000 + 60 * errorRetryTimeout);
		redisTemplate.expire(key, errorRetryTimeout * 5, TimeUnit.MINUTES);
		return false;
	}

	/**
	 * 保存code
	 * 
	 * @param clientId
	 * @param phone
	 * @param code
	 */
	private void storeCode(String clientId, String phone, String code) {
		String key = redisKey(clientId, phone, code);
		opsValue.set(key, 1, timeout + storeTimeout, TimeUnit.MINUTES);
	}

	/**
	 * redis key
	 * 
	 * @param clientId
	 * @param phone
	 * @param code
	 * @return
	 */
	private String redisKey(String clientId, String phone, String code) {
		return String.format("%s:%s:%s:%s", TOKEN_KEYPRE, clientId, phone, code);
	}

	/**
	 * 发送短信码
	 * 
	 * @param phone
	 * @param code
	 */
	private void sendSmsCode(String phone, String code) {
		SmsData smsData = new SmsData();
		smsData.setTemplateCode("SMS_125018908");
		smsData.setPhoneNumber(new String[] { phone });
		smsData.setSignName("浦尔家");
		Map<String, String> params = new java.util.HashMap<>();
		params.put("code", code);
		params.put("timeout", timeout + "");
		smsData.setParams(params);
		SmsSendEvent smsSendEvent = new SmsSendEvent(this, smsData);
		eventPublisher.publish(smsSendEvent);
	}

}
