package com.aobei.train.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.model.AuthRoleUser;
import com.aobei.train.model.AuthRoleUserExample;
import com.aobei.train.model.SysUsers;
import com.aobei.train.model.SysUsersExample;
import com.aobei.train.model.Users;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.UsersMapper;
import com.aobei.train.model.UsersExample;
import com.aobei.train.service.AuthRoleService;
import com.aobei.train.service.AuthRoleUserService;
import com.aobei.train.service.SysUsersService;
import com.aobei.train.service.UsersService;

import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Status;
import custom.bean.UserP;

@Service
public class UsersServiceImpl extends MbgServiceSupport<UsersMapper, Long, Users, Users, UsersExample> implements UsersService{

	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private AuthRoleUserService authRoleUserService;
	@Autowired
	private AuthRoleService authRoleService;
	@Autowired
	private SysUsersService sysUsersService;
	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(usersMapper);
	}

	
	
	@Override
	public List<UserP> xSelectUser(List<SysUsers> listUser) {
		List<UserP> list_user = new ArrayList<UserP>();
		
		for (SysUsers sysUser : listUser) {				
			AuthRoleUserExample authSysUsersRoleExample = new AuthRoleUserExample();
			authSysUsersRoleExample.or().andSys_user_idEqualTo(sysUser.getSys_user_id());
			List<AuthRoleUser> list_sysUserRole = authRoleUserService.selectByExample(authSysUsersRoleExample);
			List<String> list = new ArrayList<String>();
			list_sysUserRole.forEach(n -> list.add(
					
					authRoleService.selectByPrimaryKey(n.getRole_id()).getRole_name()
					));
			UserP userP = new UserP();
			Users users = usersMapper.selectByPrimaryKey(sysUser.getUser_id());
			userP.setCreate_datetime(sysUser.getCreate_datetime());
			userP.setUser_id(sysUser.getUser_id());
			userP.setUsername(users.getUsername());
			userP.setDeleted(sysUser.getDeleted());
			userP.setDept_name(sysUser.getDept_name());
			userP.setJob_title(sysUser.getJob_title());
			userP.setName(sysUser.getName());
			userP.setPhone(sysUser.getPhone());
			userP.setStatus(sysUser.getStatus());
			userP.setUser_id(sysUser.getUser_id());
			userP.setUsername(users.getUsername());
			userP.setSys_user_id(sysUser.getSys_user_id());
			userP.setRoleList(list);
			list_user.add(userP);
		}
		return list_user;
	}
	@Transactional(timeout = 5)
	@Override
	public int xInserUserAndsysUser(Users users, String dept_name, String name, String job_title, String phone,
			String[] role) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		users.setUser_id(IdGenerator.generateId());
		//密码加密
		users.setPassword(encoder.encode(users.getPassword()));
		users.setStatus((byte)1);
		int i = usersMapper.insert(users);//加入数据到User表
		SysUsers sysUsers = new SysUsers();
		sysUsers.setCreate_datetime(new Date());
		sysUsers.setDeleted(Status.DeleteStatus.no.value);
		sysUsers.setDept_name(dept_name);
		sysUsers.setName(name);
		/*sysUsers.setDept_id(dept_id);暂时无用字段四个
		sysUsers.setJob_title_id(job_title_id);
		sysUsers.setSex(sex);
		sysUsers.setEmail(email);*/
		sysUsers.setJob_title(job_title);
		sysUsers.setPhone(phone);
		sysUsers.setStatus(1);
		sysUsers.setSys_user_id(IdGenerator.generateId());
		sysUsers.setUser_id(users.getUser_id());
		int j = sysUsersService.insert(sysUsers);//加入数据到sys_users 表
		if(role!=null){			
			for (String role_ids : role) {
				long role_id = Long.parseLong(role_ids);
				AuthRoleUser authSysUsersRole = new AuthRoleUser();
				authSysUsersRole.setRole_id(role_id);
				authSysUsersRole.setSys_user_id(sysUsers.getSys_user_id());
				authRoleUserService.insert(authSysUsersRole);//插入用户角色关联表auth_sys_user_role
			}
		}
		return i+j;
	}


	@Transactional(timeout = 5)
	@Override
	public int xDelete(Long user_id) {
		Users user = usersMapper.selectByPrimaryKey(user_id);
		user.setStatus((byte)0);//设置为不可用
		int i = usersMapper.updateByPrimaryKey(user);
		SysUsersExample sysUsersExample = new SysUsersExample();//删除user表数据
		sysUsersExample.or().andUser_idEqualTo(user_id);
		SysUsers sysUser = DataAccessUtils.singleResult(sysUsersService.selectByExample(sysUsersExample));
		sysUser.setDeleted(Status.DeleteStatus.yes.value);//设置为删除状态
		int j = sysUsersService.updateByPrimaryKey(sysUser);//设置sys_users表数据删除
		return i+j;
	}


	@Transactional(timeout = 5)
	@Override
	public int xUpdateUserAndsysUser(Long UserId, Long sysUserId, String[] role, String userName,SysUsers sys) {
		SysUsers sysUsers = new SysUsers();
		sysUsers.setSys_user_id(sysUserId);
		sysUsers.setDeleted(Status.DeleteStatus.no.value);
		sysUsers.setDept_name(sys.getDept_name());
		sysUsers.setName(sys.getName());
		/*sysUsers.setDept_id(dept_id);暂时无用字段四个
		sysUsers.setJob_title_id(job_title_id);
		sysUsers.setSex(sex);
		sysUsers.setEmail(email);*/
		sysUsers.setJob_title(sys.getJob_title());
		sysUsers.setPhone(sys.getPhone());
		sysUsers.setStatus(1);
		int j = sysUsersService.updateByPrimaryKeySelective(sysUsers);//修改数据到sys_users 表
		
		AuthRoleUserExample authRoleUserExample = new AuthRoleUserExample();
		authRoleUserExample.or().andSys_user_idEqualTo(sysUsers.getSys_user_id());
		List<AuthRoleUser> list_role = authRoleUserService.selectByExample(authRoleUserExample);
		for (AuthRoleUser authRoleUser : list_role) {
			authRoleUserService.deleteByPrimaryKey(authRoleUser);//循环删除中间表数据
		}
		
		if(role!=null){			
			for (String role_ids : role) {
				long role_id = Long.parseLong(role_ids);
				AuthRoleUser authSysUsersRole = new AuthRoleUser();
				authSysUsersRole.setRole_id(role_id);
				authSysUsersRole.setSys_user_id(sysUsers.getSys_user_id());
				authRoleUserService.insertSelective(authSysUsersRole);//修改用户角色关联表auth_sys_user_role（直接插入数据）
			}
		}
		return j;
	}

	@Override
	public Users xSelectUserByUsername(String username) {
		UsersExample usersExample = new UsersExample();
		usersExample.or().andUsernameEqualTo(username);
		return DataAccessUtils.singleResult(this.selectByExample(usersExample));
	}


	@Transactional(timeout = 5)
	@Override
	public HashMap<String, String> userSwitc(Long user_id) {
		HashMap<String, String> map = new HashMap<String,String>();
		SysUsersExample example = new SysUsersExample();
		example.or().andUser_idEqualTo(user_id);
		SysUsers result = DataAccessUtils.singleResult(sysUsersService.selectByExample(example));//所有账号(姓名)
		if(result.getStatus()==0){			
			result.setStatus(1);;//设置为启用
			int j = sysUsersService.updateByPrimaryKeySelective(result);
			map.put("message", String.format("启用用户%s!", j > 0 ? "成功":"失败"));
		}else{
			result.setStatus(0);;//设置为禁用
			int j = sysUsersService.updateByPrimaryKeySelective(result);
			map.put("message", String.format("禁用用户%s!", j > 0 ? "成功":"失败"));
		}
		return map;
	}


	@Transactional(timeout = 5)
	@Override
	public HashMap<String, String> updatePassword(Long user_id,String value_p) {
		HashMap<String, String> map = new HashMap<String,String>();
		Users users = usersMapper.selectByPrimaryKey(user_id);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(value_p!=null){			
			users.setPassword(encoder.encode(value_p));
			int i = usersMapper.updateByPrimaryKeySelective(users);
			map.put("message", String.format("账号【"+users.getUsername()+"】修改密码%s!",i > 0 ? "成功":"失败"));
		}else{
			map.put("message", "请输入新密码！");
		}
		return map;
	}
}