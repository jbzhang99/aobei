package com.aobei.train.mapper;

import com.aobei.train.model.CouponAndCouponEnv;
import com.aobei.train.model.CouponAndCouponEnvExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponAndCouponEnvMapper extends MbgReadMapper<Long, CouponAndCouponEnv, CouponAndCouponEnv, CouponAndCouponEnvExample>, MbgWriteMapper<Long, CouponAndCouponEnv, CouponAndCouponEnv, CouponAndCouponEnvExample> {
}