package com.aobei.train.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.model.AuthRes;
import com.aobei.train.model.AuthRole;
import com.alibaba.fastjson.JSON;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.AuthRoleMapper;
import com.aobei.train.model.AuthRoleExample;
import com.aobei.train.model.AuthRoleRes;
import com.aobei.train.model.AuthRoleResExample;
import com.aobei.train.service.AuthResService;
import com.aobei.train.service.AuthRoleResService;
import com.aobei.train.service.AuthRoleService;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Status;

@Service
public class AuthRoleServiceImpl extends MbgServiceSupport<AuthRoleMapper, Long, AuthRole, AuthRole, AuthRoleExample> implements AuthRoleService{

	@Autowired
	private AuthRoleMapper authRoleMapper;
	@Autowired
	private AuthRoleResService authRoleResService;
	@Autowired
	private AuthResService authResService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(authRoleMapper);
	}

	@Transactional(timeout = 5)
	@Override
	public HashMap<String,String> xInsert(String role_name, String resIds) {
		HashMap<String, String> map = new HashMap<String,String>();
		AuthRole authRole = new AuthRole();
		if(role_name!="" || role_name!=null){//增加角色数据auth_role			
			authRole.setCreate_datetime(new Date());
			authRole.setRole_id(IdGenerator.generateId());
			authRole.setRole_name(role_name);
			authRole.setDeleted(Status.DeleteStatus.no.value);
			authRoleMapper.insert(authRole);
		}else{
			map.put("message", "添加失败，角色名为空！");
			return map;
		}
		if(resIds!="" || resIds !=null){			
			List<Integer> list_resIds = JSON.parseArray(resIds, Integer.class);
			list_resIds.stream().forEach(res_id -> {
				AuthRoleRes authRoleRes = new AuthRoleRes();
				authRoleRes.setRes_id(res_id);
				authRoleRes.setRole_id(authRole.getRole_id());
				authRoleResService.insert(authRoleRes);//中间表循环插入数据auth_role__res
			});
		}else{
			map.put("message", "添加失败，没有选择权限！");
			return map;
		}
		map.put("message", String.format("添加角色添加成功！"));
		return map;
	}

	@Transactional
	@Override
	public int xDelete(Long role_id) {
		AuthRole authRole = authRoleMapper.selectByPrimaryKey(role_id);
		authRole.setDeleted(Status.DeleteStatus.yes.value);
		return authRoleMapper.updateByPrimaryKey(authRole);
	}

	@Override
	public HashMap<String, String> xUpdate(String role_name, String resIds, Long role_id) {
		HashMap<String, String> map = new HashMap<String,String>();
		AuthRole role = authRoleMapper.selectByPrimaryKey(role_id);
		if(role_name!="" || role_name!=null){//增加角色数据auth_role			
			authRoleMapper.updateByPrimaryKey(role);
		}else{
			map.put("message", "修改失败，角色名为空！");
			return map;
		}
		if(resIds!="" || resIds !=null){	
			AuthRoleResExample authRoleResExample = new AuthRoleResExample();
			authRoleResExample.or().andRole_idEqualTo(role_id);
			List<AuthRoleRes> list_res = authRoleResService.selectByExample(authRoleResExample);
			for (AuthRoleRes authRoleRes : list_res) {
				authRoleResService.deleteByPrimaryKey(authRoleRes);//删除中间表数据
			}			
			List<Integer> list_resIds = JSON.parseArray(resIds, Integer.class);
			for (Integer res_id : list_resIds) {
				AuthRoleRes authRoleRes = new AuthRoleRes();
				authRoleRes.setRes_id(res_id);
				authRoleRes.setRole_id(role.getRole_id());
				authRoleResService.insert(authRoleRes);//中间表循环插入数据auth_role__res
			}
		}else{
			map.put("message", "修改失败，没有选择权限！");
			return map;
		}
		map.put("message", String.format("修改角色成功！"));
		return map;
	}

	@Override
	public HashMap<String, Object> getResUrls(Integer res_id) {
		HashMap<String, Object> map = new HashMap<String,Object>();
		AuthRes res = authResService.selectByPrimaryKey(res_id);
		List<String> list = new ArrayList<String>();
		String[] split = res.getUrls().split(",");
		for (int i = 0; i < split.length; i++) {
			list.add(split[i]);
		}
		try {
			String object_to_json = JSON.toJSONString(list);
			map.put("list", object_to_json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("res", res);
		return map;
	}
	@Transactional
	@Override
	public HashMap<String, String> addUrls(String new_urls, String res_id) {
		HashMap<String, String> map = new HashMap<String,String>();
		AuthRes res = authResService.selectByPrimaryKey(Integer.parseInt(res_id));
		StringBuffer sb = new StringBuffer() ;
		if(new_urls!=null){
			String[] split = new_urls.split(",");
			if(split!=null){				
				for (int i = 0; i < split.length; i++) {
					if(split[i].contains("_")){
						split[i].replace("_", "");
					}
					if(i==0){
						sb.append(split[i]);
					}else{
						sb.append(split[i]+",");
					}
				}
				if(sb.toString().substring(sb.length()-1, sb.length()).equals(",")){
					sb.toString().substring(0, sb.length()-1);
				}
			}else{
				map.put("message", "添加权限路径失败");
				return map;
			}
		}
		res.setUrls(res.getUrls()+","+sb.toString());
		int i = authResService.updateByPrimaryKeySelective(res);
		map.put("message", String.format("添加权限路径%s", i > 0 ? "成功":"失败"));
		return map;
	}
}