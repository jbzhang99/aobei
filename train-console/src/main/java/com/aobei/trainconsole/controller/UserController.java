package com.aobei.trainconsole.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.aobei.train.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.aobei.train.model.AuthRole;
import com.aobei.train.model.AuthRoleExample;
import com.aobei.train.model.AuthRoleUser;
import com.aobei.train.model.AuthRoleUserExample;
import com.aobei.train.model.OperateLog;
import com.aobei.train.model.OperateLogExample;
import com.aobei.train.model.OperateLogExample.Criteria;
import com.aobei.train.model.SysUsers;
import com.aobei.train.model.SysUsersExample;
import com.aobei.train.model.Users;
import com.aobei.train.model.UsersExample;
import com.aobei.train.service.AuthRoleService;
import com.aobei.train.service.AuthRoleUserService;
import com.aobei.train.service.OperateLogService;
import com.aobei.train.service.SysUsersService;
import com.aobei.train.service.UsersService;
import com.aobei.trainconsole.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.Log;
import custom.bean.Status;
import custom.bean.UserP;



@Controller
@RequestMapping("/userManager/user")
public class UserController {

	@Autowired
	private UsersService userService;
	@Autowired
	private AuthRoleService authRoleService;
	@Autowired
	private AuthRoleUserService authRoleUserService;
	@Autowired
	private SysUsersService sysUsersService;
	@Autowired
	private OperateLogService operateLogService;
	@Autowired
	private UsersService usersService;

	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	/**
	 * 账号管理列表页
	 * @param p 当前页
	 * @param ps 每页显示数量
	 * @param model
	 * @return
	 */
	@RequestMapping("/user_list")
	public String user_list(@RequestParam(defaultValue="1")Integer p,
							@RequestParam(defaultValue="10")Integer ps,Model model){
		SysUsersExample sysUsersExample = new SysUsersExample();
		sysUsersExample.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		Page<SysUsers> page = sysUsersService.selectByExample(sysUsersExample,p,ps);
		int number = page.getPageSize()*(page.getPageNo()-1);
		List<UserP> list_user = userService.xSelectUser(page.getList());
		
		model.addAttribute("number", number);
		model.addAttribute("page", page);
		model.addAttribute("list_user", list_user);
		model.addAttribute("current", p);
		return "user/user_list";
	}
	
	
	/**
	 * 跳转到添加页面
	 * @param current
	 * @param model
	 * @return
	 */
	@RequestMapping("/user_goto_add")
	public String user_goto_add(@RequestParam(value="p")Integer current,Model model){
		AuthRoleExample authRoleExample = new AuthRoleExample();
		authRoleExample.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<AuthRole> list_role = authRoleService.selectByExample(authRoleExample);//所有角色
		model.addAttribute("list_role", list_role);
		model.addAttribute("current", current);
		return "user/user_add";
	}
	/**
	 * 添加用户信息
	 * @param users 用户对象(添加user表和sysUser表)
	 * @param name 用户名称
	 * @param dept_name 部门名称
	 * @param job_title 职位名称
	 * @param phone 联系电话
	 * @param role 角色数组
	 * @return
	 */
	@Transactional
	@ResponseBody
	@RequestMapping("/user_add")
	public Object user_add(Users users, @RequestParam(value="name",required=false)String name,
						   @RequestParam(value="dept_name",required=false)String dept_name,
						   @RequestParam(value="job_title",required=false)String job_title,
						   @RequestParam(value="phone",required=false)String phone,
						   @RequestParam(value="role",required=false)String[] role, Authentication authentication){
		HashMap<String, String> map = new HashMap<String,String>();
		List<Users> list_allUser = userService.selectByExample(new UsersExample());
		for (Users user : list_allUser) {
			if(user.getUsername().equals(users.getUsername())){
				map.put("message", "已有相同[账号名称] 存在，请重新填写！s");
				return map;
			}
		}
		
		int m = userService.xInserUserAndsysUser(users,dept_name,name, job_title, phone, role);//执行了两次添加操作
		Users u = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[user] F[add_user] U[{}] param[users:{},name:{},dept_name:{},job_title:{},phone:{},角色数组role:{}];  result:{}",
				u.getUser_id(),users,name,dept_name,job_title,phone,role,String.format("添加账号信息%s" , m > 1 ? "成功":"失败"));
		map.put("message", String.format("添加账号%s！", m > 1 ? "成功" : "失败"));
		return map;
	}
	/**
	 * 跳转编辑页
	 * @param current
	 * @param model
	 * @param user_id
	 * @return
	 */
	@RequestMapping("/user_goto_edit")
	public String user_goto_edit(@RequestParam(value="p")Integer current,Model model,
								 @RequestParam(value="user_id")Long user_id) {
		Users users = userService.selectByPrimaryKey(user_id);//user对象
		
		AuthRoleExample authRoleExample = new AuthRoleExample();
		authRoleExample.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<AuthRole> list_role = authRoleService.selectByExample(authRoleExample);//所有角色,前台需要展示
		
		SysUsersExample sysUsersExample = new SysUsersExample();
		sysUsersExample.or()
				.andUser_idEqualTo(user_id);
		SysUsers sysUsers = DataAccessUtils.singleResult(sysUsersService.selectByExample(sysUsersExample)) ;//sysUser对象
		
		AuthRoleUserExample authRoleUserExample = new AuthRoleUserExample();
		authRoleUserExample.or()
				.andSys_user_idEqualTo(sysUsers.getSys_user_id());
		List<AuthRoleUser> role_checked = authRoleUserService.selectByExample(authRoleUserExample);//用户对应的角色集合
		List<Long> list = new ArrayList<Long>();//用户对应角色id集合
		role_checked.forEach(role->{
			list.add(role.getRole_id());
		});
		String json = null;
		try {
			json = JacksonUtil.object_to_json(list);
			model.addAttribute("jsonArray", json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error("json转换异常数据:{},异常报错堆栈信息:{}",list,e);
		}
		model.addAttribute("sys_user_id", sysUsers.getSys_user_id());
		model.addAttribute("user_id", user_id);
		model.addAttribute("sysUsers", sysUsers);
		model.addAttribute("list_role", list_role);
		model.addAttribute("users", users);
		model.addAttribute("current", current);
		return "user/user_edit";
	}
	/**
	 * 删除账户信息
	 * 删除时，同时删除users表的数据和sysUsers表的数据
	 * @param user_id 前台传递的用户id
	 * @return
	 */
	@Transactional
	@ResponseBody
	@RequestMapping("/del_user")
	public Object del_user(@RequestParam(value="user_id")Long user_id,Authentication authentication){
		HashMap<String, String> map = new HashMap<String,String>();
		int m = userService.xDelete(user_id);//设置user表和sysUser表为修改状态
		Users u = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[user] F[delete_user] U[{}] param[user_id:{}];  result:{}",
				u.getUser_id(),user_id,String.format("删除账号信息%s" , m > 1 ? "成功":"失败"));
		map.put("message", String.format("删除账号信息%s", m > 1 ? "成功":"失败"));
		return map;
	}

	/**
	 * 修改账号信息
	 * @param sys
	 * @param username
	 * @param role
	 * @param userId
	 * @param sysUserId
	 * @param authentication
	 * @return
	 */
	@Transactional
	@ResponseBody
	@RequestMapping("/user_edit")
	public Object user_edit(SysUsers sys,@RequestParam(value="username")String username,
						@RequestParam(value="role",required=false)String[] role,
						@RequestParam(value="userId") Long userId,
						@RequestParam(value="sysUserId") Long sysUserId,Authentication authentication){
		HashMap<String, String> map = new HashMap<String,String>();
		int j = userService.xUpdateUserAndsysUser(userId, sysUserId, role, username, sys);//此处不修改user表数据
		Users u = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[user] F[update_user] U[{}] param[sysUser:{},username:{},role:{},userId:{},sysUserId:{}];  result:{}",
				u.getUser_id(),sys,username,role,userId,sysUserId,String.format("修改账号信息%s" , j > 0 ? "成功":"失败"));
		map.put("message", String.format("修改账号%s！", j > 0 ? "成功" : "失败"));
		return map;
	}
	/**
	 * 启用 禁用 用户功能
	 * @param user_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/user_switch")
	public Object user_start(@RequestParam(value="user_id",required=false)Long user_id,Authentication authentication){
		HashMap<String, String> map = userService.userSwitc(user_id);
		Users u = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[user] F[switch_user] U[{}] param[user_id:{}];  result:{}",
				u.getUser_id(),user_id,map.get("message"));
		return map;
	}
	/**
	 * 修改密码方法
	 * @param user_id 用户id
	 * @param value_p 新密码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/update_password")
	public Object update_password(@RequestParam(value="user_id")Long user_id,
								  @RequestParam(value="value_p",required=false)String value_p,Authentication authentication){
		HashMap<String, String> map = userService.updatePassword(user_id, value_p);
		Users u = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[user] F[update_password_user] U[{}] param[user_id:{},新密码value_p:{}];  result:{}",
				u.getUser_id(),user_id,value_p,map.get("message"));
		return map;
	}
	/**
	 * 日志管理查看列表页
	 * @param p
	 * @param ps
	 * @param model
	 */
	@RequestMapping("/operateLog_list")
	public String  operateLog_list(@RequestParam(defaultValue="1")Integer p,
								   @RequestParam(defaultValue="10")Integer ps,Model model,
								   @RequestParam(defaultValue="0")Long select_userId,
								   @RequestParam(value="begin",required=false)@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")Date begin,
								   @RequestParam(value="end",required=false)@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")Date end,
								   @RequestParam(required = false)String b_time,
								   @RequestParam(required = false)String e_time){
		
		OperateLogExample operateLogExample = new OperateLogExample();
		Criteria or = operateLogExample.or();
		if(select_userId!=0){
			or.andUser_idEqualTo(select_userId);
		}
		if(begin!=null & end !=null){
			or.andCreate_datetimeBetween(begin, end);
		}
		operateLogExample.setOrderByClause(OperateLogExample.C.create_datetime+" desc");
		Page<OperateLog> page = operateLogService.selectByExample(operateLogExample,p,ps);
		List<Log> list = new ArrayList<Log>();
		
		for (OperateLog operateLog : page.getList()) {
			Long user_id = operateLog.getUser_id();
			Log log = new Log();
			Users users = userService.selectByPrimaryKey(user_id);
			SysUsersExample sysUsersExample = new SysUsersExample();
			sysUsersExample.or()
					.andUser_idEqualTo(user_id);
			SysUsers sysUsers = DataAccessUtils.singleResult(sysUsersService.selectByExample(sysUsersExample));

			AuthRoleUserExample authRoleUserExample = new AuthRoleUserExample();
			authRoleUserExample.or()
					.andSys_user_idEqualTo(sysUsers.getSys_user_id());
			List<AuthRoleUser> list_role = authRoleUserService.selectByExample(authRoleUserExample);//从中间表找到角色id集合

			List<AuthRole> list_str = list_role.stream().map(n ->{//角色集合数据
				AuthRole authRole = authRoleService.selectByPrimaryKey(n.getRole_id());
				return authRole;
			}).collect(Collectors.toList());
			log.setRolename(list_str);
			log.setName(sysUsers.getName());//用户名称
			log.setUsername(users.getUsername());//账号名称
			//角色
			//操作记录
			log.setOperated(operateLog.getOperate());
			log.setOperated_time(operateLog.getCreate_datetime());//操作时间
			list.add(log);
		}
		SysUsersExample example = new SysUsersExample();
		example.or().andDeletedEqualTo(0);
		List<SysUsers> list_sysUsers = sysUsersService.selectByExample(example);//加载下拉框数据（所有姓名）
		model.addAttribute("page", page);
		model.addAttribute("log_list", list);
		model.addAttribute("list_sysUsers", list_sysUsers);
		model.addAttribute("select_userId", select_userId);
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("b_time", b_time);
		model.addAttribute("e_time", e_time);
		return "user/operate_log";
	}
}
