package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.ProductSole;
import com.aobei.train.mapper.ProductSoleMapper;
import com.aobei.train.model.ProductSoleExample;
import com.aobei.train.model.ProductSoleKey;
import com.aobei.train.service.ProductSoleService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class ProductSoleServiceImpl extends MbgServiceSupport<ProductSoleMapper, ProductSoleKey, ProductSole, ProductSole, ProductSoleExample> implements ProductSoleService{

	@Autowired
	private ProductSoleMapper productSoleMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(productSoleMapper);
	}
}