package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Chapter;
import com.aobei.train.mapper.ChapterMapper;
import com.aobei.train.model.ChapterExample;import com.aobei.train.service.ChapterService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class ChapterServiceImpl extends MbgServiceSupport<ChapterMapper, Long, Chapter, Chapter, ChapterExample> implements ChapterService{

	@Autowired
	private ChapterMapper chapterMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(chapterMapper);
	}
}