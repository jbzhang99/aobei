package com.aobei.trainconsole.controller.test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.print.Printable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
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

import com.aobei.train.model.Student;
import com.aobei.trainconsole.controller.StudentController;
import com.aobei.trainconsole.util.JacksonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class StudentControllerTest {
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	StudentController studentController;
	
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
	 * 显示学员列表
	 * @throws Exception 
	 */
	@Test
	public void showStudent() throws Exception{
		String url="/student/showStudent";
		this.mockMvc.perform(
				post(url).param("p", "1").param("ps", "10")
				.param("sex_selected", "2")
				.param("state_selected", "2")
				.param("grade_selected", "0")
				.param("partner_name_selected", "1")
				.param("serItem_selected", "0")
				.param("conditions", "")
				.with(	//  用户名			角色		
					user("xk").roles("STUDENT_BROWSE"))
				)
		 .andExpect(MockMvcResultMatchers.model().attributeExists("serList"))
		 .andExpect(MockMvcResultMatchers.status().isOk())
		 .andDo(MockMvcResultHandlers.print())
		 .andReturn();
	}
	
	/**
	 * 跳转到学员编辑页面
	 * @throws Exception 
	 */
	@Test
	public void updStudent() throws Exception{
		String url="/student/updStudent/4/1";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("STUDENT_EDIT"))
					)
		        .andExpect(MockMvcResultMatchers.model().attributeExists("perList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("student"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pageNo"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("stationList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pfsList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("ssList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("img_against"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("img_just"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("img"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("logo_img"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	/**
	 * 编辑学员信息
	 * @throws Exception 
	 */
	@Test
	public void editStudent() throws Exception{
		Student student=new Student();
		student.setStudent_id(4L);
		student.setName("王藏三");
		student.setStation_id(1067859428202274816L);
		student.setSex(1);
		student.setNative_place("辽宁");
		student.setIdentity_card("140428198012122211");
		student.setPartner_id(1L);
		student.setPhone("13545453434");
		student.setAge(22);
		student.setState(1);
		student.setGrade(1);
		List<Student> studentList=new ArrayList<>();
		studentList.add(student);
		
		List<Long> categoryList=new ArrayList<>();
		categoryList.add(1067659430390947840L);


		List<Long> img_list=new ArrayList<>();
		img_list.add(1064889803843854336L);
		Map<String,Object> param=new HashMap<>();
		param.put("studentList", JacksonUtil.object_to_json(studentList));
		param.put("categoryAllList", JacksonUtil.object_to_json(categoryList));
		param.put("img_list",JacksonUtil.object_to_json(img_list));
		File file=new File("F:/a.jpg");
		FileInputStream inputStream = new FileInputStream(file);
		MockMultipartFile mf = new MockMultipartFile("logo",file.getName(),"MediaType.IMAGE_JPEG",inputStream);
		MockMultipartFile mf2 = new MockMultipartFile("s_health","","MediaType.IMAGE_JPEG",new byte[0]);
		MockMultipartFile mf3 = new MockMultipartFile("s_just","","MediaType.IMAGE_JPEG",new byte[0]);
		MockMultipartFile mf4 = new MockMultipartFile("s_against","","MediaType.IMAGE_JPEG",new byte[0]);
		mockMvc.perform(
				//.post("/courseTeam/addCourseTeam").param("courseT", json)
				MockMvcRequestBuilders.fileUpload("/student/editStudent/4")
					.file(mf).file(mf2).file(mf3).file(mf4)
					.param("pageNo", "1")
					.param("studentL",JacksonUtil.object_to_json(param))
					.with(	//  用户名			角色		
							user("xk").roles("STUDENT_EDIT"))
						)
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	
	/**
	 * 导入学员
	 * @throws Exception 
	 */
	@Test
	public void addStudent() throws Exception{
		File file=new File("C:/Users/xk873/Desktop/学员信息 (1).xlsx");
		FileInputStream inputStream = new FileInputStream(file);
		MockMultipartFile mf = new MockMultipartFile("file",file.getName(),"MediaType.ALL",inputStream);
		mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/student/addStudent").file(mf)
				.with(	//  用户名			角色		
						user("xk").roles("STUDENT_IMPORT"))
					)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	
	/**
	 * 查询没有被添加过得服务项目
	 * @throws Exception 
	 */
	@Test
	public void screenServiceItem() throws Exception{
		List<Long> haveServiceItemList=new ArrayList<>();
		String url="/student/screenServiceItem";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("result",JacksonUtil.object_to_json(haveServiceItemList))
				.with(	//  用户名			角色		
						user("xk").roles("STUDENT_EDIT"))
					)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	/**
	 * 根据id查对应服务对象
	 * @throws Exception 
	 */
	@Test
	public void ServiceItemSelect() throws Exception{
		String url="/student/ServiceItemSelect/1067659430390947840";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("STUDENT_EDIT"))
					)
		        .andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	/**
	 * 根据合伙人id查找其所有的站点 
	 * @throws Exception 
	 */
	@Test
	public void partnerSelectStation() throws Exception{
		String url="/student/partnerSelectStation/1067671915911208960";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("STUDENT_EDIT"))
					)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}


	@Test
	public void sendInsurance() throws Exception {
		String url="/student/sendInsurance/1067671915911208960/1";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色
					user("xk").roles("INSURANCE_REGISTER"))
		)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}

	@Test
	public void screenInsurance() throws Exception {
		String url="/student/screenInsurance/1067671915911208960";
		mockMvc.perform(MockMvcRequestBuilders.get(url))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}


	@Test
	public void addStudentInsurance() throws Exception {

		List<Long> list=new ArrayList<>();
		list.add(1107689384943837184L);
		list.add(1107799657214140416L);

		String url="/student/addStudentInsurance/1067671915911208960";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("result",JacksonUtil.object_to_json(list))
				.with(	//  用户名			角色
					user("xk").roles("INSURANCE_REGISTER"))
		)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
}
