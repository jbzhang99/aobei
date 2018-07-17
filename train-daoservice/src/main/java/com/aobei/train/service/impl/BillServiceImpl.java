package com.aobei.train.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.Bill;
import com.aobei.train.mapper.BillMapper;
import com.aobei.train.model.BillExample;import com.aobei.train.service.BillService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillServiceImpl extends MbgServiceSupport<BillMapper, Long, Bill, Bill, BillExample> implements BillService{

	@Autowired
	private BillMapper billMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(billMapper);
	}

	@Override
	@Transactional(timeout = 5)
	public int xInsert(Bill bill) {
		return this.insertSelective(bill);
	}

	@Override
	@Transactional(timeout = 5)
	public int xUpdate(Bill bill) {
		return this.updateByPrimaryKeySelective(bill);
	}
}