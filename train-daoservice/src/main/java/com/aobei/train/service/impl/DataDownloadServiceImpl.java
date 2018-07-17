package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.DataDownload;
import com.aobei.train.mapper.DataDownloadMapper;
import com.aobei.train.model.DataDownloadExample;import com.aobei.train.service.DataDownloadService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class DataDownloadServiceImpl extends MbgServiceSupport<DataDownloadMapper, Long, DataDownload, DataDownload, DataDownloadExample> implements DataDownloadService{

	@Autowired
	private DataDownloadMapper dataDownloadMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(dataDownloadMapper);
	}
}