package com.aobei.trainconsole.controller.test;

import com.aobei.train.service.CouponService;
import com.aobei.trainconsole.util.JacksonUtil;
import org.junit.Before;
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
 * Created by adminL on 2018/4/26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional            // 自动回滚事务
@ActiveProfiles("dev")    // 使用开发环境
public class BespeakControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    CouponService couponService;

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
    public void bespeak_list() throws Exception {
        String url ="/bespeak/bespeak_list";
        this.mockMvc
                .perform(
                        post(url)
                                //模拟登录用户
                                .with(	//  用户名			角色
                                        user("rpmm").roles("BESPEAK_BROWSE"))
                                //请求调协  请求头
                                .accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
                )
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(authenticated().withUsername("rpmm"));
    }
    @Test
    public void goto_add_bespeak() throws Exception {
        String url ="/bespeak/goto_add_bespeak";
        this.mockMvc
                .perform(
                        post(url)
                        .param("p","1")
                        .with(	//  用户名			角色
                                user("rpmm").roles("BESPEAK_EDIT"))
                )
                .andExpect(authenticated().withUsername("rpmm"));
    }

    @Test
    public void bespeak_add() throws Exception{
        String url ="/bespeak/add_bespeak";

        ArrayList<String> list = new ArrayList<>();
        list.add("00-00-1-11-30");
        this.mockMvc
                .perform(
                        post(url)
                                .param("sname", "测试add")
                                .param("list", JacksonUtil.object_to_json(list))
                                .with(	//  用户名			角色
                                        user("rpmm").roles("BESPEAK_EDIT"))
                )
                .andExpect(authenticated().withUsername("rpmm"));
    }

    @Test
    public void coupon_goto_edit() throws Exception {
        String url ="/bespeak/goto_edit_bespeak";
        this.mockMvc
                .perform(
                        post(url)
                                .param("p", "1")
                                .param("bespeak_id", "33")
                                .with(	//  用户名			角色
                                        user("rpmm").roles("BESPEAK_EDIT"))
                )
                .andExpect(authenticated().withUsername("rpmm"));
    }

    @Test
    public void bespeak_edit() throws Exception{
        String url ="/bespeak/add_bespeak";
        ArrayList<String> list = new ArrayList<>();
        list.add("00-00-2-14-00");
        list.add("10-00-0-14-30");
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .param("sname", "测试update")
                .param("list", JacksonUtil.object_to_json(list))
                .param("bespeak_id", "33")
                .with(
                        user("rpmm").roles("BESPEAK_EDIT")))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(authenticated().withUsername("rpmm"));
    }

    @Test
    public void coupon_del() throws Exception {
        String url ="/bespeak/del_bespeak";

        mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .param("p", "1")
                .param("bespeak_id", "33")
                .with(
                        user("rpmm").roles("BESPEAK_DEL")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(authenticated().withUsername("rpmm"));
    }
}
