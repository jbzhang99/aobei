package com.aobei.common.boot.event.listener;

import com.aobei.common.bean.IGtPushData;
import com.aobei.common.bean.SmsData;
import com.aobei.common.boot.event.IGtPushEvent;
import com.aobei.common.boot.event.SmsSendEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * 短信发送监听
 * 
 * @author liyi
 *
 */
public class IGtPushSendEventListener implements ApplicationListener<IGtPushEvent> {

	public static final String PUSH_KEY_LIST = "IGT_PUSH_SEND_LIST";

	public static final String PUSH_KEY_ZSET = "IGT_PUSH_SEND_ZSET";

	private BoundListOperations<String, Object> listOps;

	private BoundZSetOperations<String, Object> zsetOps;

	public IGtPushSendEventListener(RedisTemplate<String, Object> redisTemplate) {
		super();
		this.listOps = redisTemplate.boundListOps(PUSH_KEY_LIST);
		this.zsetOps = redisTemplate.boundZSetOps(PUSH_KEY_ZSET);
	}

	@Override
	public void onApplicationEvent(IGtPushEvent event) {
		IGtPushData data = event.getData();
		Objects.requireNonNull(data);
		listOps.rightPush(data);
	}

}
