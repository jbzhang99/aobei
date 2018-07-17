package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.PayAliNotify;
import com.aobei.train.mapper.PayAliNotifyMapper;
import com.aobei.train.model.PayAliNotifyExample;import com.aobei.train.service.PayAliNotifyService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class PayAliNotifyServiceImpl extends MbgServiceSupport<PayAliNotifyMapper, String, PayAliNotify, PayAliNotify, PayAliNotifyExample> implements PayAliNotifyService{

	@Autowired
	private PayAliNotifyMapper payAliNotifyMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(payAliNotifyMapper);
	}
}