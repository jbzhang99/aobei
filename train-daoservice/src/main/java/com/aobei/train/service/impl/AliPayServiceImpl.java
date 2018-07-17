package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.AliPay;
import com.aobei.train.mapper.AliPayMapper;
import com.aobei.train.model.AliPayExample;import com.aobei.train.service.AliPayService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class AliPayServiceImpl extends MbgServiceSupport<AliPayMapper, String, AliPay, AliPay, AliPayExample> implements AliPayService{

	@Autowired
	private AliPayMapper aliPayMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(aliPayMapper);
	}
}