package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.FallintoFineMoney;
import com.aobei.train.mapper.FallintoFineMoneyMapper;
import com.aobei.train.model.FallintoFineMoneyExample;import com.aobei.train.service.FallintoFineMoneyService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class FallintoFineMoneyServiceImpl extends MbgServiceSupport<FallintoFineMoneyMapper, Long, FallintoFineMoney, FallintoFineMoney, FallintoFineMoneyExample> implements FallintoFineMoneyService{

	@Autowired
	private FallintoFineMoneyMapper fallintoFineMoneyMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(fallintoFineMoneyMapper);
	}

	@Override
	public String selectMaxBalanceCycle() {
		return fallintoFineMoneyMapper.selectMaxBalanceCycle();
	}
}