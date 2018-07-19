package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.VideoContent;
import com.aobei.train.mapper.VideoContentMapper;
import com.aobei.train.model.VideoContentExample;import com.aobei.train.service.VideoContentService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class VideoContentServiceImpl extends MbgServiceSupport<VideoContentMapper, Long, VideoContent, VideoContent, VideoContentExample> implements VideoContentService{

	@Autowired
	private VideoContentMapper videoContentMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(videoContentMapper);
	}
}