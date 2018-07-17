package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.CourseEvaluate;
import com.aobei.train.mapper.CourseEvaluateMapper;
import com.aobei.train.model.CourseEvaluateExample;import com.aobei.train.service.CourseEvaluateService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class CourseEvaluateServiceImpl extends MbgServiceSupport<CourseEvaluateMapper, Long, CourseEvaluate, CourseEvaluate, CourseEvaluateExample> implements CourseEvaluateService{

	@Autowired
	private CourseEvaluateMapper courseEvaluateMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(courseEvaluateMapper);
	}





}