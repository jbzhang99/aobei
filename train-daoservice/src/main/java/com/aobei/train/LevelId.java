package com.aobei.train;

import java.math.BigInteger;

public class LevelId {

	private int level = 4;

	private BigInteger self;

	private long nextC = 1;

	public LevelId() {
		level = 4;
		self = BigInteger.valueOf(0);
	}

	public LevelId(int level, String self) {
		this.level = level;
		this.self = new BigInteger(self);
	}

	public String next() {
		self = self.add(BigInteger.valueOf(1));
		return format(level, self.toString());
	}

	public String nextChildren() {
		return format(level, self.toString()) + format(level, String.valueOf(nextC++));
	}

	/**
	 * 格式化id
	 * 
	 * @param level 等级
	 * @param id id 字符串
	 * @return
	 */
	public static String format(int level, String id) {
		String tid = id;
		int t = id.length() % level;
		if (t > 0) {
			for (int i = 0; i < level - t; i++) {
				tid = "0" + tid;
			}
		}
		return tid;
	}

}
