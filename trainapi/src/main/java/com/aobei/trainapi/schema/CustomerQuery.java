package com.aobei.trainapi.schema;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aobei.train.model.*;
import com.aobei.trainapi.server.ApiCommonService;
import com.aobei.trainapi.server.ApiOrderService;
import com.aobei.trainapi.server.CustomerApiService;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.util.JsonUtil;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import custom.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class CustomerQuery implements GraphQLQueryResolver {

    @Autowired
    private TokenUtil TOKEN;

    @Autowired
    private CustomerApiService customerApiService;

    @Autowired
    Producer producer;
    @Autowired
    ApiCommonService apiCommonService;
    @Autowired
    ApiOrderService apiOrderService;

    Logger logger  = LoggerFactory.getLogger(CustomerQuery.class);
    /**
     * 查看用户绑定情况，
     *
     * @return
     */
    public Customer customer_info() {
        if(TOKEN.getUuid()==null)
            Errors._41003.throwError();
        Customer customer = customerApiService.customerInfo(TOKEN.getUuid());
        if (customer == null) {
            Errors._41003.throwError();
        }
        return customer;
    }

    /**
     * 获取首页需要展示的分类信息
     *
     * @return
     */
    public List<Category> customer_home_category_list() {
        return customerApiService.homeCategoryList();
    }

    /**
     * 获取banner图
     *
     * @return
     */
    @Deprecated
    public List<OssImg> customer_banner_list(String position) {

        return customerApiService.bannerList(TOKEN.getClientId(), position);
    }

    /**
     * 根据分类的ID获取到对应的产品信息
     *
     * @param category_level_code
     * @param page_index
     * @param count
     * @return
     */
    public List<ProductWithCoupon> customer_product_list_by_category_level_code(String category_level_code, int page_index, int count,String city_id) {

        Long customer_id=null;
        if(TOKEN.getUuid()!=null) {
            Customer customer = customerApiService.customerInfo(TOKEN.getUuid());
            if(customer!=null){
                customer_id=customer.getCustomer_id();
            }
        }
        List<ProductWithCoupon> list = customerApiService.productListByCategoryLevelCode(category_level_code, page_index, count,city_id,customer_id);
    	return list;
    }

    /**
     * 分页获取产品信息，用户不需要注册即可获取！
     *
     * @param page_index
     * @param count
     * @return
     */
    public List<ProductWithCoupon> customer_home_product_list(int page_index, int count,String city_id) {
        Long customer_id=null;
        if(TOKEN.getUuid()!=null) {
            Customer customer = customerApiService.customerInfo(TOKEN.getUuid());
            if(customer!=null){
                customer_id=customer.getCustomer_id();
            }
        }
        return customerApiService.homeProductList(page_index, count,city_id,customer_id);
    }

    /**
     * 根据产品信息获取产品的详细
     *
     * @param product_id
     * @return
     */
    ProductInfo customer_product_detail(Long product_id) {
        Long customer_id=null;
        if(TOKEN.getUuid()!=null) {
            Customer customer = customerApiService.customerInfo(TOKEN.getUuid());
            if(customer!=null){
                customer_id=customer.getCustomer_id();
            }
        }
        ApiResponse<ProductInfo> response = customerApiService.productDetail(product_id,customer_id);
        if(response.getErrors()!=null)
            response.getErrors().throwError();
        return response.getT();
    }

    /**
     * 分月获取产品SKU列表，用户不需要注册
     *
     * @param product_id
     * @param page_index
     * @param count
     * @return
     */
    public List<ProSku> customer_prosku_list(Long product_id, int page_index, int count) {
        return customerApiService.proSkuList(product_id, page_index, count);
    }

    /**
     * 获取顾客已经填写的地址列表
     *
     * @return
     */
    public List<CustomerAddress> customer_address_list() {
        Customer customer = customer_info();

        return customerApiService.addressList(customer.getCustomer_id());
    }

    /**
     * 获取用户的默认服务地址
     *
     * @return
     */
    public CustomerAddress customer_get_default_address() {
        Customer customer = customer_info();
        return customerApiService.getDefaultAddress(customer.getCustomer_id());
    }

    /**
     * 根据产品ID，SKUID ，进行计算。计算该sku在所选范围内是否存在可用的时间段
     *
     * @param product_id 产品ID
     * @param psku_id    产品skuID
     * @return
     */
    public List<TimeModel> customer_product_available_times(Long product_id, Long psku_id, Long customer_address_id,int num) {
        Customer customer = customer_info();
        List<TimeModel> timeModels =null;
        try {
           timeModels  = customerApiService.productAvailableTimes(customer, product_id, psku_id, customer_address_id,num);
        }catch (Exception e){
            logger.error("ERROR  Get Product available times fail",e);
            Errors._41040.throwError();
        }
        return timeModels;
    }

    /**
     * 获取一个用户的所有优惠券的列表
     *
     * @param page_index
     * @param count
     * @return
     */
    public List<CouponResponse> customer_coupon_list(int page_index, int count) {
        Customer customer = customer_info();
        return customerApiService.couponList(customer, page_index, count);
    }


    /**
     * 根据产品获得可使用的优惠券
     * @param psku_id
     * @param num
     * @param page_index
     * @param count
     * @return
     */
    public  List<CouponResponse> customer_coupon_list_by_product(Long psku_id,int num, int page_index,int count){
        Customer customer= customer_info();
        return customerApiService.userCouponList(customer,psku_id,num,page_index,count);
    }

    /**
     * v2   获取优惠券列表，状态方式获取
     * @param type
     * @param pageIndex
     * @return
     */
    public List<CouponResponse> customerCoupons(Integer type,Integer pageIndex){
        Customer customer= customer_info();
        List<CouponResponse> responses = new ArrayList<>();
        switch (type){
            case 1:
                responses = customerApiService.couponList(customer, pageIndex, 10);
                break;
            case 2:
               responses =  customerApiService.usedCouponList(customer,pageIndex);
                break;
            case 3:
                responses = customerApiService.expireCouponList(customer,pageIndex);
                break;
        }
        return  responses;
    }
    /**
     * 根据条件重新计算价格
     *
     * @param psku_id           产品skuID
     * @param coupon_receive_id 优惠券ID
     * @param num               购买数量
     * @return Integer
     */
    public Integer customer_recalculate_price(Long psku_id, Long coupon_receive_id, int num) {
        Customer customer = customer_info();
        ApiResponse<Integer> response = customerApiService.recalculatePrice(customer, psku_id, coupon_receive_id, num);
        if (response.getErrors() != null)
            response.getErrors().throwError();
        return response.getT();
    }

    /**
     * 获取订单列表
     *
     * @param page_index 页码
     * @param count      条数
     * @return
     */
    public List<OrderInfo> customer_order_list(String status, int page_index, int count) {
        Customer customer = customer_info();
        return customerApiService.orderList(status, customer.getCustomer_id(), page_index, count);
    }

    /**
     * 获取订单详情
     *
     * @param pay_order_id 订单号
     * @return
     */
    public OrderInfo customer_order_detail(String pay_order_id) {
        Customer customer = customer_info();
        return customerApiService.orderDetail(customer, pay_order_id);
    }

    /**
     * 获取订单准备支付
     *
     * @param pay_order_id 订单号
     * @return
     */
    public Order customer_get_order_to_pay(String pay_order_id) {
        Customer customer = customer_info();
        return customerApiService.getPayOrder(customer, pay_order_id);
    }

    /**
     * 进行微信的统一下单处理
     *
     * @param pay_order_id 订单号
     * @return
     */
    public WxPaymentBody customer_wx_prepay(String pay_order_id,String openid) {
        Customer customer = customer_info();
        String path = TOKEN.getClientId();
        ApiResponse<WxPaymentBody> response = customerApiService.wxPrepay(customer, pay_order_id,path,openid);
        if (response.getErrors() != null)
            response.getErrors().throwError();
        return response.getT();
    }

    /**
     * 查看订单的支付状态
     *
     * @param pay_order_id 订单号
     * @return
     */
    public Order customer_confirm_pay_status(String pay_order_id) {
        Customer customer = customer_info();
        String path = TOKEN.getClientId();
        ApiResponse<Order> response = customerApiService.confirmPayStatus(customer, pay_order_id,path);
        if (response.getErrors() != null)
            response.getErrors().throwError();
        return response.getT();

    }
    
    /**
     * 查询开通服务的城市
     * @return
     */
    public List<TrainCity> customer_open_cities(){
    	return apiCommonService.openCities();
    }

    public String customer_test() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "test");
        String test = null;
        if (1==1){
            test.length();
        }
        return JsonUtil.toJSONString(map);
    }
    
    /**
     * 查询用户可领取优惠券列表
     */
    public List<CouponResponse> customer_query_conpon_list(Long product_id,Integer page_index,Integer count){
    	Customer customer = customer_info();
    	List<CouponResponse> coupons =  apiOrderService.queryConponList(customer,product_id,page_index,count);
    	return coupons;
    }


    /**
     * v2 版本接口
     */

    public OrderPrice customer_recalculate_price_v2(Long psku_id, Long coupon_receive_id, Integer num) {
        ApiResponse<OrderPrice> response = new ApiResponse<>();
        if (num > 1000) {
            Errors._41040.throwError("最大可购买数量：1000");
        }
        try {
            Customer customer = customer_info();
            response = apiOrderService.recalculatePriceV2(customer, psku_id, coupon_receive_id, num);
            if (response.getErrors() != null)
                response.getErrors().throwError();
        } catch (Exception e) {
            logger.error("ERROR customer_recalculate_price_v2", e);
            Errors._41040.throwError();
        }

        return response.getT();
    }

    /**
     * 实时获取预约数量接口
     */
    public Integer customer_get_number_of_appointments(Long product_id){
        return customerApiService.getNumberOfAppointments(product_id);
    }
}
