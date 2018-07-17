package com.aobei.common.boot;

import org.springframework.context.ApplicationContext;

import com.aobei.common.boot.event.AbstractEvent;

public class EventPublisher {

	private ApplicationContext applicationContext;
	
	public EventPublisher(ApplicationContext applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}

	/**
	 * 发布
	 * @param t
	 */
	public <T extends AbstractEvent<?>> void publish(T t) {
		applicationContext.publishEvent(t);
	}
}
