package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.CouponReceive;
import com.aobei.train.mapper.CouponReceiveMapper;
import com.aobei.train.model.CouponReceiveExample;import com.aobei.train.service.CouponReceiveService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class CouponReceiveServiceImpl extends MbgServiceSupport<CouponReceiveMapper, Long, CouponReceive, CouponReceive, CouponReceiveExample> implements CouponReceiveService{

	@Autowired
	private CouponReceiveMapper couponReceiveMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(couponReceiveMapper);
	}
}