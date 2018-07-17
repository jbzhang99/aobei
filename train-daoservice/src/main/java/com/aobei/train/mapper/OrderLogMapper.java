package com.aobei.train.mapper;

import com.aobei.train.model.OrderLog;
import com.aobei.train.model.OrderLogExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderLogMapper extends MbgReadMapper<Long, OrderLog, OrderLog, OrderLogExample>, MbgWriteMapper<Long, OrderLog, OrderLog, OrderLogExample> {
}