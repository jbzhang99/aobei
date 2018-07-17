package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.OutOfService;
import com.aobei.train.mapper.OutOfServiceMapper;
import com.aobei.train.model.OutOfServiceExample;import com.aobei.train.service.OutOfServiceService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class OutOfServiceServiceImpl extends MbgServiceSupport<OutOfServiceMapper, Long, OutOfService, OutOfService, OutOfServiceExample> implements OutOfServiceService{

	@Autowired
	private OutOfServiceMapper outOfServiceMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(outOfServiceMapper);
	}
}