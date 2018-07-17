package com.aobei.trainconsole.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aobei.train.model.Users;
import com.aobei.train.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aobei.train.model.School;
import com.aobei.train.model.SchoolExample;
import com.aobei.train.service.SchoolService;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.Status;

@Controller
@RequestMapping("/schoolmanager")
public class SchoolController {
	
	private static final Logger logger = LoggerFactory.getLogger(SchoolController.class);
	
	@Autowired
	private SchoolService schoolService;

	@Autowired
	private UsersService usersService;
	
	/**
	 * 跳转到学校信息列表页
	 */
	@RequestMapping("/goto_school_list")
	public String goto_school_list(Model map,
			@RequestParam(defaultValue="1") Integer p,
			@RequestParam(defaultValue="10") Integer ps) {
		//根据创建时间倒序查询
		SchoolExample example = new SchoolExample();
		//未删除的条件
		example.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		//设置排序字段
		example.setOrderByClause(SchoolExample.C.create_time + " desc");
		//分页查询
		Page<School> page = schoolService.selectByExample(example, p, ps);
		List<School> school_list = page.getList();
		if(school_list.size() == 0 & p > 1) {
			page = schoolService.selectByExample(example, p, ps);
			school_list = page.getList();
		}
		map.addAttribute("page", page);
		map.addAttribute("school_list", school_list);
		return "school/school_list";
	}

	/**
	 * 跳转到学校信息添加页面--跳转
	 * 带 列表页的页数参数跳转
	 */
	@RequestMapping("/school_add")
	public String school_add(Model map,Integer page_current_page ) {
		map.addAttribute("page_current_page", page_current_page);
		return "school/school_add";
	}

	/**
	 * 插入新的学校信息
	 * 
	 * @param school
	 * @return
	 */
	@RequestMapping("/save_school")
	@ResponseBody
	public Object save_school(School school,Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[school] F[save_school] U[{}] , params school:{} .",
				users.getUser_id(),school);
		// 执行插入操作
		int i = schoolService.xInsertSchool(school);
		logger.info("M[school] F[save_school] U[{}] , execute result:{}",
				users.getUser_id(),String.format("学校信息添加%s!", i > 0 ? "成功" : "失败"));
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("学校信息添加%s!", i > 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 学校信息详细展示
	 * 
	 * @param school_id
	 * @param map
	 * @return
	 */
	@RequestMapping("/school_detail")
	public String school_detail(Long school_id,Integer page_current_page,Model map) {
		// 根据id查询出学校的详细信息
		School school = schoolService.selectByPrimaryKey(school_id);
		map.addAttribute("school_info", school);
		map.addAttribute("page_current_page", page_current_page);
		return "school/school_detail";
	}

	/**
	 * 根据id查询出学校信息，并跳转到编辑页面---跳转
	 * 
	 * @param school_id
	 * @param map
	 * @return
	 */
	@RequestMapping("/school_edit")
	public String school_edit(Long school_id,Integer page_current_page, Model map) {
		// 根据id查询出学校的详细信息
		School school = schoolService.selectByPrimaryKey(school_id);
		map.addAttribute("school_info", school);
		map.addAttribute("page_current_page", page_current_page);
		return "school/school_edit";
	}

	/**
	 * 将编辑好的数据更新到数据库
	 * 
	 * @param school
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/school_edit_save")
	public Object school_edit_save(School school,Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[school] F[school_edit_save] U[{}] , params school:{} .",
				users.getUser_id(),school);
		// 将编辑好的数据插入到数据库中
		int i = schoolService.xUpdateByPrimaryKey(school);
		logger.info("M[school] F[school_edit_save] U[{}] , execute result:{}",
				users.getUser_id(),String.format("学校信息编辑%s!", i > 0 ? "成功" : "失败"));
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("学校信息编辑%s!", i > 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 根据school_id删除指定学校信息
	 */
	@ResponseBody
	@RequestMapping("/school_delete/{school_id}")
	public Object school_delete(@PathVariable("school_id") Long school_id,Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[school] F[school_delete] U[{}] , params school_id:{} .",
				users.getUser_id(),school_id);
		HashMap<String, String> map = new HashMap<>();
		int i = schoolService.xDeleteByPrimaryKey(school_id);
		logger.info("M[school] F[school_delete] U[{}] , execute result:{}",
				users.getUser_id(),String.format("学校信息删除%s!", i > 0 ? "成功" : "失败"));
		map.put("msg", String.format("学校信息删除%s!", i > 0 ? "成功" : "失败"));
		return map;
	}
}
