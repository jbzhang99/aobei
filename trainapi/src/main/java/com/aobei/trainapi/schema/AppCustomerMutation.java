package com.aobei.trainapi.schema;

import com.aobei.train.model.Customer;
import com.aobei.train.model.Msgtext;
import com.aobei.train.service.MsgtextService;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.ApiCommonService;
import com.aobei.trainapi.server.ApiOrderService;
import com.aobei.trainapi.server.ApiUserService;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import custom.bean.MsgTextConstant;
import custom.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppCustomerMutation implements GraphQLMutationResolver {

    @Autowired
    TokenUtil TOKEN;
    @Autowired
    ApiCommonService commonService;
    @Autowired
    MsgtextService msgtextService;
    @Autowired
    CustomerQuery query;
    @Autowired
    ApiOrderService apiOrderService;
    @Autowired
    ApiUserService apiUserService;

    public MutationResult get_code(String phone) {
        Msgtext msg;
        if (!RegexUtil.check(RegexUtil.MOBILE_PHONE_REGEX_SIMPLE, phone)) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_PHONE_FORMAT);
            Errors._41010.throwError(msg.getContent());
        }
        ApiResponse response =  response =  commonService.getCode(TOKEN.getUuid(),phone);
        if (response.getErrors() != null) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.BEYOND_SMS_MAXIMUM_LIMIT);
            response.getErrors().throwError(msg.getContent());
        }
        return response.getMutationResult();
    }
    /**
     * 用户领取优惠券
     */
    public MutationResult customer_get_coupons(Long coupon_id){
        Customer customer = query.customer_info();
        ApiResponse response =  apiOrderService.getCounpons(customer,coupon_id);
        if (response.getErrors() != null) {
            if (response.getErrors().name().equals("_42029")) {
                Msgtext msg = msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_COUNPON);
                response.getErrors().throwError(msg.getContent());
            }
            response.getErrors().throwError();
        }
        return new MutationResult();
    }

    /**
     * 使用兑换码兑换优惠券
     */
    public MutationResult customer_exchange_code_to_coupon(String exchange_code){
        Customer customer = query.customer_info();
        ApiResponse response =  apiOrderService.exchangeCodeToCoupon(customer,exchange_code);
        if (response.getErrors() != null) {
            if (response.getErrors().name().equals("_42029")) {
                Msgtext msg = msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_COUNPON);
                response.getErrors().throwError(msg.getContent());
            }else{
                Msgtext msg = msgtextService.selectByPrimaryKey(MsgTextConstant.THE_INPUT_CODE_IS_MISTAKEN);
                response.getErrors().throwError(msg.getContent());
            }
            response.getErrors().throwError();
        }
        return response.getMutationResult();
    }

    /**
     * 顾客解绑
     */
    public MutationResult customer_remove_the_bind(){
        Customer customer = query.customer_info();
        ApiResponse response = apiUserService.customerRemoveTheBing(customer);
        if (response.getErrors() != null){
            response.getErrors().throwError();
        }
        return new MutationResult();
    }
}
