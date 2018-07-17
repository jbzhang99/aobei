package com.aobei.train.mapper;

import com.aobei.train.model.CouponReceive;
import com.aobei.train.model.CouponReceiveExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponReceiveMapper extends MbgReadMapper<Long, CouponReceive, CouponReceive, CouponReceiveExample>, MbgWriteMapper<Long, CouponReceive, CouponReceive, CouponReceiveExample> {
}