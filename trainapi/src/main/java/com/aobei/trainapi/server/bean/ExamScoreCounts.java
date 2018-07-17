package com.aobei.trainapi.server.bean;

/**
 * 
 * 考试成绩统计
 * 
 * @author liyi
 *
 */
public class ExamScoreCounts {

	private long total;

	private long passed;

	private long failed;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getPassed() {
		return passed;
	}

	public void setPassed(long passed) {
		this.passed = passed;
	}

	public long getFailed() {
		return failed;
	}

	public void setFailed(long failed) {
		this.failed = failed;
	}

}
