package com.aobei.trainapi.server;

import java.util.List;
import com.aobei.train.model.Customer;
import com.aobei.train.model.Order;
import com.aobei.trainapi.schema.type.PageResult;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.server.bean.StudentInfo;
import custom.bean.CouponResponse;
import custom.bean.OrderPrice;
import weixin.popular.bean.paymch.MchPayApp;


public interface ApiOrderService {

    void paySuccess(Order order, int paytype);
    //Station dispatch(Order order, ServiceUnit serviceUnit);
	ApiResponse getCounpons(Customer customer, Long coupon_id);
	List<CouponResponse> queryConponList(Customer customer, Long product_id, Integer page_index, Integer count);

    ApiResponse<MchPayApp> appWxPrePay(Customer customer,String pay_order_id,String path);

    PageResult customerOrderList(String status,Long customerId , String nextOrderId,String preOrderId,boolean forward);

    ApiResponse<String>  appAliPrePay(Customer customer,String pay_order_id,String appId);

    ApiResponse exchangeCodeToCoupon(Customer customer,String exchange_code);

    ApiResponse<OrderPrice> recalculatePriceV2(Customer customer, Long psku_id, Long coupon_receive_id, Integer num);
    //发起抢单方法
    void startRobbing(Order order);
    //服务人员计算订单价钱
    ApiResponse<OrderPrice> studentRecalculatePrice(StudentInfo studentInfo,Long psku_id, Integer num);
}
