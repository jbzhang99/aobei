package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.ProSku;
import com.aobei.train.mapper.ProSkuMapper;
import com.aobei.train.model.ProSkuExample;import com.aobei.train.service.ProSkuService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class ProSkuServiceImpl extends MbgServiceSupport<ProSkuMapper, Long, ProSku, ProSku, ProSkuExample> implements ProSkuService{

	@Autowired
	private ProSkuMapper proSkuMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(proSkuMapper);
	}
}