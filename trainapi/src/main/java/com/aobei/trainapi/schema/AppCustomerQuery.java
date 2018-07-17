package com.aobei.trainapi.schema;

import com.alipay.api.AlipayApiException;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.aobei.train.model.*;
import com.aobei.train.service.MsgtextService;
import com.aobei.trainapi.schema.type.PageResult;
import com.aobei.trainapi.server.*;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.server.bean.AppVersion;
import com.aobei.trainapi.server.bean.MessageContent;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import custom.bean.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weixin.popular.bean.paymch.MchPayApp;

import java.util.List;

@Component
public class AppCustomerQuery implements GraphQLQueryResolver {
    @Autowired
    TokenUtil TOKEN;
    @Autowired
    ApiCommonService apiCommonService;
    @Autowired
    ApiOrderService apiOrderService;
    @Autowired
    ApiProductService apiProductService;
    @Autowired
    ApiUserService apiUserService;
    @Autowired
    CustomerApiService customerApiService;
    @Autowired
    CustomerQuery query;
    @Autowired
    ApiService apiService;
    @Autowired
    MsgtextService msgtextService;
    Logger logger = LoggerFactory.getLogger(AppCustomerQuery.class);


    /**
     * 开始进行接口定义，定义接口名称，参数，返回值等等
     * 第一部分   首页 中轮播图
     */

    /**
     * 首页轮banner图。返回list
     *
     * @param position
     * @return
     */
    public List<CmsBanner> customer_banners(String position) {


        return apiCommonService.getBanners(TOKEN.getClientId(), position);
    }

    /**
     * 获取首页的产品列表
     *
     * @param page_index 页码
     * @param count      一页显示的条数
     *                   如果存在一些配置策略的话。分页查询可能会时效
     * @return
     */
    public List<ProductWithCoupon> customer_homePageProducts(Integer page_index, Integer count, String city_id) {
        Long customer_id=null;
        if(TOKEN.getUuid()!=null) {
            Customer customer = customerApiService.customerInfo(TOKEN.getUuid());
            if(customer!=null){
                customer_id=customer.getCustomer_id();
            }
        }
        return apiProductService.homePageProducts(page_index, count, city_id,customer_id);
    }

    /**
     * 获取推荐位的产品列表
     *
     * @return 推荐位
     */
    public List<ProductWithCoupon> customer_recommendProducts(String city_id) {
        Long customer_id=null;
        if(TOKEN.getUuid()!=null) {
            Customer customer = customerApiService.customerInfo(TOKEN.getUuid());
            if(customer!=null){
                customer_id=customer.getCustomer_id();
            }
        }
        return apiProductService.recommendProducts(city_id,customer_id);
    }


    public EvaluateBase customer_productEvaluateBase(Long product_id, int num) {
        return apiProductService.productEvaluateBase(product_id, num);
    }

    public List<Evaluate> customer_productEvaluates(Long product_id, int page_index, int count) {
        return apiProductService.productEvaluatesList(product_id, page_index, count);
    }

    public MchPayApp customer_app_wx_prepay(String pay_order_id,String appid) {
        Customer customer = customerApiService.customerInfo(TOKEN.getUuid());

        if(StringUtils.isEmpty(appid)){
            appid = "wx_app_custom";
        }


        ApiResponse<MchPayApp> response = apiOrderService.appWxPrePay(customer, pay_order_id, appid);
        if (response.getErrors() != null)
            response.getErrors().throwError();
        return response.getT();
    }

    public String customer_app_ali_prepay(String pay_order_id, String appid) {
        if (StringUtils.isEmpty(appid)) {
            return null;
        }
        Customer customer = customerApiService.customerInfo(TOKEN.getUuid());
        ApiResponse<String> response = new ApiResponse<>();
        try {
            response = apiOrderService.appAliPrePay(customer, pay_order_id, appid);
        } catch (Exception e) {
            response.setErrors(Errors._41023);
            logger.error("api-method:error code:{},msg:{}", e);
        }
        if (response.getErrors() != null)
            response.getErrors().throwError();
        return response.getT();
    }

    public AssumeRoleResponse.Credentials query_alioss_credentials() {

        return apiCommonService.ossSts();
    }

    /**
     * 消息列表
     */
    public List<MessageContent> customer_message_list(Integer page_index, Integer count) {
        Customer customer = query.customer_info();
        List<MessageContent> messages = customerApiService.queryMessageList(customer, page_index, count);
        return messages;
    }

    public PageResult customer_order_list_v2(String status, String next_id, String pre_id, boolean forward) {
        Customer customer = query.customer_info();
        return apiOrderService.customerOrderList(status, customer.getCustomer_id(), next_id, pre_id, forward);
    }

    /**
     * 获取一个用户的优惠券中优惠最大的优惠券
     */
    public CouponResponse customer_best_coupon(Long psku_id,int num){
        Customer customer = query.customer_info();
        return customerApiService.theBestCoupon(customer,psku_id,num);
    }

    /**
     * App版本控制
     */
    public AppVersion app_version_control(String currentVersion,String osVersion){
        return apiCommonService.appVersionControl(currentVersion,TOKEN.getClientId(),osVersion);
    }

    /**
     * 取消策略方法
     */
    public CancleStrategyMethod customer_cancleStrategy(String pay_order_id){
        Customer customer = query.customer_info();
        ApiResponse<CancleStrategyMethod>  response= customerApiService.cancleStrategyMethod(customer,pay_order_id);
        if (response.getErrors() != null) {
            if (response.getErrors().name().equals("_41020")) {
                Msgtext msg = msgtextService.selectByPrimaryKey(MsgTextConstant.CANCLE_ORDER_FORBID);
                response.getErrors().throwError(msg.getContent());
            }
            response.getErrors().throwError();
        }
        return response.getT();
    }
}


