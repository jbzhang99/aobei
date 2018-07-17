package com.aobei.common.boot;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * 基于reids 生成id 主键
 * @author liyi
 *
 */
public class RedisIdGenerator {

	private static final String BASE_KEY = "RID_";

	private StringRedisTemplate redisTemplate;

	private ValueOperations<String, String> opsValue;

	private Random random;

	public RedisIdGenerator() {
		random = new Random();
	}

	public StringRedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 生成ID <br>
	 * 默认增长步长   100 内随机
	 * @param key ID key
	 * @return
	 */
	public Long generatorId(String key) {
		return generatorId(key, 100);
	}
	
	/**
	 * 生成id
	 * @param key ID key
	 * @param rstep 增长随机范围
	 * @return
	 */

	public Long generatorId(String key, int rstep) {
		if (opsValue == null) {
			opsValue = redisTemplate.opsForValue();
		}

		long id = opsValue.increment(gid(key), random.nextInt(rstep) + 1);
		long curent_s = System.currentTimeMillis() / 1000;
		//小于系统时间
		if (id < curent_s) {
			return opsValue.increment(gid(key), curent_s - id);
		}
		return id;
	}

	private String gid(String key) {
		return BASE_KEY + key;
	}

	/**
	 * 获取一个已传入key和redis生成的自增的id_length位随机数 拼成的
	 * @param redisKey redisKey 为 存入redis的key,也是id的前缀
	 * @param id_length 定义id后缀拼几位自增数
	 * @return
	 */
	public String getAutoIncrId(String redisKey,int id_length) {
		long val = redisTemplate.opsForValue().increment(redisKey,1);
		if (val == 1){
			redisTemplate.expire(redisKey,1, TimeUnit.DAYS);
		}
		String id = String.format("%0"+id_length+"d", val);
		return redisKey+id;
	}

	public long getAutoIncrNum(String redisKey) {
		long val = redisTemplate.opsForValue().increment(redisKey,1);
		if (val == 1){
			redisTemplate.expire(redisKey,1, TimeUnit.DAYS);
		}
		return val;
	}
}
