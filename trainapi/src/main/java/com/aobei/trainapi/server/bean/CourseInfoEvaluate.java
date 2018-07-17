package com.aobei.trainapi.server.bean;

import com.aobei.train.model.CourseEvaluate;


public class CourseInfoEvaluate {

	TrainScheduleInfo trainScheduleInfo;

	CourseEvaluate courseEvaluate;

	public TrainScheduleInfo getTrainScheduleInfo() {
		return trainScheduleInfo;
	}

	public void setTrainScheduleInfo(TrainScheduleInfo trainScheduleInfo) {
		this.trainScheduleInfo = trainScheduleInfo;
	}

	public CourseEvaluate getCourseEvaluate() {
		return courseEvaluate;
	}

	public void setCourseEvaluate(CourseEvaluate courseEvaluate) {
		this.courseEvaluate = courseEvaluate;
	}

}
