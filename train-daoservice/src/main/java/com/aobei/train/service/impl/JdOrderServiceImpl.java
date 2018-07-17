package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.JdOrder;
import com.aobei.train.mapper.JdOrderMapper;
import com.aobei.train.model.JdOrderExample;import com.aobei.train.service.JdOrderService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class JdOrderServiceImpl extends MbgServiceSupport<JdOrderMapper, String, JdOrder, JdOrder, JdOrderExample> implements JdOrderService{

	@Autowired
	private JdOrderMapper jdOrderMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(jdOrderMapper);
	}
}