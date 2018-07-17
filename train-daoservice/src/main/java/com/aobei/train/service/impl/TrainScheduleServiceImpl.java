package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.TrainSchedule;
import com.aobei.train.mapper.TrainScheduleMapper;
import com.aobei.train.model.TrainScheduleExample;import com.aobei.train.service.TrainScheduleService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class TrainScheduleServiceImpl extends MbgServiceSupport<TrainScheduleMapper, Long, TrainSchedule, TrainSchedule, TrainScheduleExample> implements TrainScheduleService{

	@Autowired
	private TrainScheduleMapper trainScheduleMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(trainScheduleMapper);
	}
}