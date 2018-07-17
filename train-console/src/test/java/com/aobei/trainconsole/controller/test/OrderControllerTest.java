package com.aobei.trainconsole.controller.test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional			// 自动回滚事务
@ActiveProfiles("dev") 	// 使用开发环境
public class OrderControllerTest {

	@Autowired
	protected WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	private String url;
	
	@Before
	public void setUp() {
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
	
	
	@Test
	public void goto_order_list() throws Exception {
		url = "/ordermanager/goto_order_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
				.param("uname", "安养")//模糊  服务名称
				.param("pay_order_id", "1072066863090737152668224")//精确
				.param("c_begin_datetime", "")//范围
				.param("c_end_datetime", "")//范围
				.param("cuname", "小")//模糊   顾客姓名
				.param("uphone", "000")//模糊
				.param("student_name", "")//模糊
				.param("partner_id", "1067670088150966272")//精确
				.param("statu", "4")
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("order/order_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partners"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("uname"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("pay_order_id"))
//					.andExpect(MockMvcResultMatchers.model().attributeExists("c_begin_datetime"))
//					.andExpect(MockMvcResultMatchers.model().attributeExists("c_end_datetime"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("cuname"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("uphone"))
//					.andExpect(MockMvcResultMatchers.model().attributeExists("student_name"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partner_id"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("statu"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void order_detail() throws Exception {
		url = "/ordermanager/order_detail";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("pay_order_id", "15853056668224")
				.param("page_current_page", "6")
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("order/order_detail"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("su_p"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("order"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("su"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page_current_page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("pay_order_id"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("logs"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partners"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void cancel_order() throws Exception {
		url = "/ordermanager/cancel_order";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				//.param("principal", "textc")//Authentication  未找到将Authentication封装的办法
				.param("result", "{\"serviceunit_id\":\"1072060843708047361\",\"customer_id\":\"1068594469228699648\",\"pay_order_id\":\"1072060843582218240699648\",\"product_id\":\"1067707378835415040\",\"psku_id\":\"1067708222838431744\",\"partner_id\":\"1067671915911208960\",\"station_id\":\"1070015828500602880\",\"active\":1,\"remark\":\"[{\\\"d\\\":1521433161416,\\\"remark\\\":\\\"用户【testc】为客户进行了订单变更，变更说明：由【经海路锋创家政】变更为【宋家庄健康家政中心】\\\",\\\"operator_name\\\":\\\"testc\\\",\\\"user_id\\\":\\\"3\\\"}]\",\"c_begin_datetime\":1521439200000,\"c_end_datetime\":1521446400000,\"p_reject_datetime\":1521433042000,\"pid\":\"0\",\"group_tag\":\"1072073845164785664\",\"status_active\":2}")
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void refund_order() throws Exception {
		url = "/ordermanager/refund_order";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("result", "{\"serviceunit_id\":\"1072060843708047361\",\"customer_id\":\"1068594469228699648\",\"pay_order_id\":\"1072060843582218240699648\",\"product_id\":\"1067707378835415040\",\"psku_id\":\"1067708222838431744\",\"partner_id\":\"1067671915911208960\",\"station_id\":\"1070015828500602880\",\"active\":1,\"remark\":\"[{\\\"d\\\":1521433161416,\\\"remark\\\":\\\"用户【testc】为客户进行了订单变更，变更说明：由【经海路锋创家政】变更为【宋家庄健康家政中心】\\\",\\\"operator_name\\\":\\\"testc\\\",\\\"user_id\\\":\\\"3\\\"}]\",\"c_begin_datetime\":1521439200000,\"c_end_datetime\":1521446400000,\"p_reject_datetime\":1521433042000,\"pid\":\"0\",\"group_tag\":\"1072073845164785664\",\"status_active\":2}")
				.param("fee", "1")
				.param("info", "不服务了啊")
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void update_sys_remark() throws Exception {
		url = "/ordermanager/update_sys_remark";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("sys_remark", "测试111111111")
				.param("pay_order_id", "1072060843582218240699648")
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andReturn();
	}
	
	@Test
	public void order_change() throws Exception {
		url = "/ordermanager/order_change";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("pay_order_id", "1072060843582218240699648")
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("order/order_change"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("optional_partners"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("order"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("su"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("ss"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("pay_order_id"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("stations"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partners"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void change_partner() throws Exception {
		url = "/ordermanager/change_partner";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("partner_id", "1083173046973325312")
				.param("station_id", "1083173050060333056")
				.param("change_intro", "xxxxxxxxxxxxxxx")
				.param("pay_order_id", "1072060843582218240699648")
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_EDIT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())			
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void area_partner_info() throws Exception {
		url = "/ordermanager/area_partner_info";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
				.param("province", "110000")
				.param("city", "110100")
				.param("area", "110115")
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_P_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("order/area_partners"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partners"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("province"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("city"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("area"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void scheduling_detail() throws Exception {
		url = "/ordermanager/scheduling_detail";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
				.param("partner_id", "1067670088150966272")
				.param("product_id", "")
				.param("psku_id", "")
				.param("c_begin_datetime", "")
				.param("c_end_datetime", "")
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_P_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("order/scheduling_detail"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("schdulings"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partner"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("products"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("skus"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partner_id"))
//					.andExpect(MockMvcResultMatchers.model().attributeExists("product_id"))
//					.andExpect(MockMvcResultMatchers.model().attributeExists("psku_id"))
//					.andExpect(MockMvcResultMatchers.model().attributeExists("c_begin_datetime"))
//					.andExpect(MockMvcResultMatchers.model().attributeExists("c_end_datetime"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void exportExcel() throws Exception {
		url = "/ordermanager/exportExcel";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("params", "{'uname':'','pay_order_id':'','c_begin_datetime':'2018-04-10 08:00"
						+ "','c_end_datetime':'2018-04-10 10:00','cuname':'','uphone':'','student_name':'','partner_id':'','statu':''}")
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_EXPORT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())			
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void download_task() throws Exception {
		url = "/ordermanager/download_task";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_EXPORT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("order/download_task"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("task_list"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void download() throws Exception {
		url = "/ordermanager/download";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("data_download_id", "")
				.with(	//  用户名			角色		
						user("rpmm").roles("ORDER_EXPORT")))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void goto_order_refunds() throws Exception {
		url = "/ordermanager/goto_order_refunds";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
//				.param("pay_order_id", "")
//				.param("begin_date", "")
//				.param("end_date", "")
//				.param("cuname", "")
//				.param("uphone", "")
//				.param("student_name", "")
//				.param("partner_id", "")
//				.param("status", "")
				.with(	//  用户名			角色		
						user("rpmm").roles("REFUND_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("order/refunds"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("refunds"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partners"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("userses"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void exportRefundsExcel() throws Exception {
		url = "/ordermanager/exportRefundsExcel";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("params", "{'pay_order_id':'','begin_date':'2018-04-10 08:00"
						+ "','end_date':'2018-04-10 10:00','cuname':'','uphone':'','student_name':'','partner_id':'','status':''}")
				.with(	//  用户名			角色		
						user("rpmm").roles("REFUND_EXPORT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())			
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
	
	@Test
	public void refund() throws Exception {
		url = "/ordermanager/refund";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("refund_id", "1088709354213392384")
				.param("pay_order_id", "1087371889535197184358720")
				.with(	//  用户名			角色		
						user("rpmm").roles("REFUND")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())			
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	@Test
	public void compensation_check_exist() throws Exception {
		url = "/ordermanager/compensation_check_exist";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("pay_order_id", "1524568066")
				.with(	//  用户名			角色
						user("rpmm").roles("COMPENSATION_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	@Test
	public void compensation_add() throws Exception {
		url = "/ordermanager/compensation_add";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("amount", "123")
				.param("compensation_info", "要赔偿")
				.param("partner_bear_amount", "100")
				.param("payee", "李四")
				.param("payee_card", "99999999999999999999999")
				.param("receiving_bank", "中国建设银行")
				.param("pay_order_id", "1524840557")
				.param("partner_id", "1067671915911208960")
				.with(	//  用户名			角色
						user("rpmm").roles("COMPENSATION_ADD")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	@Test
	public void compensations() throws Exception {
		url = "/ordermanager/compensations";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
//				.param("pay_order_id", "")
//				.param("q_begin_datetime", "")
//				.param("q_end_datetime", "")
//				.param("cuname", "")
//				.param("uphone", "")
//				.param("student_name", "")
//				.param("partner_id", "")
//				.param("compensation_status", "")
				.with(	//  用户名			角色
						user("rpmm").roles("COMPENSATION_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("order/compensations"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partners"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	@Test
	public void compensation_confirm() throws Exception {
		url = "/ordermanager/compensation_confirm";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("compensation_id", "1107783008586874880")
				.with(	//  用户名			角色
						user("rpmm").roles("COMPENSATION_OP")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	@Test
	public void compensation_reject() throws Exception {
		url = "/ordermanager/compensation_reject";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("compensation_id", "1107783008586874880")
				.with(	//  用户名			角色
						user("rpmm").roles("COMPENSATION_OP")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void exportCompensations() throws Exception {
		url = "/ordermanager/exportCompensations";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("params", "{'pay_order_id':'','q_begin_datetime':'"
						+ "','q_end_datetime':'','cuname':'','uphone':'','student_name':'','partner_id':'','compensation_status':''}")
				.with(	//  用户名			角色
						user("rpmm").roles("COMPENSATION_EXPORT")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	/**
	 ************* 我是宇宙无敌分割线 *******************************************************************
	 *                                        服务完成申请申请
	 **************************************************************************************************
	 */
	@Test
	public void complete_apply() throws Exception {
		url = "/ordermanager/complete_apply";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("pay_order_id", "1527603536_1")
				.param("complete_info","就是想完成！！")
				.with(	//  用户名			角色
						user("rpmm").roles("COMPLETE_APPLY_ADD")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void complete_apply_list() throws Exception {
		url = "/ordermanager/complete_apply_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
//				.param("pay_order_id", "")
//				.param("apply_status", "")
				.with(	//  用户名			角色
						user("rpmm").roles("COMPLETE_APPLY_BROWSE")))
				//返回检查  返回头检查
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("order/complete_apply"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("infos"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void complete_apply_confirm() throws Exception {
		url = "/ordermanager/complete_apply_confirm";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("complete_apply_id", "1134331457980923904")
				.with(	//  用户名			角色
						user("rpmm").roles("COMPLETE_APPLY_OP")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void complete_apply_reject() throws Exception {
		url = "/ordermanager/complete_apply_reject";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("complete_apply_id", "1134331457980923904")
				.with(	//  用户名			角色
						user("rpmm").roles("COMPLETE_APPLY_OP")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void exportCompleteApplyExcel() throws Exception {
		url = "/ordermanager/exportCompleteApplyExcel";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("params", "{'pay_order_id':'','apply_status':'','mts':''}")
				.with(	//  用户名			角色
						user("rpmm").roles("COMPLETE_APPLY_EXPORT")))
					//返回检查  返回头检查
					.andExpect(content().contentType("application/json;charset=UTF-8"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	/**
	 ************* 我是宇宙无敌分割线 *******************************************************************
	 *                                        订单扣款申请
	 **************************************************************************************************
	 */

	@Test
	public void deduct_money_check_exist() throws Exception {
		url = "/ordermanager/deduct_money_check_exist";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("pay_order_id", "1527603536_1")
				.with(	//  用户名			角色
						user("rpmm").roles("DEDUCT_MONEY_BROWSE")))
				//返回检查  返回头检查
//				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void deduct_money_add() throws Exception {
		url = "/ordermanager/deduct_money_add";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("pay_order_id", "1527603536_1")
				.param("deduct_info","就是想扣款！！")
				.param("deduct_amount","100")
				.with(	//  用户名			角色
						user("rpmm").roles("DEDUCT_MONEY_ADD")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void deduct_money_list() throws Exception {
		url = "/ordermanager/deduct_money_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
//				.param("pay_order_id", "")
//				.param("deduct_status", "")
				.with(	//  用户名			角色
						user("rpmm").roles("DEDUCT_MONEY_BROWSE")))
				//返回检查  返回头检查
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("order/deduct_money_list"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("infos"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("partners"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void deduct_money_confirm() throws Exception {
		url = "/ordermanager/deduct_money_confirm";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("deduct_money_id", "1138717011900170240")
				.with(	//  用户名			角色
						user("rpmm").roles("DEDUCT_MONEY_OP")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void deduct_money_reject() throws Exception {
		url = "/ordermanager/deduct_money_reject";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("deduct_money_id", "1138717011900170240")
				.with(	//  用户名			角色
						user("rpmm").roles("DEDUCT_MONEY_OP")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void exportDeductMoneyExcel() throws Exception {
		url = "/ordermanager/exportDeductMoneyExcel";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("params", "{'pay_order_id':'','deduct_status':'','mts':''}")
				.with(	//  用户名			角色
						user("rpmm").roles("DEDUCT_MONEY_EXPORT")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	/**
	 * ************************************************************************************
	 * 							拒单列表查询
	 * ************************************************************************************
	 */
	@Test
	public void reject_unit_list() throws Exception {
		url = "/ordermanager/reject_unit_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
//				.param("partner_id", "")
//				.param("qs_create_time", "")
//				.param("qe_create_time", "")
//				.param("qs_pay_time", "")
//				.param("qe_pay_time", "")
				.with(	//  用户名			角色
						user("rpmm").roles("REJECT_UNIT_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("order/reject_unit_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partners"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	@Test
	public void exportRejectUnitExcel() throws Exception {
		url = "/ordermanager/exportRejectUnitExcel";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("params", "{'partner_id':'','qs_create_time':'','qe_create_time':'','qs_pay_time':'','qe_pay_time':'','mts':''}")
				.with(	//  用户名			角色
						user("rpmm").roles("REJECT_UNIT_EXPORT")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	/**
	 ************* 我是宇宙无敌分割线 *******************************************************************
	 *                                        订单罚款申请
	 **************************************************************************************************
	 */

	@Test
	public void fine_money_check_exist() throws Exception {
		url = "/ordermanager/fine_money_check_exist";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("pay_order_id", "15853056668224")
				.param("partner_id", "1067670088150966272")
				.with(	//  用户名			角色
						user("rpmm").roles("FINE_MONEY_BROWSE")))
				//返回检查  返回头检查
//				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void fine_money_add() throws Exception {
		url = "/ordermanager/fine_money_add";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("pay_order_id", "15853056668224")
				.param("fine_info","就是想罚款ss！！")
				.param("fine_amount","112")
				.param("partner_id","1067670088150966272")
				.with(	//  用户名			角色
						user("rpmm").roles("FINE_MONEY_ADD")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void fine_money_list() throws Exception {
		url = "/ordermanager/fine_money_list";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
//				.param("pay_order_id", "")
//				.param("fine_status", "")
				.with(	//  用户名			角色
						user("rpmm").roles("FINE_MONEY_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("order/fine_money_list"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("infos"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partners"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}

	@Test
	public void fine_money_confirm() throws Exception {
		url = "/ordermanager/fine_money_confirm";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("fine_money_id", "1139644243522945024")
				.with(	//  用户名			角色
						user("rpmm").roles("FINE_MONEY_OP")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void fine_money_reject() throws Exception {
		url = "/ordermanager/fine_money_reject";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("fine_money_id", "1139644243522945024")
				.with(	//  用户名			角色
						user("rpmm").roles("FINE_MONEY_OP")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void exportFineMoneyExcel() throws Exception {
		url = "/ordermanager/exportFineMoneyExcel";
		mockMvc.perform(MockMvcRequestBuilders.post(url)
				.param("params", "{'pay_order_id':'','fine_status':'','mts':''}")
				.with(	//  用户名			角色
						user("rpmm").roles("FINE_MONEY_EXPORT")))
				//返回检查  返回头检查
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void scheduling_task() throws Exception {
		url = "/ordermanager/scheduling_task";
		mockMvc.perform(MockMvcRequestBuilders.get(url)
				.param("p", "1")
				.param("ps", "10")
				.param("partner_id","1040844315167580160")
//				.param("station_id", "")
//				.param("product_id", "")
//				.param("psku_id", "")
//				.param("c_begin_date", "")
//				.param("c_end_date", "")
				.with(	//  用户名			角色
						user("rpmm").roles("ORDER_P_BROWSE")))
					//返回检查  返回头检查
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andExpect(MockMvcResultMatchers.view().name("order/scheduling_task"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("timeUnisMap"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("stores"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("products"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("skus"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("station_id"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partner"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("stations"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("partner_id"))
					.andExpect(MockMvcResultMatchers.model().attributeExists("page"))
					.andExpect(status().isOk())
					.andDo(MockMvcResultHandlers.print())
					.andReturn();
	}
}
