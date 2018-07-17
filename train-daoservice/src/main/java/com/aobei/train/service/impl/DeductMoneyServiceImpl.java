package com.aobei.train.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aobei.train.model.*;
import com.aobei.train.service.DataDownloadService;
import com.aobei.train.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import com.aobei.train.mapper.DeductMoneyMapper;
import com.aobei.train.service.DeductMoneyService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@Service
public class DeductMoneyServiceImpl extends MbgServiceSupport<DeductMoneyMapper, Long, DeductMoney, DeductMoney, DeductMoneyExample> implements DeductMoneyService{

	@Autowired
	private DeductMoneyMapper deductMoneyMapper;

	@Autowired
	private DataDownloadService dataDownloadService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(deductMoneyMapper);
	}

	@Override
	public DeductMoneyExample generateDownloadTaskAndPottingParam(String str, String username, long id) {
		DataDownload dataDownload = new DataDownload();
		dataDownload.setData_download_id(id);
		Date date = new Date();
		dataDownload.setCreate_datetime(date);
		dataDownload.setName(LocalDate.now().toString() + " 扣款单列表下载");
		dataDownload.setType(1);
		dataDownload.setParams(str);
		dataDownload.setStatus(0);
		UsersExample usersExample = new UsersExample();
		usersExample.or().andUsernameEqualTo(username);
		Users users = DataAccessUtils.singleResult(usersService.selectByExample(usersExample));
		dataDownload.setUser_id(users.getUser_id());
		dataDownloadService.insertSelective(dataDownload);
		// 将字符串json数据转换成json对象
		JSONObject resultJson = JSONObject.parseObject(str);
		// 将json对象按照map进行封装
		Map<String, Object> map = resultJson;
		String pay_order_id = (String) map.get("pay_order_id");
		String sta = (String) map.get("deduct_status");
		Integer deduct_status = null;
		if (!("".equals(sta))) {
			deduct_status = Integer.valueOf(sta);
		}
		DeductMoneyExample deductMoneyExample = new DeductMoneyExample();
		deductMoneyExample.setOrderByClause(DeductMoneyExample.C.create_datetime + " desc");
		DeductMoneyExample.Criteria criteria = deductMoneyExample.or();
		if (!"".equals(pay_order_id) && pay_order_id != null){
			criteria.andPay_order_idEqualTo(pay_order_id);
		}
		if (deduct_status != null){
			criteria.andDeduct_statusEqualTo(deduct_status);
		}
		return deductMoneyExample;
	}
}