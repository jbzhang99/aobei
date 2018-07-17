package com.aobei.trainconsole.controller.test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.aobei.train.service.QuestionService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev")
public class RoleControllerTest {

	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				// spring security
				.apply(springSecurity())
				// 打印输出
				.alwaysDo(print())
				// 返回检查  HTTP状态
				.alwaysExpect(status().isOk())
				.build();
	}
	
	@Test
	public void role_list() throws Exception{
		String url ="/roleManager/role/role_list";
		
		this.mockMvc
		.perform(
				post(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("ROLES_BROWSE"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
		
		
	}
	
	@Test
	public void role_goto_edit() throws Exception{
		String url ="/roleManager/role/role_goto_edit";
		
		this.mockMvc
		.perform(
				post(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("ROLES_EDIT"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void role_edit() throws Exception{
		String url ="/roleManager/role/role_edit";
		String resIds ="[1,2]";
		this.mockMvc
		.perform(
				post(url)
				.param("role_name", "火影忍者")
				.param("resIds", resIds)
				.param("roleId", "1082181489974075392")
				.with(	//  用户名			角色		
						user("rpmm").roles("ROLES_EDIT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void role_goto_add() throws Exception{
		String url ="/roleManager/role/role_goto_add";
		
		this.mockMvc
		.perform(
				post(url)
				.param("p", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("ROLES_EDIT"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void role_add() throws Exception{
		String url ="/roleManager/role/role_add";
		String resIds ="[1,2]";
		this.mockMvc
		.perform(
				post(url)
				.param("role_name", "火影忍者")
				.param("resIds", resIds)
				.with(	//  用户名			角色		
						user("rpmm").roles("ROLES_EDIT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	//已通过
	@Test
	public void privileges_list() throws Exception{
		String url ="/roleManager/role/privileges_list";
		this.mockMvc
		.perform(
				post(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("RES_BROWSE"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void privileges_goto_add() throws Exception{
		String url ="/roleManager/role/goto_add_privileges";
		this.mockMvc
		.perform(
				post(url)
				.param("p", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("RES_EDIT"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void privileges_add() throws Exception{
		String url ="/roleManager/role/add_privileges";
		this.mockMvc
		.perform(
				post(url)
				.param("name", "哇哈哈不给你")
				.param("role_key", "ROLE")
				.param("tag", "哇")
				.param("urls", "sssssssssssssss")
				.with(	//  用户名			角色		
						user("rpmm").roles("RES_EDIT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
}
