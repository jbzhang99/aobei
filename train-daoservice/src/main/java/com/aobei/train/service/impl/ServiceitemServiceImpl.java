package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Serviceitem;
import com.aobei.train.mapper.ServiceitemMapper;
import com.aobei.train.model.ServiceitemExample;import com.aobei.train.service.ServiceitemService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class ServiceitemServiceImpl extends MbgServiceSupport<ServiceitemMapper, Long, Serviceitem, Serviceitem, ServiceitemExample> implements ServiceitemService{

	@Autowired
	private ServiceitemMapper serviceitemMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(serviceitemMapper);
	}
}