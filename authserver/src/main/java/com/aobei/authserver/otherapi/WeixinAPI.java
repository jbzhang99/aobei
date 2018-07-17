package com.aobei.authserver.otherapi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.aobei.authserver.model.Wxapp;
import com.aobei.authserver.repository.WxappRepository;

import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.Jscode2sessionResult;
import weixin.popular.bean.sns.SnsToken;

@Controller
@RequestMapping("/wxapi")
public class WeixinAPI {

	private static String AUTH_WXM_CODE = "AUTH_WXM_CODE:";

	private static Map<String, Wxapp> WEIXIN_KS = new HashMap<>();

	@Autowired
	private WxappRepository wxappRepository;

	@Autowired
	private StringRedisTemplate redisTemplate;

	private ValueOperations<String, String> opsValue;

	@PostConstruct
	private void initOpsValue() {
		opsValue = redisTemplate.opsForValue();
	}

	/**
	 * 加载WXAPP 数据
	 */
	@Scheduled(initialDelay = 0, fixedDelay = 1000 * 60 * 5)
	private void loadWxappData() {
		WEIXIN_KS = wxappRepository.findAll().stream().collect(Collectors.toMap(Wxapp::getApp_id, Function.identity()));
	}

	/**
	 * 微信小程序 code 换取 session_key
	 * 
	 * @param appid
	 * @param js_code
	 * @return
	 */
	public Jscode2sessionResult jscode2session(String appid, String js_code) {
		// 从 redis 获取 code 缓存数据
		String code_cache = opsValue.get(AUTH_WXM_CODE + js_code);
		if (code_cache != null) {
			return JSON.parseObject(code_cache, Jscode2sessionResult.class);
		}
		return SnsAPI.jscode2session(appid, WEIXIN_KS.get(appid).getApp_secret(), js_code);
	}

	@PostMapping("/jscode2session")
	@ResponseBody
	public Jscode2sessionResult jscode2sessionRest(String appid, String js_code) {
		Jscode2sessionResult result = SnsAPI.jscode2session(appid, WEIXIN_KS.get(appid).getApp_secret(), js_code);
		if (result != null && result.isSuccess()) {
			// redis 缓存 code 及相关数据 8 秒
			opsValue.set(AUTH_WXM_CODE + js_code, JSON.toJSONString(result), 8, TimeUnit.SECONDS);
			result.setSession_key(null);
		}
		return result;
	}

	/**
	 * 获取token
	 * 
	 * @param appid
	 * @param code
	 * @return
	 */
	public SnsToken oauth2AccessToken(String appid, String code) {
		return SnsAPI.oauth2AccessToken(appid, WEIXIN_KS.get(appid).getApp_secret(), code);
	}

}
