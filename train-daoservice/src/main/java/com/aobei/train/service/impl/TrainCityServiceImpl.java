package com.aobei.train.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import com.aobei.train.model.TrainCity;
import com.aobei.train.mapper.TrainCityMapper;
import com.aobei.train.model.TrainCityExample;
import com.aobei.train.service.TrainCityService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class TrainCityServiceImpl extends MbgServiceSupport<TrainCityMapper, String, TrainCity, TrainCity, TrainCityExample> implements TrainCityService{

	@Autowired
	private TrainCityMapper trainCityMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(trainCityMapper);
	}
    /**
     * 通过id获取地址对象信息（train_city)
     */
	@Override
	public TrainCity xSelectCityById(String id) {
		return trainCityMapper.selectByPrimaryKey(id);
	}
	/**
	 * 通name查询对象
	 */
	@Override
	public TrainCity xSelectCityByName(String name) {
		TrainCityExample trainCityExample1 = new TrainCityExample();
		trainCityExample1.or().andNameEqualTo(name);
		TrainCity trainCity = DataAccessUtils.singleResult(trainCityMapper.selectByExample(trainCityExample1));
		return trainCity;
	}
}