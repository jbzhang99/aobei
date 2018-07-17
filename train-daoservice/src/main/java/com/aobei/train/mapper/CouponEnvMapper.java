package com.aobei.train.mapper;

import com.aobei.train.model.CouponEnv;
import com.aobei.train.model.CouponEnvExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponEnvMapper extends MbgReadMapper<Long, CouponEnv, CouponEnv, CouponEnvExample>, MbgWriteMapper<Long, CouponEnv, CouponEnv, CouponEnvExample> {
}