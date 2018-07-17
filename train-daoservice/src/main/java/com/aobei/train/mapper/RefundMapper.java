package com.aobei.train.mapper;

import com.aobei.train.model.Refund;
import com.aobei.train.model.RefundExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefundMapper extends MbgReadMapper<Long, Refund, Refund, RefundExample>, MbgWriteMapper<Long, Refund, Refund, RefundExample> {
}