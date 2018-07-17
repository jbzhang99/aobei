package com.aobei.trainconsole.controller.test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
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

import com.alibaba.fastjson.JSONArray;
import com.aobei.train.service.QuestionService;
import com.aobei.trainconsole.controller.QuestionBankController;
import custom.bean.Option;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev")
public class QuestionControllerTest {

	@Autowired
	QuestionBankController questionControlelr;
	
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
	/**
	 * 题库列表页测试数据
	 * @throws Exception
	 */
	@Test
	public void question_list() throws Exception{
		String url ="/questionBankManager/question/questionBankList";
		
		this.mockMvc
		.perform(
				post(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("QEBANK_BROWSE"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
		
		
	}
	/**
	 * 题库跳转添加页测试
	 * @throws Exception
	 */
	@Test
	public void question_goto_add() throws Exception{
		String url ="/questionBankManager/question/gotoAddQuestionBank";
		
		
		this.mockMvc
		.perform(
				post(url).param("p", "1")
				.with(	//  用户名			角色		
						user("rpmm").roles("QEBANK_EDIT"))
				.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
	}
	/**
	 * 试卷添加方法
	 * @throws Exception
	 */
	@Test
	public void question_add() throws Exception{
		String url ="/questionBankManager/question/addQuestionBank";
		//组装json数据，传递
		Option option1 = new Option();
		option1.setT("A");
		option1.setO("刘永强");
		option1.setR(0);
		Option option2 = new Option();
		option2.setT("B");
		option2.setO("哈哈");
		option2.setR(0);
		Option option3 = new Option();
		option3.setT("C");
		option3.setO("SDFDSFS");
		option3.setR(0);
		Option option4 = new Option();
		option4.setT("D");
		option4.setO("XULSD");
		option4.setR(1);
		List<Option> list = new ArrayList<Option>();
		list.add(option1);
		list.add(option2);
		list.add(option3);
		list.add(option4);
		Object json = JSONArray.toJSON(list);
		String string = json.toString();
		//执行请求handler映射
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("QEBANK_EDIT"))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.param("answer", "D")
				.param("course", "1042404053151932416")
				.param("topic", "22312")
				.param("type", "1")
				.param("param", string))
		.andExpect(authenticated().withUsername("rpmm"));
	}
	/**
	 * 题库跳转编辑页测试
	 * @throws Exception
	 */
	@Test
	public void question_goto_edit() throws Exception{
		String url ="/questionBankManager/question/gotoEditQuestionBank";
		mockMvc.perform(
				post(url)
				.param("p", "1")
				.param("question_id", "1072279391945908224")
				.with(	//  用户名			角色		
						user("rpmm").roles("QEBANK_EDIT"))
				//请求调协  请求头
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
			.andExpect(authenticated().withUsername("rpmm"));
	}
	/**
	 * 题库编辑测试(执行编辑请求)
	 * @throws Exception
	 */
	@Test
	public void question_edit() throws Exception{
		
		//组装json数据，传递
		Option option1 = new Option();
		option1.setT("A");
		option1.setO("修改后的选项A");
		option1.setR(0);
		Option option2 = new Option();
		option2.setT("B");
		option2.setO("修改后的选项B");
		option2.setR(0);
		Option option3 = new Option();
		option3.setT("C");
		option3.setO("修改后的选项C");
		option3.setR(0);
		Option option4 = new Option();
		option4.setT("D");
		option4.setO("修改后的选项D");
		option4.setR(1);
		List<Option> list = new ArrayList<Option>();
		list.add(option1);
		list.add(option2);
		list.add(option3);
		list.add(option4);
		Object json = JSONArray.toJSON(list);
		String string = json.toString();
		
		String url ="/questionBankManager/question/updateQuestionBank";
		//执行请求handler映射
		mockMvc.perform(
				post(url)
				.param("p", "1")
				.param("question_id", "1072279391945908224")
				.param("answer", "D")
				.param("course", "1042404053151932416")//科目id
				.param("topic", "22312")
				.param("type", "1")
				.param("param", string)//传递的题选项的json数据
				.with(	//  用户名			角色		
						user("rpmm").roles("QEBANK_EDIT"))
				//请求调协  请求头
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
			.andExpect(authenticated().withUsername("rpmm"));
	}
	/**
	 * 试卷删除方法(随机删除一个教师)
	 * @throws Exception
	 */
	@Test
	public void question_del() throws Exception{
		
		String url = "/questionBankManager/question/deleteQuestionBank";
		
		mockMvc.perform(
				post(url)
				.param("question_id", "1072279391945908224")
				.with(	//  用户名			角色		
						user("rpmm").roles("QEBANK_DEL"))
				//请求调协  请求头
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				)
			.andExpect(authenticated().withUsername("rpmm"));
	}
}
