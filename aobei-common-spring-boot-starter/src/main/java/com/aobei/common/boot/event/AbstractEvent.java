package com.aobei.common.boot.event;

import org.springframework.context.ApplicationEvent;

public abstract class AbstractEvent<T> extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private T data;
	
	private AbstractEvent(Object source) {
		super(source);
	}

	public AbstractEvent(Object source, T data) {
		this(source);
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
