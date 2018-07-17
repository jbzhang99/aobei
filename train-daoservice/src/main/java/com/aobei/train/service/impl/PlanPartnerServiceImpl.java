package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.PlanPartner;
import com.aobei.train.mapper.PlanPartnerMapper;
import com.aobei.train.model.PlanPartnerExample;
import com.aobei.train.model.PlanPartnerKey;
import com.aobei.train.service.PlanPartnerService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class PlanPartnerServiceImpl extends MbgServiceSupport<PlanPartnerMapper, PlanPartnerKey, PlanPartner, PlanPartner, PlanPartnerExample> implements PlanPartnerService{

	@Autowired
	private PlanPartnerMapper planPartnerMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(planPartnerMapper);
	}
}