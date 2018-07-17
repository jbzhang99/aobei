package com.aobei.trainconsole.controller.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import com.aobei.train.model.Coupon;
import com.aobei.train.model.CouponExample;
import com.aobei.train.model.Users;
import com.aobei.train.service.CouponService;
import com.aobei.trainconsole.controller.CouponController;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class CouponControllerTest {
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	CouponController couponController;
	
	@Autowired
	CouponService couponService;
	private MockMvc mockMvc;

	//private static final Logger logger = LoggerFactory.getLogger(CouponControllerTest.class);
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				// spring security
				.apply(springSecurity())
				// 打印输出
				.alwaysDo(print())
				// 返回检查  HTTP状态
				.alwaysExpect(status().isOk())
				.build();
	}
	
	/**
	 * 测试事务回滚
	 */
	@Test
	public void transactionRollBack() {
		Coupon coupon = new Coupon();
		coupon.setCoupon_id(121l);
		coupon.setName("测试事务回滚");
		int count = couponService.insertSelective(coupon);
		assertThat(count).isEqualTo(1);
	}
	
	@Test
	public void coupon_list() throws Exception{
		String url ="/couponmanager/coupon_list";
		this.mockMvc
			.perform(
					post(url)
					//模拟登录用户
					.with(	//  用户名			角色		
							user("rpmm").roles("COUPON_BROWSE"))
					//请求调协  请求头
					.accept(MediaType.parseMediaType("text/html;charset=UTF-8"))
					)
			.andExpect(content().contentType("text/html;charset=UTF-8"))
		.andExpect(authenticated().withUsername("rpmm"));

	}
	
	@Test
	@RequestMapping(path="/couponmanager/record_detail",produces="application/json")
	public void coupon_detail() throws Exception{
		String url ="/couponmanager/record_detail";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("COUPON_R_BROWSE")))
			.andExpect(MockMvcResultMatchers.model().attributeExists("page"))//分页对象
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(authenticated().withUsername("rpmm"));
			
	}
	
	@Test
	public void coupon_goto_add() throws Exception{
		String url ="/couponmanager/coupon_goto_add";
		mockMvc.perform(MockMvcRequestBuilders.get(url).param("p", "1")
		 .with(
							user("rpmm").roles("COUPON_EDIT")))
			.andExpect(MockMvcResultMatchers.model().attributeExists("current"))//当前页参数
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(authenticated().withUsername("rpmm"));
				
	}
	
	@Test
	public void coupon_add() throws Exception{
		String url ="/couponmanager/coupon_add";
		String condition = "{\"title\":\"指定商品给予优惠\",\"list_product\":[\"1078586809128869888\",\"1075182741706530816\"]}";
		String list = "[\"1078586809128869888\",\"1075182741706530816\"]";
		String programme = "{\"title\":\"订单减固定43元价格出售\",\"value\":4300}";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("start_datetime", "2018-3-4")
				.param("end_datetime", "2018-3-9")
				.param("condition", condition)
				.param("condition_type", "4")
				.param("condition_model_4", "指定商品给予优惠")
				.param("name", "测试优惠券")
				.param("data_hidden_ids_4", list)
				.param("plan_money", "2")
				.param("num_limit", "1")
				.param("valid", "1")
				.param("exclusive", "0")
				.param("num_total", "3")
				.param("num_able", "3")
				.param("priority", "4")
				.param("programme", programme)
				.param("programme_type", "2")
				.param("programme_model_2", "订单减固定x元价格出售")
				.param("programme_2", "43")
		 .with(
							user("rpmm").roles("COUPON_EDIT")))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(authenticated().withUsername("rpmm"));
	}
	@Test
    public void coupon_goto_edit() throws Exception {
		String url ="/couponmanager/coupon_goto_edit";
		
		mockMvc.perform(MockMvcRequestBuilders.get(url).param("p", "1").param("coupon_id", "1068653913490087936")
				.param("p", "1")
				.with(user("rpmm").roles("COUPON_EDIT")))
				.andExpect(MockMvcResultMatchers.model().attributeExists("coupon"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("current"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void coupon_edit() throws Exception{
		String url ="/couponmanager/coupon_edit";
		String condition = "{\"title\":\"指定商品给予优惠\",\"list_product\":[\"1078586809128869888\",\"1075182741706530816\"]}";
		String list = "[\"1078586809128869888\",\"1075182741706530816\"]";
		String programme = "{\"title\":\"订单减固定43元价格出售\",\"value\":4300}";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("start_datetime", "2018-3-4")
				.param("end_datetime", "2018-3-9")
				.param("condition", condition)
				.param("condition_type", "4")
				.param("condition_model_4", "指定商品给予优惠")
				.param("name", "测试优惠券----修改")
				.param("data_hidden_ids_4", list)
				.param("plan_money", "2")
				.param("num_limit", "1")
				.param("valid", "1")
				.param("exclusive", "0")
				.param("num_total", "3")
				.param("num_able", "3")
				.param("priority", "4")
				.param("programme", programme)
				.param("programme_type", "2")
				.param("programme_model_2", "订单减固定x元价格出售")
				.param("programme_2", "43")
		 .with(
							user("rpmm").roles("COUPON_EDIT")))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(content().contentType("application/json;charset=UTF-8"))//未输出json
			.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void coupon_on() throws Exception{
		
		String url = "/couponmanager/coupon_on";
		
		mockMvc.perform(MockMvcRequestBuilders.get(url).param("coupon_id", "1083174073903112192")
				.with(user("rpmm").roles("COUPON_EDIT")))
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void coupon_distritbiut() throws Exception{
		
		String url = "/couponmanager/distribute_coupon";
		
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("coupon_id", "1083174073903112192")
				.param("ids", "15313882039")
				.with(user("rpmm").roles("COUPON_EDIT")))
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void coupon_reload_pro() throws Exception{
		
		String url = "/couponmanager/coupon_reload_pro";
		
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(user("rpmm").roles("COUPON_EDIT")))
		.andExpect(authenticated().withUsername("rpmm"));
	}
	
	@Test
	public void coupon_to_products() throws Exception{
		
		String url = "/couponmanager/to_products";
		
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("coupon_id", "1083154747724685312")
				.with(user("rpmm").roles("COUPON_EDIT")))
		.andExpect(authenticated().withUsername("rpmm"));
	}
}
