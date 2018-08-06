package com.aobei.trainconsole.controller.test;

import com.aobei.train.model.Fallinto;
import com.aobei.trainconsole.controller.BalanceController;
import com.aobei.trainconsole.util.JacksonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 数据统计优惠卷测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional            // 自动回滚事务
@ActiveProfiles("dev")    // 使用开发环境
public class DataStatisticsCouponControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BalanceController balanceController;

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
     * 优惠卷首页
     *
     * @throws Exception
     */
    @Test
    public void index() throws Exception {
        String url = "/data_statistics/coupon/index";

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
    public void loadCouponData() throws Exception {
        String url = "/data_statistics/coupon/loadCouponData";
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
     * 加载 优惠卷表格数据
     *
     * @throws Exception
     */
    @Test
    public void loadCouponTableData() throws Exception {
        String url = "/data_statistics/coupon/loadCouponTableData";
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
     * 加载 优惠卷地图数据
     *
     * @throws Exception
     */
    @Test
    public void loadCouponMapData() throws Exception {
        String url = "/data_statistics/coupon/loadCouponMapData";
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
