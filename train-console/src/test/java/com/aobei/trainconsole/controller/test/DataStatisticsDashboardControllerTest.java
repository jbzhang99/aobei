package com.aobei.trainconsole.controller.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Dashboard 首页 测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional            // 自动回滚事务
@ActiveProfiles("dev")    // 使用开发环境
public class DataStatisticsDashboardControllerTest {

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
                //.alwaysExpect(status().isOk())
                .build();
    }


    /**
     * Dashboard首页
     *
     * @throws Exception
     */
    @Test
    public void index() throws Exception {
        String url = "/data_statistics/dashboard/index";

        this.mockMvc
                .perform(
                        //请求设置  URL
                        get(url)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("DATA_STATISTICS")))
                .andExpect(status().isOk());
    }


    /**
     * 加载 订单总GMV统计
     *
     * @throws Exception
     */
    @Test
    public void gmvYearTotalSum() throws Exception {
        String url = "/data_statistics/dashboard/gmvYearTotalSum";
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.mockMvc
                .perform(
                        //请求设置  URL
                        get(url)
                                .param("type", "3")
                                .param("startDate", "2018-01-01")
                                .param("endDate", endDate)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("DATA_STATISTICS")))
                .andExpect(status().isOk());
    }

    /**
     * 加载 订单总GMV柱状图统计
     *
     * @throws Exception
     */
    @Test
    public void gmvYearSum() throws Exception {
        String url = "/data_statistics/dashboard/gmvYearSum";
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.mockMvc
                .perform(
                        //请求设置  URL
                        get(url)
                                .param("type", "3")
                                .param("startDate", "2018-01-01")
                                .param("endDate", endDate)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("DATA_STATISTICS")))
                .andExpect(status().isOk());
    }

    /**
     * APP GMV统计
     *
     * @throws Exception
     */
    @Test
    public void gmvYearAppSum() throws Exception {
        String url = "/data_statistics/dashboard/gmvYearAppSum";
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.mockMvc
                .perform(
                        //请求设置  URL
                        get(url)
                                .param("type", "3")
                                .param("startDate", "2018-01-01")
                                .param("endDate", endDate)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("DATA_STATISTICS")))
                .andExpect(status().isOk());
    }

    /**
     * 小程序 GMV统计
     *
     * @throws Exception
     */
    @Test
    public void gmvYearWxmSum() throws Exception {
        String url = "/data_statistics/dashboard/gmvYearWxmSum";
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.mockMvc
                .perform(
                        //请求设置  URL
                        get(url)
                                .param("type", "3")
                                .param("startDate", "2018-01-01")
                                .param("endDate", endDate)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("DATA_STATISTICS")))
                .andExpect(status().isOk());
    }

    /**
     * H5 GMV统计
     *
     * @throws Exception
     */
    @Test
    public void gmvYearH5Sum() throws Exception {
        String url = "/data_statistics/dashboard/gmvYearH5Sum";
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.mockMvc
                .perform(
                        //请求设置  URL
                        get(url)
                                .param("type", "3")
                                .param("startDate", "2018-01-01")
                                .param("endDate", endDate)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("DATA_STATISTICS")))
                .andExpect(status().isOk());
    }

    /**
     * 电商 GMV统计
     *
     * @throws Exception
     */
    @Test
    public void gmvYearEbSum() throws Exception {
        String url = "/data_statistics/dashboard/gmvYearEbSum";
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.mockMvc
                .perform(
                        //请求设置  URL
                        get(url)
                                .param("type", "3")
                                .param("startDate", "2018-01-01")
                                .param("endDate", endDate)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("DATA_STATISTICS")))
                .andExpect(status().isOk());
    }


}
