package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.ExamPlan;
import com.aobei.train.mapper.ExamPlanMapper;
import com.aobei.train.model.ExamPlanExample;import com.aobei.train.service.ExamPlanService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class ExamPlanServiceImpl extends MbgServiceSupport<ExamPlanMapper, Long, ExamPlan, ExamPlan, ExamPlanExample> implements ExamPlanService{

	@Autowired
	private ExamPlanMapper examPlanMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(examPlanMapper);
	}
}