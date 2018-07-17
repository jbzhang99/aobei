package com.aobei.trainconsole.controller.test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;

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


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class CmsBannerControllerTest {
	
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
	public void goto_banner_list() throws Exception {
		url = "/bannermanager/goto_banner_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("app", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("BANNER_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("cmsbanner/banner_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("app"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("banner_list"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void banner_add() throws Exception {
		url = "/bannermanager/banner_add";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("app", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("BANNER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("cmsbanner/banner_add"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("app"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void save_banner() throws Exception {
		url = "/bannermanager/save_banner";
		
		String fileUrl = "D:/aa.jpg";
		File file = new File(fileUrl);
		FileInputStream inputStream = new FileInputStream(file);
		
		MockMultipartFile mv = new MockMultipartFile("cover_img",file.getName(),"MediaType.IMAGE_JPEG",inputStream);
		
		mockMvc.perform(MockMvcRequestBuilders.fileUpload(url).file(mv)
				.param("title", "banner test")
				.param("intro", "very good banner")
				.param("app", "1")
				.param("online", "2018-05-12T10:42")
				.param("offline", "2018-05-19T12:00")
				.with(	//  用户名			角色		
						user("rpmm").roles("BANNER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void banner_edit() throws Exception {
		url = "/bannermanager/banner_edit";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("cms_banner_id", "1072753264967598080")
				.param("app", "2")
				.with(	//  用户名			角色		
						user("rpmm").roles("BANNER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("cmsbanner/banner_edit"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("cmsBanner"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("img"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("app"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void edit_save_banner() throws Exception {
		url = "/bannermanager/edit_save_banner";
		
		MockMultipartFile mv = new MockMultipartFile("cover_img","","MediaType.IMAGE_JPEG",new byte[0]);
		
		mockMvc.perform(MockMvcRequestBuilders.fileUpload(url).file(mv)
				.param("cms_banner_id", "1072753264967598080")
				.param("title", "edit banner test")
				.param("intro", "edit very good banner")
				.param("app", "1")
				.param("online", "2018-05-12T10:42")
				.param("offline", "2018-05-19T12:00")
				.with(	//  用户名			角色		
						user("rpmm").roles("BANNER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void change_state() throws Exception {
		url = "/bannermanager/change_state";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("sign", "1")
				.param("cms_banner_id", "1072753264967598080")
				.param("app", "2")
				.with(	//  用户名			角色		
						user("rpmm").roles("BANNER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void move_up() throws Exception {//-1
		url = "/bannermanager/move_up";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("serial_number", "8")
				.param("cms_banner_id", "1072753264967598080")
				.param("pre_cms_id", "1072753264967598080")
				.param("app", "2")
				.with(	//  用户名			角色		
						user("rpmm").roles("BANNER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void move_down() throws Exception {//+1
		url = "/bannermanager/move_down";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("serial_number", "7")
				.param("cms_banner_id", "1072753264967598080")
				.param("next_cms_id", "1072753264967598080")
				.param("app", "2")
				.with(	//  用户名			角色		
						user("rpmm").roles("BANNER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
}
