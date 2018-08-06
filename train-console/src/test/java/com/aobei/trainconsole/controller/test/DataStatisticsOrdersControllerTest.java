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
 * 订单数据统计 测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional            // 自动回滚事务
@ActiveProfiles("dev")    // 使用开发环境
public class DataStatisticsOrdersControllerTest {

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
     * 订单统计首页
     *
     * @throws Exception
     */
    @Test
    public void index() throws Exception {
        String url = "/data_statistics/orders/index";

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
     * 加载 柱状图数据
     *
     * @throws Exception
     */
    @Test
    public void getDataSet() throws Exception {
        String url = "/data_statistics/orders/getDataSet";
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.mockMvc
                .perform(
                        //请求设置  URL
                        get(url)
                                .param("type", "1")
                                .param("startDate", "2018-01-01")
                                .param("endDate", endDate)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("DATA_STATISTICS")))
                .andExpect(status().isOk());
    }


    /**
     * 加载 订单表格数据
     *
     * @throws Exception
     */
    @Test
    public void getDataSetTable() throws Exception {
        String url = "/data_statistics/orders/getDataSetTable";
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.mockMvc
                .perform(
                        //请求设置  URL
                        get(url)
                                .param("type", "1")
                                .param("startDate", "2018-01-01")
                                .param("endDate", endDate)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("DATA_STATISTICS")))
                .andExpect(status().isOk());
    }

    /**
     * 加载 地图数据
     *
     * @throws Exception
     */
    @Test
    public void getDataSetMap() throws Exception {
        String url = "/data_statistics/orders/getDataSetMap";
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.mockMvc
                .perform(
                        //请求设置  URL
                        get(url)
                                .param("type", "1")
                                .param("startDate", "2018-01-01")
                                .param("endDate", endDate)
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("DATA_STATISTICS")))
                .andExpect(status().isOk());
    }

}
