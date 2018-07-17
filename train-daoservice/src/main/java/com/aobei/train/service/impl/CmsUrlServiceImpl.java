package com.aobei.train.service.impl;

import com.aobei.train.IdGenerator;
import custom.bean.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.CmsUrl;
import com.aobei.train.mapper.CmsUrlMapper;
import com.aobei.train.model.CmsUrlExample;import com.aobei.train.service.CmsUrlService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CmsUrlServiceImpl extends MbgServiceSupport<CmsUrlMapper, Long, CmsUrl, CmsUrl, CmsUrlExample> implements CmsUrlService{

	@Autowired
	private CmsUrlMapper cmsUrlMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(cmsUrlMapper);
	}

	@Override
	public int xAdd_cmsUrl(CmsUrl cmsUrl) {
		cmsUrl.setDeleted(Status.DeleteStatus.no.value);//0未删除
		cmsUrl.setCms_url_id(IdGenerator.generateId());
		cmsUrl.setCreate_datetime(new Date());

		return this.cmsUrlMapper.insertSelective(cmsUrl);
	}

	@Override
	public int xDel_cmsUrl(Long cms_url_id) {
		CmsUrl cmsUrl = new CmsUrl();
		cmsUrl.setDeleted(Status.DeleteStatus.yes.value);//为 1 ，删除状态
		cmsUrl.setCms_url_id(cms_url_id);

		return this.cmsUrlMapper.updateByPrimaryKeySelective(cmsUrl);
	}

	@Override
	@Transactional(timeout = 5)
	public int xEdit_cmsUrl(CmsUrl cmsUrl,Long cms_url_id) {
		cmsUrl.setDeleted(Status.DeleteStatus.no.value);//0未删除
		cmsUrl.setCms_url_id( cms_url_id);

		return this.cmsUrlMapper.updateByPrimaryKeySelective(cmsUrl);
	}
}