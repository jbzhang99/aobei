package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.TeacherCourse;
import com.aobei.train.mapper.TeacherCourseMapper;
import com.aobei.train.model.TeacherCourseExample;
import com.aobei.train.model.TeacherCourseKey;
import com.aobei.train.service.TeacherCourseService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class TeacherCourseServiceImpl extends MbgServiceSupport<TeacherCourseMapper, TeacherCourseKey, TeacherCourse, TeacherCourse, TeacherCourseExample> implements TeacherCourseService{

	@Autowired
	private TeacherCourseMapper teacherCourseMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(teacherCourseMapper);
	}
}