package com.aobei.trainapi.server;

import java.util.Date;
import java.util.List;

import com.aobei.train.model.Message;
import com.aobei.train.model.Order;
import com.aobei.train.model.Student;
import com.aobei.trainapi.schema.input.StudentOrderInput;
import com.aobei.trainapi.server.bean.*;

import custom.bean.OrderInfo;
import custom.bean.ProductInfo;
import custom.bean.TimeModel;
import custom.bean.WorkStatusResult;

public interface StudentApiService {

	List<OrderInfo> selectStuUndoneOrder(Long student_id, int page_index, int count);

	List<OrderInfo> selectStuCompleteOrder(Long student_id, Date dateValue, int page_index, int count);

	ApiResponse<WorkStatusResult> student_update_work_status(Long student_id, String pay_order_id, String serviceStatus,
			String remark, String lbs_lat, String lbs_lng);

	OrderInfo selectStuShowTaskdetail(Long student_id, String pay_order_id);

	List<MessageContent> selectStuMessage(Student student, int page_index, int count);

	Message selectMessageDetail(Long student_id, Long id);

	/**
	 * 订单列表
	 * 
	 * @param status
	 * @param student_id
	 * @param page_index
	 * @param count
	 * @return
	 */
	List<OrderInfo> student_order_list(String status, Long student_id, int page_index, int count);

	/**
	 * 创建订单
	 * 
	 * @param student
	 * @param orderInput
	 * @return
	 */
	ApiResponse<Order> create_order(Student student, StudentOrderInput orderInput);

	/**
	 * 商品详情
	 * 
	 * @param product_id
	 * @return
	 */
	ProductInfo productDetail(Long product_id);

	/**
	 * 查询顾客信息
	 * 
	 * @param student
	 * @param pay_order_id
	 * @return
	 */
	CustomerDetail studentCustomerDetail(Student student, String pay_order_id);

	/**
	 * 服务人员可预约时间
	 * 
	 * @param student
	 * @param product_id
	 * @param psku_id
	 * @param customer_address_id
	 * @param num
	 * @return
	 */
	List<TimeModel> productAvailableTimes(Student student, Long psku_id, int num);
	
	/**
	 * 服务人员取消订单
	 * @param student
	 * @param pay_order_id
	 * @return
	 */
	ApiResponse cancelOrder(Student student, String pay_order_id);

	/**
	 * 是否有新的消息
	 * @param studentInfo
	 * @return
	 */
    int whetherHaveNewMessages(StudentInfo studentInfo);
}
