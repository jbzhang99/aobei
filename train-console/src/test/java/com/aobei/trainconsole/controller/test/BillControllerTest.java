package com.aobei.trainconsole.controller.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class BillControllerTest {
	
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
	public void goto_bill_batch_list() throws Exception {
		url = "/billmanager/goto_bill_batch_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("bill_date", "")
				.with(	//  用户名			角色		
						user("rpmm").roles("BILL_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("bill/bill_batch_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("bill_batchs"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	@Test
	public void goto_normal_list() throws Exception {
		url = "/billmanager/goto_normal_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
//				.param("s_transaction_datetime", "")
//				.param("e_transaction_datetime", "")
//				.param("qs_create_time", "")
//				.param("qe_create_time", "")
//				.param("bill_batch_id", "")
//				.param("pay_order_id", "")
//				.param("bill_type", "")
				.with(	//  用户名			角色
						user("rpmm").roles("BILL_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("bill/bill_normal_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("bills"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	@Test
	public void goto_cache_list() throws Exception {
		url = "/billmanager/goto_cache_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色
						user("rpmm").roles("BILL_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("bill/bill_normal_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("bills"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	@Test
	public void goto_error_list() throws Exception {
		url = "/billmanager/goto_error_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
//				.param("qs_create_time", "")
//				.param("qe_create_time", "")
//				.param("bill_batch_id", "")
//				.param("pay_order_id", "")
//				.param("bill_status", "")
				.with(	//  用户名			角色
						user("rpmm").roles("BILL_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("bill/bill_normal_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("bills"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	@Test
	public void process_bill() throws Exception {
		url = "/billmanager/process_bill";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("refund_id_three", "")
				.param("transaction_id", "")
				.param("bill_fee", "")
				.param("manual_info", "")
				.param("manual_remark", "")
				.with(	//  用户名			角色
						user("rpmm").roles("BILL_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}

	@Test
	public void exportExcel() throws Exception {
		url = "/billmanager/exportExcel";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
//				.param("refund_id_three", "")
//				.param("transaction_id", "")
//				.param("bill_fee", "")
//				.param("manual_info", "")
//				.param("manual_remark", "")
				.with(	//  用户名			角色
						user("rpmm").roles("BILL_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
}
