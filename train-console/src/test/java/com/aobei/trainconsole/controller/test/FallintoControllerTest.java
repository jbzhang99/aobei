package com.aobei.trainconsole.controller.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class FallintoControllerTest {
	
	@Autowired
	private WebApplicationContext context;

	
	private MockMvc mockMvc;

	@Before
    public void setup(){
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
    public void fallintoList() throws Exception {
		mockMvc.perform(
				post("/fallinto/fallintoList")
				.param("p","1")
				.param("ps","10")
				.with(	//  用户名			角色
					user("xk").roles("FALLINTO_BROWSE"))
				)
		.andExpect(authenticated().withUsername("xk"));
	}


	/*@Test
	public void insuranceAddShow() throws Exception {
		mockMvc.perform(
				post("/insurance/insuranceAddShow")
				.param("p","1"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}*/


	@Test
	public void addFallinto() throws Exception {
		mockMvc.perform(
				post("/fallinto/addFallinto")
                .param("fallinto_id","1L")
                .param("fallinto_name","测试Test")
                .param("percent","30")
                .param("create_datetime","2018-09-11 00:00:00")
                .param("deleted","0")
                .with(	//  用户名			角色
                    user("xk").roles("FALLINTO_EDIT"))
		    )

            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(authenticated().withUsername("xk"));
	}

	@Test
	public void editFallinto() throws Exception {
        mockMvc.perform(
                post("/fallinto/editFallinto")
				.param("fallinto_id","1L")
				.param("fallinto_name","测试Test修改")
				.param("percent","30")
				.param("create_datetime","2018-09-11 00:00:00")
				.param("deleted","0")
				.with(	//  用户名			角色
					user("xk").roles("FALLINTO_EDIT"))
		)

		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(authenticated().withUsername("xk"));
    }

}
