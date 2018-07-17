package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.CourseExamSubject;
import com.aobei.train.mapper.CourseExamSubjectMapper;
import com.aobei.train.model.CourseExamSubjectExample;
import com.aobei.train.model.CourseExamSubjectKey;
import com.aobei.train.service.CourseExamSubjectService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class CourseExamSubjectServiceImpl extends MbgServiceSupport<CourseExamSubjectMapper, CourseExamSubjectKey, CourseExamSubject, CourseExamSubject, CourseExamSubjectExample> implements CourseExamSubjectService{

	@Autowired
	private CourseExamSubjectMapper courseExamSubjectMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(courseExamSubjectMapper);
	}
}