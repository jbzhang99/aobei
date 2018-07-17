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
public class CouponEnvControllerTest {
	
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
	public void couEnv_list() throws Exception{
		String url ="/couponEnv/couponEnv_list";
		
		this.mockMvc
		.perform(
				post(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("COUENV_BROWSE"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
		
	}
	
	@Test
	public void couEnv_gotoadd() throws Exception{
		String url ="/couponEnv/goto_add";
		
		this.mockMvc
		.perform(
				post(url)
				.param("p", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("COUENV_EDIT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
		
	}
	
	@Test
	public void couEnv_add() throws Exception{
		String url ="/couponEnv/add_couponEnv";
		
		this.mockMvc
		.perform(
				post(url)
				.param("s_date", "2017-2-4 12:03:44")
				.param("e_date", "2018-2-1 12:02:24")
				.param("coupon_id", "1064800660503420928")
				.param("type", "2")
				.param("name",  "测试的优惠策略")
				.param("coupon_number", "2")
				.param("start_datetime","2017-2-12" )
				.param("end_datetime", "2017-3-2")
				.with(	//  用户名			角色		
						user("rpmm").roles("COUENV_EDIT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
		
	}
	
	@Test
	public void couEnv_edit() throws Exception{
		String url ="/couponEnv/edit_couponEnv";
		
		this.mockMvc
		.perform(
				post(url)
				.param("s_date", "2017-2-4 12:03:44")
				.param("e_date", "2018-2-1 12:02:24")
				.param("coupon_id", "1064800660503420928")
				.param("coupon_env_id", "1089699485529432064")
				.param("type", "2")
				.param("name",  "测试的优惠策略")
				.param("coupon_number", "2")
				.param("start_datetime","2017-2-12" )
				.param("end_datetime", "2017-3-2")
				.with(	//  用户名			角色		
						user("rpmm").roles("COUENV_EDIT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
		
	}

}
