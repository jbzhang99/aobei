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
public class SchoolControllerTest {
	
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
	public void goto_school_list() throws Exception {
		url = "/schoolmanager/goto_school_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
				.with(	//  用户名			角色		
						user("rpmm").roles("SCHOOL_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("school/school_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("school_list"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void school_add() throws Exception {
		url = "/schoolmanager/school_add";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("page_current_page", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("SCHOOL_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("school/school_add"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page_current_page"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void save_school() throws Exception {
		url = "/schoolmanager/save_school";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("name", "chain")
				.param("address", "new  sadas address 18")
				.param("intro", "vary good school")
				.param("linkman", "mr bu")
				.param("contact_number", "13238419188")
				.with(	//  用户名			角色		
						user("rpmm").roles("SCHOOL_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void school_detail() throws Exception {
		url = "/schoolmanager/school_detail";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("school_id", "1072150391112122368")
				.param("page_current_page", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("SCHOOL_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("school/school_detail"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("school_info"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page_current_page"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void school_edit() throws Exception {
		url = "/schoolmanager/school_edit";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("school_id", "1072150391112122368")
				.param("page_current_page", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("SCHOOL_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("school/school_edit"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("school_info"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page_current_page"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void school_edit_save() throws Exception {
		url = "/schoolmanager/school_edit_save";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("school_id", "1072150391112122368")
				.param("name", "chainedit")
				.param("address", "new edit sadas address 18")
				.param("intro", "vary good school edit")
				.param("linkman", "mr bu edit")
				.param("contact_number", "13238419188")
				.with(	//  用户名			角色		
						user("rpmm").roles("SCHOOL_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void school_delete() throws Exception {
		url = "/schoolmanager/school_delete/1072150391112122368";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("SCHOOL_DEL")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
}
