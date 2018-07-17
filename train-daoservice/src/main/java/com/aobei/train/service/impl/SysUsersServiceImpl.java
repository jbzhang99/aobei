package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.SysUsers;
import com.aobei.train.mapper.SysUsersMapper;
import com.aobei.train.model.SysUsersExample;import com.aobei.train.service.SysUsersService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class SysUsersServiceImpl extends MbgServiceSupport<SysUsersMapper, Long, SysUsers, SysUsers, SysUsersExample> implements SysUsersService{

	@Autowired
	private SysUsersMapper sysUsersMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(sysUsersMapper);
	}
}