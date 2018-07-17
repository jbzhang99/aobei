package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.FallintoCompensation;
import com.aobei.train.mapper.FallintoCompensationMapper;
import com.aobei.train.model.FallintoCompensationExample;import com.aobei.train.service.FallintoCompensationService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class FallintoCompensationServiceImpl extends MbgServiceSupport<FallintoCompensationMapper, Long, FallintoCompensation, FallintoCompensation, FallintoCompensationExample> implements FallintoCompensationService{

	@Autowired
	private FallintoCompensationMapper fallintoCompensationMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(fallintoCompensationMapper);
	}

	@Override
	public String selectMaxBalanceCycle() {
		return fallintoCompensationMapper.selectMaxBalanceCycle();
	}
}