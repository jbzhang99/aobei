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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * 视频管理
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional            // 自动回滚事务
@ActiveProfiles("dev")    // 使用开发环境
public class VideoControllerTest {


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
     * 视频列表数据
     *
     * @throws Exception
     */

    @Test
    public void videoList() throws Exception {
        String url = "/videomanager/video_list";

        this.mockMvc
                .perform(
                        //请求设置  URL
                        post(url)

                                .param("p", "1")
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("VIDEO_BROWSE")))
                .andExpect(status().isOk());
    }

    /**
     * 添加视频
     * @throws Exception
     */
    @Test
    public void videoInsert() throws Exception {
        String url = "/videomanager/video_update";
        MockMultipartFile imgFile = new MockMultipartFile("imgFile", "filename.png", "text/plain", "some xml".getBytes());
        this.mockMvc
                .perform(
                        //请求设置  URL
                        MockMvcRequestBuilders.fileUpload(url)
                                .file(imgFile)
                                .param("title", "标题")
                                .param("descripte", "简介")
                                .param("client_type", "custom")
                                .param("video_url", "https://test.mp4")
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("VIDEO_BROWSE")))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * 更新视频
     * @throws Exception
     */
    @Test
    public void videoUpdate() throws Exception {
        String url = "/videomanager/video_update";
        MockMultipartFile imgFile = new MockMultipartFile("imgFile", "filename.png", "text/plain", "some xml".getBytes());
        this.mockMvc
                .perform(
                        //请求设置  URL
                        MockMvcRequestBuilders.fileUpload(url)
                                .file(imgFile)
                                .param("video_content_id", "1163491022636802048")
                                .param("title", "标题")
                                .param("descripte", "简介")
                                .param("client_type", "custom")
                                .param("video_url", "https://test.mp4")
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("VIDEO_BROWSE")))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * 更新权重
     *
     * @throws Exception
     */

    @Test
    public void updateOrderNum() throws Exception {
        String url = "/videomanager/updateOrderNum";

        this.mockMvc
                .perform(
                        //请求设置  URL
                        post(url)

                                .param("id", "1")
                                .param("orderNum", "200")
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("VIDEO_BROWSE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(false)) ;
    }

    /**
     * 上下线操作
     * @throws Exception
     */
    @Test
    public void updateOnline() throws Exception {
        String url = "/videomanager/updateOnline";

        this.mockMvc
                .perform(
                        //请求设置  URL
                        post(url)

                                .param("id", "1")
                                .param("online", "1")
                                //模拟登录用户*/
                                .with(    //  用户名			角色
                                        user("rpmm").roles("VIDEO_BROWSE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(false)) ;
    }



}
