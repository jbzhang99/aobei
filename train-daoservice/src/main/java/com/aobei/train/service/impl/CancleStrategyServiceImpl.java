package com.aobei.train.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aobei.train.mapper.CancleStrategyMapper;
import com.aobei.train.model.CancleStrategy;
import com.aobei.train.model.CancleStrategyExample;
import com.aobei.train.model.Users;
import com.aobei.train.service.CancleStrategyService;
import com.aobei.train.service.ProductService;
import com.aobei.train.service.UsersService;
import com.gexin.fastjson.JSON;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import custom.bean.CancleStrategyJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class CancleStrategyServiceImpl extends MbgServiceSupport<CancleStrategyMapper, Integer, CancleStrategy, CancleStrategy, CancleStrategyExample> implements CancleStrategyService{

	@Autowired
	private CancleStrategyMapper cancleStrategyMapper;
	@Autowired
	private UsersService usersService;
	@Autowired
	private ProductService productService;
	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(cancleStrategyMapper);
	}

	@Transactional(timeout = 5)
	@Override
	public Integer xInsertCancleStratrgy(String list, CancleStrategy strategy,Authentication authentication) {
		String json = null;
		List<CancleStrategyJson> cancle_strategy = Dispose(list,strategy.getCancle_type());
		Collections.sort(cancle_strategy);
		json = JSONObject.toJSONString(cancle_strategy);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		strategy.setCancle_strategy(json);
		strategy.setCreate_datetime(new Date());
		strategy.setDeleted(0);
		strategy.setOperator_datetime(new Date());
		strategy.setOperator_id(users.getUser_id());
		strategy.setOperator_name(authentication.getName());
		return this.cancleStrategyMapper.insertSelective(strategy);
	}

	@Transactional(timeout = 5)
	@Override
	public Integer xUpdateCancleStratrgy(String list, CancleStrategy strategy, Authentication authentication) {

		String json = null;
		List<CancleStrategyJson> cancle_strategy = Dispose(list,strategy.getCancle_type());
		Collections.sort(cancle_strategy);
		json = JSONObject.toJSONString(cancle_strategy);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		strategy.setCancle_strategy(json);
		strategy.setDeleted(0);
		strategy.setOperator_datetime(new Date());
		strategy.setOperator_id(users.getUser_id());
		strategy.setOperator_name(authentication.getName());
		return this.cancleStrategyMapper.updateByPrimaryKeySelective(strategy);
	}

	//处理取消策略的接送数据，返回封装的object
	private List<CancleStrategyJson> Dispose(String list,int type){
		ArrayList<CancleStrategyJson> cancle_strategy = new ArrayList<>();
		if(!list.isEmpty()){
			List<String> strings = JSON.parseArray(list, String.class);
			for (String str: strings) {
				CancleStrategyJson strategyJson = new CancleStrategyJson();
				Integer begin_time = null;
				String price = null;
				Boolean allow = null;
				String[] split = str.split("-");
				if(Integer.parseInt(split[2])==0){
					begin_time = Integer.parseInt(split[0]);
					price = split[1];
					allow = true;
				}else{
					begin_time = Integer.parseInt(split[0]);
					price = split[1];
					allow = false;
				}
				strategyJson.setBeforeHour(begin_time);
				if(type==3){
					strategyJson.setValue(formatPrice(price));
				}else {
					strategyJson.setValue(price);
				}
				strategyJson.setAllow(allow);
				cancle_strategy.add(strategyJson);
			}
		}
		return cancle_strategy;
	}

	private String formatPrice(String price) {
		double parseDouble = Double.parseDouble(price) * 100;
		String data = String.valueOf(parseDouble);
		String s = data.substring(0, data.indexOf("."));
		return s;
	}
}