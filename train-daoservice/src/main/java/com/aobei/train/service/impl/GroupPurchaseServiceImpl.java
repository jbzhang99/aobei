package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.GroupPurchase;
import com.aobei.train.mapper.GroupPurchaseMapper;
import com.aobei.train.model.GroupPurchaseExample;import com.aobei.train.service.GroupPurchaseService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class GroupPurchaseServiceImpl extends MbgServiceSupport<GroupPurchaseMapper, Long, GroupPurchase, GroupPurchase, GroupPurchaseExample> implements GroupPurchaseService{

	@Autowired
	private GroupPurchaseMapper groupPurchaseMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(groupPurchaseMapper);
	}
}