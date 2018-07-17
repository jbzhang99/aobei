package com.aobei.train.mapper;

import com.aobei.train.model.Compensation;
import com.aobei.train.model.CompensationExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompensationMapper extends MbgReadMapper<Long, Compensation, Compensation, CompensationExample>, MbgWriteMapper<Long, Compensation, Compensation, CompensationExample> {
}