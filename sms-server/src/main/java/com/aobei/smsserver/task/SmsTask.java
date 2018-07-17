package com.aobei.smsserver.task;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import com.aobei.common.bean.SmsData;
import com.aobei.common.boot.event.listener.SmsSendEventListener;
import com.aobei.smsserver.send.SmsSender;

/**
 * 即时短信发送
 * 
 * @author liyi
 *
 */
@Configuration
public class SmsTask {

	private static String key = SmsSendEventListener.SMS_KEY_LIST;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private BoundListOperations<String, Object> opsList;

	@Autowired
	private SmsSender smsSender;

	@PostConstruct
	private void initOps() {
		opsList = redisTemplate.boundListOps(key);
	}

	/**
	 * 调度
	 */
	@Scheduled(initialDelay = 1000, fixedDelay = 1200)
	private void task_data_from_redis() {
		Object object = 1;
		// 如果有获取到list 中的数据
		while (object != null) {
			object = opsList.leftPop(1, TimeUnit.SECONDS);
			if (object != null) {
				SmsData smsData = (SmsData) object;
				smsSender.send(smsData);
			}
		}
	}

}
