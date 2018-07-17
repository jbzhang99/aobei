package com.aobei.trainconsole.controller.test;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.Fallinto;
import com.aobei.trainconsole.controller.BalanceController;
import com.aobei.trainconsole.controller.CourseTeamController;
import com.aobei.trainconsole.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class BalanceControllerTest {
	
	@Autowired
	private WebApplicationContext context;

	@Autowired
	BalanceController balanceController;
	
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
	 * 结算策略添加
	 * @throws Exception
	 */
	@Test
	public void addFallinto() throws Exception{
		List<Fallinto> fallintoList=new ArrayList<>();
		Fallinto fallinto=new Fallinto();
		//fallinto.setFallinto_id(IdGenerator.generateId());
		//fallinto.setCreate_datetime(new Date());
		fallinto.setFallinto_name("单元测试底价结算添加");
		fallinto.setFallinto_type(1);
		fallinto.setFloor_price(100*100);
		fallintoList.add(fallinto);

		String url="/balance/addFallinto";
		mockMvc.perform(MockMvcRequestBuilders.get(url).param("result", JacksonUtil.object_to_json(fallintoList))
				.with(	//  用户名			角色		
						user("xk").roles("BALANCE_TACTICS_EDIT"))
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	/**
	 * 生效
	 * @throws Exception
	 */
	@Test
	public void effect() throws Exception{
		
		String url="/balance/effect/1135295719856168960/1";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("xk").roles("BALANCE_TACTICS_EDIT"))
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}	
	
	/**
	 * 失效
	 * @throws Exception
	 */
	@Test
	public void efficacy() throws Exception{
		 this.mockMvc.perform(
				MockMvcRequestBuilders.get("/balance/efficacy/1135295719856168960/1")
				.with(	//  用户名			角色		
						user("xk").roles("BALANCE_TACTICS_EDIT"))
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	/**
	 * 结算策略删除
	 * @throws Exception
	 */
	@Test
	public void del() throws Exception{
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/balance/del/1135295719856168960/1")
						.with(	//  用户名			角色
								user("xk").roles("BALANCE_TACTICS_EDIT"))
		)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}


	
	/**
	 * 待结算列表
	 * @throws Exception 
	 */
	@Test
	public void balance_waiting_list() throws Exception{
		String url="/balance/balance_waiting_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url).param("p","1").param("ps","10").param("balanceDate","")
				.with(	//  用户名			角色		
						user("xk").roles("BALANCE_ORDER_BROWSE"))
				)
				.andExpect(MockMvcResultMatchers.status().isOk())
		        .andExpect(MockMvcResultMatchers.model().attributeExists("page"))
		        .andExpect(MockMvcResultMatchers.model().attributeExists("balanceOrderList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("products"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("partnerFallintos"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("fallintos"))
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}


	/**
	 * 订单完成 挂起
	 * @throws Exception
	 */
	@Test
	public void hangUp() throws Exception{
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/balance/hangUp/1140142928997146624")
						.with(	//  用户名			角色
								user("xk").roles("BALANCE_ORDER_EDIT"))
		)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}


	/**
	 * 订单完成  结算
	 * @throws Exception
	 */
	@Test
	public void updCycle() throws Exception{
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/balance/updCycle").param("id","1140142928997146624").param("cycle","20180501")
						.param("pageNo","1")
						.with(	//  用户名			角色
								user("xk").roles("BALANCE_ORDERUP_EDIT"))
		)
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}


	/**
	 * 退款挂起
	 * @throws Exception
	 */
	@Test
	public void refundHangUp() throws Exception{
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/balance/refundHangUp/1144501123530121216")
						.with(	//  用户名			角色
								user("xk").roles("BALANCE_REFUND_EDIT"))
		)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	/**
	 * 退款   结算
	 * @throws Exception
	 */
	@Test
	public void updCycleRefund() throws Exception{
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/balance/updCycleRefund").param("id","1144501123530121216").param("cycle","20180501")
						.param("pageNo","1")
						.with(	//  用户名			角色
								user("xk").roles("BALANCE_REFUNDUP_EDIT"))
		)
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}


	/**
	 * 赔偿  挂起
	 * @throws Exception
	 */
	@Test
	public void compensationHangUp() throws Exception{
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/balance/compensationHangUp/1145409863062151168")
						.with(	//  用户名			角色
								user("xk").roles("BALANCE_COMPENSATION_EDIT"))
		)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	/**
	 * 赔偿  结算
	 * @throws Exception
	 */
	@Test
	public void updCycleCompensation() throws Exception{
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/balance/updCycleCompensation").param("id","1145409863062151168").param("cycle","20180501")
						.param("pageNo","1")
						.with(	//  用户名			角色
								user("xk").roles("BALANCE_COMPENSATIONUP_EDIT"))
		)
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}


	/**
	 *扣款   挂起
	 * @throws Exception
	 */
	@Test
	public void deductMoneyHangUp() throws Exception{
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/balance/deductMoneyHangUp/1145412677205254144")
						.with(	//  用户名			角色
								user("xk").roles("BALANCE_DEDUCT_EDIT"))
		)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	/**
	 * 扣款  结算
	 * @throws Exception
	 */
	@Test
	public void updCycleDeductMoney() throws Exception{
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/balance/updCycleDeductMoney").param("id","1145412677205254144").param("cycle","20180501")
						.param("pageNo","1")
						.with(	//  用户名			角色
								user("xk").roles("BALANCE_DEDUCTUP_EDIT"))
		)
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}



	/**
	 *罚款   挂起
	 * @throws Exception
	 */
	@Test
	public void fineMoneyHangUp() throws Exception{
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/balance/fineMoneyHangUp/1145391045711519744")
						.with(	//  用户名			角色
								user("xk").roles("BALANCE_FINE_EDIT"))
		)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	/**
	 * 罚款  结算
	 * @throws Exception
	 */
	@Test
	public void updCycleFineMoney() throws Exception{
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/balance/updCycleFineMoney").param("id","1145391045711519744").param("cycle","20180501")
						.param("pageNo","1")
						.with(	//  用户名			角色
								user("xk").roles("BALANCE_FINEUP_EDIT"))
		)
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}


}
