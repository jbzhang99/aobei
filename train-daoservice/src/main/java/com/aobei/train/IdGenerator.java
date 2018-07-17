package com.aobei.train;

import com.github.ddth.id.SnowflakeIdGenerator;

public class IdGenerator {

	private static SnowflakeIdGenerator idGenerator = SnowflakeIdGenerator.getInstance();

	public static long generateId() {
		return idGenerator.generateId64();
	}
	
	public static long generateId48() {
		return idGenerator.generateId48();
	}
	
	public static long generateIdMini() {
		return idGenerator.generateIdMini();
	}

}
