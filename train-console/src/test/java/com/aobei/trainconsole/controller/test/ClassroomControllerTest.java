package com.aobei.trainconsole.controller.test;

import org.junit.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class ClassroomControllerTest {
	
	@Autowired
	protected WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	private String url;
	
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
	public void get_classroom_list() throws Exception {
		url = "/classroommanager/goto_classroom_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
				.with(	//  用户名			角色		
						user("rpmm").roles("CLASSROOM_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("classroom/classroom_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("classroom_list"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void classroom_add() throws Exception {
		url = "/classroommanager/classroom_add";
		String page_current_page = "1";//点击到该链接前的页面的当前页数
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("page_current_page", page_current_page)
				.with(	//  用户名			角色		
						user("rpmm").roles("CLASSROOM_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("classroom/classroom_add"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page_current_page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("school_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("courseteam_list"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void save_classroom() throws Exception {
		url = "/classroommanager/save_classroom";
		List<Long> ids = new ArrayList<>();
		ids.add(1067688556988489728l);
		ids.add(1067688993724588032l);
		ids.add(1032264886736437248l);
		ids.add(1053908657110032384l);
		ids.add(1032267457643462656l);
		ids.add(1032264532837842944l);
		ids.add(1031441649072914432l);
		ids.add(1032268585684746240l);
		ids.add(1031040738102132736l);
		mockMvc.perform(MockMvcRequestBuilders.post(url)
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.param("support_course_ids", "1067664578395791360","1067665807242977280")
				.param("school_id", ids.get(new Random().nextInt(9))+"")
				.param("block_number", "测试楼"+(new Random().nextInt(100))+"室")
				.param("capacity", "45")
				.param("useable", "1")
				.param("timescope_json", "[{\"w\":1,\"se\":[{\"s\":96,\"e\":114},{\"s\":120,\"e\":138},{\"s\":168,\"e\":186},{\"s\":192,\"e\":210}]},"
									+ "{\"w\":2,\"se\":[{\"s\":96,\"e\":114},{\"s\":120,\"e\":138},{\"s\":168,\"e\":186},{\"s\":192,\"e\":210}]},"
									+ "{\"w\":3,\"se\":[{\"s\":96,\"e\":114},{\"s\":120,\"e\":138},{\"s\":168,\"e\":186},{\"s\":192,\"e\":210}]},"
									+ "{\"w\":4,\"se\":[{\"s\":96,\"e\":114},{\"s\":120,\"e\":138},{\"s\":168,\"e\":186},{\"s\":192,\"e\":210}]},"
									+ "{\"w\":5,\"se\":[{\"s\":96,\"e\":114},{\"s\":168,\"e\":186},{\"s\":192,\"e\":210}]},"
									+ "{\"w\":6,\"se\":[{\"s\":120,\"e\":138},{\"s\":168,\"e\":186},{\"s\":192,\"e\":210}]},"
									+ "{\"w\":7,\"se\":[{\"s\":96,\"e\":114},{\"s\":168,\"e\":186},{\"s\":192,\"e\":210}]}]")
				.with(	//  用户名			角色		
						user("rpmm").roles("CLASSROOM_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void get_class_schedule() throws Exception {
		url = "/classroommanager/get_class_schedule";
		String classroom_id = "1067695042447826944";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("classroom_id", classroom_id)
				.with(	//  用户名			角色		
						user("rpmm").roles("CLASSROOM_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void goto_classroom_edit() throws Exception {
		url = "/classroommanager/classroom_edit";
		Long classroom_id = 1067695042447826944l;
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("classroom_id", classroom_id.toString())
				.param("page_current_page", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("CLASSROOM_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("classroom/classroom_edit"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page_current_page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("school_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("allcourseteam"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("classroom"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void edit_submit_classroom() throws Exception {
		url = "/classroommanager/edit_submit_classroom";
		List<Long> ids = new ArrayList<>();
		ids.add(1067688556988489728l);
		ids.add(1067688993724588032l);
		ids.add(1032264886736437248l);
		ids.add(1053908657110032384l);
		ids.add(1032267457643462656l);
		ids.add(1032264532837842944l);
		ids.add(1031441649072914432l);
		ids.add(1032268585684746240l);
		ids.add(1031040738102132736l);
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.param("support_course_ids", "1067664578395791360","1067665807242977280")
				.param("school_id", ids.get(new Random().nextInt(9))+"")
				.param("classroom_id","1070115575327932416")
				.param("block_number", "测试编辑楼"+(new Random().nextInt(100))+"室")
				.param("capacity", "72")
				.param("useable", "1")
				.param("timescope_json", "[{\"w\":1,\"se\":[{\"s\":96,\"e\":114},{\"s\":120,\"e\":138},{\"s\":168,\"e\":186},{\"s\":192,\"e\":210}]},"
									+ "{\"w\":3,\"se\":[{\"s\":96,\"e\":114},{\"s\":120,\"e\":138},{\"s\":168,\"e\":186},{\"s\":192,\"e\":210}]},"
									+ "{\"w\":5,\"se\":[{\"s\":96,\"e\":114},{\"s\":168,\"e\":186},{\"s\":192,\"e\":210}]},"
									+ "{\"w\":7,\"se\":[{\"s\":96,\"e\":114},{\"s\":168,\"e\":186},{\"s\":192,\"e\":210}]}]")
				.with(	//  用户名			角色		
						user("rpmm").roles("CLASSROOM_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void classroom_delete() throws Exception {
		url = "/classroommanager/classroom_delete/1067695042447826944";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("CLASSROOM_DEL")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
}
