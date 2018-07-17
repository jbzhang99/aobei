package com.aobei.train.service.impl;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.PayWxNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.mapper.OrderLogMapper;
import com.aobei.train.service.OrderLogService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderLogServiceImpl extends MbgServiceSupport<OrderLogMapper, Long, OrderLog, OrderLog, OrderLogExample> implements OrderLogService{

	@Autowired
	private OrderLogMapper orderLogMapper;
	@Autowired
	PayWxNotifyService payWxNotifyService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(orderLogMapper);
	}

	@Override
	public OrderLog initOrderLog(Customer customer, Order order, String operator, String text) {

		OrderLog orderLog = new OrderLog();
		orderLog.setPay_order_log_id(IdGenerator.generateId());
		orderLog.setCreate_time(new Date());
		orderLog.setLog_text(text);
		orderLog.setPay_order_id(order.getPay_order_id());
		orderLog.setOperator_name(operator);
		orderLog.setUser_id(customer.getUser_id());
		return orderLog;
	}

	@Override
	@Transactional(timeout = 5)
	public int xInsert(String operator,Long user_id,String orderid,String logtext) {
		OrderLog orderLog = new OrderLog();
		orderLog.setPay_order_log_id(IdGenerator.generateId());
		orderLog.setCreate_time(new Date());
		orderLog.setLog_text(logtext);
		orderLog.setPay_order_id(orderid);
		orderLog.setOperator_name(operator);
		orderLog.setUser_id(user_id);
		return insertSelective(orderLog);
	}
}