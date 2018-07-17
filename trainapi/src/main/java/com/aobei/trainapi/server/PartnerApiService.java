package com.aobei.trainapi.server;

import java.util.Date;
import java.util.List;

import com.aobei.train.model.Message;
import com.aobei.train.model.Partner;
import com.aobei.train.model.Station;
import com.aobei.train.model.Student;
import com.aobei.train.model.Teacher;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.bean.*;

import custom.bean.OrderInfo;
import custom.bean.OutOfServiceStatistics;
import org.springframework.web.multipart.MultipartFile;

/**
 * 合伙人
 * 
 * @author 15010
 *
 */
public interface PartnerApiService {

	/**
	 * 获取合伙人信息
	 * 
	 * @param user_id
	 * @return
	 */
	Partner partnerInfoByUserId(Long user_id);

	/**
	 * 绑定合伙人
	 * 
	 * @param phone
	 * @param id_num
	 * @return
	 */
	Partner partnerByPhoneAndLinkman(String phone, String linkman);

	/**
	 * 绑定合伙人信息
	 * 
	 * @param user_id
	 * @param student_id
	 * @return
	 */
	int bindPartner(Long user_id, Long partner_id);

	/**
	 * 用户添加角色
	 * 
	 * @param user_id
	 * @param roleName
	 * @return
	 */
	int userAddRole(Long user_id, String roleName);

	/**
	 * 获取员工信息
	 */
	public List<EmployeeManagement> my_employeeManagement(Partner partner, int page_index, int count);

	/**
	 * 员工详情
	 * 
	 * @param student_id
	 * @return
	 */
	EmployeeManagement partnerEmployeeDetail(Partner partner,Long student_id);

	/**
	 * 我的任务排期
	 * 
	 * @param partner_id
	 * @param dataValue
	 * @return
	 */
	List<StudentServiceunit> my_mission_scheduled_information(Long partner_id, Date dataValue, int page_index,
			int count);

	/**
	 * 本月累计订单
	 */
	AccumulatedOrdersMonth Accumulated_orders_month(Long partner_id);

	/**
	 * 店铺信息
	 * 
	 * @param partner_id
	 * @return
	 */
	List<Station> Store_information(Long partner_id);

	/**
	 * 获取订单列表
	 * 
	 * @return
	 */
	List<OrderInfo> partner_order_list(String status, Long partner_id, int page_index, int count);

	/**
	 * 获取订单详情
	 */
	OrderInfo partner_order_detail(Partner partner,String pay_order_id);

	/**
	 * 查看可进行指派的服务人员信息
	 * 
	 * @param pay_order_id
	 */
	List<StudentType> partnerFindAvailableStudent(Partner partner,String pay_order_id);

	/**
	 * 拒绝接单
	 * 
	 * @param pay_order_id
	 * @return
	 */
	ApiResponse partnerRefusedOrder(Partner partner,String pay_order_id, String orderStr);

	/**
	 * 待服务状态下修改
	 * 
	 * @param pay_order_id
	 * @return
	 */
	ApiResponse partnerAlterOrder(Partner partner,String pay_order_id, List<Long> student_ids);

	/**
	 * 合伙人接单
	 * 
	 * @param pay_order_id
	 * @param student_id
	 * @return
	 */
	ApiResponse partnerConfirmOrder(Partner partner,String pay_order_id, List<Long> student_id);

	/**
	 * 查询消息列表
	 * @param partner
	 * @param page_index
	 * @param count
	 * @return
	 */
	List<MessageContent> partnerMessageInfo(Partner partner, int page_index, int count);

	/**
	 * 学员停止接单
	 * 
	 * @param student_id
	 * @param start
	 * @param end
	 */
	ApiResponse partnerStopOrder(Partner partner,Long student_id, String date, String start, String end, Boolean statu);

	/**
	 * 消息详情
	 * 
	 * @param id
	 * @return
	 */
	Message partnerMessageDetail(Long id);

	/**
	 * 修改服务站
	 * 
	 * @param station_id
	 * @param student_id
	 * @return
	 */
	ApiResponse partnerUpdateStation(Partner partner,Long station_id, Long student_id);

	/**
	 * 停单日期当前日期7天
	 */
	List<StudentStopDate> partnerStopDate(Long student_id,String clientId);
	/**
	 * 查询可以抢单的服务单列表
	 * @param partner_id
	 * @return
	 */
	List<OrderInfo> partnerRobberies(Long partner_id,int page_index, int count);
	
	/**
	 * 抢单
	 * @param pay_order_id
	 * @return
	 */
	ApiResponse partnerOrderRobbing(Partner partner,String pay_order_id,List<Long> student_ids);

	/**
	 * 本月订单统计接口
	 * @param partner
	 * @return
	 */
	MonthOrderStatistics monthOrderStatistics(Partner partner);

	/**
	 * 上传头像
	 * @param partner,logoImg
	 * @return
	 */
    MutationResult uploadTheLogimg(Partner partner,String logoImg);


	ApiResponse<OutOfServiceStatistics> outOfServiceStatistics(Partner partner,String key,Long student_id);

	/**
	 * 消息状态修改
	 * @param partner
	 * @param message_id
	 * @return
	 */
	MutationResult messageStatusAlter(Partner partner, Long message_id);

	/**
	 * 抢单详情
	 * @param partner
	 * @param pay_order_id
	 * @return
	 */
    OrderInfo partnerRobbingDetail(Partner partner, String pay_order_id);

	/**
	 * 学员停单(多个日期)
	 * @param student_id
	 * @param dates
	 * @param statu
	 * @return
	 */
    ApiResponse partnerStopOrderDates(Partner partner,Long student_id, List<String> dates, String start, String end, Integer statu);
}
