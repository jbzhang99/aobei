package com.aobei.train.service.impl;

import com.aobei.train.model.ChannelType;
import com.aobei.train.service.ChannelTypeService;
import custom.bean.ChannelAndType;
import custom.bean.Status;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Channel;
import com.aobei.train.mapper.ChannelMapper;
import com.aobei.train.model.ChannelExample;import com.aobei.train.service.ChannelService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelServiceImpl extends MbgServiceSupport<ChannelMapper, Integer, Channel, Channel, ChannelExample> implements ChannelService{

	@Autowired
	private ChannelMapper channelMapper;
	@Autowired
	private ChannelTypeService channelTypeService;
	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(channelMapper);
	}

	
	@Transactional(timeout = 5)
	@Override
	public Integer xInsertChannel(Channel channel) {
		ChannelExample channelExample = new ChannelExample();
		channelExample.or()
				.andCodeEqualTo(channel.getCode());
		long l = this.channelMapper.countByExample(channelExample);
		if(l != 0 ) {
			return 0;
		}
		channel.setCreate_datetime(new Date());
		channel.setDeleted(0);
		return this.channelMapper.insertSelective(channel);
	}
	@Transactional(timeout = 5)
	@Override
	public Integer xUpdateChannel(Channel channel) {
		ChannelExample channelExample = new ChannelExample();
		channelExample.or()
				.andCodeEqualTo(channel.getCode())
				.andChannel_idNotEqualTo(channel.getChannel_id());
		long l = this.channelMapper.countByExample(channelExample);
		if(l != 0 ) {
			return 0;
		}
		return this.channelMapper.updateByPrimaryKeySelective(channel);
	}
	@Transactional(timeout = 5)
	@Override
	public Integer xDeleteChannel(Integer channel_id) {
		Channel channel = new Channel();
		channel.setChannel_id(channel_id);
		channel.setDeleted(Status.DeleteStatus.yes.value);

		return this.channelMapper.updateByPrimaryKeySelective(channel);
	}

	@Override
	public List<ChannelAndType> xExport(Integer channel_type_id) {
		ChannelExample channelExample = new ChannelExample();
		ChannelExample.Criteria or = channelExample.or();
		if(channel_type_id!=0 && channel_type_id!=null){
			or.andChannel_type_idEqualTo(channel_type_id);
		}
		or.andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Channel> channels = this.channelMapper.selectByExample(channelExample);
		List<ChannelAndType> list = channels.stream().map( n->{
			ChannelAndType channelAndType = new ChannelAndType();
			channelAndType.setChannel_type_name(channelTypeService.selectByPrimaryKey(n.getChannel_type_id()).getChannel_type_name());
			BeanUtils.copyProperties(n,channelAndType);
			return channelAndType;
		}).collect(Collectors.toList());
		return list;
		/*List<Object[]> list = channels.stream().map( n->{
			Object[] o = {channelTypeService.selectByPrimaryKey(n.getChannel_type_id()).getChannel_type_name(),
					n.getChannel_name(),
					n.getCode()};

			return o;
		}).collect(Collectors.toList());
		return list;*/
	}
}