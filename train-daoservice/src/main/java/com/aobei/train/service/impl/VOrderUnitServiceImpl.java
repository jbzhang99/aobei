package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.VOrderUnit;
import com.aobei.train.mapper.VOrderUnitMapper;
import com.aobei.train.model.VOrderUnitExample;
import com.github.liyiorg.mbg.support.model.NoKey;
import com.aobei.train.service.VOrderUnitService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class VOrderUnitServiceImpl extends MbgServiceSupport<VOrderUnitMapper, NoKey, VOrderUnit, VOrderUnit, VOrderUnitExample> implements VOrderUnitService{

	@Autowired
	private VOrderUnitMapper vOrderUnitMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(vOrderUnitMapper);
	}
}