package com.aobei.trainapi.schema.type;

import java.util.List;

public class PageResult {

	// 总记录条数
	private long total;

	private int count;

	private String next_id;
	private String pre_id;
	
	private List<?> data;
	
	@SuppressWarnings("unchecked")
	public <T> List<T> result(){
		return (List<T>) data;
	}

	public PageResult() {
		super();
	}
	
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getNext_id() {
		return next_id;
	}

	public void setNext_id(String next_id) {
		this.next_id = next_id;
	}

	public String getPre_id() {
		return pre_id;
	}

	public void setPre_id(String pre_id) {
		this.pre_id = pre_id;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}
	
}
