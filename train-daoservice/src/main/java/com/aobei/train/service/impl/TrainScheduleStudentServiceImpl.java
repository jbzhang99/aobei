package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.TrainScheduleStudent;
import com.aobei.train.mapper.TrainScheduleStudentMapper;
import com.aobei.train.model.TrainScheduleStudentExample;
import com.aobei.train.model.TrainScheduleStudentKey;
import com.aobei.train.service.TrainScheduleStudentService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class TrainScheduleStudentServiceImpl extends MbgServiceSupport<TrainScheduleStudentMapper, TrainScheduleStudentKey, TrainScheduleStudent, TrainScheduleStudent, TrainScheduleStudentExample> implements TrainScheduleStudentService{

	@Autowired
	private TrainScheduleStudentMapper trainScheduleStudentMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(trainScheduleStudentMapper);
	}
}