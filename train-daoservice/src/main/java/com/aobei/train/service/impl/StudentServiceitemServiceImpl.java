package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.StudentServiceitem;
import com.aobei.train.mapper.StudentServiceitemMapper;
import com.aobei.train.model.StudentServiceitemExample;
import com.aobei.train.model.StudentServiceitemKey;
import com.aobei.train.service.StudentServiceitemService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class StudentServiceitemServiceImpl extends MbgServiceSupport<StudentServiceitemMapper, StudentServiceitemKey, StudentServiceitem, StudentServiceitem, StudentServiceitemExample> implements StudentServiceitemService{

	@Autowired
	private StudentServiceitemMapper studentServiceitemMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(studentServiceitemMapper);
	}
}