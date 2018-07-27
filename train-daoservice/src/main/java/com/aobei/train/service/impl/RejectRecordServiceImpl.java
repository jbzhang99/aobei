package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.RejectRecord;
import com.aobei.train.mapper.RejectRecordMapper;
import com.aobei.train.model.RejectRecordExample;import com.aobei.train.service.RejectRecordService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class RejectRecordServiceImpl extends MbgServiceSupport<RejectRecordMapper, Long, RejectRecord, RejectRecord, RejectRecordExample> implements RejectRecordService{

	@Autowired
	private RejectRecordMapper rejectRecordMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(rejectRecordMapper);
	}
}