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


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev")
public class UserContorllerTest {

	
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
	public void user_list() throws Exception{
		String url ="/userManager/user/user_list";
		
		this.mockMvc
		.perform(
				post(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("USER_BROWSE"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void operate_list() throws Exception{
		String url ="/userManager/user/operateLog_list";
		
		this.mockMvc
		.perform(
				post(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("USER_BROWSE"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void user_goto_edit() throws Exception{
		String url ="/userManager/user/user_goto_edit";
		
		this.mockMvc
		.perform(
				post(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("USER_EDIT"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void user_edit() throws Exception{
		String url ="/userManager/user/user_edit";
		
		this.mockMvc
		.perform(
				post(url)
				.param("username","我")
				.param("role", "1082181489974075392")
				.param("userId", "1089082046902984704")
				.param("sysUserId", "1089082048580706304")
				.with(	//  用户名			角色		
						user("rpmm").roles("USER_EDIT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void user_goto_add() throws Exception{
		String url ="/userManager/user/user_goto_add";
		
		this.mockMvc
		.perform(
				post(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("USER_EDIT"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void user_add() throws Exception{
		String url ="/userManager/user/user_add";
		
		this.mockMvc
		.perform(
				post(url)
				.param("username","我")
				.param("role", "1082181489974075392")
				.param("dept_name","扫地")
				.param("job_title","sds")
				.param("phone","17635459870")
				.param("password","22222t")
				.with(	//  用户名			角色		
						user("rpmm").roles("USER_EDIT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void user_del() throws Exception{
		String url ="/userManager/user/del_user";
		this.mockMvc
		.perform(
				post(url)
				.param("user_id","1089082046902984704")
				.with(	//  用户名			角色		
						user("rpmm").roles("USER_DEL"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void user_switch() throws Exception{
		String url ="/userManager/user/user_switch";
		this.mockMvc
		.perform(
				post(url)
				.param("user_id","1089082046902984704")
				.with(	//  用户名			角色		
						user("rpmm").roles("USER_EDIT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void user_updatePassword() throws Exception{
		String url ="/userManager/user/update_password";
		this.mockMvc
		.perform(
				post(url)
				.param("user_id","1089082046902984704")
				.param("value_p","22222t")
				.with(	//  用户名			角色		
						user("rpmm").roles("USER_EDIT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
}
