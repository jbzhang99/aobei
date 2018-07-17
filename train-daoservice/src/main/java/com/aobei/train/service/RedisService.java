package com.aobei.train.service;


import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisService {


    /**
     * 创建一个没有有效期限制的key
     * @param key
     * @param value
     * @return
     */
    void setStringValue(String key, String value);

    /**
     * 创建指定时有效期限制的key
     * @param key
     * @param value
     * @param expire 格式：仅支持"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    boolean setStringValue(String key, String value, String expire);

    /**
     * 设置过期时间到指定日期
     * @param key
     * @param value
     * @param date
     * @return
     */
    boolean setStringValue(String key, String value, Date date);

    /**
     * 设置过期之间到指定时长之后
     * @param key
     * @param value
     * @param expire
     * @param timeUnit
     */
    void setStringValue(String key, String value, int expire, TimeUnit timeUnit);


    String getStringValue(String key);



    void  delete(String key);
    void delete(Collection<String> collection);
    long increment(String key, long incr);
    long getExpire(String key, TimeUnit timeUnit);
    boolean  expireAt(String key, Long time, TimeUnit timeUnit);
    boolean exits(String key);

    void sAdd(String key, String value);
    String sPop(String key);
    Set<String> sMembers(String key);
    void convertAndSend(String topic, String message);
}
