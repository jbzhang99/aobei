package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Robbing;
import com.aobei.train.mapper.RobbingMapper;
import com.aobei.train.model.RobbingExample;import com.aobei.train.service.RobbingService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class RobbingServiceImpl extends MbgServiceSupport<RobbingMapper, Long, Robbing, Robbing, RobbingExample> implements RobbingService{

	@Autowired
	private RobbingMapper robbingMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(robbingMapper);
	}

	@Override
	public void xStartRobbing(String pay_order_id) {

	}
}