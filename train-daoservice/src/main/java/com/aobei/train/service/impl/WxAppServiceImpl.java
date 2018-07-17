package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.WxApp;
import com.aobei.train.mapper.WxAppMapper;
import com.aobei.train.model.WxAppExample;import com.aobei.train.service.WxAppService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class WxAppServiceImpl extends MbgServiceSupport<WxAppMapper, String, WxApp, WxApp, WxAppExample> implements WxAppService{

	@Autowired
	private WxAppMapper wxAppMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(wxAppMapper);
	}
}