package com.aobei.trainapi.server.impl;


import com.aobei.train.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.support.ExpireKey;

import java.util.concurrent.TimeUnit;

@Service
public class ExpireKeyRedis implements ExpireKey {
    @Autowired
    RedisService redisService;
    @Override
    public boolean add(String s, int i) {
        redisService.setStringValue(s,i+"",DEFAULT_EXPIRE, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public boolean add(String s) {
        redisService.setStringValue(s,s,DEFAULT_EXPIRE,TimeUnit.SECONDS);
        return false;
    }

    @Override
    public boolean exists(String s) {
        return redisService.exits(s);
    }
}
