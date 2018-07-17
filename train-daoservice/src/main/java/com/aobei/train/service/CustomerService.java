package com.aobei.train.service;

import com.aobei.train.model.CustomerExample;

import java.util.Map;

import com.aobei.train.model.Customer;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CustomerService extends MbgReadService<Long, Customer, Customer, CustomerExample>,MbgWriteService<Long, Customer, Customer, CustomerExample>,MbgUpsertService<Long, Customer, Customer, CustomerExample>{
	
	/**
	 * 冻结和解冻客户
	 * @param locked
	 * @param customer_id
	 * @return
	 */
	Map<String , String> change_locked(Integer locked,Long customer_id);
}