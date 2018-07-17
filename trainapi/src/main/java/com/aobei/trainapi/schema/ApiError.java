package com.aobei.trainapi.schema;

public class ApiError extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 821868658692893551L;

	public ApiError(String errcode, String errmsg) {
		super(String.format("[errcode:%s] [errmsg:%s]", errcode, errmsg));
	}
	
}
