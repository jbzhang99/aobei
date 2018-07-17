package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.BillBatch;
import com.aobei.train.mapper.BillBatchMapper;
import com.aobei.train.model.BillBatchExample;import com.aobei.train.service.BillBatchService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class BillBatchServiceImpl extends MbgServiceSupport<BillBatchMapper, String, BillBatch, BillBatch, BillBatchExample> implements BillBatchService{

	@Autowired
	private BillBatchMapper billBatchMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(billBatchMapper);
	}
}