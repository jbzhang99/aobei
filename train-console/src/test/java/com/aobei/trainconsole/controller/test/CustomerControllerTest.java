package com.aobei.trainconsole.controller.test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class CustomerControllerTest {
	
	@Autowired
	protected WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	private String url;
	
	@Before
	public void setUp() {
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
	public void goto_customer_list() throws Exception {
		url = "/customermanager/goto_customer_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
				.param("phone", "13100000000")
				.param("locked", "0")
				.with(	//  用户名			角色		
						user("rpmm").roles("CUSTOMER_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("customer/customer_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("customers"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("phone"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("locked"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void change_locked() throws Exception {
		url = "/customermanager/change_locked";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("locked", "1")
				.param("customer_id", "1068594469228699648")
				.with(	//  用户名			角色		
						user("rpmm").roles("CUSTOMER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void cus_address() throws Exception {
		url = "/customermanager/cus_address";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("customer_id", "1068594469228699648")
				.with(	//  用户名			角色		
						user("rpmm").roles("CUSTOMER_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void order_view() throws Exception {
		url = "/customermanager/order_view";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("customer_id", "1068594469228699648")
				.param("p", "1")
				.param("ps", "10")
				.with(	//  用户名			角色		
						user("rpmm").roles("CUSTOMER_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("customer/customer_orders"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("customer_id"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
}
