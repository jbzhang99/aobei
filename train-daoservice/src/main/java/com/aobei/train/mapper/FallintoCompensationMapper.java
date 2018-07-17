package com.aobei.train.mapper;

import com.aobei.train.model.FallintoCompensation;
import com.aobei.train.model.FallintoCompensationExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FallintoCompensationMapper extends MbgReadMapper<Long, FallintoCompensation, FallintoCompensation, FallintoCompensationExample>, MbgWriteMapper<Long, FallintoCompensation, FallintoCompensation, FallintoCompensationExample> {
    String selectMaxBalanceCycle();
}