package com.aobei.trainapi.schema;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aobei.train.model.Msgtext;
import com.aobei.train.model.Order;
import com.aobei.train.model.Student;
import com.aobei.train.service.MsgtextService;
import com.aobei.trainapi.schema.input.StudentOrderInput;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.StudentApiService;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;

import custom.bean.MsgTextConstant;
import custom.bean.WorkStatusResult;
@Component
public class StudentMutation implements GraphQLMutationResolver{

	@Autowired
	Query query;
	@Autowired
	private StudentApiService studentApiService;
	@Autowired
    private MsgtextService msgtextService;
	
	/**
	 * 修改学员的4个状态（到达，开始，结束，离开）
	 * @return
	 */
	public WorkStatusResult student_update_work_status(String pay_order_id,String serviceStatus,String remark,String lbs_lat,String lbs_lng){
		Student student = query.my_student_bindinfo();
		ApiResponse<WorkStatusResult> apiResponse=this.studentApiService.student_update_work_status(student.getStudent_id(),pay_order_id,serviceStatus,remark,lbs_lat,lbs_lng);
		Errors errors = apiResponse.getErrors();
		if(errors!=null){
			errors.throwError();
		}
		return apiResponse.getT();
	}
	
	/**
	 * 服务人员创建订单
	 */
	public Order student_create_order(StudentOrderInput orderInput){
		Student student = query.my_student_bindinfo();
		ApiResponse<Order> response = studentApiService.create_order(student,orderInput);
		 Msgtext msg = new Msgtext();
		if (response.getErrors() != null) {
	           if(response.getErrors().name().equals("_41021")){
	                msg.setContent("库存不足");
	            }else{
	                msg = msgtextService.selectByPrimaryKey(MsgTextConstant.SERVICE_400);
	            }
	            response.getErrors().throwError(msg.getContent());
	        }
	        return response.getT(); 
	}
	
	/**
	 * 服务人员取消订单
	 */
	public MutationResult student_order_cancel(String pay_order_id){
		Student student = query.my_student_bindinfo();
		ApiResponse response =  studentApiService.cancelOrder(student,pay_order_id);
		return response.getMutationResult();
	}
	
}
