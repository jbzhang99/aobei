package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Metadata;
import com.aobei.train.mapper.MetadataMapper;
import com.aobei.train.model.MetadataExample;import com.aobei.train.service.MetadataService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class MetadataServiceImpl extends MbgServiceSupport<MetadataMapper, String, Metadata, Metadata, MetadataExample> implements MetadataService{

	@Autowired
	private MetadataMapper metadataMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(metadataMapper);
	}
}