package com.aobei.trainapi.server.bean;

import java.util.Date;

/**
 * 本月累计订单
 * @author 15010
 *
 */
public class AccumulatedOrdersMonth {
	//订单数量
	private Long count;
	//当前日期
	private Date date;
	//本月天数
	private Integer daysNum; 
	

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getDaysNum() {
		return daysNum;
	}

	public void setDaysNum(Integer daysNum) {
		this.daysNum = daysNum;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	};

	
	
	
}
