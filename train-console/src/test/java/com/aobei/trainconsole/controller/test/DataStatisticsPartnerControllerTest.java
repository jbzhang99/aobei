package com.aobei.trainconsole.controller.test;

import com.aobei.train.model.Partner;
import com.aobei.train.model.PartnerFallinto;
import com.aobei.train.model.VirtualAddress;
import com.aobei.trainconsole.controller.PartnerController;
import com.aobei.trainconsole.util.JacksonUtil;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class DataStatisticsPartnerControllerTest {
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	PartnerController partnerController;
	
	private MockMvc mockMvc;

	@Before
    public void setup(){
       // mockMvc = MockMvcBuilders.standaloneSetup(partnerController).build();
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				// spring security
				.apply(springSecurity())
				// 打印输出
				.alwaysDo(print())
				// 返回检查  HTTP状态
				//.alwaysExpect(status().isOk())
				.build();
    }

	@Test
	public void index() throws Exception {
		String url = "/data_statistics/partner/index";

		this.mockMvc
				.perform(
						//请求设置  URL
						get(url)
								//模拟登录用户*/
								.with(    //  用户名			角色
										user("xk").roles("DATA_STATISTICS")))
				.andExpect(status().isOk());
	}

	/**
	 * 加载 柱状图数据
	 *
	 * @throws Exception
	 */
	@Test
	public void loadPartnerRegData() throws Exception {
		String url = "/data_statistics/partner/loadPartnerRegData";
		String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.mockMvc
				.perform(
						//请求设置  URL
						get(url)
								.param("type", "1")
								.param("startDate", "2018-01-01")
								.param("endDate", endDate)
								//模拟登录用户*/
								.with(    //  用户名			角色
										user("xk").roles("DATA_STATISTICS")))
				.andExpect(status().isOk());
	}

	/**
	 * 加载 表格数据
	 *
	 * @throws Exception
	 */
	@Test
	public void loadPartnerTableData() throws Exception {
		String url = "/data_statistics/partner/loadPartnerTableData";
		String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.mockMvc
				.perform(
						//请求设置  URL
						get(url)
								.param("type", "1")
								.param("startDate", "2018-01-01")
								.param("endDate", endDate)
								//模拟登录用户*/
								.with(    //  用户名			角色
										user("xk").roles("DATA_STATISTICS")))
				.andExpect(status().isOk());
	}


	/**
	 * 加载 地图数据
	 *
	 * @throws Exception
	 */
	@Test
	public void loadPartnerMapData() throws Exception {
		String url = "/data_statistics/partner/loadPartnerMapData";
		String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.mockMvc
				.perform(
						//请求设置  URL
						get(url)
								.param("type", "2")
								.param("startDate", "2018-06-25")
								.param("endDate", endDate)
								//模拟登录用户*/
								.with(    //  用户名			角色
										user("xk").roles("DATA_STATISTICS")))
				.andExpect(status().isOk());
	}



}
