package com.aobei.common.boot.event;

import com.aobei.common.bean.SmsData;

/**
 * 
 * 短信发送事件
 * @author liyi
 *
 */
public class SmsSendEvent extends AbstractEvent<SmsData>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6655325049711308697L;

	public SmsSendEvent(Object source, SmsData data) {
		super(source, data);
	}

}