package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.OssVideo;
import com.aobei.train.mapper.OssVideoMapper;
import com.aobei.train.model.OssVideoExample;import com.aobei.train.service.OssVideoService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class OssVideoServiceImpl extends MbgServiceSupport<OssVideoMapper, Long, OssVideo, OssVideo, OssVideoExample> implements OssVideoService{

	@Autowired
	private OssVideoMapper ossVideoMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(ossVideoMapper);
	}
}