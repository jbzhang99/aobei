package com.aobei.trainapi.schema;

import com.aobei.train.model.Message;
import com.aobei.train.model.Partner;
import com.aobei.train.model.Station;
import com.aobei.train.service.RedisService;
import com.aobei.trainapi.server.PartnerApiService;
import com.aobei.trainapi.server.bean.*;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import custom.bean.OrderInfo;
import custom.bean.OutOfServiceStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PartnerQuery implements GraphQLQueryResolver {

	@Autowired
	private TokenUtil TOKEN;
	@Autowired
	private PartnerApiService partnerApiService;
	@Autowired
	private RedisService redisService;

	Logger  logger  = LoggerFactory.getLogger(PartnerQuery.class);
	/**
	 * 绑定的合伙人信息
	 * @return
	 */   
	public Partner partner_bindinfo(){
		Partner partner = partnerApiService.partnerInfoByUserId(TOKEN.getUuid());
		if(partner == null){
			Errors._42001.throwError();
		}
		return partner;
	}
	
	/**
	 * 获取员工信息
	 */
	public List<EmployeeManagement> partner_employee_management(int page_index,int count){
		Partner partner = partner_bindinfo();
		List<EmployeeManagement> list = partnerApiService.my_employeeManagement(partner,page_index,count);
		return list;
	}
	 
	/**
	 * 获取员工信息详情
	 */
	public EmployeeManagement partner_employee_detail(Long student_id){
		Partner partner = partner_bindinfo();
		EmployeeManagement employee = partnerApiService.partnerEmployeeDetail(partner,student_id);
		return employee;
	}
	
	/**
	 * 我的任务排期
	 */
	public List<StudentServiceunit> partner_mission_is_scheduled_info(Date dateValue,int page_index,int count){
		Partner partner = partner_bindinfo();
		return partnerApiService.my_mission_scheduled_information(partner.getPartner_id(),dateValue,page_index,count);
	}
	
	/**
	 * 本月累计订单
	 */
	public AccumulatedOrdersMonth partner_accumulated_orders_month(){
		Partner partner = partner_bindinfo();
		AccumulatedOrdersMonth accumulated = partnerApiService.Accumulated_orders_month(partner.getPartner_id());
		return accumulated;
	}
	
	/**
	 * 店铺信息
	 */
	public List<Station> partner_store_information(){
		Partner partner = partner_bindinfo();
		return  partnerApiService.Store_information(partner.getPartner_id());
	}
	

    /**
     * 获取合伙人订单列表
     * @param page_index
     * @param count
     * @return
     */
    public List<OrderInfo> partner_order_list(String status,int page_index, int count){
        Partner partner = partner_bindinfo();
		List<OrderInfo> list  = new ArrayList<>();
		try {
			list = partnerApiService.partner_order_list(status, partner.getPartner_id(), page_index, count);
		}catch (Exception e){
			logger.error("ERROR partner_order_list ",e);

		}
        return list;
    }
	
	/**
	 * 订单详情
	 */
	public OrderInfo partner_order_detail(String pay_order_id){
		Partner partner = partner_bindinfo();
		if(StringUtils.isEmpty(pay_order_id)){
			 Errors._42008.throwError();
		}
        return partnerApiService.partner_order_detail(partner,pay_order_id);
    }
	
	/**
	 * 查看可进行指派的服务人员信息
	 */
	public List<StudentType> partner_find_available_student(String pay_order_id){
		Partner partner = partner_bindinfo();
		List<StudentType> studentList = partnerApiService.partnerFindAvailableStudent(partner,pay_order_id);
		if(studentList == null || studentList.size() < 0){
			Errors._42007.throwError();
		}
		return studentList;
	}
	
	/**
	 * 消息列表
	 */
	public List<MessageContent> partner_message_info(int page_index,int count){
		Partner partner = partner_bindinfo();
		List<MessageContent> messages = partnerApiService.partnerMessageInfo(partner,page_index, count);
		if(messages == null){
			Errors._42013.throwError();
		}
		return messages;
	}
	
	/**
	 * 消息详情
	 */
	public Message partner_message_detail(Long id){
		Partner partner = partner_bindinfo();
		Message message = partnerApiService.partnerMessageDetail(id);
		return message;
	}
	
	/**
	 * 停单日期当前日期7天
	 */
	public List<StudentStopDate> partner_stop_date(Long student_id){
		Partner partner = partner_bindinfo();
		List<StudentStopDate> stopDates = partnerApiService.partnerStopDate(student_id,TOKEN.getClientId());
		return stopDates;
	}
	
	/**
	 * 查询可抢单订单列表
	 */
	public List<OrderInfo>  partner_robbing_list(int page_index, int count){
		Partner partner = partner_bindinfo();
		List<OrderInfo> orderlist =  partnerApiService.partnerRobberies(partner.getPartner_id(),page_index,count);
		return orderlist;
	}
	
	/**
	 * 获取当前时间
	 */
	public PresentTime query_current_time(){
		PresentTime time = new PresentTime();
		long timeMillis = System.currentTimeMillis();
		time.setMillisecond(timeMillis);
		Date date = new Date();
		String timestamp = String.valueOf(date.getTime()/1000);
		int second = Integer.parseInt(timestamp);
		time.setSecond(second);
		return time;
	}

	/**
	 * 本月订单统计
	 */
	public MonthOrderStatistics partner_month_order_statistics(){
		Partner partner = partner_bindinfo();
		partner.setLogo_img("");
		return partnerApiService.monthOrderStatistics(partner);
	}

	/**
	 * 获取指定月份，某个服务人员的出勤统计
	 * @param student_id
	 * @param yearAndMonth
	 * @return
	 */
	public OutOfServiceStatistics partnerGetStudentOutOfServices(Long  student_id,String  yearAndMonth){
		Partner partner = partner_bindinfo();
        ApiResponse<OutOfServiceStatistics> response = partnerApiService.outOfServiceStatistics(partner,yearAndMonth,student_id);
        if (response.getErrors()!=null){
        	response.getErrors().throwError();
		}
		return  response.getT();
	}

	/**
	 * 抢单详情
	 */
	public OrderInfo partner_robbing_detail(String pay_order_id){
		Partner partner = partner_bindinfo();
		return partnerApiService.partnerRobbingDetail(partner,pay_order_id);
	}
}
