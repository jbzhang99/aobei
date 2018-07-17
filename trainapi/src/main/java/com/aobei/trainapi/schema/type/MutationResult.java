package com.aobei.trainapi.schema.type;

public class MutationResult {
	
	private int status;

	private String  extra;
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
}
