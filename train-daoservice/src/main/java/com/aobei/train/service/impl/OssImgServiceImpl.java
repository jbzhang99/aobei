package com.aobei.train.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.OssImg;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.OssImgMapper;
import com.aobei.train.model.OssImgExample;import com.aobei.train.service.OssImgService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OssImgServiceImpl extends MbgServiceSupport<OssImgMapper, Long, OssImg, OssImg, OssImgExample> implements OssImgService{

	@Autowired
	private OssImgMapper ossImgMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(ossImgMapper);
	}

	@Override
	@Transactional(timeout = 5)
	public OssImg xInsertOssImg(Map<String, String> params, String effect, String privileges) {
		OssImg ossImg = new OssImg();
		ossImg.setOss_img_id(IdGenerator.generateId());
		ossImg.setFormat(params.get("file_format"));
		ossImg.setName(params.get("file_name"));
		ossImg.setEffect(effect);
		ossImg.setUrl(params.get("file_url"));
		ossImg.setBucket(params.get("bucketName"));
		ossImg.setAccess_permissions(privileges);
		this.insertSelective(ossImg);
		return ossImg;
	}
}