package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.CustomerAddress;
import com.aobei.train.mapper.CustomerAddressMapper;
import com.aobei.train.model.CustomerAddressExample;import com.aobei.train.service.CustomerAddressService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class CustomerAddressServiceImpl extends MbgServiceSupport<CustomerAddressMapper, Long, CustomerAddress, CustomerAddress, CustomerAddressExample> implements CustomerAddressService{

	@Autowired
	private CustomerAddressMapper customerAddressMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(customerAddressMapper);
	}
}