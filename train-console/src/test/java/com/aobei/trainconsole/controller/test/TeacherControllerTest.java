package com.aobei.trainconsole.controller.test;



import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.File;
import java.io.FileInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;



@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class TeacherControllerTest {

	
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
				// 返回检查  HTTP状态
				/*.alwaysExpect(status().is3xxRedirection())
//				.alwaysExpect(status().is3xxRedirection())
				.build();*/
	}
	/**
	 * 教师列表返回数据
	 * @throws Exception
	 */
	//已测试
	@Test
    public void teacehr_list() throws Exception {
		String url ="/teacherManager/teacherInfo/teacherInfoList";
		
		this.mockMvc
		.perform(
				//请求设置  URL
				post(url)
				/*//设置请求参数
				.param("param1", "value")
				//模拟登录用户*/
				.with(	//  用户名			角色		
						user("rpmm").roles("TEACHER_BROWSE"))
				).andExpect(status().isOk())
				.andExpect(authenticated().withUsername("rpmm"));
    }
	/**
	 * 教师添加方法
	 * @throws Exception
	 */
	@Test
    public void teacehr_add() throws Exception {
		String url ="/teacherManager/teacherInfo/addTeacherInfo";
		
		String fileUrl = "D:/aa.jpg";
		File file = new File(fileUrl);
		FileInputStream inputStream = new FileInputStream(file);
		
		MockMultipartFile mv = new MockMultipartFile("img_icon",file.getName(),"MediaType.IMAGE_JPEG",inputStream);
		
		this.mockMvc
		.perform(
				//请求设置  URL
				MockMvcRequestBuilders.fileUpload(url).file(mv)
				.param("train_course", "1072071923796303872","1067664578395791360")
				.param("age", "12")
				.param("identity_card", "140123199012240067")
				.param("intro", "sssssssss")
				.param("name", "测试教师数据")
				.param("phone", "15313890721")
				.param("sex", "1")
				.param("type", "1")
				.param("state","1")
				.param("userProvinceId", "110000")
				.param("userCityId", "110100")
				.param("userDistrictId", "110105")
				.with(	//  用户名			角色		
						user("rpmm").roles("TEACHER_EDIT"))
				).andExpect(status().is3xxRedirection())
		.andExpect(authenticated().withUsername("rpmm"));
 
	}
	
	/**
	 * 测试跳转教师添加方法
	 * @throws Exception
	 */
	@Test
    public void teacehr_goto_add() throws Exception {
		String url ="/teacherManager/teacherInfo/gotoAdd";
		
		this.mockMvc
		.perform(
				post(url)
				.param("p", "1")
				.with(		
						user("rpmm").roles("TEACHER_EDIT")
						)
				).andExpect(status().isOk())
				.andExpect(authenticated().withUsername("rpmm"));
		
		
	}
	
	/**
	 * 跳转到教师id为1067920663394148352的教师编辑页面
	 * @throws Exception
	 */
	@Test
    public void teacehr_goto_edit() throws Exception {
		String url ="/teacherManager/teacherInfo/gotoEdit";
		this.mockMvc
		.perform(
				post(url)
				.param("p", "1")
				.param("teacher_id", "1067920663394148352")
				.with(		
						user("rpmm").roles("TEACHER_EDIT"))
				).andExpect(status().isOk())
				.andExpect(authenticated().withUsername("rpmm"));
	}
	/**
	 * 执行教师编辑请求
	 * @throws Exception
	 */
	//已测试，需传文件对象
	@Test
	public void teacher_edit()throws Exception{
		String url ="/teacherManager/teacherInfo/updateTeacherInfo";//跳转目标路径
		
		String fileUrl = "D:/aa.jpg";
		File file = new File(fileUrl);
		FileInputStream inputStream = new FileInputStream(file);
		MockMultipartFile mv = new MockMultipartFile("img_eicon",file.getName(),"MediaType.IMAGE_JPEG",inputStream);
		MockMultipartFile mv1 = new MockMultipartFile("img_ecertification",file.getName(),"MediaType.IMAGE_JPEG",new byte[0]);
		mockMvc.perform(
				MockMvcRequestBuilders.fileUpload(url).file(mv).file(mv1)
				.param("train_course", "1070079472529334272","1067918334305525760")
				.param("age", "12")
				.param("identity_card", "140602199507290535")
				.param("intro", "sssssssss")
				.param("name", "测试教师数据")
				.param("phone", "15313890721")
				.param("sex", "1")
				.param("type", "1")
				.param("teacher_id", "1090192759151157248")
				.param("state","1")
				.param("userProvinceId", "110000")
				.param("userCityId", "110100")
				.param("userDistrictId", "110105")
				.with(	//  用户名			角色		
						user("rpmm").roles("TEACHER_EDIT"))
				).andExpect(status().is3xxRedirection())
				//.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
		.andExpect(authenticated().withUsername("rpmm"));
	}
	/**
	 * 执行教师删除请求(随机删除教师：测试数据)
	 * @throws Exception
	 */
	@Test
    public void teacehr_del() throws Exception {
		String url ="/teacherManager/teacherInfo/deleteTeacherInfo/1090192759151157248";
		
		this.mockMvc
		.perform(
				post(url)
				.with(		
						user("rpmm").roles("TEACHER_DEL"))
				).andExpect(status().isOk())
				.andExpect(authenticated().withUsername("rpmm"));
		
		
	}
	
}
