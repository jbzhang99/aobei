package com.aobei.trainconsole.controller.test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.aobei.train.model.ProSku;
import com.aobei.trainconsole.controller.ProductController;
import com.aobei.trainconsole.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class ProductControllerTest {
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	ProductController productController;
	
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
	 * 显示分类
	 * @throws Exception 
	 */
	@Test
	public void showProduct() throws Exception{
		String url="/product/showProductCategory";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
        .andExpect(MockMvcResultMatchers.model().attributeExists("categoryList"))
        .andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	/**
	 * 根据分类找出其子分类和服务项目
	 * @throws Exception
	 */
	@Test
	public void selectCategory() throws Exception{
		String url="/product/selectCategory/1067645388003762176";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	
	/**
	 * 跳转到添加商品页面，展示已选择的服务项目信息  
	 * @throws Exception 
	 */
	@Test
	public void showCategoryNext() throws Exception{
		String url="/product/showCategoryNext/1067659430390947840";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
				.andExpect(MockMvcResultMatchers.model().attributeExists("category"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("serviceitem"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	/**
	 * 添加商品
	 * @throws Exception 
	 */
	@Test
	public void addCommodityProduct() throws Exception{
		File file=new File("F:/a.jpg");
		FileInputStream inputStream = new FileInputStream(file);
		MockMultipartFile mf = new MockMultipartFile("commodity_img",file.getName(),"MediaType.IMAGE_JPEG",inputStream);
		String url="/product/addCommodityProduct";
		mockMvc.perform(MockMvcRequestBuilders.fileUpload(url).file(mf)
				.param("name", "单元测试商品").param("descript", "单元测试")
				.param("seo", "测试").param("base_buyed", "1000")
				.param("sort_num", "1").param("online", "1")
				.param("ueditorContext","").param("serviceitem_id","1067659430390947840")
				.param("category_id","1067645388003762176")
				.param("category_level_code","0007")
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
		.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	/**
	 * 添加sku
	 * @throws Exception 
	 */
	@Test
	public void addSku() throws Exception{
		List<ProSku> proSkuList=new ArrayList<>();
		ProSku proSku=new ProSku();
		proSku.setProduct_id(1098211414441222144L);
		proSku.setName("单元测试Sku1");
		proSku.setPricev(18800);
		proSku.setPrice(16800);
		proSku.setSort_num(1);
		proSku.setDescript("测试");
		proSku.setService_time_length(2);
		proSku.setService_times("08:00-10:00,12:00-14:00");
		proSku.setBuy_limit(6);
        proSku.setBuy_multiple(1);
        proSku.setBuy_multiple_min(2);
        proSku.setBuy_multiple_max(34);
        proSku.setBuy_multiple_o2o(1);
		proSkuList.add(proSku);
		ProSku proSku2=new ProSku();
		proSku2.setProduct_id(1098211414441222144L);
		proSku2.setName("单元测试Sku2");
		proSku2.setPricev(28800);
		proSku2.setPrice(26800);
		proSku2.setSort_num(2);
		proSku2.setDescript("测试");
		proSku2.setService_time_length(2);
		proSku2.setService_times("14:00-16:00");
		proSku2.setBuy_limit(6);
        proSku2.setBuy_limit(6);
        proSku2.setBuy_multiple(1);
        proSku2.setBuy_multiple_min(2);
        proSku2.setBuy_multiple_max(34);
        proSku2.setBuy_multiple_o2o(1);
		proSkuList.add(proSku2);
        List<ProSku> proSkuList2=new ArrayList<>();
		String url="/product/addSku/1098211414441222144";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("result",JacksonUtil.object_to_json(proSkuList))
                .param("unsetSkuList",JacksonUtil.object_to_json(proSkuList2))
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	
	/**
	 * 显示商品列表页
	 * @throws Exception 
	 */
	@Test
	public void showProductList() throws Exception{
		String url="/product/showProductList";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p","1").param("ps","10")
				.param("category_name_selected","0").param("serviceItem_name_selected","0")
				.param("online_selected","2").param("name","")
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_BROWSE"))
				)
		.andExpect(MockMvcResultMatchers.model().attributeExists("productList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("ossImgList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("categoryList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("category_name_selected"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("serviceItem_name_selected"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("online_selected"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("name"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	/**
	 * 下架
	 * @throws Exception 
	 */
	@Test
	public void downProduct() throws Exception{
		String url="/product/downProduct/1087486520988557312";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	
	/**
	 * 上架
	 * @throws Exception 
	 */
	@Test
	public void upProduct() throws Exception{
		String url="/product/upProduct/1087486520988557312";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	
	/**
	 * 商品编辑页面
	 * @throws Exception 
	 */
	@Test
	public void editProductShow() throws Exception{
		String url="/product/editProductShow/1087486520988557312/1";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
		.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("serviceitem"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("category"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	/**
	 * 商品Sku编辑页面
	 * @throws Exception 
	 */
	@Test
	public void editProduct() throws Exception{
		//File file=new File("F:/a.jpg");
		//FileInputStream inputStream = new FileInputStream(file);
		MockMultipartFile mf = new MockMultipartFile("commodity_img","","MediaType.IMAGE_JPEG",new byte[0]);
		String url="/product/editProduct/1";
		mockMvc.perform(MockMvcRequestBuilders.fileUpload(url).file(mf)
				.param("product_id","1074207930654154752")
				.param("name", "单元测试商品修改").param("descript", "单元测试修改")
				.param("seo", "测试").param("base_buyed", "1000")
				.param("sort_num", "1").param("online", "1")
				.param("ueditorContext","<p>测试修改富文本编辑器</p>").param("serviceitem_id","1067659430390947840")
				.param("category_id","1067645388003762176")
				.param("category_level_code","0007")
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
		.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("proSkuList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("timeUnis"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("returnList"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	/**
	 * 编辑sku
	 * @throws Exception 
	 * @throws JsonProcessingException 
	 */
	@Test
	public void editSku() throws Exception{
		List<ProSku> proSkuList=new ArrayList<>();
		ProSku proSku=new ProSku();
        proSku.setPsku_id(1098212152101855232L);
		proSku.setProduct_id(1098211414441222144L);
		proSku.setName("单元测试Sku1修改");
		proSku.setPricev(18800);
		proSku.setPrice(16800);
		proSku.setSort_num(1);
		proSku.setDescript("测试修改");
		proSku.setService_time_length(0);
		proSku.setService_times("08:00");
		proSku.setBuy_limit(6);
		proSku.setBuy_multiple(1);
        proSku.setBuy_multiple_min(2);
        proSku.setBuy_multiple_max(34);
        proSku.setBuy_multiple_o2o(1);
		proSkuList.add(proSku);

        List<ProSku> proSkuList2=new ArrayList<>();
		ProSku proSku2=new ProSku();
        proSku2.setPsku_id(1098212152504508416L);
		proSku2.setProduct_id(1098211414441222144L);
		proSku2.setName("单元测试Sku2修改");
		proSku2.setPricev(28800);
		proSku2.setPrice(26800);
		proSku2.setSort_num(2);
		proSku2.setDescript("测试");
		proSku2.setService_time_length(2);
		proSku2.setService_times("14:00-16:00");
		proSku2.setBuy_limit(6);
        proSku2.setBuy_multiple(1);
        proSku2.setBuy_multiple_min(2);
        proSku2.setBuy_multiple_max(34);
        proSku2.setBuy_multiple_o2o(1);
        proSkuList2.add(proSku2);

        List<ProSku> proSkuList3=new ArrayList<>();
		String url="/product/editSku/1098211414441222144";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("result",JacksonUtil.object_to_json(proSkuList2))
                .param("delPskIdList",JacksonUtil.object_to_json(proSkuList3))
                .param("unsetSkuList",JacksonUtil.object_to_json(proSkuList))
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	
	@Test
	public void selectTime() throws Exception{
		String url="/product/selectTime/18/2";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	
	@Test
	public void selectEditShowTime() throws Exception{
		String p_service_times="08:00-10:00";
		String url="/product/selectEditShowTime/"+p_service_times;
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("PRODUCT_EDIT"))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	
	@Test
	public void categorySelectSerList() throws Exception{
		String url="/product/categorySelectSerList/1067645388003762176";
		mockMvc.perform(MockMvcRequestBuilders.get(url))
		//返回检查  返回头检查
		//.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	

	@Test
	public void cancleDelProduct() throws Exception{
		String url="/product/cancleDelProduct/1074207930654154752";
		mockMvc.perform(MockMvcRequestBuilders.get(url))
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}


	@Test
	public void screenPartner() throws Exception {
		String url="/product/screenPartner/1094693084682018816";
		List<Long> longs=new ArrayList<>();
		longs.add(1067671915911208960L);
		longs.add(1067670088150966272L);
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("result",JacksonUtil.object_to_json(longs))
				.with(	//  用户名			角色
					user("xk").roles("PRODUCT_SENDORDERS"))
				)
                .andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

    @Test
    public void sendOrderPartner() throws Exception {
        String url="/product/sendOrderPartner/1094693084682018816/1";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .with(	//  用户名			角色
                        user("xk").roles("PRODUCT_SENDORDERS"))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void addOrderPartner() throws Exception {
        String url="/product/addOrderPartner/1094693084682018816";
        List<Long> longs=new ArrayList<>();
        longs.add(1067671915911208960L);
        longs.add(1067670088150966272L);
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .param("result",JacksonUtil.object_to_json(longs))
                .with(	//  用户名			角色
                        user("xk").roles("PRODUCT_SENDORDERS"))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void productDetails() throws Exception {
        String url="/product/productDetails/1094693084682018816";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void editPSkuShow() throws Exception {
        String url="/product/editPSkuShow/1094693084682018816/1";
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

}
