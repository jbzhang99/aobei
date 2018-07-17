package com.aobei.train.service;

import com.aobei.train.model.*;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

import custom.bean.CouponList;
import custom.bean.CouponReceiveList;
import custom.bean.CouponResponse;
import custom.bean.DiscountData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CouponService extends MbgReadService<Long, Coupon, Coupon, CouponExample>, MbgWriteService<Long, Coupon, Coupon, CouponExample>, MbgUpsertService<Long, Coupon, Coupon, CouponExample> {

    List<CouponResponse> selectByUid(Map<String, Object> map);

    List<CouponResponse> filterByProductId(List<CouponResponse> list, Long product_id, int price);

    List<CouponResponse> mapCoupon(List<CouponResponse> list);

    int recalculatePrice(Coupon coupon, ProSku sku, int num);
    DiscountData recalculatePrice(Customer customer,Long coupon_receive_id, ProSku proSku , int num);
    
    /**
     * 返回所有有商品的分类数据 和商品数据
     * @return
     */
    HashMap<String, Object> getCategoryProduct();
    /**
     * 返回优惠券失效时间
     * @param coupon_id
     * @return
     */
    HashMap<String, String> getEndTime(Long coupon_id);
    /**
     * 指定商品数据集合展示
     * @param coupon_id
     * @return
     */
    HashMap<String, List<Product>> getProductsList(Long coupon_id);
    /**
     * 优惠券列表页封装数据展示
     * @param example
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<CouponList> xSelectCouponList(CouponExample example,int pageNo,int pageSize,int type);

    List<Object[]> xSelectCouponList(CouponExample example);

    List<Object[]> xSelectCouponList(CouponExample example,int type);
    /**
     * 优惠券派发功能
     * @param coupon_id
     * @param ids
     * @return
     */
    Map<String,String> xDistribute_coupon(Long coupon_id,String ids);
    /**
     * 使用记录列表页封装数据展示
     * @param example
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<CouponReceiveList> xSelectRecordDetail(CouponReceiveExample example,int pageNo,int pageSize);

    List<CouponResponse> xCouponResponse(List<CouponReceive> receives);
    //审核
    Integer xVerify(Long coupon_id,Integer verify,String verify_comments);
}