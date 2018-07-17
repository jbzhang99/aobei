package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Msgtext;
import com.aobei.train.mapper.MsgtextMapper;
import com.aobei.train.model.MsgtextExample;import com.aobei.train.service.MsgtextService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class MsgtextServiceImpl extends MbgServiceSupport<MsgtextMapper, String, Msgtext, Msgtext, MsgtextExample> implements MsgtextService{

	@Autowired
	private MsgtextMapper msgtextMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(msgtextMapper);
	}
}