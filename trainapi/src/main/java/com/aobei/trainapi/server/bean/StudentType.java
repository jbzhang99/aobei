package com.aobei.trainapi.server.bean;

import com.aobei.train.model.Student;

public class StudentType implements Comparable<StudentType>{

	private Student student;
	private Integer type;
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Override
	public int compareTo(StudentType o) {
		return this.getType().compareTo(o.getType());
	}
	
	
	
}
