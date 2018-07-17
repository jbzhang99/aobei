package com.aobei.train.service;

import com.aobei.train.model.AuthRoleExample;

import java.util.HashMap;

import com.aobei.train.model.AuthRole;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface AuthRoleService extends MbgReadService<Long, AuthRole, AuthRole, AuthRoleExample>,MbgWriteService<Long, AuthRole, AuthRole, AuthRoleExample>,MbgUpsertService<Long, AuthRole, AuthRole, AuthRoleExample>{
	
	/**
	 * 返回单个角色对应的权限集合
	 * @param res_id
	 * @return
	 */
	HashMap<String, Object> getResUrls(Integer res_id);
	/**
	 * 添加权限路径
	 * @param new_urls
	 * @param res_id
	 * @return
	 */
	HashMap<String, String> addUrls(String new_urls,String res_id);
	/**
	 * 添加角色
	 * @param role_name 角色名
	 * @param resIds 权限id
	 * @return
	 */
	HashMap<String,String> xInsert(String role_name,String resIds);
	/**
	 * 删除角色（修改状态）
	 * @param role_id
	 * @return
	 */
	int xDelete(Long role_id);
	/**
	 * 修改角色
	 * @param role_name
	 * @param resIds
	 * @param role_id
	 * @return
	 */
	HashMap<String,String> xUpdate(String role_name,String resIds,Long role_id );
}