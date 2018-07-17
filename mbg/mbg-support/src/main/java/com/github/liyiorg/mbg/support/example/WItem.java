package com.github.liyiorg.mbg.support.example;

/**
 * 
 * @author LiYi
 *
 */
public class WItem<T> {

	private String name;

	private String delimitedAliasName;

	private int jdbcType;

	public WItem(String name, String delimitedAliasName, int jdbcType) {
		this.name = name;
		this.delimitedAliasName = delimitedAliasName;
		this.jdbcType = jdbcType;
	}

	public static <C> WItem<C> warp(CInterface item) {
		return new WItem<C>(item.name(), item.delimitedAliasName(), item.getJdbcType());
	}

	public String name() {
		return name;
	}

	public String delimitedAliasName() {
		return delimitedAliasName;
	}

	public int getJdbcType() {
		return jdbcType;
	}
}
