package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.OssFile;
import com.aobei.train.mapper.OssFileMapper;
import com.aobei.train.model.OssFileExample;import com.aobei.train.service.OssFileService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class OssFileServiceImpl extends MbgServiceSupport<OssFileMapper, Long, OssFile, OssFile, OssFileExample> implements OssFileService{

	@Autowired
	private OssFileMapper ossFileMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(ossFileMapper);
	}
}