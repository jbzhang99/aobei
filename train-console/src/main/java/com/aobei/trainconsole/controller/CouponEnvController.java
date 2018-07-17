package com.aobei.trainconsole.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.aobei.train.model.*;
import com.aobei.train.service.OperateLogService;
import com.aobei.train.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.aobei.train.IdGenerator;
import com.aobei.train.service.CouponEnvService;
import com.aobei.train.service.CouponService;
import com.aobei.trainconsole.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.CouponEnvDate;

@Controller
@RequestMapping("/couponEnv")
public class CouponEnvController {

	@Autowired
	private CouponEnvService couponEnvService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private OperateLogService operateLogService;


	private static Logger logger = LoggerFactory.getLogger(CouponEnvController.class);

	/**
	 * 优惠策略列表页数据
	 * @param p
	 * @param ps
	 * @param model
	 * @return
	 */
	@RequestMapping("/couponEnv_list")
	public String couponEnv_list(@RequestParam(defaultValue="1")Integer p,
								 @RequestParam(defaultValue="10")Integer ps,Model model){
		
		CouponEnvExample couponEnvExample = new CouponEnvExample();
		couponEnvExample
				.setOrderByClause(CouponEnvExample.C.create_datetime+" desc");
		Page<CouponEnv> page = couponEnvService.selectByExample(couponEnvExample,p,ps);
		model.addAttribute("list_couponEnv", page.getList());
		model.addAttribute("current", p);
		model.addAttribute("page", page);
		model.addAttribute("date", new Date());
		return "couponEnv/couponEnv_list";
	}
	/**
	 * 跳转到添加优惠策略页面方法
	 * @param model
	 * @param p
	 * @return
	 */
	@RequestMapping("/goto_add")
	public String goto_add(Model model,@RequestParam(value="p")Integer p){
		if(p==0){
			p+=1;
		}
		CouponExample couponExample = new CouponExample();
		couponExample.or()
			.andValidEqualTo(1)
			.andUse_end_datetimeGreaterThan(new Date());//查询有效，未失效的优惠券
		List<Coupon> list_coupon = couponService.selectByExample(couponExample);//可用优惠券集合
		
		model.addAttribute("current", p);
		model.addAttribute("list_coupon", list_coupon);
		return "couponEnv/couponEnv_add";
	}
	/**
	 * 优惠策略添加功能
	 * @param couponEnv
	 * @param s_date  条件开始时间
	 * @param e_date  条件结束时间
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add_couponEnv")
	public Object add_couponEnv(CouponEnv couponEnv,
			@RequestParam(value="s_date",required=false)@DateTimeFormat(pattern="yyyy-MM-dd HH:ss:mm")String s_date,
			@RequestParam(value="e_date",required=false)@DateTimeFormat(pattern="yyyy-MM-dd HH:ss:mm")String e_date,
								Authentication authentication){
		HashMap<String,String> map = new HashMap<String,String>();
		int i = 0;
		try {
			CouponEnvDate couponEnvDate = new CouponEnvDate();
			couponEnvDate.setSdate(s_date);
			couponEnvDate.setEdate(e_date);
			String object_to_json = JacksonUtil.object_to_json(couponEnvDate);
			couponEnv.setCoupon_env_id(IdGenerator.generateId());
			couponEnv.setCondition_env(object_to_json);
			couponEnv.setCreate_datetime(new Date());
			Coupon coupon = couponService.selectByPrimaryKey(couponEnv.getCoupon_id());
			if(coupon.getNum_limit()==1){				
				if(couponEnv.getCoupon_number()>coupon.getNum_able()){
					map.put("message", "优惠券数量不能大于优惠券可用数量,添加失败！");
					return map;
				}
			}
			i = couponEnvService.insertSelective(couponEnv);
			Users users = usersService.xSelectUserByUsername(authentication.getName());
			logger.info("M[couponEnv] F[add_couponEnv] U[{}]; param[couponEnv:{}]; other[begin_date:{},end_date:{}]; result:{}",
					users.getUser_id(),couponEnv,s_date,e_date,String.format("添加优惠策略%s", i > 0 ? "成功":"失败"));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		map.put("message", String.format("添加优惠策略%s", i > 0 ? "成功":"失败"));
		return map;
	}
	/**
	 * 跳转到优惠策略编辑页
	 * @param model
	 * @param couponEnv_id
	 * @param current
	 * @return
	 */
	@RequestMapping("/goto_edit_couponEnv")
	public String goto_edit_couponEnv(Model model,
			@RequestParam(value="couponEnv_id")Long couponEnv_id,
			@RequestParam(value="p")Integer current){
		if(current==0){
			current+=1;
		}
		CouponEnv env = couponEnvService.selectByPrimaryKey(couponEnv_id);
		CouponExample couponExample = new CouponExample();
		couponExample.or()
			.andValidEqualTo(1)
			.andUse_end_datetimeGreaterThan(new Date());//查询有效，未失效的优惠券
		List<Coupon> list_coupon = couponService.selectByExample(couponExample);//可用优惠券集合
		try {
			CouponEnvDate envDate = JacksonUtil.json_to_object(env.getCondition_env(), CouponEnvDate.class);
			model.addAttribute("sdate", envDate.getSdate());
			model.addAttribute("edate", envDate.getEdate());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("status",env.getStatus());
		model.addAttribute("list_coupon", list_coupon);
		model.addAttribute("current", current);
		model.addAttribute("env", env);
		return "couponEnv/couponEnv_edit";
	}
	/**
	 * 编辑优惠策略
	 * @param model
	 * @param couponEnv
	 * @param s_date
	 * @param e_date
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/edit_couponEnv")
	public Object edit_couponEnv(Model model,CouponEnv couponEnv,
			@RequestParam(value="s_date",required=false)@DateTimeFormat(pattern="yyyy-MM-dd HH:ss:mm")String s_date,
			@RequestParam(value="e_date",required=false)@DateTimeFormat(pattern="yyyy-MM-dd HH:ss:mm")String e_date,
								 Authentication authentication){
		HashMap<String,String> map = new HashMap<String,String>();
		int i = 0;
		try {
			CouponEnvDate couponEnvDate = new CouponEnvDate();
			couponEnvDate.setSdate(s_date);
			couponEnvDate.setEdate(e_date);
			String object_to_json = JacksonUtil.object_to_json(couponEnvDate);
			couponEnv.setCondition_env(object_to_json);
			//couponEnv.setCreate_datetime(couponEnvService.selectByPrimaryKey(couponEnv.getCoupon_env_id()).getCreate_datetime());
			Coupon coupon = couponService.selectByPrimaryKey(couponEnv.getCoupon_id());
			if(coupon.getNum_limit()==1){				
				if(couponEnv.getCoupon_number()>coupon.getNum_able()){
					map.put("message", "优惠券数量不能大于优惠券可用数量，添加失败！");
					return map;
				}
			}
			if(couponEnv.getStatus()==3){
				if(couponEnv.getEnd_datetime().before(new Date())){
					couponEnv.setStatus(3);//已过期
				}else{
					couponEnv.setStatus(1);//已生效
				}
			}

			i = couponEnvService.updateByPrimaryKeySelective(couponEnv);
			Users users = usersService.xSelectUserByUsername(authentication.getName());
			logger.info("M[couponEnv] F[update_couponEnv] U[{}]; param[couponEnv:{}]; other[begin_date:{},end_date:{}]; result:{}",
					users.getUser_id(),couponEnv,s_date,e_date,String.format("修改优惠策略%s", i > 0 ? "成功":"失败"));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		map.put("message", String.format("修改优惠策略%s", i > 0 ? "成功":"失败"));
		return map;
	}
	/**
	 * 获取单条优惠券数量
	 * @param model
	 * @param coupon_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryNumber")
	public Object queryNumber(Model model,@RequestParam(value="coupon_id")Long coupon_id){
		HashMap<String,Object> map = new HashMap<String,Object>();
		Coupon coupon = couponService.selectByPrimaryKey(coupon_id);
		
		map.put("message", coupon);
		return map;
	}

	@ResponseBody
	@RequestMapping("/change_status")
	public Object change_status(Long couponEnv_id,Authentication authentication){
		HashMap<String,Object> map = new HashMap<String,Object>();
		CouponEnv couponEnv = couponEnvService.selectByPrimaryKey(couponEnv_id);
		OperateLog operateLog = new OperateLog();
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		if(couponEnv.getStatus()==0){
			couponEnv.setStatus(1);//改为生效
			int i = couponEnvService.updateByPrimaryKeySelective(couponEnv);
			map.put("message", String.format("生效%s",i>0?"成功":"失败"));

			logger.info("M[couponEnv] F[change_status] U[{}]; param[couponEnv:{}]; result:{}",
					users.getUser_id(),couponEnv,String.format("修改优惠策略%s", i > 0 ? "成功":"失败"));
			return map;
		}else{
			couponEnv.setStatus(0);//改为失效
			int i = couponEnvService.updateByPrimaryKeySelective(couponEnv);
			map.put("message", String.format("失效%s",i>0?"成功":"失败"));

			logger.info("M[couponEnv] F[change_status] U[{}]; param[couponEnv:{}]; result:{}",
					users.getUser_id(),couponEnv,String.format("修改优惠策略%s", i > 0 ? "成功":"失败"));

			operateLog.setOperate_log_id(IdGenerator.generateId());
			operateLog.setOperate("M[couponEnv] F[change_status] U["+users.getUsername()+"] D[couponEnv_id:"+couponEnv.getCoupon_env_id()+"] ");
			operateLog.setUser_id(users.getUser_id());
			operateLog.setCreate_datetime(new Date());
			operateLogService.insertSelective(operateLog);
			return map;
		}
	}
}


