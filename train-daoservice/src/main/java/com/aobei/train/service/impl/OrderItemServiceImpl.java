package com.aobei.train.service.impl;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.Order;
import com.aobei.train.model.ProSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.OrderItem;
import com.aobei.train.mapper.OrderItemMapper;
import com.aobei.train.model.OrderItemExample;import com.aobei.train.service.OrderItemService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

@Service
public class OrderItemServiceImpl extends MbgServiceSupport<OrderItemMapper, Long, OrderItem, OrderItem, OrderItemExample> implements OrderItemService{

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(orderItemMapper);
	}

	@Override
	public OrderItem initOrderItem(Order order, ProSku proSku, int num) {

		OrderItem orderItem = new OrderItem();
		orderItem.setPay_order_item_id(IdGenerator.generateId());
		orderItem.setPay_order_id(order.getPay_order_id());
		orderItem.setProduct_id(proSku.getProduct_id());
		orderItem.setPsku_id(proSku.getPsku_id());
		orderItem.setPrice(proSku.getPrice());
		orderItem.setNum(num);
		return orderItem;
	}

	@Override
	public Integer xSumNum(OrderItemExample orderItemExample) {
		return orderItemMapper.xSumNum(orderItemExample);
	}
}