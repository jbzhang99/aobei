package com.aobei.trainapi.server.bean;

/**
 * 学习中的培训信息
 * 
 * @author liyi
 *
 */
public class StudingTrainCourseInfo extends TrainCourseInfo {

	/**
	 * 已学课程小时
	 */
	private int studed;

	public int getStuded() {
		return studed;
	}

	public void setStuded(int studed) {
		this.studed = studed;
	}

}
