package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.FallintoRefund;
import com.aobei.train.mapper.FallintoRefundMapper;
import com.aobei.train.model.FallintoRefundExample;import com.aobei.train.service.FallintoRefundService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class FallintoRefundServiceImpl extends MbgServiceSupport<FallintoRefundMapper, Long, FallintoRefund, FallintoRefund, FallintoRefundExample> implements FallintoRefundService{

	@Autowired
	private FallintoRefundMapper fallintoRefundMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(fallintoRefundMapper);
	}

	@Override
	public String selectMaxBalanceCycle() {
		return fallintoRefundMapper.selectMaxBalanceCycle();

	}
}