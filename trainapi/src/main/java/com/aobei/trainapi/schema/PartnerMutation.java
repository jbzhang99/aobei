package com.aobei.trainapi.schema;




import java.util.List;

import custom.util.ParamsCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aobei.train.model.Msgtext;
import com.aobei.train.model.Partner;
import com.aobei.train.service.MsgtextService;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.ApiService;
import com.aobei.trainapi.server.PartnerApiService;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;

import custom.bean.MsgTextConstant;
import org.springframework.web.multipart.MultipartFile;


@Component
public class PartnerMutation implements GraphQLMutationResolver{

	private static final boolean MutationResult = false;
	@Autowired
	private TokenUtil TOKEN;
	@Autowired
	private PartnerApiService partnerApiService;
	@Autowired
	private ApiService apiService;
	@Autowired
    StringRedisTemplate redisTemplate;
	@Autowired
    private MsgtextService msgtextService;
	@Autowired
	PartnerQuery partnerQuery;
	
	/**
	 * 绑定合伙人信息
	 * @param phone
	 * @return
	 */
	public MutationResult partner_binduser(String phone,String linkman){
		ApiResponse response = new ApiResponse();
		Msgtext msg = new Msgtext();
		if(StringUtils.isEmpty(phone)){
			 response.setErrors(Errors._42015);
			 msg =  msgtextService.selectByPrimaryKey(MsgTextConstant.PARTNER_BINDING_FAILED);
	         response.getErrors().throwError(msg.getContent());
		}
		if(StringUtils.isEmpty(linkman)){
			 response.setErrors(Errors._42016);
			 msg =  msgtextService.selectByPrimaryKey(MsgTextConstant.PARTNER_BINDING_FAILED);
	         response.getErrors().throwError(msg.getContent());
		}
		Partner partner = partnerApiService.partnerByPhoneAndLinkman(phone, linkman);
		if(partner == null){
			 response.setErrors(Errors._42001);
			 msg =  msgtextService.selectByPrimaryKey(MsgTextConstant.PARTNER_BINDING_FAILED);
	         response.getErrors().throwError(msg.getContent());
		}
		if(partner.getUser_id() != null && partner.getUser_id() != 0){
			 response.setErrors(Errors._42004);
			 msg =  msgtextService.selectByPrimaryKey(MsgTextConstant.PARTNER_BINDING_FAILED);
	         response.getErrors().throwError(msg.getContent());
		}
		Long partner_id = partner.getPartner_id();
		//去掉角色互斥条件
		//apiService.teacherInfoByUserId(TOKEN.getUuid()) != null || apiService.studentInfoByUserId(TOKEN.getUuid()) != null
		if(partnerApiService.partnerInfoByUserId(TOKEN.getUuid()) != null){
			 response.setErrors(Errors._42005);
			 msg =  msgtextService.selectByPrimaryKey(MsgTextConstant.PARTNER_BINDING_FAILED);
	         response.getErrors().throwError(msg.getContent());
		}
		int count = partnerApiService.bindPartner(TOKEN.getUuid(), partner_id);
		if(count == 1){
			 return new MutationResult();
		}
		response.setErrors(Errors._42006);
		msg =  msgtextService.selectByPrimaryKey(MsgTextConstant.PARTNER_BINDING_FAILED);
        response.getErrors().throwError(msg.getContent());
		return response.getMutationResult();
	}
	
