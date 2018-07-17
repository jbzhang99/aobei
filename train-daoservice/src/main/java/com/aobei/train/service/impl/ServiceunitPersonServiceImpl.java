package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.ServiceunitPerson;
import com.aobei.train.mapper.ServiceunitPersonMapper;
import com.aobei.train.model.ServiceunitPersonExample;import com.aobei.train.service.ServiceunitPersonService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class ServiceunitPersonServiceImpl extends MbgServiceSupport<ServiceunitPersonMapper, Long, ServiceunitPerson, ServiceunitPerson, ServiceunitPersonExample> implements ServiceunitPersonService{

	@Autowired
	private ServiceunitPersonMapper serviceunitPersonMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(serviceunitPersonMapper);
	}
}