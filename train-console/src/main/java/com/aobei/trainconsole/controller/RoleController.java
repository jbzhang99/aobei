package com.aobei.trainconsole.controller;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.model.AuthResExample.Criteria;
import com.aobei.train.service.*;
import com.aobei.trainconsole.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/roleManager/role")
public class RoleController {
	
	@Autowired
	private AuthResService authResService;
	
	@Autowired
	private AuthRoleService authRoleService;
	
	@Autowired
	private AuthRoleResService authRoleResService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private OperateLogService operateLogService;


	private static Logger logger = LoggerFactory.getLogger(RoleController.class);
	/**
	 * 角色管理列表页
	 * @param p
	 * @param ps
	 * @param model
	 * @return
	 */
	@RequestMapping("/role_list")
	public String role_list(@RequestParam(defaultValue="1")Integer p,
							@RequestParam(defaultValue="10")Integer ps,Model model){
		AuthRoleExample authRoleExample = new AuthRoleExample();
		authRoleExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		Page<AuthRole> page = authRoleService.selectByExample(authRoleExample,p,ps);
		int number = page.getPageSize()*(page.getPageNo()-1);
		
		model.addAttribute("number", number);
		model.addAttribute("current", p);
		model.addAttribute("page", page);
		model.addAttribute("list", page.getList());
		return "roleManager/role_list";
	}
	/**
	 * 跳转到添加角色页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/role_goto_add")
	public String role_goto_add(Model model,@RequestParam(value="p")Integer p){
		//查询所有基础角色
		//再页面展示集合，通过tag标签来分组
		HashMap<String,List<AuthRes>> map = new HashMap<String, List<AuthRes>>();
		AuthResExample authResExample = new AuthResExample();
		authResExample.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<AuthRes> list_authRes = authResService.selectByExample(authResExample);//所有基础角色集合auth_res
		/*list_authRes.stream().forEach(authRes -> {
			AuthResExample example = new AuthResExample();
			authResExample.or().andDeletedEqualTo(0); 
			map.put(authRes.getTag(), authResService.selectByExample(example));
		});*/
		Map<String ,List<Map<String,Object>> > map2 =new HashMap<>();
		for(AuthRes res  :list_authRes){
			Map<String,Object> map1= new HashMap<>();
			map1.put("res",res);
		    List<Map<String,Object>>  resss = map2.get(res.getTag());

			if(resss==null){
				resss= new ArrayList<>();
			}
			resss.add(map1);
           map2.put(res.getTag(),resss);
		}
		model.addAttribute("map2", map2);
		model.addAttribute("map", map);
		model.addAttribute("list_authRes", list_authRes);
		model.addAttribute("current", p);
		return "roleManager/role_add";
	}
	/**
	 * 角色添加功能
	 * @param role_name
	 * @param resIds
	 * @return
	 */
	@Transactional
	@ResponseBody
	@RequestMapping("/role_add")
	public Object role_add(@RequestParam(value="role_name",required=false)String role_name,
					       @RequestParam(value="resIds",required=false)String resIds,Authentication authentication){
		HashMap<String,String> map = authRoleService.xInsert(role_name, resIds);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[role] F[add_role] U[{}] param[role_name:{},resIds:{}];  result:{}",
				users.getUser_id(),role_name,resIds,map.get("message"));
		return map;
	}
	/**
	 * 删除角色功能
	 * @param role_id
	 * @return
	 */
	@Transactional
	@ResponseBody
	@RequestMapping("/role_del")
	public Object role_del(@RequestParam(value="role_id")Long role_id,Authentication authentication){
		HashMap<String, String> map = new HashMap<String,String>();
		int i = authRoleService.xDelete(role_id);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[role] F[delete_role] U[{}] param[role_id:{}];  result:{}",
				users.getUser_id(),role_id, String.format("删除角色信息%s", i > 0 ? "成功":"失败"));
		map.put("message", String.format("删除角色信息%s", i > 0 ? "成功":"失败"));
		return map;
	}
	
	@RequestMapping("/role_goto_edit")
	public String role_goto_edit(@RequestParam(value="role_id")Long role_id,Model model,
								 @RequestParam(value="p")Integer p){
		AuthResExample authResExample = new AuthResExample();
		authResExample.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<AuthRes> list_authRes = authResService.selectByExample(authResExample);//所有基础角色集合auth_res
		Map<String ,List<Map<String,Object>> > map2 =new HashMap<>();
		for(AuthRes res  :list_authRes){
			Map<String,Object> map1= new HashMap<>();
			map1.put("res",res);
		    List<Map<String,Object>>  resss = map2.get(res.getTag());

			if(resss==null){
				resss= new ArrayList<>();
			}
			resss.add(map1);
           map2.put(res.getTag(),resss);
		}
		Set<Integer> set = new HashSet<Integer>();//返回给前台的所有已选中的权限
		//遍历所有角色
		AuthRoleExample authRoleExample = new AuthRoleExample();
		authRoleExample.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<AuthRole> list_role = authRoleService.selectByExample(authRoleExample);
		//遍历角色中间表和权限中间表
		AuthRoleResExample authRoleResExample = new AuthRoleResExample(); 
		authRoleResExample.or().andRole_idEqualTo(role_id);
		List<AuthRoleRes> list_res = authRoleResService.selectByExample(authRoleResExample);
		for (AuthRole authRole : list_role) {
			for (AuthRoleRes authRoleRes : list_res) {
				if(authRole.getRole_id().equals(authRoleRes.getRole_id())){
					set.add(authRoleRes.getRes_id());
				}
			}
		}
		AuthRoleExample example = new AuthRoleExample();
		example.or().andRole_idEqualTo(role_id);
		AuthRole role = DataAccessUtils.singleResult(authRoleService.selectByExample(example));
		model.addAttribute("role_id", role_id);
		model.addAttribute("roleName", role.getRole_name());
		model.addAttribute("set", set);
		model.addAttribute("map2", map2);
		model.addAttribute("current", p);
		model.addAttribute("list_authRes", list_authRes);
		return "roleManager/role_edit";
	}
	/**
	 * 编辑角色信息
	 * @param role_name 角色名称
	 * @param resIds 权限id数组
	 * @param role_id 角色id
	 * @return
	 */
	@Transactional
	@ResponseBody
	@RequestMapping("/role_edit")
	public Object role_edit(@RequestParam(value="role_name",required=false)String role_name,
		       @RequestParam(value="resIds",required=false)String resIds,
		       @RequestParam(value="roleId",required=false)Long role_id ,Authentication authentication){
		HashMap<String,String> map = authRoleService.xUpdate(role_name, resIds, role_id);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[role] F[update_role] U[{}] param[role_name:{},resIds:{},role_id:{}];  result:{}",
				users.getUser_id(),role_name,resIds,role_id,map.get("message"));
		return map;
	}
	/**
	 * 权限列表
	 * @param p
	 * @param ps
	 * @param model
	 * @return
	 */
	@RequestMapping("/privileges_list")
	public String privileges_list(@RequestParam(defaultValue="1")Integer p,
								  @RequestParam(defaultValue="10")Integer ps,Model model,String roleKey){
		AuthResExample authResExample = new AuthResExample();
		Criteria or = authResExample.or();
		Criteria or1 = authResExample.or();
		if(roleKey!=null){
			or.andRole_keyLike("%"+roleKey+"%");
			or1.andTagLike("%"+roleKey+"%");
		}
		authResExample.setOrderByClause(AuthResExample.C.tag+" asc");
		or.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		Page<AuthRes> page = authResService.selectByExample(authResExample,p,ps);
		model.addAttribute("current", p);
		model.addAttribute("page", page);
		model.addAttribute("list_res", page.getList());
		model.addAttribute("roleKey", roleKey);
		return "roleManager/privileges_list";
	}
	/**
	 * 模态框展示权限数据
	 * @param res_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/res_list")
	public Object res_list(@RequestParam(value="res_id")Integer res_id){
		HashMap<String, Object> map = authRoleService.getResUrls(res_id);
		return map;
	}
	/**
	 * 跳转到添加权限页面
	 * @return
	 */
	@RequestMapping("/goto_add_privileges")
	public String goto_add_privileges(Model model,@RequestParam(value="p")Integer p){
		
		model.addAttribute("current", p);
		return "roleManager/add_privileges";
	}
	/**
	 * 权限添加(每添加一项权限，都会和角色[系统管理员] 关联)
	 * @param authRes
	 * @return
	 */
	@Transactional
	@ResponseBody
	@RequestMapping("/add_privileges")
	public Object add_privileges(AuthRes authRes,Authentication authentication){
		HashMap<String, String> map = new HashMap<String,String>();
		
		authRes
			.setDeleted(Status.DeleteStatus.no.value);//0
		authRes.setCreate_datetime(new Date());
		int i = authResService.insert(authRes);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[privileges] F[add_privileges] U[{}] param[authRes:{}];  result:{}",
				users.getUser_id(),authRes,i>0?"成功":"失败");
		AuthResExample authResExample = new AuthResExample();
		authResExample.or()
				.andNameLike("%"+authRes.getName()+"%");
		List<AuthRes> list = authResService.selectByExample(authResExample);
		if(list.size()!=1){
			map.put("message", "已有["+authRes.getName()+"] 权限，请勿重复添加！");
			return map;
		}
		AuthRes res = DataAccessUtils.singleResult(list);
		//为系统管理员自动加入新权限
		AuthRoleRes authRoleRes = new AuthRoleRes();
		authRoleRes.setRes_id(res.getRes_id());
		authRoleRes.setRole_id(1078640810622738432L);
		int j = authRoleResService.insert(authRoleRes);
		int m = i + j;
		logger.info("M[privileges] F[add_privileges_admin] U[{}] param[authRoleRes:{}];  result:{}",
				users.getUser_id(),authRoleRes,String.format("添加权限%s", m > 1 ? "成功": "失败"));
		map.put("message", String.format("添加["+authRes.getName()+"] 权限%s", m > 1 ? "成功": "失败"));
		return map;
	}
	//添加权限路径
	@ResponseBody
	@RequestMapping("/add_urls")
	public Object add_urls(@RequestParam(value="new_urls")String new_urls,
						   @RequestParam(value="res_id")String res_id,Authentication authentication){
		HashMap<String,String> map = authRoleService.addUrls(new_urls, res_id);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[privileges] F[add_privileges_url] U[{}] param[new_urls:{},res_id:{}];  result:{}",
				users.getUser_id(),new_urls,res_id,map.get("message"));
		return map;
	}
	/**
	 * 添加权限时，展示的权限urls
	 * @param model
	 * @param res_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/urls")
	public Object urls(Model model,@RequestParam(value="res_id")Integer res_id){
		HashMap<String, Object> map = new HashMap<String,Object>();
		AuthRes res = authResService.selectByPrimaryKey(res_id);
		List<String> list = new ArrayList<String>();
		String[] split = res.getUrls().split(",");
		for (int i = 0; i < split.length; i++) {
			list.add(split[i]);
		}
		try {
			String object_to_json = JacksonUtil.object_to_json(list);
			map.put("list_url", object_to_json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("resId", res_id);
		return map;
	}
}
