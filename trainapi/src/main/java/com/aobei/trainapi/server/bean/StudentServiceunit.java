package com.aobei.trainapi.server.bean;

import java.util.List;

import com.aobei.train.model.Student;

import custom.bean.OrderInfo;

public class StudentServiceunit {

	private Student student;
	private List<OrderInfo> orderList;
	
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public List<OrderInfo> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<OrderInfo> orderList) {
		this.orderList = orderList;
	}
	
	
	
	
}
