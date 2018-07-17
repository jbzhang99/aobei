package com.aobei.train.mapper;

import com.aobei.train.model.Coupon;
import com.aobei.train.model.CouponExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import custom.bean.CouponResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CouponMapper extends MbgReadMapper<Long, Coupon, Coupon, CouponExample>, MbgWriteMapper<Long, Coupon, Coupon, CouponExample> {
    List<CouponResponse> selectByUid(Map<String, Object> map);
}