	/**
	 * 拒绝接单
	 */
	public MutationResult partner_refused_order(String pay_order_id,String orderStr){
		Partner partner = partnerQuery.partner_bindinfo();
		if (!ParamsCheck.checkStrAndLength(orderStr,200)){
			Errors._41040.throwError("拒单理由过长");
		}
		ApiResponse response = partnerApiService.partnerRefusedOrder(partner,pay_order_id,orderStr);
		Msgtext msg = new Msgtext();
			if (response.getErrors() != null) {
	            if (response.getErrors().name().equals("_42008")) {
	                msg =  msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_ORDER);
	                response.getErrors().throwError(msg.getContent());
	            }
	            response.getErrors().throwError();
	        }
        return response.getMutationResult();
	}
	
	/**
	 * 待服务状态下修改订单
	 */
	public MutationResult partner_alter_order(String pay_order_id,List<Long> student_ids){
		Partner partner = partnerQuery.partner_bindinfo();
		ApiResponse response = partnerApiService.partnerAlterOrder(partner,pay_order_id,student_ids);
		if (response.getErrors() != null)
            response.getErrors().throwError();
        return response.getMutationResult();
	} 
	
	/**
	 * 合伙人接单
	 */
	public MutationResult partner_confirm_order(String pay_order_id,List<Long> student_id){
		Partner partner = partnerQuery.partner_bindinfo();
		ApiResponse response = partnerApiService.partnerConfirmOrder(partner,pay_order_id,student_id);
		if (response.getErrors() != null)
            response.getErrors().throwError();
        return response.getMutationResult();
	}
	
	/**
	 * 学员停止接单
	 */
	public MutationResult partner_stop_order(Long student_id,String date,String start, String end,Boolean statu){
		Msgtext msg = new Msgtext();
		Partner partner = partnerQuery.partner_bindinfo();
		ApiResponse response = partnerApiService.partnerStopOrder(partner,student_id,date,start,end,statu);
		if (response.getErrors() != null) {
            if (response.getErrors().name().equals("_42019")) {
                msg =  msgtextService.selectByPrimaryKey(MsgTextConstant.WORKER_HAS_TASK);
                response.getErrors().throwError(msg.getContent());
            }
            response.getErrors().throwError();
        }
        return response.getMutationResult();
    }
	
	/**
	 * 修改服务站
	 */
	public MutationResult partner_update_station(Long station_id, Long student_id) {
		Partner partner = partnerQuery.partner_bindinfo();
		ApiResponse response = partnerApiService.partnerUpdateStation(partner,station_id, student_id);
		Msgtext msg = new Msgtext();
		if (response.getErrors() != null) {
			if (response.getErrors().name().equals("_42019")) {
				msg = msgtextService.selectByPrimaryKey(MsgTextConstant.WORKER_HAS_TASK);
				response.getErrors().throwError(msg.getContent());
			}
			response.getErrors().throwError();
		}
		return response.getMutationResult();
	}
	
	/**
	 * 抢单
	 */
	public MutationResult partner_order_robbing(String pay_order_id,List<Long> student_ids){
		Partner partner = partnerQuery.partner_bindinfo();
		ApiResponse response = partnerApiService.partnerOrderRobbing(partner,pay_order_id,student_ids);
		Msgtext msg = new Msgtext();
		if(response.getErrors() != null){
			if (response.getErrors().name().equals("_42026")) {
				msg = msgtextService.selectByPrimaryKey(MsgTextConstant.HAS_BEEN_ORDER_SEIZED);
				response.getErrors().throwError(msg.getContent());
			}
			response.getErrors().throwError();
		}
		return response.getMutationResult();
	}

	/**
	 * 合伙人上传头像
	 */
	public MutationResult partner_upload_logimg(String logoImg){
		Partner partner = partnerQuery.partner_bindinfo();
		return partnerApiService.uploadTheLogimg(partner,logoImg);
	}

	/**
	 * 消息状态修改
	 */
	public MutationResult partner_message_status_alter(Long message_id){
		Partner partner = partnerQuery.partner_bindinfo();
		return partnerApiService.messageStatusAlter(partner,message_id);
	}

	/**
	 * 学员停单(多个日期)
	 */
	public MutationResult partner_stop_order_dates(Long student_id,List<String> dates,String start, String end,Integer statu){
		Partner partner = partnerQuery.partner_bindinfo();
		ApiResponse response = partnerApiService.partnerStopOrderDates(partner,student_id,dates,start,end,statu);
		Msgtext text = new Msgtext();
		if (response.getErrors() != null) {
			if (response.getErrors().name().equals("_42019")) {
				text = msgtextService.selectByPrimaryKey(MsgTextConstant.WORKER_HAS_TASK);
				response.getErrors().throwError(text.getContent());
			}
			response.getErrors().throwError();
		}
		return response.getMutationResult();
	}

}
