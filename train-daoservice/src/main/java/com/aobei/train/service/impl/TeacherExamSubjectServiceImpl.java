package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.TeacherExamSubject;
import com.aobei.train.mapper.TeacherExamSubjectMapper;
import com.aobei.train.model.TeacherExamSubjectExample;
import com.aobei.train.model.TeacherExamSubjectKey;
import com.aobei.train.service.TeacherExamSubjectService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class TeacherExamSubjectServiceImpl extends MbgServiceSupport<TeacherExamSubjectMapper, TeacherExamSubjectKey, TeacherExamSubject, TeacherExamSubject, TeacherExamSubjectExample> implements TeacherExamSubjectService{

	@Autowired
	private TeacherExamSubjectMapper teacherExamSubjectMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(teacherExamSubjectMapper);
	}
}