package com.aobei.trainapi.server.bean;

import com.aobei.train.model.Chapter;
import com.aobei.train.model.Classroom;
import com.aobei.train.model.Course;
import com.aobei.train.model.Plan;
import com.aobei.train.model.School;
import com.aobei.train.model.Teacher;
import com.aobei.train.model.TrainSchedule;
import com.aobei.train.model.TrainScheduleStudent;

/**
 * 上课排期信息
 * 
 * @author liyi
 *
 */
public class TrainScheduleInfo {

	private TrainSchedule trainSchedule;

	private Course course;

	private Teacher teacher;

	private School school;

	private Classroom classroom;

	private Plan plan;
	
	private Chapter chapter;
	
	

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public TrainSchedule getTrainSchedule() {
		return trainSchedule;
	}

	public void setTrainSchedule(TrainSchedule trainSchedule) {
		this.trainSchedule = trainSchedule;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Teacher getTeacherInfo() {
		return teacher;
	}

	public void setTeacherInfo(Teacher teacher) {
		this.teacher = teacher;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Classroom getClassroom() {
		return classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	

}
