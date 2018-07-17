package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.AuthRoleUser;
import com.aobei.train.mapper.AuthRoleUserMapper;
import com.aobei.train.model.AuthRoleUserExample;
import com.aobei.train.model.AuthRoleUserKey;
import com.aobei.train.service.AuthRoleUserService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class AuthRoleUserServiceImpl extends MbgServiceSupport<AuthRoleUserMapper, AuthRoleUserKey, AuthRoleUser, AuthRoleUser, AuthRoleUserExample> implements AuthRoleUserService{

	@Autowired
	private AuthRoleUserMapper authRoleUserMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(authRoleUserMapper);
	}
}