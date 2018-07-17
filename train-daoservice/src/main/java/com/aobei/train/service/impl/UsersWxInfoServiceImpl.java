package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.UsersWxInfo;
import com.aobei.train.mapper.UsersWxInfoMapper;
import com.aobei.train.model.UsersWxInfoExample;import com.aobei.train.service.UsersWxInfoService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class UsersWxInfoServiceImpl extends MbgServiceSupport<UsersWxInfoMapper, String, UsersWxInfo, UsersWxInfo, UsersWxInfoExample> implements UsersWxInfoService{

	@Autowired
	private UsersWxInfoMapper usersWxInfoMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(usersWxInfoMapper);
	}
}