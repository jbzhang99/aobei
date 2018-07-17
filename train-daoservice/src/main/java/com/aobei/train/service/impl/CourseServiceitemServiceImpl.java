package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.CourseServiceitem;
import com.aobei.train.mapper.CourseServiceitemMapper;
import com.aobei.train.model.CourseServiceitemExample;
import com.aobei.train.model.CourseServiceitemKey;
import com.aobei.train.service.CourseServiceitemService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class CourseServiceitemServiceImpl extends MbgServiceSupport<CourseServiceitemMapper, CourseServiceitemKey, CourseServiceitem, CourseServiceitem, CourseServiceitemExample> implements CourseServiceitemService{

	@Autowired
	private CourseServiceitemMapper courseServiceitemMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(courseServiceitemMapper);
	}
}