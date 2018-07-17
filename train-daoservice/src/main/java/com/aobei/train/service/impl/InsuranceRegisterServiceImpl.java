package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.InsuranceRegister;
import com.aobei.train.mapper.InsuranceRegisterMapper;
import com.aobei.train.model.InsuranceRegisterExample;import com.aobei.train.service.InsuranceRegisterService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class InsuranceRegisterServiceImpl extends MbgServiceSupport<InsuranceRegisterMapper, Long, InsuranceRegister, InsuranceRegister, InsuranceRegisterExample> implements InsuranceRegisterService{

	@Autowired
	private InsuranceRegisterMapper insuranceRegisterMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(insuranceRegisterMapper);
	}
}