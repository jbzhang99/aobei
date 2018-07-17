package com.aobei.train.service;

import com.aobei.train.model.UsersExample;

import java.util.HashMap;
import java.util.List;

import com.aobei.train.model.SysUsers;
import com.aobei.train.model.Users;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;

import custom.bean.UserP;

import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface UsersService extends MbgReadService<Long, Users, Users, UsersExample>,MbgWriteService<Long, Users, Users, UsersExample>,MbgUpsertService<Long, Users, Users, UsersExample>{

	/**
	 * 用户修改密码
	 * @param user_id 用户id
	 * @param value_p 新密码
	 * @return
	 */
	HashMap<String,String> updatePassword(Long user_id,String value_p);
	/**
	 * 用户的禁用 启用
	 * @param user_id sysUser表的user_id
	 * @return
	 */
	HashMap<String,String> userSwitc(Long user_id);
	/**
	 * 封装user列表信息返回
	 * @param listUser 
	 * @return
	 */
	List<UserP> xSelectUser(List<SysUsers> listUser);
	/**
	 * 插入数据，分别添加user表和sysUser表数据
	 * @param users
	 * @param dept_name
	 * @param name
	 * @param job_title
	 * @param phone
	 * @param role
	 * @return
	 */
	int xInserUserAndsysUser(Users users,String dept_name,String name,String job_title,
			String phone,String[] role);
	/**
	 * 改变状态为修改
	 * @param user_id
	 * @return
	 */
	int xDelete(Long user_id);
	/**
	 * 修改user,sysUset,authRoleUser三表数据
	 * @param UserId
	 * @param sysUserId
	 * @param role
	 * @param name
	 * @param sys
	 * @return
	 */
	int xUpdateUserAndsysUser(Long UserId ,Long sysUserId,String[] role,String userName,SysUsers sys);

	/**
	 * 根据用户名获取当前系统登陆用户的信息Users
	 * @param username
	 * @return
	 */
	Users xSelectUserByUsername(String username);
}