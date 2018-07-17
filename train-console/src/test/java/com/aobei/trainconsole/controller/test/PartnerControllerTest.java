package com.aobei.trainconsole.controller.test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aobei.train.model.Fallinto;
import com.aobei.train.model.PartnerFallinto;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.aobei.train.model.Partner;
import com.aobei.train.model.VirtualAddress;
import com.aobei.trainconsole.controller.PartnerController;
import com.aobei.trainconsole.util.JacksonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class PartnerControllerTest {
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	PartnerController partnerController;
	
	private MockMvc mockMvc;

	@Before
    public void setup(){
       // mockMvc = MockMvcBuilders.standaloneSetup(partnerController).build();
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
	 * 列表展示
	 * @throws Exception 
	 */
	@Test
	public  void showPartner() throws Exception{
		/*String url="/partner/showPartner";
		mockMvc.perform(MockMvcRequestBuilders.get(url).param("p", "2").param("ps", "10"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("page"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("partnerList"))
		.andDo(MockMvcResultHandlers.print())
		.andReturn();*/
		this.mockMvc
		.perform(
				//请求设置  URL
				post("/partner/showPartner")
				//设置请求参数
				.param("p", "2").param("ps", "10")
				//模拟登录用户
				.with(	//  用户名			角色		
					user("rpmm").roles("PARTNER_BROWSE"))
				)
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	/**
	 * 跳转新增合伙人页面
	 * @throws Exception
	 */
	@Test
	public void addpartnerShow() throws Exception{
		String url="/partner/addpartnerShow/1";
		/*mockMvc.perform(MockMvcRequestBuilders.get(url))
		.andDo(MockMvcResultHandlers.print())
		.andReturn();*/
		this.mockMvc
		.perform(
				//请求设置  URL
				post(url)
				//模拟登录用户
				.with(	//  用户名			角色		
					user("xk").roles("PARTNER_EDIT"))
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	/**
	 * 新增合伙人跳转下一步页面
	 * @throws Exception 
	 */
	@Test
	public void next() throws Exception{
		String url="/partner/next/1";
		File just=new File("F:/a.jpg");
		FileInputStream inputStream = new FileInputStream(just);
		MockMultipartFile mf = new MockMultipartFile("just",just.getName(),"MediaType.IMAGE_JPEG",inputStream);
		File against=new File("F:/b.jpg");
		FileInputStream inputStream2 = new FileInputStream(against);
		MockMultipartFile mf2 = new MockMultipartFile("against",against.getName(),"MediaType.IMAGE_JPEG",inputStream2);
		//FileInputStream inputStream3 = new FileInputStream("sad");
		MockMultipartFile mf3 = new MockMultipartFile("license","","MediaType.IMAGE_JPEG",new byte[0]);
		mockMvc.perform(MockMvcRequestBuilders.fileUpload(url)
				.file(mf).file(mf2).file(mf3)
				.param("name","单元测试家政服务中心")
				.param("address","锋创科技园")
				.param("linkman","许久")
				.param("phone","13245551221")
				.param("code","111111111111111111")
				.param("bank_name","北京银行")
				.param("bank_code","653121290876543")
				.param("coo_start","2018-03-20")
				.param("coo_end","2018-07-30")
				.param("ope_start","")
				.param("ope_end","")
				.with(	//  用户名			角色		
						user("rpmm").roles("PARTNER_EDIT"))
				)
		/*.andExpect(MockMvcResultMatchers.model().attributeExists("pcList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("perList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("pageNo"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("partner"))*/
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
	
	/**
	 * 添加合伙人信息
	 * @throws ParseException 
	 */
	@Test
	public void addPartner() throws Exception{
		String coo_start="2018-03-20";
		String coo_end="2018-07-30";
		Partner partner=new Partner();
		partner.setName("xxx家政服务中心");
		partner.setAddress("锋创科技园");
		partner.setLinkman("许久");
		partner.setPhone("13245551221");
		partner.setCode("111111111111111111");
		partner.setBank_name("北京银行");
		partner.setBank_code("653121290876543");
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		partner.setCooperation_start(sf.parse(coo_start));
		partner.setCooperation_end(sf.parse(coo_end));
		partner.setIdentity_card_just("1072940522245218304");
		partner.setIdentity_card_against("1072940541027311616");
		JacksonUtil.object_to_json(partner);
		
		List<Long> ids=new ArrayList<>();
		ids.add(1067661137892106240L);
		ids.add(1067659430390947840L);
		
		Map<String,Object> params=new HashMap<>();
		params.put("ids", JacksonUtil.object_to_json(ids));
		params.put("parList", JacksonUtil.object_to_json(partner));
		
		List<VirtualAddress> list=new ArrayList<>();
		String online="2018-03-20";
		String upline="2018-09-30";
		VirtualAddress virtualAddress=new VirtualAddress();
		virtualAddress.setAddress("北京市 北京市 大兴区 科创十三街18号院5号楼13层");
		virtualAddress.setAddressV("110000 110100 110115");
		virtualAddress.setOnline(sf.parse(online));
		virtualAddress.setUpline(sf.parse(upline));
		list.add(virtualAddress);
		
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/partner/addPartner")
				.param("result",JacksonUtil.object_to_json(params))
				.param("stationList",JacksonUtil.object_to_json(list))
				.with(	//  用户名			角色		
						user("xk").roles("PARTNER_EDIT"))
					)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		
	}
	
	/**
	 * 跳转合伙人编辑页面
	 * @throws Exception 
	 */
	@Test
	public void editPartner() throws Exception{
		String url="/partner/editPartner/1073007545612525568/1";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("PARTNER_EDIT"))
					)
				//返回检查  返回头检查
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pageNo"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("partner"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("licenseUrl"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("justUrl"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("againstUrl"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}
	
	/**
	 * 跳转编辑合伙人下一步页面
	 * @throws Exception 
	 */
	@Test
	public void nextEdit() throws Exception{
	/*	String coo_start="2018-03-20";
		String coo_end="2018-07-30";
		Partner partner=new Partner();
		partner.setName("单元测试家政服务中心进行了修改");
		partner.setAddress("锋创科技园5号楼");
		partner.setLinkman("许久");
		partner.setPhone("13245551221");
		partner.setCode("111111111111111111");
		partner.setBank_name("北京银行");
		partner.setBank_code("6531212908765434");
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		partner.setCooperation_start(sf.parse(coo_start));
		partner.setCooperation_end(sf.parse(coo_end));
		partner.setIdentity_card_just("1072940522245218304");
		partner.setIdentity_card_against("1072940541027311616");*/
		
		String url="/partner/nextEdit/1";
		File license=new File("F:/a.jpg");
		FileInputStream inputStream = new FileInputStream(license);
		MockMultipartFile mf = new MockMultipartFile("license",license.getName(),"MediaType.IMAGE_JPEG",inputStream);
		MockMultipartFile mf2 = new MockMultipartFile("just","","MediaType.IMAGE_JPEG",new byte[0]);
		MockMultipartFile mf3 = new MockMultipartFile("against","","MediaType.IMAGE_JPEG",new byte[0]);
		
		mockMvc.perform(MockMvcRequestBuilders.fileUpload(url)
				.file(mf).file(mf2).file(mf3)
				.param("partner_id","1073007545612525568")
				.param("name","单元测试家政服务中心进行了修改")
				.param("address","锋创科技园5号楼")
				.param("linkman","许久")
				.param("phone","13245551221")
				.param("code","111111111111111111")
				.param("phone","13245551221")
				.param("bank_name","北京银行")
				.param("bank_code","6531212908765439")
				.param("coo_start","2018-03-20")
				.param("coo_end","2018-07-30")
				.param("ope_start","2017-03-20")
				.param("ope_end","2020-03-20")
				.with(	//  用户名			角色		
						user("xk").roles("PARTNER_EDIT"))
					)
		//返回检查  返回头检查
		.andExpect(content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("pfpList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("perList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("pcList"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("partner"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("pageNo"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("list_station"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
		
	}
	
	/**
	 * 修改合伙人
	 * @throws Exception 
	 */
	@Test
	public void updatePartner() throws Exception{
		String coo_start="2018-03-20";
		String coo_end="2018-07-30";
		Partner partner=new Partner();
		partner.setPartner_id(Long.parseLong("1073007545612525568"));
		partner.setName("单元测试家政服务中心进行了修改");
		partner.setAddress("锋创科技园5号楼");
		partner.setLinkman("许久");
		partner.setPhone("13245551221");
		partner.setCode("111111111111111111");
		partner.setBank_name("北京银行");
		partner.setBank_code("6531212908765434");
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		partner.setCooperation_start(sf.parse(coo_start));
		partner.setCooperation_end(sf.parse(coo_end));
		partner.setOperation_start(sf.parse("2017-03-20"));
		partner.setOperation_end(sf.parse("2020-03-20"));
		partner.setIdentity_card_just("1072940522245218304");
		partner.setIdentity_card_against("1072940541027311616");
		partner.setBusiness_license("1073468365991600128");
		JacksonUtil.object_to_json(partner);
		
		List<Long> ids=new ArrayList<>();
		ids.add(1067661137892106240L);
		ids.add(1067659430390947840L);
		
		Map<String,Object> params=new HashMap<>();
		params.put("ids", JacksonUtil.object_to_json(ids));
		params.put("parList", JacksonUtil.object_to_json(partner));
		
		List<VirtualAddress> list=new ArrayList<>();
		String online="2018-03-20";
		String upline="2018-09-30";
		VirtualAddress virtualAddress=new VirtualAddress();
		virtualAddress.setAddress("北京市 北京市 大兴区 科创十三街18号院5号楼13层");
		virtualAddress.setAddressV("110000 110100 110115");
		virtualAddress.setOnline(sf.parse(online));
		virtualAddress.setUpline(sf.parse(upline));
		virtualAddress.setId("0");
		list.add(virtualAddress);
		
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/partner/updatePartner")
				.param("result",JacksonUtil.object_to_json(params))
				.param("stationList",JacksonUtil.object_to_json(list))
				.with(	//  用户名			角色		
						user("xk").roles("PARTNER_EDIT"))
					)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}


	@Test
	public void addFallintoShow() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.post("/partner/addFallintoShow/1108236163540082688/1")
				.with(	//  用户名			角色
					user("xk").roles("PARTNER_FALLINTO"))
		)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}


	@Test
	public void addPartnerToFallino() throws Exception {

		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

		List<PartnerFallinto> pf=new ArrayList<>();
		PartnerFallinto p1=new PartnerFallinto();
		p1.setPartner_id(1108236163540082688L);
		p1.setProduct_id(1078586809128869888L);
		p1.setFallinto_id(1108346875776098304L);
		p1.setStart_datetime(sf.parse("2018-05-11 00:00:00"));
		p1.setEnd_datetime(sf.parse("2018-09-11 00:00:00"));

		PartnerFallinto p2=new PartnerFallinto();
		p2.setPartner_id(1108236163540082688L);
		p2.setProduct_id(1079997533029949440L);
		p2.setFallinto_id(1108346875776098304L);
		p2.setStart_datetime(sf.parse("2018-05-11 00:00:00"));
		p2.setEnd_datetime(sf.parse("2018-09-11 00:00:00"));
		pf.add(p2);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/partner/addPartnerToFallino/1108236163540082688")
				.param("result",JacksonUtil.object_to_json(pf))
				.with(	//  用户名			角色
					user("xk").roles("PARTNER_FALLINTO"))
		)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
	}
}
