package com.aobei.train.mapper;

import com.aobei.train.model.Fallinto;
import com.aobei.train.model.FallintoExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FallintoMapper extends MbgReadMapper<Long, Fallinto, Fallinto, FallintoExample>, MbgWriteMapper<Long, Fallinto, Fallinto, FallintoExample> {
}