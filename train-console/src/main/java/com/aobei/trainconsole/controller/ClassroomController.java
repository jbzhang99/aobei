package com.aobei.trainconsole.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aobei.train.model.*;
import com.aobei.train.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.aobei.train.service.ClassroomService;
import com.aobei.train.service.CourseService;
import com.aobei.train.service.SchoolService;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.Status;


@Controller
@RequestMapping("/classroommanager")
public class ClassroomController {
	
	private static final Logger logger = LoggerFactory.getLogger(ClassroomController.class);
	
	@Autowired
	private ClassroomService classroomService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private CourseService courseService;

	@Autowired
	private UsersService usersService;
	
	/**
	 * 跳转到教室信息列表的首页
	 * @param map 携带转发数据
	 * @param p 页码
	 * @return
	 */
	@RequestMapping("/goto_classroom_list")
	public String goto_classroom_list(Model map,
			@RequestParam(defaultValue="1") Integer p,
			@RequestParam(defaultValue="10") Integer ps) {
		//根据创建时间倒序查询
		ClassroomExample example = new ClassroomExample();
		example.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		example.setOrderByClause(ClassroomExample.C.create_time + " desc");
		Page<ClassroomInfo> page = classroomService.xSelectClassroomList(example, p, ps);
		List<ClassroomInfo> classroomInfo_list = page.getList();
		map.addAttribute("classroom_list", classroomInfo_list);
		map.addAttribute("page", page);
		return "classroom/classroom_list";
	}

	/**
	 * 跳转到教室信息添加页面
	 * @return
	 */
	@RequestMapping("/classroom_add")
	public String classroom_add(Integer page_current_page,Model map) {
		SchoolExample example = new SchoolExample();
		example.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<School> school_list = schoolService.selectByExample(example);
		CourseExample courseExample = new CourseExample();
		courseExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Course> courses = courseService.selectByExample(courseExample);
		map.addAttribute("page_current_page", page_current_page);
		map.addAttribute("school_list", school_list);
		map.addAttribute("courseteam_list", courses);
		return "classroom/classroom_add";
	}
	
	/**
	 * 异步表单提交
	 * @param classroom 封装教室信息
	 * @param support_course_ids 支持的课程id数组
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save_classroom")
	public Object save_classroom(Classroom classroom,Authentication authentication,
			@RequestParam(value="support_course_ids")String[] support_course_ids) {
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[classroom] F[save_classroom] U[{}] , params classroom:{},support_course_ids:{} .",
                users.getUser_id(),classroom,support_course_ids);
        int i = classroomService.xInsertClassroom(classroom, support_course_ids);
        logger.info("M[classroom] F[save_classroom] U[{}] , execute result:{}",
                users.getUser_id(),String.format("教室信息添加%s!", i > 0 ? "成功" : "失败"));
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("教室信息添加%s!", i > 0 ? "成功" : "失败"));
		return map;
	}
	
	/**
	 * 异步查询课程表
	 * @param classroom_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get_class_schedule")
	public Object get_class_schedule(String classroom_id) {
		ClassroomExample example = new ClassroomExample();
		example.or().andClassroom_idEqualTo(Long.valueOf(classroom_id));
		Classroom classroom = DataAccessUtils.singleResult(classroomService.selectByExample(example));
		String json = classroom.getTimescope_json();
		List<Schedule> mList = new ArrayList<>();
		if(!StringUtils.isEmpty(json)) {
			mList = JSONObject.parseArray(json,Schedule.class);
		}
		return mList;
	}
	
	/**
	 * 跳转到教室信息编辑页面
	 * @param page_current_page 当前页
	 * @param classroom_id 
	 * @param map
	 * @return
	 */
	@RequestMapping("/classroom_edit")
	public String goto_classroom_edit(Integer page_current_page,Long classroom_id,Model map) {
		Classroom classroom = classroomService.selectByPrimaryKey(classroom_id);
		ClassroomInfo classroomInfo = classroomService.getClassroomInfo(classroom);
		
		SchoolExample schoolExample = new SchoolExample();
		schoolExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<School> school_list = schoolService.selectByExample(schoolExample);
		CourseExample courseExample = new CourseExample();
		courseExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Course> allCourseTeam = courseService.selectByExample(courseExample);
		map.addAttribute("school_list", school_list);
		map.addAttribute("allcourseteam", allCourseTeam);
		map.addAttribute("classroom", classroomInfo);
		map.addAttribute("page_current_page", page_current_page);
		return "classroom/classroom_edit";
	}
	
	/**
	 * 异步提交并保存修改信息
	 * @param classroom
	 * @param support_course_ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/edit_submit_classroom")
	public Object edit_submit_classroom(Classroom classroom,Authentication authentication,
			@RequestParam(value="support_course_ids")String[] support_course_ids) {
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[classroom] F[edit_submit_classroom] U[{}] , params classroom:{},support_course_ids:{} .",
                users.getUser_id(),classroom,support_course_ids);
		int i = classroomService.xUpdateClassroom(classroom, support_course_ids);
        logger.info("M[classroom] F[edit_submit_classroom] U[{}] , execute result:{}",
                users.getUser_id(),String.format("教室信息编辑%s!", i > 0 ? "成功" : "失败"));
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("教室信息编辑%s!", i > 0 ? "成功" : "失败"));
		return map;
	}
	
	/**
	 * 教室信息删除
	 * @param classroom_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/classroom_delete/{classroom_id}")
	public Object classroom_delete(@PathVariable("classroom_id") Long classroom_id,Authentication authentication) {
        Users users = usersService.xSelectUserByUsername(authentication.getName());
        logger.info("M[classroom] F[classroom_delete] U[{}] , params classroom_id:{} .",
                users.getUser_id(),classroom_id);
		int i = classroomService.xDeleteClassroom(classroom_id);
        logger.info("M[classroom] F[classroom_delete] U[{}] , execute result:{}",
                users.getUser_id(),String.format("执行教室删除%s!", i > 0 ? "成功" : "失败"));
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("教室信息删除%s!", i > 0 ? "成功" : "失败"));
		return map;
	}

}
