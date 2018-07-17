package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.PayWxNotify;
import com.aobei.train.mapper.PayWxNotifyMapper;
import com.aobei.train.model.PayWxNotifyExample;import com.aobei.train.service.PayWxNotifyService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class PayWxNotifyServiceImpl extends MbgServiceSupport<PayWxNotifyMapper, String, PayWxNotify, PayWxNotify, PayWxNotifyExample> implements PayWxNotifyService{

	@Autowired
	private PayWxNotifyMapper payWxNotifyMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(payWxNotifyMapper);
	}
}