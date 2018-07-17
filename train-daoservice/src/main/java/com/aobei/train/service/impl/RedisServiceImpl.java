package com.aobei.train.service.impl;


import com.aobei.train.service.RedisService;
import custom.util.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    private Environment env;

    @Override
    public void setStringValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public boolean setStringValue(String key, String value, String expire) {

        if (!RegexUtil.check(RegexUtil.DATE_REGEX_SIMPLE, expire)) {
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(expire);
        } catch (ParseException e) {
            logger.error("format " + expire + "failed");
            return false;
        }

        return setStringValue(key, value, date);
    }

    @Override
    public boolean setStringValue(String key, String value, Date date) {
        setStringValue(key, value);
        return stringRedisTemplate.opsForValue().getOperations().expireAt(key, date);
    }

    @Override
    public void setStringValue(String key, String value, int expire, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }

    @Override
    public String getStringValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public  void delete(Collection<String> collection){
        stringRedisTemplate.delete(collection);
    }
    @Override
    public long increment(String key, long incr) {
        if(!exits(key)){
            stringRedisTemplate.opsForValue().set(key,"0");
        }
        return stringRedisTemplate.opsForValue().increment(key, incr);
    }

    @Override
    public long getExpire(String key, TimeUnit timeUnit) {
        if (timeUnit == null)
            return stringRedisTemplate.opsForValue().getOperations().getExpire(key);
        return stringRedisTemplate.opsForValue().getOperations().getExpire(key, timeUnit);
    }

    @Override
    public boolean expireAt(String key ,Long time,TimeUnit timeUnit) {
       return stringRedisTemplate.expire(key,time,timeUnit);
    }

    @Override
    public boolean exits(String key ){

        if(stringRedisTemplate.opsForValue().get(key)!=null)
            return  true;
        return false;
    }

    @Override
    public void sAdd(String key, String value) {
        stringRedisTemplate.opsForSet().add(key,value);
    }

    @Override
    public String sPop(String key) {
       return stringRedisTemplate.opsForSet().pop(key);
    }

    public Set<String> sMembers(String key){
        return stringRedisTemplate.opsForSet().members(key);
    }
    @Override
    public void convertAndSend(String topic, String message) {
        String[] arr = env.getActiveProfiles();
        String active = "";
        if(arr!=null && arr.length>0){
            active = arr[0];
        }
        stringRedisTemplate.convertAndSend(topic+active,message);
    }
    public static void main(String[] args) {


        System.out.println(RegexUtil.check(RegexUtil.DATE_REGEX_SIMPLE, "2018-01-33 12:33:22"));
        String s = String.format("yyyy-MM-dd HH:mm:ss", "2018-10-10");
        System.out.println(s);
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, 0, 0, 0);
        Date date = calendar.getTime();

        System.out.println(date);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = "2018-03-12 00:00:00";
        try {
            format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
