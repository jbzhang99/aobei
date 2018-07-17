package com.aobei.trainapi.schema;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.Customer;
import com.aobei.train.model.CustomerAddress;
import com.aobei.train.model.Msgtext;
import com.aobei.train.model.Order;
import com.aobei.train.service.MsgtextService;
import com.aobei.trainapi.schema.input.CustomerAddressInput;
import com.aobei.trainapi.schema.input.OrderInput;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.ApiOrderService;
import com.aobei.trainapi.server.ApiUserService;
import com.aobei.trainapi.server.CustomerApiService;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import custom.bean.MsgTextConstant;
import custom.util.ParamsCheck;
import custom.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMutation implements GraphQLMutationResolver {

    @Autowired
    private TokenUtil TOKEN;
    @Autowired
    private CustomerApiService customerApiService;
    @Autowired
    private CustomerQuery query;
    @Autowired
    private MsgtextService msgtextService;
    @Autowired
    private ApiOrderService apiOrderService;
    @Autowired
    private ApiUserService apiUserService;
    Logger logger = LoggerFactory.getLogger(CustomerMutation.class);

    /**
     * 向顾客指定的号码发送验证码短息，
     * 注册用验证码，此时用户还没有绑定到customer上，
     *
     * @param phone 电话号码
     * @return
     */
    public MutationResult customer_n_send_verification_code(String phone) {

        Msgtext msg = new Msgtext();
        if (!RegexUtil.check(RegexUtil.MOBILE_PHONE_REGEX_SIMPLE, phone)) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_PHONE_FORMAT);
            Errors._41010.throwError(msg.getContent());

        }
        ApiResponse response = customerApiService.sendVerificationCode(TOKEN.getUuid(), phone);
        if (response.getErrors() != null) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.BEYOND_SMS_MAXIMUM_LIMIT);
            response.getErrors().throwError(msg.getContent());
        }

        return response.getMutationResult();
    }

    /***
     * 向顾客指定号码发送验证码，
     * 找回密码，更换手机号码绑定，此时用户已经与customer绑定
     * @param phone 电话号码
     * @return
     */
    public MutationResult customer_o_send_verification_code(String phone) {
        Customer customer = query.customer_info();
        Msgtext msg = new Msgtext();
        if (!RegexUtil.check(RegexUtil.MOBILE_PHONE_REGEX_SIMPLE, phone)) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_PHONE_FORMAT);
            Errors._41010.throwError(msg.getContent());
        }

        if (!customer.getPhone().equals(phone)) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.PHONE_TAKEN);
            Errors._41237.throwError(msg.getContent());
        }
        ApiResponse response = customerApiService.sendVerificationCode(TOKEN.getUuid(), phone);
        if (response.getErrors() != null) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.BEYOND_SMS_MAXIMUM_LIMIT);
            response.getErrors().throwError(msg.getContent());
        }
        return response.getMutationResult();
    }

    /**
     * 验证手机号验证码是否匹配。
     * @param phone
     * @param code
     * @return
     */
    public boolean customer_checking_code(String phone,String code){
        Customer customer = query.customer_info();
        return customerApiService.checkVerificationCode(customer.getUser_id(),code,phone);
    }


    /**
     * 通过验证码和手机号码的验证，进行用户的绑定
     *
     * @param code  验证码
     * @param phone 手机号码
     * @return
     */
    public MutationResult customer_bind_user(String code, String phone) {
        Msgtext msg = new Msgtext();
        if (!RegexUtil.check(RegexUtil.MOBILE_PHONE_REGEX_SIMPLE, phone)) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_PHONE_FORMAT);
            Errors._41010.throwError(msg.getContent());
        }
        if (!customerApiService.checkVerificationCode(TOKEN.getUuid(), code, phone)) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_CODE);
            Errors._41011.throwError(msg.getContent());
        }

        Customer customer = customerApiService.customerInfo(TOKEN.getUuid());
        if (customer != null) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.PHONE_TAKEN);
            Errors._41022.throwError(msg.getContent());
        }
        ApiResponse response = apiUserService.bindUser(TOKEN.getUuid(), phone,TOKEN.getChannel());

        if (response.getErrors() != null) {
            response.getErrors().throwError();
        }
        return response.getMutationResult();
    }

    /**
     * 变更手机号码。
     *
     * @param phone 电话号码
     * @param code  验证码
     * @return
     */
    public MutationResult customer_change_phone(String phone, String code) {
        Customer customer = query.customer_info();
        Msgtext msg = new Msgtext();
        if (!RegexUtil.check(RegexUtil.MOBILE_PHONE_REGEX_SIMPLE, phone)) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_PHONE_FORMAT);
            Errors._41010.throwError(msg.getContent());
        }
        if (!customerApiService.checkVerificationCode(TOKEN.getUuid(), code, phone)) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_CODE);
            Errors._41011.throwError(msg.getContent());
        }
        if (StringUtils.equals(customer.getPhone(), phone)) {
            return new MutationResult();
        }
        ApiResponse response = customerApiService.changePhone(customer, phone);
        if (response.getErrors() != null) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.PHONE_TAKEN);
            response.getErrors().throwError(msg.getContent());
        }

        return response.getMutationResult();
    }

    /**
     * 用户设置密码
     *
     * @param password 密码
     * @param repeat   重复密码
     * @param phone    电话号码
     * @param code     验证码
     * @return MutationResult
     */
    public MutationResult customer_set_password(String password, String repeat, String phone, String code) {
        query.customer_info();
        Msgtext msg = new Msgtext();
        if (!customerApiService.checkVerificationCode(TOKEN.getUuid(), code, phone)) {
            msg = msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_CODE);
            Errors._41011.throwError(msg.getContent());
        }
        if (!(StringUtils.equals(password, repeat) && StringUtils.isNotEmpty(password))) {
            Errors._41238.throwError();
        }
        return customerApiService.setPassword(TOKEN.getUuid(), password, repeat);
    }

    /**
     * 通过客户端传送回来的信息进行顾客地址的存储，客户端通过地图定位的方式获得到具体地址信息。
     *
     * @param input 用户地址输入项
     * @return
     */
    public CustomerAddress customer_address_add(CustomerAddressInput input) {

        if(StringUtils.equals("undefined",input.getProvince())||
                StringUtils.equals("undefined",input.getCity())||
                StringUtils.equals("undefined",input.getDistrict())){
            Errors._41040.throwError("添加地址失败，请稍后重试！");

        }
        if(!ParamsCheck.checkStrAndLength(input.getAddress(),200)
                ||!ParamsCheck.checkStrAndLength(input.getSub_address(),200)){
            Errors._41040.throwError("地址超长");
        }
        if(input.getUsername()!=null && !ParamsCheck.checkStrAndLength(input.getUsername(),30)){
            Errors._41040.throwError("姓名超长");
        }
        Customer customer = query.customer_info();
        input.setCustomer_address_id(IdGenerator.generateId());
        CustomerAddress address = customerApiService.addressAdd(customer, input);
        if(address==null){
            Errors._41040.throwError("添加地址失败，请稍后重试！");
        }
        return address;
    }

    /**
     * 修改顾客填写的地址信息
     *
     * @param input 用户地址输入项
     * @return
     */
    public CustomerAddress customer_address_update(CustomerAddressInput input) {
        if(!ParamsCheck.checkStrAndLength(input.getAddress(),200)
                ||!ParamsCheck.checkStrAndLength(input.getSub_address(),200)){
            Errors._41040.throwError("地址超长");
        }
        if(input.getUsername()!=null && !ParamsCheck.checkStrAndLength(input.getUsername(),30)){
            Errors._41040.throwError("姓名超长");
        }
        Customer customer = query.customer_info();
        return customerApiService.addressUpdate(customer, input);
    }

    public MutationResult customer_address_delete(Long customer_address_id) {
        Customer customer = query.customer_info();
        return customerApiService.addressDelete(customer,customer_address_id);
    }

    /**
     * 设置默认地址
     *
     * @param customer_address_id 用户地址ID
     * @return
     */
    public MutationResult customer_set_default_address(Long customer_address_id) {
        Customer customer = query.customer_info();
        return customerApiService.setDefaultAddress(customer.getCustomer_id(), customer_address_id);
    }


    /**
     * 创建订单
     *
     * @param orderInput 创建订单需要的输入项
     * @return
     */
    public Order customer_create_order(OrderInput orderInput) {

        Customer customer = query.customer_info();
        Msgtext msg = new Msgtext();
        ApiResponse<Order> response = new ApiResponse<>();
        if (orderInput.getNum() > 1000) {
            Errors._41040.throwError("最大可购买数量：1000");
        }
        try {
            String channelId = TOKEN.getChannel();
            if (StringUtils.isEmpty(channelId)) {
                channelId = "0";
            }
            response = customerApiService.createOrder(customer, orderInput, channelId, TOKEN.getClientId());
        } catch (Exception e) {
            logger.error("ERROR Customer create order fail ", e);
            Errors._41040.throwError();
        }

        if (response.getErrors() != null) {
            if (response.getErrors().name().equals("_41021")) {
                msg.setContent("库存不足");
            } else if (response.getErrors().name().equals("_41006")) {
                msg.setContent("该产品已下架");
            } else {
                msg = msgtextService.selectByPrimaryKey(MsgTextConstant.SERVICE_400);
            }

            response.getErrors().throwError(msg.getContent());
        }

        return response.getT();
    }


    /**
     * 用户取消订单，已支付情况下取消
     *
     * @param pay_order_id  订单号
     * @param remark_cancel 取消备注
     * @return
     */
    public MutationResult customer_order_cancel(String pay_order_id, String remark_cancel) {
        Customer customer = query.customer_info();
        Msgtext msg = new Msgtext();
        ApiResponse response = customerApiService.cancelOrder(customer, pay_order_id, remark_cancel);
        if (response.getErrors() != null) {
            if (response.getErrors().name().equals("_41020")) {
                msg = msgtextService.selectByPrimaryKey(MsgTextConstant.CANCLE_ORDER_FORBID);
            } else {
                msg = msgtextService.selectByPrimaryKey(MsgTextConstant.SERVICE_400);
            }
            response.getErrors().throwError(msg.getContent());
        }
        return response.getMutationResult();
    }

    /**
     * 0元支付接口
     */
    public Order customer_pay_free_charge(String pay_order_id){
        Customer customer = query.customer_info();
        ApiResponse<Order> response = customerApiService.wxPayFree(customer, pay_order_id);
        if (response.getErrors() != null)
            response.getErrors().throwError();
        return response.getT();
    }

    /**
     * 顾客评价订单和服务
     *
     * @param pay_order_id 订单号
     * @param score        评价得分
     * @param comment      评价内容
     * @return
     */
    public MutationResult customer_service_evaluate(String pay_order_id, int score, String comment) {
        if(!ParamsCheck.checkStrAndLength(comment,600)){
            Errors._41040.throwError("评论超长！");
        }
        Customer customer = query.customer_info();
        ApiResponse response = customerApiService.serviceEvaluate(customer, pay_order_id, score, comment);
        if (response.getErrors() != null) {
            if (response.getErrors().name().equals("_41236")) {
                Msgtext msg = msgtextService.selectByPrimaryKey(MsgTextConstant.DUPLICATE_EVALUATE);
                response.getErrors().throwError(msg.getContent());
            }
            response.getErrors().throwError();
        }
        return response.getMutationResult();
    }

    /**
     * 服务人员离开后,顾客确定完成
     */
    public MutationResult customer_service_complete(String pay_order_id) {
        Customer customer = query.customer_info();
        ApiResponse response = customerApiService.serviceComplete(customer, pay_order_id);
        if (response.getErrors() != null) {
            if (response.getErrors().name().equals("_41041")) {
                Msgtext msg = msgtextService.selectByPrimaryKey(MsgTextConstant.INVALID_COMPLETE_ORDER);
                response.getErrors().throwError(msg.getContent());
            }
            response.getErrors().throwError();
        }
        return response.getMutationResult();
    }
    

    
    /**
     * 修改顾客信息
     */
    public MutationResult customer_info_modification(String name,String logo_img,Integer gender,String idcard){
        if(name!=null && !ParamsCheck.checkStrAndLength(name,30)){
            Errors._41040.throwError("姓名超长");
        }
    	Customer customer = query.customer_info();
    	return  customerApiService.customerInfoUpdate(customer,name,logo_img,gender,idcard);
    }

}
