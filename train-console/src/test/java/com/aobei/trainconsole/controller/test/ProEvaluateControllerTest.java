package com.aobei.trainconsole.controller.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.service.CouponService;
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

import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by adminL on 2018/5/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional            // 自动回滚事务
@ActiveProfiles("dev")    // 使用开发环境
public class ProEvaluateControllerTest {
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

    @Test
    public void proEvaluate_list() throws Exception {
        String url ="/proEvaluate/proEvaluate_list";
        this.mockMvc
                .perform(
                        post(url)
                                //模拟登录用户
                                .with(	//  用户名			角色
                                        user("rpmm").roles("PROEVALUATE_BROWSE"))
                                //请求调协  请求头
                                .accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
                )
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(authenticated().withUsername("rpmm"));
    }

    @Test
    public void gotoOrder_list() throws Exception {
        String url ="/proEvaluate/goto_Order";
        this.mockMvc
                .perform(
                        post(url)
                                //模拟登录用户
                                .with(	//  用户名			角色
                                        user("rpmm").roles("PROEVALUATE_BROWSE"))
                                //请求调协  请求头
                                .accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
                )
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(authenticated().withUsername("rpmm"));
    }

    @Test
    public void allreject() throws Exception {
        String url ="/proEvaluate/allReject";
        String list = "[\"1086700916959977472\"]";
        this.mockMvc
                .perform(
                        post(url)
                                .param("list_proEva_ids",list)
                                //模拟登录用户
                                .with(	//  用户名			角色
                                        user("rpmm").roles("PROEVALUATE_EDIT"))

                )

                .andExpect(authenticated().withUsername("rpmm"));
    }

    @Test
    public void allpass() throws Exception {
        String url ="/proEvaluate/allPass";
        String list = "[\"1086700916959977472\"]";
        this.mockMvc
                .perform(
                        post(url)
                                .param("product_evaluate_id",list)
                                //模拟登录用户
                                .with(	//  用户名			角色
                                        user("rpmm").roles("PROEVALUATE_EDIT"))

                )

                .andExpect(authenticated().withUsername("rpmm"));
    }

    @Test
    public void passVer() throws Exception {
        String url ="/proEvaluate/passVer";
        this.mockMvc
                .perform(
                        post(url)
                                .param("product_evaluate_id","1086700916959977472l")
                                //模拟登录用户
                                .with(	//  用户名			角色
                                        user("rpmm").roles("PROEVALUATE_EDIT"))
                                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))

                )

                .andExpect(authenticated().withUsername("rpmm"));
    }

    @Test
    public void nopassVer() throws Exception {
        String url ="/proEvaluate/noPassVer";
        this.mockMvc
                .perform(
                        post(url)
                                .param("product_evaluate_id","1086700916959977472l")
                                //模拟登录用户
                                .with(	//  用户名			角色
                                        user("rpmm").roles("PROEVALUATE_EDIT"))
                                .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))

                )

                .andExpect(authenticated().withUsername("rpmm"));
    }

}
