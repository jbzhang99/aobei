package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.AppPack;
import com.aobei.train.mapper.AppPackMapper;
import com.aobei.train.model.AppPackExample;import com.aobei.train.service.AppPackService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppPackServiceImpl extends MbgServiceSupport<AppPackMapper, String, AppPack, AppPack, AppPackExample> implements AppPackService{

	@Autowired
	private AppPackMapper appPackMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(appPackMapper);
	}

	@Override
	public List<AppPack> xGetPort(String group_name) {
		AppPackExample appPackExample = new AppPackExample();
		appPackExample.or()
				.andGroup_nameEqualTo(group_name);
		List<AppPack> appPacks = this.appPackMapper.selectByExample(appPackExample);
		List<AppPack> list = appPacks.stream().map(n->{
			AppPack appPack = new AppPack();
			//appPack.setApp_pack_id(n.getApp_pack_id());
			//appPack.setApp_pack_name(n.getApp_pack_name().contains("合伙人端")?n.getApp_pack_name().substring(4,n.getApp_pack_name().length()):n.getApp_pack_name().substring(3,n.getApp_pack_name().length()));
			if("wx_m_custom".equals(n.getApp_pack_id())){
				appPack.setApp_pack_id(n.getApp_pack_id());
				appPack.setApp_pack_name(n.getApp_pack_name());
				return appPack;
			}else{
				return null;
			}
		}).collect(Collectors.toList());
		return list;
	}

	public List<AppPack> xGetPortBanner(String group_name) {
		AppPackExample appPackExample = new AppPackExample();
		appPackExample.or()
				.andGroup_nameEqualTo(group_name);
		List<AppPack> appPacks = this.appPackMapper.selectByExample(appPackExample);
		List<AppPack> list = appPacks.stream().map(n->{
			AppPack appPack = new AppPack();
			appPack.setApp_pack_id(n.getApp_pack_id());
			appPack.setApp_pack_name(n.getApp_pack_name().contains("合伙人端")?n.getApp_pack_name().substring(4,n.getApp_pack_name().length()):n.getApp_pack_name().substring(3,n.getApp_pack_name().length()));
			return appPack;
		}).collect(Collectors.toList());
		return list;
	}
}