package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Refund;
import com.aobei.train.mapper.RefundMapper;
import com.aobei.train.model.RefundExample;import com.aobei.train.service.RefundService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class RefundServiceImpl extends MbgServiceSupport<RefundMapper, Long, Refund, Refund, RefundExample> implements RefundService{

	@Autowired
	private RefundMapper refundMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(refundMapper);
	}
}