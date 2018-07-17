package com.aobei.trainapi.server;

import com.aobei.train.model.*;
import com.aobei.trainapi.schema.input.CustomerAddressInput;
import com.aobei.trainapi.schema.input.OrderInput;
import com.aobei.trainapi.schema.type.MutationResult;
import com.aobei.trainapi.server.bean.ApiResponse;
import com.aobei.trainapi.server.bean.MessageContent;

import custom.bean.*;

import java.util.List;


/**
 * create  by renpiming 2018/02/11
 * 接口定义按照新用户进入，到下单的流程顺序进行定义。
 * 1
 */
public interface CustomerApiService {


    //查询接口

    /**
     * 用户进入，验证绑定的user信息。如果没有绑定需要进行下一步的绑定操作
     *
     * @param user_id 用户ID
     * @return Customer
     */
    Customer customerInfo(Long user_id);

    /**
     * 验证用户的验证码是否正确
     *
     * @param user_id 顾客的用户ID
     * @param code    验证码
     * @param phone   手机号码
     * @return boolean
     */
    boolean checkVerificationCode(Long user_id, String code, String phone);

    /**
     * 检查短信发送次数
     *
     * @param phone 手机号码
     * @param type  短信类型（通知类，验证码类）
     * @return int
     */
    int smsCount(String phone, String type);

    /**
     * 获取首页展示的分类信息
     *
     * @return list
     */
    List<Category> homeCategoryList();

    /**
     * 根据分类的levelcode 来获取分类下的产品
     *
     * @param category_level_code 分类层级code
     * @param page_index          页码
     * @param count               条数
     * @return list
     */
    List<ProductWithCoupon> productListByCategoryLevelCode(String category_level_code, int page_index, int count,String city_id,Long customer_id);

    /**
     * 分页获取产品列表
     *
     * @param page_index 页码  从1开始
     * @param count      每页显示条数
     * @return list
     */
    List<ProductWithCoupon> homeProductList(int page_index, int count,String city_id,Long customer_id);

    /**
     * 获取banner广告图
     *
     * @param appid    应用名称
     * @param position 位置
     * @return list
     */
    List<OssImg> bannerList(String appid, String position);

    /**
     * 根据产品ID获得一个产品详细信息
     *
     * @param product_id 产品ID
     * @return ProductInfo
     */
    ApiResponse<ProductInfo> productDetail(Long product_id,Long customer_id);

    /**
     * 分页获取一个产品的sku集合
     *
     * @param product_id 产品ID
     * @param page_index 页码 从1开始
     * @param count      每页显示条数
     * @return list
     */
    List<ProSku> proSkuList(Long product_id, int page_index, int count);

    /**
     * 获得顾客地址列表，
     *
     * @param customer_id 顾客ID
     * @return list
     */
    List<CustomerAddress> addressList(Long customer_id);

    /**
     * 获得顾客最后一次使用过的服务地址。
     *
     * @param customer_id 顾客ID
     * @return CustomerAddress
     */
    CustomerAddress getDefaultAddress(Long customer_id);

    /**
     * 获取用户选择的服务的所有可用时间
     *
     * @param customer            顾客对象
     * @param product_id          产品ID
     * @param psku_id             产品skuID
     * @param customer_address_id 服务地址ID
     * @return list
     */
    List<TimeModel> productAvailableTimes(Customer customer, Long product_id, Long psku_id, Long customer_address_id,int num);

    /**
     * 获取我的优惠券列表
     *
     * @param customer 顾客对象
     * @return list
     */
    List<CouponResponse> couponList(Customer customer, int page_index, int count);

    List<CouponResponse> expireCouponList(Customer customer,int pageIndex);
    List<CouponResponse> usedCouponList(Customer customer,int pageIndex);

    /**
     * 获取我的优惠券。关联产品和购买数量
     * @param customer 顾客
     * @param psku_id 产品ID
     * @param num 购买数量
     * @param page_index 页码
     * @param count 条数
     * @return
     */
    List<CouponResponse> userCouponList(Customer customer,Long psku_id,int num,int page_index,int count);
    /**
     * 获得订单列表。
     *
     * @param status      订单状态，可以为空
     * @param customer_id 顾客ID
     * @param page_index  页码
     * @param count       条数
     * @return list
     */
    List<OrderInfo> orderList(String status, Long customer_id, int page_index, int count);

    /**
     * 获取订单详情
     *
     * @param pay_order_id 订单号
     * @return OrderInfo
     */
    OrderInfo orderDetail(Customer customer, String pay_order_id);

    /**
     * 用户支付前获取订单数据，用来支付
     *
     * @param pay_order_id 订单号
     * @return Order
     */
    Order getPayOrder(Customer customer, String pay_order_id);

    /**
     * 调用微信统一下单前的数据准备
     *
     * @param customer 顾客信息
     * @param pay_order_id 订单号
     * @return  ApiResponse<WxPaymentBody>
     */
    ApiResponse<WxPaymentBody> wxPrepay(Customer customer, String pay_order_id,String path,String open_id);

    /**
     * 0元支付接口
     *
     * @param customer 顾客信息
     * @param pay_order_id 订单号
     * @return ApiResponse<Order>
     */
    ApiResponse<Order> wxPayFree(Customer customer, String pay_order_id);

