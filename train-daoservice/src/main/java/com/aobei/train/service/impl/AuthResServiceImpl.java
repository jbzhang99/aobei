package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.AuthRes;
import com.aobei.train.mapper.AuthResMapper;
import com.aobei.train.model.AuthResExample;import com.aobei.train.service.AuthResService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class AuthResServiceImpl extends MbgServiceSupport<AuthResMapper, Integer, AuthRes, AuthRes, AuthResExample> implements AuthResService{

	@Autowired
	private AuthResMapper authResMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(authResMapper);
	}
}