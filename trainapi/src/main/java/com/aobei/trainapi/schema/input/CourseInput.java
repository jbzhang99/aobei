package com.aobei.trainapi.schema.input;

import com.aobei.train.model.Course;

public class CourseInput extends Course{

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
