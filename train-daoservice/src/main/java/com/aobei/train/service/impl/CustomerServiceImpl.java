package com.aobei.train.service.impl;


import java.util.HashMap;
import java.util.Map;

import com.aobei.train.handler.CacheReloadHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.model.Customer;
import com.aobei.train.mapper.CustomerMapper;
import com.aobei.train.model.CustomerExample;import com.aobei.train.service.CustomerService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class CustomerServiceImpl extends MbgServiceSupport<CustomerMapper, Long, Customer, Customer, CustomerExample> implements CustomerService{

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private CacheReloadHandler cacheReloadHandler;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(customerMapper);
	}

	@Override
	@Transactional(timeout = 5)
	public Map<String, String> change_locked(Integer locked, Long customer_id) {
		Map<String , String> map = new HashMap<>();
		Customer customer = new Customer();
		customer.setCustomer_id(customer_id);
		if(1 == locked) {//执行解冻操作
			customer.setLocked(0);
			int i = this.updateByPrimaryKeySelective(customer);
			map.put("msg", String.format("为该顾客执行解冻操作%s!", i > 0 ? "成功" : "失败"));
		}else {//执行冻结操作
			customer.setLocked(1);
			int i = this.updateByPrimaryKeySelective(customer);
			map.put("msg", String.format("为该顾客执行冻结操作%s!", i > 0 ? "成功" : "失败"));
		}
		Long user_id = this.selectByPrimaryKey(customer_id).getUser_id();
		cacheReloadHandler.customerInfoReload(user_id);
		return map;
	}

}