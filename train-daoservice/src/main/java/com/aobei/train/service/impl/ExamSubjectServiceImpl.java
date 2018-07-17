package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.ExamSubject;
import com.aobei.train.mapper.ExamSubjectMapper;
import com.aobei.train.model.ExamSubjectExample;import com.aobei.train.service.ExamSubjectService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class ExamSubjectServiceImpl extends MbgServiceSupport<ExamSubjectMapper, Long, ExamSubject, ExamSubject, ExamSubjectExample> implements ExamSubjectService{

	@Autowired
	private ExamSubjectMapper examSubjectMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(examSubjectMapper);
	}
}