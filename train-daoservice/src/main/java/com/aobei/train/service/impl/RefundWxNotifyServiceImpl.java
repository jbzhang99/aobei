package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.RefundWxNotify;
import com.aobei.train.mapper.RefundWxNotifyMapper;
import com.aobei.train.model.RefundWxNotifyExample;import com.aobei.train.service.RefundWxNotifyService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class RefundWxNotifyServiceImpl extends MbgServiceSupport<RefundWxNotifyMapper, String, RefundWxNotify, RefundWxNotify, RefundWxNotifyExample> implements RefundWxNotifyService{

	@Autowired
	private RefundWxNotifyMapper refundWxNotifyMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(refundWxNotifyMapper);
	}
}