package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.ServiceUnit;
import com.aobei.train.mapper.ServiceUnitMapper;
import com.aobei.train.model.ServiceUnitExample;import com.aobei.train.service.ServiceUnitService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class ServiceUnitServiceImpl extends MbgServiceSupport<ServiceUnitMapper, Long, ServiceUnit, ServiceUnit, ServiceUnitExample> implements ServiceUnitService{

	@Autowired
	private ServiceUnitMapper serviceUnitMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(serviceUnitMapper);
	}
}