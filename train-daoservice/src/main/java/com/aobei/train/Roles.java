package com.aobei.train;

public enum Roles {

	/** 培训经理 */		TMANAGER,
	/** 质量监控 */		ZLJC,
	/** 合伙人 */		PARTNER,
	/** 合伙人用户*/     PARTNER_USER,
	/** 老师 */			TEACHER,
	/** 学生 */			STUDENT,
	/** 顾客 */         CUSTOMER;
	
	/**
	 * 获取角色名
	 * @return
	 */
	public String roleName(){
		return String.format("ROLE_%s", name());
	}
}
