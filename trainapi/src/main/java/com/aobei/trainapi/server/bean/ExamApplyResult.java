package com.aobei.trainapi.server.bean;

import com.aobei.train.model.Course;
/*import com.aobei.train.model.CourseTeam;*/
import com.aobei.train.model.ExamApply;
import com.aobei.train.model.ExamPlan;

public class ExamApplyResult implements ApiResult {

	private ExamPlan examPlan;

	private ExamApply examApply;

	private Course course;

	public ExamPlan getExamPlan() {
		return examPlan;
	}

	public void setExamPlan(ExamPlan examPlan) {
		this.examPlan = examPlan;
	}

	public ExamApply getExamApply() {
		return examApply;
	}

	public void setExamApply(ExamApply examApply) {
		this.examApply = examApply;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	

}
