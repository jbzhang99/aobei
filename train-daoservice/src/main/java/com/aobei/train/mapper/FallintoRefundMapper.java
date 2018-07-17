package com.aobei.train.mapper;

import com.aobei.train.model.FallintoRefund;
import com.aobei.train.model.FallintoRefundExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FallintoRefundMapper extends MbgReadMapper<Long, FallintoRefund, FallintoRefund, FallintoRefundExample>, MbgWriteMapper<Long, FallintoRefund, FallintoRefund, FallintoRefundExample> {
    String selectMaxBalanceCycle();
}