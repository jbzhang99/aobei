package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Message;
import com.aobei.train.mapper.MessageMapper;
import com.aobei.train.model.MessageExample;import com.aobei.train.service.MessageService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class MessageServiceImpl extends MbgServiceSupport<MessageMapper, Long, Message, Message, MessageExample> implements MessageService{

	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(messageMapper);
	}
}