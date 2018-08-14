package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Business;
import com.aobei.train.mapper.BusinessMapper;
import com.aobei.train.model.BusinessExample;import com.aobei.train.service.BusinessService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class BusinessServiceImpl extends MbgServiceSupport<BusinessMapper, String, Business, Business, BusinessExample> implements BusinessService{

	@Autowired
	private BusinessMapper businessMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(businessMapper);
	}
}