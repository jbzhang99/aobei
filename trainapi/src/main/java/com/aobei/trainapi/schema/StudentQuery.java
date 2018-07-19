package com.aobei.trainapi.schema;

import java.util.Date;
import java.util.List;

import com.aobei.trainapi.server.bean.MessageContent;
import com.aobei.trainapi.server.bean.StudentServiceOrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aobei.train.model.Message;
import com.aobei.train.model.Student;
import com.aobei.trainapi.server.ApiService;
import com.aobei.trainapi.server.CustomerApiService;
import com.aobei.trainapi.server.StudentApiService;
import com.aobei.trainapi.server.bean.CustomerDetail;
import com.aobei.trainapi.server.bean.StudentInfo;
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
		if (list.size() == 0) {
			return null;
		}
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
	 * @param serviceunit_id
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
	public int student_whether_have_new_messages(){
		StudentInfo studentInfo = student_info();
		return studentApiService.whetherHaveNewMessages(studentInfo);
	}

	public StudentServiceOrderStatistics studentStatisticsOrder(Long student_id){
		StudentInfo studentInfo = student_info();
		return studentApiService.studentStatisticsOrder(studentInfo.getStudent_id());
	}

}
