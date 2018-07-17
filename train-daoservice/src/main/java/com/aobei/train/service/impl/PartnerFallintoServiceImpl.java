package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.PartnerFallinto;
import com.aobei.train.mapper.PartnerFallintoMapper;
import com.aobei.train.model.PartnerFallintoExample;import com.aobei.train.service.PartnerFallintoService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class PartnerFallintoServiceImpl extends MbgServiceSupport<PartnerFallintoMapper, Long, PartnerFallinto, PartnerFallinto, PartnerFallintoExample> implements PartnerFallintoService{

	@Autowired
	private PartnerFallintoMapper partnerFallintoMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(partnerFallintoMapper);
	}
}