package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.OperateLog;
import com.aobei.train.mapper.OperateLogMapper;
import com.aobei.train.model.OperateLogExample;import com.aobei.train.service.OperateLogService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class OperateLogServiceImpl extends MbgServiceSupport<OperateLogMapper, Long, OperateLog, OperateLog, OperateLogExample> implements OperateLogService{

	@Autowired
	private OperateLogMapper operateLogMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(operateLogMapper);
	}
}