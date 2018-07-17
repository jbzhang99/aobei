package com.aobei.common.boot.event.listener;

import java.util.Objects;

import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.aobei.common.bean.SmsData;
import com.aobei.common.boot.event.SmsSendEvent;

/**
 * 短信发送监听
 * 
 * @author liyi
 *
 */
public class SmsSendEventListener implements ApplicationListener<SmsSendEvent> {

	public static final String SMS_KEY_LIST = "ALI_SMS_SEND_LIST";

	public static final String SMS_KEY_ZSET = "ALI_SMS_SEND_ZSET";

	private BoundListOperations<String, Object> listOps;
	
	private BoundZSetOperations<String, Object> zsetOps;

	public SmsSendEventListener(RedisTemplate<String, Object> redisTemplate) {
		super();
		this.listOps = redisTemplate.boundListOps(SMS_KEY_LIST);
		this.zsetOps = redisTemplate.boundZSetOps(SMS_KEY_ZSET);
	}

	@Override
	public void onApplicationEvent(SmsSendEvent event) {
		SmsData data = event.getData();
		Objects.requireNonNull(data);
		//延时短信
		if (data.getStartDeliverTime() != null && data.getStartDeliverTime() > System.currentTimeMillis() / 1000) {
			zsetOps.add(data, data.getStartDeliverTime());
		}else{
			listOps.rightPush(data);
		}
	}

}
