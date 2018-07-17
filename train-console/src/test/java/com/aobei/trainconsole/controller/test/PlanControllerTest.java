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
public class PlanControllerTest {
	
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
	public void goto_plan_list() throws Exception {
		url = "/planmanager/goto_plan_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
				.param("begin_date", "2018-01-01")//required=false
				.param("end_date", "2018-05-30")//required=false
				.param("school_id", "1031040738102132736")//defaultValue="0"
				.param("teacher_id", "0")//defaultValue="0"
				.param("partner_id", "0")//defaultValue="0"
				.param("classroom_id", "0")//defaultValue="0"
				.with(	//  用户名			角色		
						user("rpmm").roles("PLAN_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("trainplan/plan_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("plan_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("begin_date"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("end_date"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("school_id"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("teacher_id"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partner_id"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("classroom_id"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("school_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partner_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("teacher_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("courses"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("classroom_list"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void train_plan_add() throws Exception {
		url = "/planmanager/train_plan_add";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("page_current_page", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("PLAN_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("trainplan/plan_add"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page_current_page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("school_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("teacher_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("assistant_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("course_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partner_list"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void get_optional_school_classroom() throws Exception {
		url = "/planmanager/get_optional_school_classroom";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("course_id", "1067664578395791360")
				.with(	//  用户名			角色		
						user("rpmm").roles("PLAN_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void get_optional_classroom() throws Exception {
		url = "/planmanager/get_optional_classroom";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("course_id", "1067664578395791360")
				.param("school_id", "1067688993724588032")
				.with(	//  用户名			角色		
						user("rpmm").roles("PLAN_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void get_student_list() throws Exception {
		url = "/planmanager/get_student_list";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("partner_id", "1067670088150966272")
				.param("train_begin", "2018-03-16")
				.param("train_end", "2018-03-19")
				.param("condition", "毛")
				.with(	//  用户名			角色		
						user("rpmm").roles("PLAN_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void save_plan() throws Exception {
		url = "/planmanager/save_plan";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("train_begin", "2018-04-16")
				.param("train_end", "2018-04-18")
				.param("course_id", "1067665807242977280")
				.param("school_id", "1067688556988489728")
				.param("classroom_id", "1067694722766364672")
				.param("teacher_id", "1067701693666058240")
				.param("train_way", "0")
				.param("trainers_number", "4")
				.param("partnerids", "1067670088150966272","1067671915911208960")
				.param("students", "1067677205725470720","1067677206153289728","1046870857073057792","1067681879874625536")
				.with(	//  用户名			角色		
						user("rpmm").roles("PLAN_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void plan_edit() throws Exception {
		url = "/planmanager/plan_edit";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("train_plan_id", "1072222455361748992")
				.param("page_current_page", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("PLAN_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("trainplan/plan_edit"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page_current_page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("plan"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("planPartners"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("planPartner_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("school_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("teacher_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("assistant_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("course_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partner_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("student_list"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void plan_edit_save() throws Exception {
		url = "/planmanager/plan_edit_save";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("train_plan_id", "1072222455361748992")
				.param("train_begin", "2018-04-25")
				.param("train_end", "2018-04-27")
				.param("course_id", "1067665807242977280")
				.param("school_id", "1067688556988489728")
				.param("classroom_id", "1067694722766364672")
				.param("teacher_id", "1067701693666058240")
				.param("train_way", "0")
				.param("trainers_number", "4")
				.param("partnerids", "1067670088150966272","1067671915911208960")
				.param("students", "1067677205725470720","1067677206153289728","1046870857073057792","1067681879874625536")
				.with(	//  用户名			角色		
						user("rpmm").roles("PLAN_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void plan_delete() throws Exception {
		url = "/planmanager/plan_delete/1072222455361748992";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("PLAN_DEL")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void get_train_time() throws Exception {
		url = "/planmanager/get_train_time";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("train_begin", "2018-04-25")
				.param("course_id", "1067665807242977280")
				.param("classroom_id", "1067694722766364672")
				.with(	//  用户名			角色		
						user("rpmm").roles("PLAN_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
}
