package com.aobei.trainconsole.controller.test;

import com.aobei.train.service.ChannelService;
import com.aobei.trainconsole.util.JacksonUtil;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
 * Created by adminL on 2018/7/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional            // 自动回滚事务
@ActiveProfiles("dev")    // 使用开发环境
public class ChannelControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ChannelService channelService;

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

    @org.junit.Test
    public void channel_list() throws Exception {
        String url ="/channel/channel_list";
        this.mockMvc
                .perform(
                        post(url)
                                //模拟登录用户
                                .with(	//  用户名			角色
                                        user("rpmm").roles("CHANNEL_BROWSE"))
                                //请求调协  请求头
                                .accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
                )
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(authenticated().withUsername("rpmm"));
    }

    @org.junit.Test
    public void channel_add() throws Exception{
        String url ="/channel/channel_add";

        this.mockMvc
                .perform(
                        post(url)
                                .param("channel_name", "测试渠道")
                                .param("channel_type_id", "1")
                                .param("code", "haiding-1231")
                                .with(	//  用户名			角色
                                        user("rpmm").roles("CHANNEL_EDIT"))
                )
                .andExpect(authenticated().withUsername("rpmm"));
    }

    @Test
    public void coupon_del() throws Exception {
        String url ="/channel/channel_del";

        mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .param("p", "1")
                .param("channel_id", "1")
                .with(
                        user("rpmm").roles("CHANNEL_DEL")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(authenticated().withUsername("rpmm"));
    }
}
