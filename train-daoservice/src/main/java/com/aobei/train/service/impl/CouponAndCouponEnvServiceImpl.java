package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.CouponAndCouponEnv;
import com.aobei.train.mapper.CouponAndCouponEnvMapper;
import com.aobei.train.model.CouponAndCouponEnvExample;import com.aobei.train.service.CouponAndCouponEnvService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class CouponAndCouponEnvServiceImpl extends MbgServiceSupport<CouponAndCouponEnvMapper, Long, CouponAndCouponEnv, CouponAndCouponEnv, CouponAndCouponEnvExample> implements CouponAndCouponEnvService{

	@Autowired
	private CouponAndCouponEnvMapper couponAndCouponEnvMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(couponAndCouponEnvMapper);
	}
}