package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Fallinto;
import com.aobei.train.mapper.FallintoMapper;
import com.aobei.train.model.FallintoExample;import com.aobei.train.service.FallintoService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class FallintoServiceImpl extends MbgServiceSupport<FallintoMapper, Long, Fallinto, Fallinto, FallintoExample> implements FallintoService{

	@Autowired
	private FallintoMapper fallintoMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(fallintoMapper);
	}
}