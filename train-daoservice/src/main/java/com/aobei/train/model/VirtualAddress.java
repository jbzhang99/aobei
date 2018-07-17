package com.aobei.train.model;

import java.util.Date;

public class VirtualAddress {
	
	private String id;
	


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String address;
	
	private Date upline;
	
	private Date online;

	private String addressV;
	
	public String getAddressV() {
		return addressV;
	}

	public void setAddressV(String addressV) {
		this.addressV = addressV;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getUpline() {
		return upline;
	}

	public void setUpline(Date upline) {
		this.upline = upline;
	}

	public Date getOnline() {
		return online;
	}

	public void setOnline(Date online) {
		this.online = online;
	}

	

	public VirtualAddress(String address, Date upline, Date online, String addressV) {
		super();
		this.address = address;
		this.upline = upline;
		this.online = online;
		this.addressV = addressV;
	}

	public VirtualAddress() {
		super();
	}

}
