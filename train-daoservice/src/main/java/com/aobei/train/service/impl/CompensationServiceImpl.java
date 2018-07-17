package com.aobei.train.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aobei.train.model.*;
import com.aobei.train.service.CouponService;
import com.aobei.train.service.UsersService;
import com.aobei.train.service.VOrderUnitService;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.Programme_type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import com.aobei.train.mapper.CompensationMapper;
import com.aobei.train.service.CompensationService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CompensationServiceImpl extends MbgServiceSupport<CompensationMapper, Long, Compensation, Compensation, CompensationExample> implements CompensationService{

	@Autowired
	private CompensationMapper compensationMapper;

	@Autowired
	private VOrderUnitService vOrderUnitService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(compensationMapper);
	}

	@Override
	public Page<CompensationInfo> compensationInfoList(CompensationExample compensationExample,
													   VOrderUnitExample example,
													   Map<String,String> params,
													   int page, int size) {
		List<Compensation> compensations = this.selectByExample(compensationExample);
		List<String> orderids = compensations.stream().map(n -> n.getPay_order_id()).collect(Collectors.toList());
		List<Long> creators = compensations.stream().map(n -> n.getOperator_create()).collect(Collectors.toList());
		List<Long> confirmers = compensations.stream().map(n -> n.getOperator_confirm()).collect(Collectors.toList());

		//查询条件的对象
		VOrderUnitExample.Criteria or = example.or();
		or.andPay_order_idLessThan(Integer.MAX_VALUE + "");
		if (orderids.size() > 0){
			or.andPay_order_idIn(orderids);
		} else {
			or.andPay_order_idEqualTo("00");
		}
		String pay_order_id = params.get("pay_order_id");
		String cuname = params.get("cuname");
		String uphone = params.get("uphone");
		String partner_id = params.get("partner_id");
		String student_name = params.get("student_name");
		if (pay_order_id != null){
			or.andPay_order_idEqualTo(pay_order_id);
		}
		if (cuname != null){
			or.andCus_usernameLike("%" + cuname + "%");
		}
		if (uphone != null){
			or.andCus_phoneLike("%" + uphone + "%");
		}
		if (partner_id != null){
			or.andPartner_idEqualTo(Long.valueOf(partner_id));
		}
		if (student_name != null){
			or.andStudent_nameLike("%" + student_name + "%");
		}
		List<VOrderUnit> vOrderUnits = vOrderUnitService.selectByExample(example);
		Map<String, VOrderUnit> ou_map = vOrderUnits.stream().collect(Collectors.toMap(VOrderUnit::getPay_order_id, Function.identity()));
		//根据订单查询结果过滤
		compensations = compensations.stream().filter(n -> ou_map.get(n.getPay_order_id()) != null).collect(Collectors.toList());
		UsersExample usersExample = new UsersExample();
		usersExample.setDistinct(true);
		if (confirmers.size() > 0){
			usersExample.or().andUser_idIn(confirmers);
		} else {
			usersExample.or().andUser_idEqualTo(0l);
		}
		if (creators.size() > 0){
			usersExample.or().andUser_idIn(creators);
		} else {
			usersExample.or().andUser_idEqualTo(0l);
		}

		List<Users> users = usersService.selectByExample(usersExample);
		Map<Long, Users> usersMap = users.stream().collect(Collectors.toMap(Users::getUser_id, Function.identity()));
		List<CompensationInfo> compensationInfos = compensations.stream().map(n -> {
			Programme_type programmeType = null;
			if (n.getCoupon_id() != null){
				Coupon coupon = couponService.selectByPrimaryKey(n.getCoupon_id());
				if (coupon != null){
					programmeType = JSONObject.parseObject(coupon.getProgramme(), Programme_type.class);
				}
			}
			CompensationInfo compensationInfo = new CompensationInfo();
			compensationInfo.setCompensation(n);
			BeanUtils.copyProperties(ou_map.get(n.getPay_order_id()), compensationInfo);
			compensationInfo.setOperator_create(usersMap.get(n.getOperator_create()));
			compensationInfo.setOperator_confirm(usersMap.get(n.getOperator_confirm()));
			compensationInfo.setProgrammeType(programmeType);
			return compensationInfo;
		}).collect(Collectors.toList());
		Page<CompensationInfo> compensationInfoPage =
				new Page<CompensationInfo>(compensationInfos,compensations.size(),page,size);
		return compensationInfoPage;
	}

	@Override
	@Transactional(timeout = 5)
	public int xUpdateConfirm(Compensation compensation) {
		return this.updateByPrimaryKeySelective(compensation);
	}

	@Override
	@Transactional(timeout = 5)
	public int xUpdateReject(Compensation compensation) {
		return this.updateByPrimaryKeySelective(compensation);
	}
}