package com.aobei.authserver.util;

import com.github.ddth.id.SnowflakeIdGenerator;

public class IdGenerator {

	private static SnowflakeIdGenerator idGenerator = SnowflakeIdGenerator.getInstance();

	public static long generateId() {
		return idGenerator.generateId64();
	}
}
