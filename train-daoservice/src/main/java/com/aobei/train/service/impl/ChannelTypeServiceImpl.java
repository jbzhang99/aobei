package com.aobei.train.service.impl;

import com.aobei.train.model.ChannelExample;
import com.aobei.train.service.ChannelService;
import custom.bean.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.ChannelType;
import com.aobei.train.mapper.ChannelTypeMapper;
import com.aobei.train.model.ChannelTypeExample;import com.aobei.train.service.ChannelTypeService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import java.util.Date;
import java.util.HashMap;

@Service
public class ChannelTypeServiceImpl extends MbgServiceSupport<ChannelTypeMapper, Integer, ChannelType, ChannelType, ChannelTypeExample> implements ChannelTypeService{

	@Autowired
	private ChannelTypeMapper channelTypeMapper;
	@Autowired
	private ChannelService channelService;


	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(channelTypeMapper);
	}

	@Override
	public Integer xInsertChannelType(ChannelType channelType) {
		channelType.setCreate_datetime(new Date());
		return this.channelTypeMapper.insertSelective(channelType);
	}

	@Override
	public Integer xDeleteChannelType(Integer channel_type_id) {
		ChannelExample channelExample = new ChannelExample();
		channelExample.or()
				.andChannel_type_idEqualTo(channel_type_id)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);
		long i = channelService.countByExample(channelExample);
		return i == 0 ? this.channelTypeMapper.deleteByPrimaryKey(channel_type_id) : 0;
	}

	@Override
	public Integer xUpdateChannelType(ChannelType channelType) {
		return this.channelTypeMapper.updateByPrimaryKeySelective(channelType);
	}
}