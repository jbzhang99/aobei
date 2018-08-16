package com.aobei.trainconsole.controller.test;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * 拼团活动管理
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional            // 自动回滚事务
@ActiveProfiles("dev")    // 使用开发环境
public class GroupPurchaseControllerTest {


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                // spring security
                .apply(springSecurity())
                // 打印输出
                .alwaysDo(print())
                .build();

    }

    /**
     * 拼团列表数据
     *
     * @throws Exception
     */

    @Test
    public void list() throws Exception {
        String url = "/group_purchase/list";

        this.mockMvc
                .perform(
                        //请求设置  URL
                        post(url)

                                .param("p", "1")
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("GROUP_PURCHASE")))
                .andExpect(status().isOk());
    }

    /**
     * 添加拼团数据
     *
     * @throws Exception
     */
    @Test
    public void groupPurchaseInsert() throws Exception {
        String url = "/group_purchase/update";
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.mockMvc
                .perform(
                        post(url)
                                .param("startTime", "2018-08-16 00:00:00")
                                .param("endTime", "2018-09-16 00:00:00")
                                .param("organizerCondition", "0")
                                .param("memberCondition", "0")
                                .param("organizerCouponId", "0")
                                .param("memberCouponId", "0")
                                .param("productId", "1055391812477018112")
                                .param("pskuId", "1059894260910546944")
                                .param("groupLimit", "10")
                                .param("duration", "10")
                                .param("successLimit", "10")
                                .param("orderNum", "10")
                                .param("groupPrice", "2000")
                                .param("successLimit", "10")
                                .param("baseSuccessNum", "100")
                                .param("description", "活动描述")
                                .param("rightInfo", "权益说明")
                                .param("groupPurchaseInfo", "团购说明")
                                .param("createTime", date)
                                .param("updateTime", date)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("GROUP_PURCHASE")))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * 更新拼团活动
     *
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        String url = "/group_purchase/update";
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.mockMvc
                .perform(
                        post(url)
                                .param("id", "1180850664001986560")
                                .param("startTime", "2018-08-16 00:00:00")
                                .param("endTime", "2018-09-16 00:00:00")
                                .param("organizerCondition", "0")
                                .param("memberCondition", "0")
                                .param("organizerCouponId", "0")
                                .param("memberCouponId", "0")
                                .param("productId", "1055391812477018112")
                                .param("pskuId", "1059894260910546944")
                                .param("groupLimit", "10")
                                .param("duration", "10")
                                .param("successLimit", "10")
                                .param("orderNum", "10")
                                .param("groupPrice", "2000")
                                .param("successLimit", "10")
                                .param("baseSuccessNum", "100")
                                .param("description", "活动描述")
                                .param("rightInfo", "权益说明")
                                .param("groupPurchaseInfo", "团购说明")
                                .param("updateTime", date)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("GROUP_PURCHASE")))
                .andExpect(status().is3xxRedirection());
    }


    /**
     * 上下线操作
     *
     * @throws Exception
     */
    @Test
    public void updateOnline() throws Exception {
        String url = "/group_purchase/updateOnline";

        this.mockMvc
                .perform(
                        //请求设置  URL
                        post(url)

                                .param("id", "1180850664001986560")
                                .param("online", "1")
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("GROUP_PURCHASE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(true));
    }


}
