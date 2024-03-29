package com.aobei.trainapi.schema;

import java.util.Date;
import java.util.List;

import com.aobei.train.model.VideoContent;
import com.aobei.trainapi.server.ApiOrderService;
import com.aobei.trainapi.server.bean.*;
import custom.bean.OrderPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aobei.train.model.Message;
import com.aobei.train.model.Student;
import com.aobei.trainapi.server.ApiService;
import com.aobei.trainapi.server.CustomerApiService;
import com.aobei.trainapi.server.StudentApiService;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import custom.bean.OrderInfo;
import custom.bean.ProductInfo;
import custom.bean.TimeModel;

@Component
public class StudentQuery implements GraphQLQueryResolver {
	private static final String Student = null;

	@Autowired
	ApiService apiService;

	@Autowired
	private TokenUtil TOKEN;
	@Autowired
	CustomerApiService customerApiService;
	@Autowired
	private StudentApiService studentApiService;
	@Autowired
	private ApiOrderService apiOrderService;

	Logger logger  = LoggerFactory.getLogger(StudentQuery.class);

	/**
	 * 服务人员信息
	 * @return
	 */
	public StudentInfo student_info() {
		StudentInfo studentInfo =  apiService.studentInfoByUserId(TOKEN.getUuid());
		if (studentInfo == null) {
			Errors._40101.throwError();
		}
		return studentInfo;
	}

	/**
	 * 服务人员未完成的订单
	 * 
	 * @param page_index,count
	 * @return
	 */
	public List<OrderInfo> student_undone_orderinfo(int page_index, int count) {
		// 获取绑定的学员信息
		Student student = student_info();
		// 根据绑定学员找到合伙人已指派的服务单
		List<OrderInfo> list = this.studentApiService.selectStuUndoneOrder(student.getStudent_id(), page_index, count);
		return list;
	}

	/**
	 * 根据选择的日期进行查询指定日期服务人员完成订单
	 * @return
	 */
	public List<OrderInfo> student_complete_orderinfo(Date dateValue, int page_index, int count) {
		// 获取绑定的学员信息
		Student student = student_info();
		// 获取日期
		List<OrderInfo> list = this.studentApiService.selectStuCompleteOrder(student.getStudent_id(), dateValue,
				page_index, count);
		if (list.size() == 0) {
			return null;
		}
		return list;
	}

	/**
	 * 服务人员端 任务详情
	 * 
	 * @param
	 * @param pay_order_id
	 * @return
	 */
	public OrderInfo student_show_taskdetail(String pay_order_id) {
		// 获取绑定的学员信息
		Student student = student_info();
		OrderInfo orderInfo = this.studentApiService.selectStuShowTaskdetail(student.getStudent_id(), pay_order_id);
		return orderInfo;
	}

	/**
	 * 查看学员的消息列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<MessageContent> student_show_massageInfo(int page_index, int count) {
		// 获取绑定的学员信息
		Student student = student_info();
		List<MessageContent> messageList = studentApiService.selectStuMessage(student,page_index, count);
		return messageList;
	}

	/**
	 * 单个消息详情
	 * 
	 * @param id
	 * @return
	 */
	public Message student_message_detail(Long id) {
		// 获取绑定的学员信息
		Student student = student_info();
		Message message = this.studentApiService.selectMessageDetail(student.getStudent_id(), id);
		return message;
	}

	/**
	 * 学员订单列表
	 */
	public List<OrderInfo> student_order_list(String status, int page_index, int count) {
		Student student = student_info();
		List<OrderInfo> orderInfoList = studentApiService.student_order_list(status, student.getStudent_id(),
				page_index, count);
		return orderInfoList;
	}

	/**
	 * 商品详情
	 */
	public ProductInfo student_product_detail(Long product_id) {
		return studentApiService.productDetail(product_id);
	}

	/**
	 * 根据订单查询顾客信息
	 */
	public CustomerDetail student_customer_detail(String pay_order_id) {
		Student student = student_info();
		CustomerDetail customerDetail = studentApiService.studentCustomerDetail(student, pay_order_id);
		return customerDetail;
	}

	/**
	 * 服务人员的可预约时间
	 */
	public List<TimeModel> student_product_available_times(Long psku_id, int num) {
		Student student = student_info();
		return studentApiService.productAvailableTimes(student, psku_id, num);
	}

	/**
	 * 是否有新的消息
	 */
	public MessageState student_whether_have_new_messages(){
		StudentInfo studentInfo = student_info();
		return studentApiService.whetherHaveNewMessages(studentInfo);
	}

	/**
	 * 统计学员订单量
	 * @return
	 */
	public StudentServiceOrderStatistics student_statistics_order(){
		StudentInfo studentInfo = student_info();
		return studentApiService.studentStatisticsOrder(studentInfo);
	}


	/**
	 * 视频列表
	 */
	public List<VideoContent> select_video_list(int page_index,int count){
		return studentApiService.studentVideoList(TOKEN.getClientId(),page_index,count);
	}

	/**
	 * 服务人员计算价格
	 */
	public OrderPrice student_recalculate_price(Long psku_id, Integer num){
		ApiResponse<OrderPrice> response = new ApiResponse<>();
		if (num > 1000) {
			Errors._41040.throwError("最大可购买数量：1000");
		}
		try {
			StudentInfo studentInfo = student_info();
			response = apiOrderService.studentRecalculatePrice(studentInfo,psku_id, num);
			if (response.getErrors() != null)
				response.getErrors().throwError();
		} catch (Exception e) {
			logger.error("ERROR student_recalculate_price", e);
			Errors._41040.throwError();
		}
		return response.getT();
	}

}
