package com.aobei.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.model.Fallinto;
import com.aobei.train.model.Users;
import com.aobei.train.service.FallintoService;
import custom.bean.BalanceHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aobei.train.model.BalanceOrder;
import com.aobei.train.mapper.BalanceOrderMapper;
import com.aobei.train.model.BalanceOrderExample;import com.aobei.train.service.BalanceOrderService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BalanceOrderServiceImpl extends MbgServiceSupport<BalanceOrderMapper, Long, BalanceOrder, BalanceOrder, BalanceOrderExample> implements BalanceOrderService{

	@Autowired
	private BalanceOrderMapper balanceOrderMapper;

	@Autowired
	private FallintoService fallintoService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(balanceOrderMapper);
	}

	@Override
	public String selectMaxBalanceCycle() {

		return balanceOrderMapper.selectMaxBalanceCycle();
	}

	/**
	 * 添加结算策略
	 * @param str
	 * @param users
	 * @return
	 */
	@Override
	@Transactional
	public int xAddFallinto(String str, Users users) {
		List<Fallinto> fallintos = JSONObject.parseArray(str, Fallinto.class);
		Fallinto fallinto=new Fallinto();
		if(fallintos.size()>0){
			fallinto=fallintos.get(0);
		}
		fallinto.setFallinto_id(IdGenerator.generateId());
		fallinto.setCreate_datetime(new Date());
		fallinto.setDeleted(0);
		fallinto.setActived(0);
		fallinto.setIs_actived(0);
		fallinto.setCreate_name(users.getUsername());
		int num = this.fallintoService.insert(fallinto);
		return num;
	}

	/**
	 * 订单完成  挂起
	 * @param balance_order_id
	 * @param users
	 * @return
	 */
	@Override
	public int xHangUp(Long balance_order_id,Users users) {
		BalanceOrder balanceOrder = balanceOrderMapper.selectByPrimaryKey(balance_order_id);
		balanceOrder.setStatus(3);
		List<BalanceHistory> balanceHistoryList=new ArrayList<>();
		if(balanceOrder.getChange_history()!=null){
			balanceHistoryList = JSONArray.parseArray(balanceOrder.getChange_history(), BalanceHistory.class);
		}
		BalanceHistory balanceHistory=new BalanceHistory();
		balanceHistory.setType("挂起");
		Date date = new Date();
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

		balanceHistory.setD(localDateTime.toString());
		balanceHistory.setOperation(users.getUsername());
		balanceHistoryList.add(balanceHistory);
		balanceOrder.setChange_history(JSONArray.toJSONString(balanceHistoryList));
		int num = this.balanceOrderMapper.updateByPrimaryKeySelective(balanceOrder);
		return num;
	}
}