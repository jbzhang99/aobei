package com.aobei.train.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.model.Bespeak;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.mapper.BespeakMapper;
import com.aobei.train.model.BespeakExample;import com.aobei.train.service.BespeakService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.BespeakTime;

@Service
public class BespeakServiceImpl extends MbgServiceSupport<BespeakMapper, Integer, Bespeak, Bespeak, BespeakExample> implements BespeakService{

	@Autowired
	private BespeakMapper bespeakMapper;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(bespeakMapper);
	}

	@Override
	public int xDelete(int bespeak_id) {
		return bespeakMapper.deleteByPrimaryKey(bespeak_id);
	}


	@Transactional(timeout = 5)
	@Override
	public int xInsert(String name, String list) {
		List<String> array = JSONObject.parseArray(list, String.class);
		Bespeak bespeak = new Bespeak();
		bespeak.setCreate_datetime(new Date());
		bespeak.setName(name);
		List<BespeakTime> list_data = new ArrayList<>();
		for (String string : array) {
			BespeakTime bespeakTime = new BespeakTime();
			String[] split = string.split("-");
			int data = 0;
			int one = 0;
			int two = 0;
			int three = 0;
			int four = 0;
			int five = 0;
			
			for (int i = 0; i < split.length; i++) {
				bespeakTime.setBefore_hour(split[0]+":"+split[1]);
				one = Integer.parseInt(split[0]);
				two = Integer.parseInt(split[1]);
				three = Integer.parseInt(split[2]);
				four = Integer.parseInt(split[3]);
				five = Integer.parseInt(split[4]);
				
				data = (three*24*60+four*60+five)-(one*60+two);
				bespeakTime.setAfter_minutes(String.valueOf(data));
				bespeakTime.setTime_format(string);
			}
			
			list_data.add(bespeakTime);
			bespeak.setBespeak_strategy(JSONObject.toJSONString(list_data));
		}
		return bespeakMapper.insertSelective(bespeak);
	}

	@Override
	public List getBespeakList(Integer bespeak_id){
		List<String> list_data = new ArrayList<>();
		Bespeak bespeak = bespeakMapper.selectByPrimaryKey(bespeak_id);
		List<BespeakTime> list_bespeak_strategy = JSONObject.parseArray(bespeak.getBespeak_strategy(), BespeakTime.class);
		for (BespeakTime bespeakTime : list_bespeak_strategy) {
			String time_format = bespeakTime.getTime_format();
			String[] split = time_format.split("-");
			String a = "";
			String b = "";
			String c = "";
			String d = "";
			String e = "";
			for (int i = 0; i < split.length; i++) {
				a=split[0];
				b=split[1];
				c=split[2];
				d=split[3];
				e=split[4];
			}
			list_data.add(a);
			list_data.add(b);
			list_data.add(c);
			list_data.add(d);
			list_data.add(e);
		}
		return list_data;
	}
	@Transactional(timeout = 5)
	@Override
	public int xUpdate(String name, String list,int id) {
		List<String> array = JSONObject.parseArray(list, String.class);
		Bespeak bespeak = bespeakMapper.selectByPrimaryKey(id);
		bespeak.setName(name);
		List<BespeakTime> list_data = new ArrayList<>();
		for (String string : array) {
			BespeakTime bespeakTime = new BespeakTime();
			String[] split = string.split("-");
			for (int i = 0; i < split.length; i++) {
				bespeakTime.setBefore_hour(split[0]+":"+split[1]);
				int one = Integer.parseInt(split[0]);
				int two = Integer.parseInt(split[1]);
				int three = Integer.parseInt(split[2]);
				int four = Integer.parseInt(split[3]);
				int five = Integer.parseInt(split[4]);
				
				int data = (three*24*60+four*60+five)-(one*60+two);
				bespeakTime.setAfter_minutes(String.valueOf(data));
				bespeakTime.setTime_format(string);
			}
			
			list_data.add(bespeakTime);
			bespeak.setBespeak_strategy(JSONObject.toJSONString(list_data));
		}
		
		return bespeakMapper.updateByPrimaryKeySelective(bespeak);
	}
}