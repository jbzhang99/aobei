package com.aobei.trainconsole.controller.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mr_bl on 2018/7/2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional            // 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class SystemControllerTest {

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
    public void metas() throws Exception {
        url = "/systemmanager/metas";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .with(	//  用户名			角色
                        user("rpmm").roles("META_BROWSE")))
                //返回检查  返回头检查
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("system/metas"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void jd_access_token() throws Exception {
        url = "/systemmanager/jd_access_token";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .with(	//  用户名			角色
                        user("rpmm").roles("META_BROWSE")))
                //返回检查  返回头检查
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("system/jd_access_token"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client_id"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
