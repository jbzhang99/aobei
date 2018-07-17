package com.aobei.trainapi.schema;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.aobei.trainapi.server.bean.ApiCode;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;

@Component
public class ApiCodeQuery implements GraphQLQueryResolver {

	private static String KEY_REDIS_APICODE = "APIMTS:";

	@Value("#{${api_code_timeout}}")
	private int api_code_timeout;

	@Autowired
	private StringRedisTemplate redisTemplate;

	private ValueOperations<String, String> opsValue;

	@PostConstruct
	private void initRedisOps() {
		opsValue = redisTemplate.opsForValue();
	}

	public static String codeKey(String code) {
		return KEY_REDIS_APICODE + code;
	}

	public ApiCode apicode() {
		String code = UUID.randomUUID().toString();
		opsValue.set(codeKey(code), "1", api_code_timeout, TimeUnit.SECONDS);
		ApiCode apiCode = new ApiCode();
		apiCode.setCode(code);
		apiCode.setExpires_in(api_code_timeout);
		return apiCode;
	}
}
