package com.aobei.trainapi.server.bean;

import java.util.List;

import com.aobei.train.model.Course;
import com.aobei.train.model.ExamApply;
import com.aobei.train.model.Plan;
import com.aobei.train.model.PlanStudent;

/**
 * 已培训课程信息
 * 
 * @author liyi
 *
 */
public class TrainedInfo {

	private Course course;

	private Plan plan;

	private PlanStudent planStudent;
	
	private List<ExamApply> examApplys;
	


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

	public PlanStudent getPlanStudent() {
		return planStudent;
	}

	public void setPlanStudent(PlanStudent planStudent) {
		this.planStudent = planStudent;
	}

	public List<ExamApply> getExamApplys() {
		return examApplys;
	}

	public void setExamApplys(List<ExamApply> examApplys) {
		this.examApplys = examApplys;
	}
	
}
