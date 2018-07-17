package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.FallintoDeductMoney;
import com.aobei.train.mapper.FallintoDeductMoneyMapper;
import com.aobei.train.model.FallintoDeductMoneyExample;import com.aobei.train.service.FallintoDeductMoneyService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class FallintoDeductMoneyServiceImpl extends MbgServiceSupport<FallintoDeductMoneyMapper, Long, FallintoDeductMoney, FallintoDeductMoney, FallintoDeductMoneyExample> implements FallintoDeductMoneyService{

	@Autowired
	private FallintoDeductMoneyMapper fallintoDeductMoneyMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(fallintoDeductMoneyMapper);
	}

	@Override
	public String selectMaxBalanceCycle() {
		return fallintoDeductMoneyMapper.selectMaxBalanceCycle();
	}
}