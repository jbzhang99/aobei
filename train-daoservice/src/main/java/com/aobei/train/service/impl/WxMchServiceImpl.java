package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.WxMch;
import com.aobei.train.mapper.WxMchMapper;
import com.aobei.train.model.WxMchExample;import com.aobei.train.service.WxMchService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class WxMchServiceImpl extends MbgServiceSupport<WxMchMapper, String, WxMch, WxMch, WxMchExample> implements WxMchService{

	@Autowired
	private WxMchMapper wxMchMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(wxMchMapper);
	}
}