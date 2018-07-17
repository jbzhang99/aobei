package com.aobei.pushserver.task;

import com.aobei.common.bean.IGtPushData;
import com.aobei.common.boot.event.listener.IGtPushSendEventListener;
import com.aobei.pushserver.send.Pusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Configuration
public class IGtPustTask {

    public static String  key  = IGtPushSendEventListener.PUSH_KEY_LIST;

    @Autowired
    Pusher pusher;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private BoundListOperations<String, Object> opsList;

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
                IGtPushData iGtPushData = (IGtPushData) object;
                if(iGtPushData.getType()==IGtPushData.BIND){
                    pusher.bind(iGtPushData);
                }else if(iGtPushData.getType()==IGtPushData.BIND){
                    pusher.unbind(iGtPushData);
                }else {
                    pusher.send(iGtPushData);
                }

            }
        }
    }
}
