package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.AuthRoleRes;
import com.aobei.train.mapper.AuthRoleResMapper;
import com.aobei.train.model.AuthRoleResExample;
import com.aobei.train.model.AuthRoleResKey;
import com.aobei.train.service.AuthRoleResService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class AuthRoleResServiceImpl extends MbgServiceSupport<AuthRoleResMapper, AuthRoleResKey, AuthRoleRes, AuthRoleRes, AuthRoleResExample> implements AuthRoleResService{

	@Autowired
	private AuthRoleResMapper authRoleResMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(authRoleResMapper);
	}
}