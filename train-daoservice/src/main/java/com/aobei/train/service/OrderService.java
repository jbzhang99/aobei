package com.aobei.train.service;

import com.aobei.train.Roles;
import com.aobei.train.model.*;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;
import custom.bean.CancleStrategyMethod;
import custom.bean.OrderInfo;

import java.util.Date;
import java.util.List;

public interface OrderService extends MbgReadService<String, Order, Order, OrderExample>,MbgWriteService<String, Order, Order, OrderExample>,MbgUpsertService<String, Order, Order, OrderExample>{
     /**
      * 初始化一个订单数据
      * @param product 产品
      * @param proSku 产品sku
      * @param customerAddress 用户地址
      * @param num 数量
      * @return Order
      */
     Order initOrder(Product product, ProSku proSku,CustomerAddress customerAddress,int num);

     /**
      * 从订单入手 获得组装后的orderinfo
      * @param roles 用户角色
      * @param example  订单的条件
      * @param page 页码
      * @param size 条数
      * @return Page
      */
     Page<OrderInfo> orderInfoList(Roles roles, OrderExample example, int page, int size);

     /**
      *
      * 从服务单入手。获得组装后的orderinfo
      * @param roles 用户角色
      * @param example 服务单条件
      * @param page 页码
      * @param size 条数
      * @return Page
      */
     Page<OrderInfo> orderInfoList( Roles roles,ServiceUnitExample example,int page,int size);

    /**
     *
     * 从服务单入手。获得组装后的orderinfo 的集合
     * @param roles 用户角色
     * @param example 服务单条件
     */
     List<OrderInfo> orderInfoListWithOutPage(Roles roles,ServiceUnitExample example);

     /**
      * 从订单入手获得订单详情
      * @param roles 用户角色
      * @param order 订单
      * @return Orderinfo
      */
     OrderInfo orderInfoDetail(Roles roles, Order order);
     /**
      * 从服务单入手获得订单详情
      * @param roles 用户角色
      * @param serviceUnit 订单
      * @return Orderinfo
      */
     OrderInfo orderInfoDetail(Roles roles, ServiceUnit serviceUnit);

     /**
      * 取消为支付订单
      * @param order_id 订单号
      * @return boolean
      */
     boolean xCancelOrderWithoutPay(String order_id,String remark);

     /**
      * 取消以支付订单
      * @param customer 顾客
      * @param order 订单
      * @param remark_cancel 备注
      * @return boolean
      */

     boolean xCancelOrderPayedAndRefund(Customer customer, Order order, String remark_cancel, CancleStrategyMethod cancleStrategyMethod);
     /**
      * 拒单，
      * @param order_id 订单号
      * @param partner_id 合伙人ID
      * @return boolean
      */
     boolean xRejectOrder(String order_id,Long partner_id,String remark,int regectType);
     
     /**
      * 后台取消订单
      * @param order
      * @return
      */
     int xCancelOrderBackstage(Order order);
     
     /**
      * 申请退款（若此时订单未取消----一并取消订单）
      * @param fee
      * @param info
      * @param unit
      * @param users
      * @return
      */
	 int applyRefund(String fee, String info, ServiceUnit unit, Users users);

    /**
     * 插入赔偿单信息
     * @param compensation
     * @return
     */
     int xInsertCompensation(Compensation compensation);

    /**
     * 插入订单完成申请
     * @param completeApply
     * @return
     */
    int xInsertCompleteApply(CompleteApply completeApply);

    /**
     * 申请服务单确认完成
     * @param completeApply
     * @return
     */
    int xUpdateComfirmCompleteApply(CompleteApply completeApply,Users users);

    /**
     * 申请服务单确认完成驳回
     * @param completeApply
     * @return
     */
    int xUpdateRejectCompleteApply(CompleteApply completeApply,Users users);

    /**
     * 插入订单扣款申请
     * @param deductMoney
     * @return
     */
    int xInsertDeductMoney(DeductMoney deductMoney);

    /**
     * 申请扣款确认
     * @param deductMoney
     * @return
     */
    int xUpdateComfirmDeductMoney(DeductMoney deductMoney ,Users users);

    /**
     * 申请扣款驳回
     * @param deductMoney
     * @return
     */
    int xUpdateRejectDeductMoney(DeductMoney deductMoney ,Users users);

	 /**
	  * 订单变更
	  * @param name
	  * @param partner_id
	  * @param station_id
	  * @param change_intro
	  * @param order
	  * @param c_begin_datetime
	  * @param c_end_datetime
	  * @return
	  */
	 int changeOrder(String name, Long partner_id, Long station_id, String change_intro, Order order,
			Date c_begin_datetime, Date c_end_datetime,ServiceUnit unit);

	 <T> T generateDownloadTaskAndPottingParam(String str, String username, long id,
			Class<T> class1);

    /**
     * 插入订单罚款申请
     * @param fineMoney
     * @return
     */
    int xInsertFineMoney(FineMoney fineMoney);

    /**
     * 申请罚款确认
     * @param fineMoney
     * @return
     */
    int xUpdateComfirmFineMoney(FineMoney fineMoney, Users users);

    /**
     * 申请罚款驳回
     * @param fineMoney
     * @return
     */
    int xUpdateRejectFineMoney(FineMoney fineMoney, Users users);

    /**
     * 派发订单
     * @param order
     * @param serviceUnit
     * @return
     */
    Station dispatchOrder(Order order, ServiceUnit serviceUnit);
}