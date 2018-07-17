package com.aobei.trainapi.server.bean;

import com.aobei.train.model.Course;
import com.aobei.train.model.Plan;
import com.aobei.train.model.Teacher;

/**
 * 培训信息
 * @author liyi
 *
 */
public class TrainCourseInfo {

	private Course course;
	
	private Plan plan;
	
	private Teacher teacher;
	

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	
}
