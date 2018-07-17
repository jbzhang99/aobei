package com.aobei.trainconsole.controller.test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.aobei.train.model.Serviceitem;
import com.aobei.trainconsole.controller.CategorySunController;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class CategorySunControllerTest {
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	CategorySunController categorySunController;
	
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
	 * 新增分类
	 * @throws Exception 
	 */
	@Test
	public void addCategorySun() throws Exception{
		String name="junit测试分类二级分类的分类";
		String descr="junit测试二级分类的分类";
		String pid=null;
		File file=new File("F:/a.jpg");
		FileInputStream inputStream = new FileInputStream(file);
		MockMultipartFile mf = new MockMultipartFile("logo",file.getName(),"MediaType.IMAGE_JPEG",inputStream);
		mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/categorySun/addCategorySun")
						.file(mf).param("name", name).param("descr",descr).param("pid",pid)
						.with(	//  用户名			角色		
								user("xk").roles("CATEGORY_EDIT")))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	/**
	 * 添加服务项目
	 * @throws Exception 
	 */
	@Test
	public void addServiceItem() throws Exception{
		String url="/categorySun/addServiceItem";
		/*Serviceitem  serviceitem=new Serviceitem();
		serviceitem.setName("大分类服务项目");
		serviceitem.setDescr("测试添加服务无项目");*/
		
		String onlineDate="2018-01-20 10:57:50";
		String offlineDate="2018-08-20 10:57:50";
		
		mockMvc.perform(
				post("/categorySun/addServiceItem")
				.param("category_id","1088228185529409536")
				.param("name","大分类服务项目")
				.param("descr","测试添加服务无项目")
				.param("onlineDate",onlineDate)
				.param("offlineDate",offlineDate)
				.with(	//  用户名			角色		
						user("rpmm").roles("CATEGORY_EDIT"))
				)
		.andExpect(authenticated().withUsername("rpmm"));
		
	}
	
	/**
	 * 编辑分类
	 * @throws Exception 
	 */
	@Test
	public void editCategory() throws Exception{
		File file=new File("F:/b.jpg");
		FileInputStream inputStream = new FileInputStream(file);
		MockMultipartFile mf = new MockMultipartFile("category_logo",file.getName(),"MediaType.IMAGE_JPEG",inputStream);
		String url="/categorySun/editCategory";
		mockMvc.perform(MockMvcRequestBuilders.fileUpload(url)
				.file(mf)
				.param("category_id","1072754929712259072")
				.param("name","修改了分类")
				.param("descr","测试修改分类")
				.with(	//  用户名			角色		
					user("xk").roles("CATEGORY_EDIT"))
				)
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	/**
	 * 编辑服务项目
	 * @throws Exception 
	 */
	@Test
	public void editServiceItem() throws Exception{
		String onlineDate="2018-03-22 10:57:50";
		String offlineDate="2018-06-23 10:57:50";
		String url="/categorySun/editServiceItem";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("serviceitem_id","1072764206413799424")
				.param("category_id","1072752237002645504")
				.param("name","大分类服务项目进行了修改")
				.param("descr","测试修改服务无项目")
				.param("onlineDate",onlineDate)
				.param("offlineDate",offlineDate)
				.with(	//  用户名			角色		
						user("xk").roles("CATEGORY_EDIT"))
					)
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	
	/**
	 * 服务项目下线
	 * @throws Exception 
	 */
	@Test
	public void downServiceItem() throws Exception{
		String url="/categorySun/downServiceItem/1088228339451977728";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("service_id","1088228339451977728")
				.with(	//  用户名			角色		
						user("xk").roles("CATEGORY_EDIT"))
					)
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	
	/**
	 * 服务项目上线
	 * @throws Exception 
	 */
	@Test
	public void upServiceItem() throws Exception{
		String url="/categorySun/upServiceItem/1088228339451977728";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("ss"))
					)
		.andExpect(authenticated().withUsername("xk"));
	}
	/**
	 * 分类删除
	 * @throws Exception
	 */
	@Test
	public void delcategoryParent() throws Exception{
		String url="/categorySun/delcategoryParent/1072752237002645504";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("CATEGORY_EDIT"))
					)
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	/**
	 * 分类跳转编辑页面
	 * @throws Exception 
	 */
	@Test
	public void showCategoryEdit() throws Exception{
		String url="/categorySun/showCategoryEdit/1072752237002645504";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("CATEGORY_EDIT"))
					)
		.andExpect(MockMvcResultMatchers.model().attributeExists("category"))
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	/**
	 * 服务项目跳转编辑页面
	 * @throws Exception 
	 */
	@Test
	public void editServiceItemShow() throws Exception{
		String url="/categorySun/editServiceItemShow/1072764206413799424";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("CATEGORY_EDIT"))
					)
		.andExpect(MockMvcResultMatchers.model().attributeExists("serviceitem"))
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
}
