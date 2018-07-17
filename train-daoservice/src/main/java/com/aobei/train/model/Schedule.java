package com.aobei.train.model;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
	
	private String w;
	private List<Se> se = new ArrayList<>();
	
	public String getW() {
		return w;
	}
	public void setW(String w) {
		this.w = w;
	}
	public List<Se> getSe() {
		return se;
	}
	public void setSe(List<Se> se) {
		this.se = se;
	}
	
	
}

