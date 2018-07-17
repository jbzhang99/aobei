package com.aobei.smsserver.task;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import com.aobei.common.bean.SmsData;
import com.aobei.common.boot.event.listener.SmsSendEventListener;
import com.aobei.smsserver.send.SmsSender;

/**
 * 延时短信发送
 * 
 * @author liyi
 *
 */
@Configuration
public class SmsLazeTask {

	private static String key = SmsSendEventListener.SMS_KEY_ZSET;

	private static String key_h = key + "_H";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private BoundZSetOperations<String, Object> opsZset;

	private BoundValueOperations<String, Object> opsValue;

	private long min = System.currentTimeMillis() / 1000 - 60 * 60 * 24 * 7; // 最小范围

	@Autowired
	private SmsSender smsSender;

	@PostConstruct
	private void initOps() {
		opsZset = redisTemplate.boundZSetOps(key);
		opsValue = redisTemplate.boundValueOps(key_h);
		/*
		 * SmsData smsData = new SmsData(); 
		 * smsData.addParam("code", "9999");
		 * smsData.setPhoneNumber(new String[] { "13520335460" });
		 * smsData.setTemplateCode("SMS_125018908"); 
		 * smsData.setSignName("浦尔家");
		 * smsData.setStartDeliverTime(System.currentTimeMillis() / 1000 + 10);
		 * opsZset.add(smsData, smsData.getStartDeliverTime());
		 */
	}

	/**
	 * 调度
	 */
	@Scheduled(initialDelay = 1800, fixedDelay = 2800)
	private void task_data_from_redis() {
		//占位ZSET 操作
		if (opsValue.setIfAbsent(1)) {
			//预占操作时长 5 分钟
			opsValue.expire(60 * 5, TimeUnit.SECONDS);
			long max = System.currentTimeMillis() / 1000;
			Set<Object> objects = opsZset.rangeByScore(min, max);
			if (objects != null) {
				for (Object obj : objects) {
					SmsData smsData = (SmsData) obj;
					smsSender.send(smsData);
					opsZset.remove(obj);
				}
			}
			//预占操作时长 3 秒
			opsValue.expire(3, TimeUnit.SECONDS);
			//设置下次执行的开始范围
			min = max - 1;
		}
	}

}