    /**
     * 确认订单支付状态
     *
     * @param customer 顾客信息
     * @param pay_order_id 订单号
     * @return  ApiResponse<Order>
     */
    ApiResponse<Order> confirmPayStatus(Customer customer, String pay_order_id,String path);

    /**********************************************我是分割线以下位置定义变更接口*******************************************************************/

    /**
     * 向顾客填写的电话号码发送短信
     *
     * @param user_id 顾客的用户ID
     * @param phone   手机号码
     * @return MutationResult
     */

    ApiResponse sendVerificationCode(Long user_id, String phone);

    /**
     * 创建顾客信息，
     *
     * @param user_id 用户ID
     * @param phone   手机号码
     * @return ApiResponse
     */

    ApiResponse bindUser(Long user_id, String user_name, String phone,Integer channel);

    /**
     * 变更手机号码。默认只要是用户能够登陆，就能够进行变更。不要求和之前的电话号码进行对比
     *
     * @param customer 顾客信息
     * @param phone 电话号码
     * @return ApiResponse
     */
    ApiResponse changePhone(Customer customer, String phone);

    /**
     * 设置密码
     *
     * @param user_id  用户ID
     * @param password 密码
     * @param repeat   重复验证密码
     * @return MutationResult
     */
    MutationResult setPassword(Long user_id, String password, String repeat);

    /**
     * 顾客添加一个需要被服务的地址。包含具体地址，被服务人员的姓名，姓名不一定是顾客姓名
     *
     * @param customerAddressInput 服务地址输入项
     * @return CustomerAddress
     */
    CustomerAddress addressAdd(Customer customer, CustomerAddressInput customerAddressInput);

    /**
     * 修改一个需要被服务的地址。包含具体地址，被服务人员的姓名，姓名不一定是顾客姓名
     *
     * @param customerAddressInput 服务地址输入项
     * @return CustomerAddress
     */
    CustomerAddress addressUpdate(Customer customer, CustomerAddressInput customerAddressInput);

    /**
     * 删除顾客服务地址
     * @param  customer 用户信心
     * @param customer_address_id 顾客服务地址ID
     * @return MutationResult
     */
    MutationResult addressDelete(Customer customer,Long customer_address_id);

    /**
     * 用户设置默认地址
     *
     * @param customer_id         顾客ID
     * @param customer_address_id 服务地址ID
     * @return MutationResult
     */
    MutationResult setDefaultAddress(Long customer_id, Long customer_address_id);

    /**
     * 根据所选的优惠券，重新计算订单的价格
     *
     * @param customer          顾客
     * @param psku_id 产品skuID
     * @param coupon_receive_id 获得的优惠券ID
     * @return ApiResponse<Integer>
     */
    ApiResponse<Integer> recalculatePrice(Customer customer, Long psku_id, Long coupon_receive_id, int num);

    /**
     * 创建订单，根据传入的参数进行订单的创建
     *
     * @param input 订单输入项
     * @return ApiResponse
     */
    ApiResponse<Order> createOrder(Customer customer, OrderInput input,String channelId,String clientId);


    /**
     * 订单支付成功，
     *
     * @param pay_order_id 订单号
     * @return ApiResponse
     */
    ApiResponse orderPaysuccess(String pay_order_id, int paytype);


    /**
     * 顾客主动取消订单 ，针对已经支付的订单进行取消。
     *
     * @param customer      顾客对象
     * @param pay_order_id  订单号
     * @param remark_cancel 取消备注
     * @return ApiResponse
     */
    ApiResponse cancelOrder(Customer customer, String pay_order_id, String remark_cancel);

    /**
     * 顾客提交对本次服务的评价。评价为一次行评价。评价后不能在进行评价
     *
     * @param customer     顾客对象
     * @param pay_order_id 订单号
     * @param score        评分
     * @param comment      评价内容
     * @return ApiResponse
     */
    ApiResponse serviceEvaluate(Customer customer, String pay_order_id, int score, String comment);




    void transactionTest();
    /**
     * 服务人员离开后,顾客确定完成
     * @param customer
     * @param pay_order_id
     * @return
     */
	ApiResponse serviceComplete(Customer customer, String pay_order_id);
	
	/**
	 * 修改顾客信息
	 * @param customer
	 * @param name
	 * @param logo_img
	 * @param gender
	 * @param idcard 
	 * @return
	 */
	MutationResult customerInfoUpdate(Customer customer, String name, String logo_img, Integer gender, String idcard);
	
	/**
	 * 消息列表
	 * @param customer 
	 * @return
	 */
	List<MessageContent> queryMessageList(Customer customer,int page_index,int count);

    /**
     * 获取一个用户的优惠券中优惠最大的优惠券
     */
    CouponResponse theBestCoupon(Customer customer,Long psku_id,int num);

    /**
     * 实时获取预约数量接口
     * @param product_id
     * @return
     */
    Integer getNumberOfAppointments(Long product_id);

    /**
     * 取消策略方法
     * @param customer
     * @param pay_order_id
     * @return
     */
    ApiResponse<CancleStrategyMethod> cancleStrategyMethod(Customer customer, String pay_order_id);
}