package com.aobei.trainconsole.controller;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.OperateLogService;
import com.aobei.train.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.aobei.train.service.BespeakService;
import com.aobei.train.service.ProSkuService;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.BespeakTime;

@Controller
@RequestMapping("/bespeak")
public class BespeakProController {
	
	@Autowired
	private BespeakService bespeakService;
	@Autowired
	private ProSkuService proSkuService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private OperateLogService operateLogService;
	
	private String[] days ={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};

	private static Logger logger = LoggerFactory.getLogger(BespeakProController.class);

	/**
	 * 预约策略列表页查询
	 * @param p
	 * @param ps
	 * @param model
	 */
	@RequestMapping("/bespeak_list")
	public void bespeak_list(@RequestParam(defaultValue="1")Integer p,
			 				 @RequestParam(defaultValue="10")Integer ps,Model model){
		Page<Bespeak> page = bespeakService.selectByExample(new BespeakExample(),p,ps);

		model.addAttribute("page", page);
		model.addAttribute("current", p);
		model.addAttribute("list", page.getList());
	}

	/**
	 * 跳转到预约策略添加页
	 * @param model
	 * @param p
	 * @return
	 */
	@RequestMapping("/goto_add_bespeak")
	public String goto_add_bespeak(Model model,@RequestParam(value="p")Integer p){
		if(p==0){
			p+=1;
		}
		model.addAttribute("current", p);
		model.addAttribute("days", days);
		return "bespeak/bespeak_add";
	}

	/**
	 * 添加预约策略
	 * @param req
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add_bespeak")
	public Object add_bespeak(HttpServletRequest req, Authentication authentication){
		HashMap<String,Object> map = new HashMap<>();
		String name = req.getParameter("sname");
		String list = req.getParameter("list");
		String[] split = list.split("-");
		for (String string : split) {
			if(string.equals("")){
				map.put("message", "没有选择预约时间策略，添加失败！");
				return map;
			}
		}
		int i = bespeakService.xInsert(name, list);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[bespeak] F[add_bespeak] U[{}]; param[name:{},list:{}]; result:{}",
				users.getUser_id(),name,list,String.format("修改预约策略%s", i > 0 ? "成功":"失败"));
		map.put("message", String.format("添加预约策略%s", i > 0 ? "成功":"失败"));
		return map;
	}

	/**
	 * 删除预约策略
	 * @param bespeak_id
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/del_bespeak")
	public Object del_bespeak(@RequestParam(value="")Integer bespeak_id,Authentication authentication){
		HashMap<String,Object> map = new HashMap<>();
		ProSkuExample proSkuExample = new ProSkuExample();
		proSkuExample.or()
			.andBespeak_strategy_idEqualTo(bespeak_id);
		List<ProSku> list_psku = proSkuService.selectByExample(proSkuExample);
		if(list_psku.size()!=0){
			map.put("message", "已有商品关联此预约策略，不允许删除！");
			return map;
		}
		int i = bespeakService.xDelete(bespeak_id);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[bespeak] F[delete_bespeak] U[{}]; param[bespeak_id:{}; result:{}]",
				users.getUser_id(),bespeak_id,String.format("删除预约策略%s", i > 0 ? "成功":"失败"));
		map.put("message", String.format("删除预约策略%s", i > 0 ? "成功":"失败"));
		return map;
	}

	/**
	 * 跳转到修改预约策略
	 * @param bespeak_id
	 * @param p
	 * @param model
	 * @return
	 */
	@RequestMapping("/goto_edit_bespeak")
	public String goto_edit_bespeak(@RequestParam(value="bespeak_id")Integer bespeak_id,
			@RequestParam(value="p")Integer p,Model model){
		if(p==0){
			p+=1;
		}
		try {
			List bespeakList = bespeakService.getBespeakList(bespeak_id);
			Bespeak bespeak = bespeakService.selectByPrimaryKey(bespeak_id);
			List<BespeakTime> list_bespeak_strategy = JSONObject.parseArray(bespeak.getBespeak_strategy(), BespeakTime.class);
			Collections.sort(list_bespeak_strategy);
			model.addAttribute("current", p);
			model.addAttribute("bespeak_id", bespeak_id);
			model.addAttribute("name", bespeak.getName());
			model.addAttribute("list_data", list_bespeak_strategy);
			model.addAttribute("bespeakList", bespeakList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "bespeak/bespeak_edit";
	}

	/**
	 * 修改预约策略
	 * @param req
	 * @param authentication
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/edit_bespeak")
	public Object edit_bespeak(HttpServletRequest req,Authentication authentication){
		HashMap<String,Object> map = new HashMap<>();
		String name = req.getParameter("sname");
		String list = req.getParameter("list");
		String bespeak_id = req.getParameter("bespeak_id");
		int id = Integer.parseInt(bespeak_id);
		String[] split = list.split("-");
		for (String string : split) {
			if(string.equals("")){
				map.put("message", "预约时间策略有空值，修改失败！");
				return map;
			}
		}
		int i = bespeakService.xUpdate(name, list, id);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		//添加日志
		logger.info("M[bespeak] F[update_bespeak] U[{}]; param[bespeak_id:{},name:{},list:{}]; result:{}",
				users.getUser_id(),bespeak_id,name,list,String.format("修改预约策略%s", i > 0 ? "成功":"失败"));
		map.put("message", String.format("修改预约策略%s", i > 0 ? "成功":"失败"));
		return map;
	}
}
