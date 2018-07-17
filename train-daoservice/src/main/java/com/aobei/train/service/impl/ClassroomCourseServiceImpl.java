package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.ClassroomCourse;
import com.aobei.train.mapper.ClassroomCourseMapper;
import com.aobei.train.model.ClassroomCourseExample;
import com.aobei.train.model.ClassroomCourseKey;
import com.aobei.train.service.ClassroomCourseService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class ClassroomCourseServiceImpl extends MbgServiceSupport<ClassroomCourseMapper, ClassroomCourseKey, ClassroomCourse, ClassroomCourse, ClassroomCourseExample> implements ClassroomCourseService{

	@Autowired
	private ClassroomCourseMapper classroomCourseMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(classroomCourseMapper);
	}
}