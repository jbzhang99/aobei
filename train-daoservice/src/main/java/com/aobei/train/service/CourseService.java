package com.aobei.train.service;

import com.aobei.train.model.CourseExample;

import java.util.List;
import java.util.Map;

import com.aobei.train.model.Category;
import com.aobei.train.model.Course;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;

import custom.bean.CourseToCategory;

import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CourseService extends MbgReadService<Long, Course, Course, CourseExample>,MbgWriteService<Long, Course, Course, CourseExample>,MbgUpsertService<Long, Course, Course, CourseExample>{

	List xShowCourseTeam(List<Course> cList);

	List<Category> xCreateTeamShow();

	int xAddCourseTeam(Map<String, Object> map, Map<String, String> params);

	Course xEditCourseTeam(Map<String, Object> map,Long course_id);

	Boolean xDelCourseByTeamId(Long course_id);

	List<CourseToCategory> xFeng(Long courseid);

}