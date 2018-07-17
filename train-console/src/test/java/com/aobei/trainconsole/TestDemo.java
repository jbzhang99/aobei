package com.aobei.trainconsole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.aobei.train.model.Users;
import com.aobei.train.service.UsersService;

/**
 * 
 * 测试示例
 * 
 * @author liyi
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class TestDemo {

	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Autowired
	private UsersService usersService;
	
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
	 * 测试事务回滚
	 */
	@Test
	public void transactionRollBack() {
		Users users = new Users();
		users.setUser_id(110L);
		users.setUsername("添加用户事务回滚");
		int count = usersService.insertSelective(users);
		assertThat(count).isEqualTo(1);
	}

	/**
	 * MVC 测试
	 * @throws Exception
	 */
	@Test
	public void mvcTest() throws Exception {
		this.mockMvc
				.perform(
						//请求设置  URL
						post("/test/user.json")
						//设置请求参数
						.param("param1", "value")
						//模拟登录用户
						.with(	//  用户名			角色		
								user("testB").roles("USER"))
						//请求调协  请求头
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
						)
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				//返回检查   数据 JSON 比对
				.andExpect(jsonPath("$.name").value("Lee"))
				//权限检查   用户名比对
				.andExpect(authenticated().withUsername("testa"));
	}
	
}
