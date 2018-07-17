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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev")
public class QuestionPaperControllerTest {

	
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
	/**
	 * 请求试卷列表页数据
	 * @throws Exception 
	 */
	@Test
	public void questionPaper_list() throws Exception{
		String url = "/questionPaperManager/questionPaper/questionPaperList";
		this.mockMvc
		.perform(
				post(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("QEPAPER_BROWSE"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	//没有添加页面，从列表页模态框添加
	
	@Test
	public void questionPaper_add() throws Exception{
		String url = "/questionPaperManager/questionPaper/addQuestionPaper";
		
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("name", "试卷测试")
				.param("train_course", "1089459711030091776")
				.param("singleCount", "2")
				.param("multipartCount", "2")
				.with(	//  用户名			角色		
						user("rpmm").roles("QEPAPER_EDIT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(authenticated().withUsername("rpmm"));
	}
	//已测试
	@Test
	public void questionPaper_del() throws Exception{
		String url = "/questionPaperManager/questionPaper/deleteQuestionPaper";
		
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("question_paper_id", "1058436960360095744")
				.with(	//  用户名			角色		
						user("rpmm").roles("QEPAPER_DEL"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void questionPaper_export() throws Exception{
		String url = "/questionPaperManager/questionPaper/printOut";
		
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("question_paper_id", "1058436960360095744")
				.with(	//  用户名			角色		
						user("rpmm").roles("QEPAPER_EXPORT"))
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(authenticated().withUsername("rpmm"));
	}
}
