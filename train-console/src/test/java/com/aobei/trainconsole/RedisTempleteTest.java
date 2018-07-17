package com.aobei.trainconsole;

import com.aobei.common.boot.RedisIdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * Created by mr_bl on 2018/6/21.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTempleteTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void test(){
        redisTemplate.opsForValue().set("ADD_2018_06_22","1");
    }

    @Test
    public void testIncr(){
        redisTemplate.opsForValue().increment("ADD_2018_06_21",1);
    }

    @Test
    public void testExpire(){
        redisTemplate.expire("ADD_2018_06_21",30, TimeUnit.SECONDS);
    }

    @Test
    public void testGet(){
        String s = redisTemplate.opsForValue().get("ADD_2018_06_22");
        System.out.println(s);
    }

    @Test
    public void testUtil(){
        RedisIdGenerator idGenerator = new RedisIdGenerator();
        idGenerator.setRedisTemplate(redisTemplate);
        new Thread(()->{
            for (int i = 0;i < 10;i++){
                String id = idGenerator.getAutoIncrId("PC20180622", 4);
                System.out.println(Thread.currentThread().getName()+ "\t" + id);
            }
        },"AAA").start();

        new Thread(()->{
            for (int i = 0;i < 10;i++) {
                String id = idGenerator.getAutoIncrId("PC20180622", 4);
                System.out.println(Thread.currentThread().getName() + "\t" + id);
            }
        },"BBB").start();

        try {
            Thread.sleep(10000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGenerator(){
        RedisIdGenerator idGenerator = new RedisIdGenerator();
        idGenerator.setRedisTemplate(redisTemplate);
        Long aLong = idGenerator.getAutoIncrNum("BC20180621");
        System.out.println(aLong);
    }
}
