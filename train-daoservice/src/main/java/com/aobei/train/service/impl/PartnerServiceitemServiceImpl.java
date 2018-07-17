package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.PartnerServiceitem;
import com.aobei.train.mapper.PartnerServiceitemMapper;
import com.aobei.train.model.PartnerServiceitemExample;
import com.aobei.train.model.PartnerServiceitemKey;
import com.aobei.train.service.PartnerServiceitemService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class PartnerServiceitemServiceImpl extends MbgServiceSupport<PartnerServiceitemMapper, PartnerServiceitemKey, PartnerServiceitem, PartnerServiceitem, PartnerServiceitemExample> implements PartnerServiceitemService{

	@Autowired
	private PartnerServiceitemMapper partnerServiceitemMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(partnerServiceitemMapper);
	}
}