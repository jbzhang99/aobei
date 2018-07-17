package com.aobei.trainconsole.controller.test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.print.Printable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.exceptions.MockitoLimitations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.aobei.train.model.Chapter;
import com.aobei.train.model.Course;
import com.aobei.trainconsole.controller.CourseTeamController;
import com.aobei.trainconsole.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.liyiorg.mbg.bean.Page;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class CourseTeamControllerTest {
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	CourseTeamController courseTeamController;
	
	private MockMvc mockMvc;

	@Before
    public void setup(){
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
	 * 显示列表
	 * @throws Exception
	 */
	@Test
	public void showCourseTeam() throws Exception{
		String url="/courseTeam/showCourseTeam";
		mockMvc.perform(MockMvcRequestBuilders.get(url).param("p", "1").param("ps", "10")
				.with(	//  用户名			角色		
						user("xk").roles("COURSE_BROWSE"))
				)
		        .andExpect(MockMvcResultMatchers.model().attributeExists("page"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("categoryList"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();

	}
	
	/**
	 * 课程详情
	 * @throws Exception 
	 */
	@Test
	public void courseTeamDetails() throws Exception{
		
		String url="/courseTeam/courseTeamDetails/1067665807242977280";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("COURSE_BROWSE"))
				)
		        .andExpect(MockMvcResultMatchers.model().attributeExists("categoryList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("chapList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("course"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}	
	
	/**
	 * 删除课程
	 * @throws Exception
	 */
	@Test
	public void delCourseByTeamId() throws Exception{
		ResultActions resultActions = this.mockMvc.perform(
				MockMvcRequestBuilders.post("/courseTeam/delCourseByTeamId/1089459711030091776")
				.with(	//  用户名			角色		
						user("xk").roles("COURSE_DEL"))
				)
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 跳转到新增页面，分类，服务项目列表显示
	 * @throws Exception
	 */
	@Test
	public void createTeamShow() throws Exception{
		/*ResultActions resultActions = this.mockMvc.perform(
				MockMvcRequestBuilders.post("/courseTeam/createTeamShow/1"));*/
		String url="/courseTeam/createTeamShow/1";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("COURSE_EDIT"))
				)
        .andExpect(MockMvcResultMatchers.model().attributeExists("cList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("sList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("serviceItemList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("cgory"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	/**
	 * 新增课程
	 * @throws Exception
	 */
	@Test
	public void addCourseTeam() throws Exception{
		//封装课程信息
		List<Map<String,Object>> courseList=new ArrayList<>();
		Map<String,Object> courseMap=new HashMap<>();
		courseMap.put("name", "单元测试课程");
		courseMap.put("credit",24);
		courseList.add(courseMap);
		
		//封装章节信息
		List<Map<String,Object>> chpterList=new ArrayList<>();
		Map<String,Object> chpterMap=new HashMap<>();
		chpterMap.put("section_name", "1");
		chpterMap.put("headline", "单元测试");
		chpterMap.put("section_content", "一步一步来研究");
		chpterMap.put("time_length",90);
		chpterList.add(chpterMap);
		
		//封装服务项
		List<Long> categorysList=new ArrayList<>();
		categorysList.add(1067662669953916928L);
		
		//图片
		Map<String,Object> paramsMap=new HashMap<>();
		paramsMap.put("courseTeamList",JacksonUtil.object_to_json(courseList));
		paramsMap.put("chapterList", JacksonUtil.object_to_json(chpterList));
		paramsMap.put("categoryAllList", JacksonUtil.object_to_json(categorysList));
		
		String json = JacksonUtil.object_to_json(paramsMap);
		File file=new File("F:/a.jpg");
		FileInputStream inputStream = new FileInputStream(file);
		MockMultipartFile mf = new MockMultipartFile("course_img",file.getName(),"MediaType.IMAGE_JPEG",inputStream);
		mockMvc.perform(
				//.post("/courseTeam/addCourseTeam").param("courseT", json)
				MockMvcRequestBuilders.fileUpload("/courseTeam/addCourseTeam").file(mf).param("courseT", json)
								.with(	//  用户名			角色		
										user("xk").roles("COURSE_EDIT"))
								)
								.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
								.andDo(MockMvcResultHandlers.print())
								.andReturn();
		
	}
	
	/**
	 * 跳转课程编辑页面
	 * @throws Exception 
	 */
	@Test
	public void updCourseTeam() throws Exception{
		String url="/courseTeam/updCourseTeam/1072071923796303872/1";
		Long course_id=1072071923796303872L;
		int pageNo=1;
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("COURSE_EDIT"))
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
		        .andExpect(MockMvcResultMatchers.model().attributeExists("chpList"))
		        .andExpect(MockMvcResultMatchers.model().attributeExists("ossImg"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("course"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("vptList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cgory"))
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	/**
	 * 修改课程
	 * @throws JsonProcessingException 
	 */
	@Test
	public void editCourseTeam() throws Exception{
		List<Map<String,Object>> courseList=new ArrayList<>();
		Map<String,Object> courseMap=new HashMap<>();
		courseMap.put("name", "单元课程");
		courseMap.put("credit",24);
		courseList.add(courseMap);
		//封装章节信息
		List<Map<String,Object>> chpterList=new ArrayList<>();
		Map<String,Object> chpterMap=new HashMap<>();
		
		chpterMap.put("chapter_id","1072071923796303873");
		chpterMap.put("section_name", "1");
		chpterMap.put("headline", "单元测试");
		chpterMap.put("section_content", "一步一步来研究");
		chpterMap.put("time_length",90);
		chpterList.add(chpterMap);
		
		Map<String,Object> chpterMap1=new HashMap<>();
		chpterMap1.put("chapter_id", "0");
		chpterMap1.put("section_name", "2");
		chpterMap1.put("headline", "单元");
		chpterMap1.put("section_content", "来研究");
		chpterMap1.put("time_length",90);
		chpterList.add(chpterMap1);
		
		//封装服务项
		List<Long> categorysList=new ArrayList<>();
		categorysList.add(1067662669953916928L);
		categorysList.add(1067659430390947840L);
		
		List<Long> delcourses=new ArrayList<>();
		
		

		//图片
		Map<String,Object> paramsMap=new HashMap<>();
		paramsMap.put("courseTeamList",JacksonUtil.object_to_json(courseList));
		paramsMap.put("courseList", JacksonUtil.object_to_json(chpterList));
		paramsMap.put("categoryAllList", JacksonUtil.object_to_json(categorysList));
		paramsMap.put("deleteClist", JacksonUtil.object_to_json(delcourses));
		 
		String json = JacksonUtil.object_to_json(paramsMap);
		File file=new File("F:/a.jpg");
		FileInputStream inputStream = new FileInputStream(file);
		MockMultipartFile mf = new MockMultipartFile("course_img",file.getName(),"MediaType.IMAGE_JPEG",inputStream);
		mockMvc.perform(
				//.post("/courseTeam/addCourseTeam").param("courseT", json)
				MockMvcRequestBuilders.fileUpload("/courseTeam/editCourseTeam/1072071923796303872")
									.file(mf).param("courseT", json).param("pageNo","1")
									.with(	//  用户名			角色		
											user("xk").roles("COURSE_EDIT"))
									)
									.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
									.andDo(MockMvcResultHandlers.print())
									.andReturn();
	}
	
	/**
	 * 根据课程查找培训计划
	 * @throws Exception
	 */
	@Test
	public void courseFindPlan() throws Exception{
		String url="/courseTeam/courseFindPlan/1072071923796303872";
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("COURSE_EDIT"))
				)
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
