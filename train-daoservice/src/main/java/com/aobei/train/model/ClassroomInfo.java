package com.aobei.train.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClassroomInfo extends Classroom implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String school_name;
	private List<String> courses = new ArrayList<>();
	
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
	public List<String> getCourses() {
		return courses;
	}
	public void setCourses(List<String> courses) {
		this.courses = courses;
	}
	
	@Override
	public String toString() {
		return "ClassroomInfo [school_name=" + school_name + ", courses=" + courses + "]";
	}
	
}
