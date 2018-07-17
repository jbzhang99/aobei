package com.aobei.train.mapper;

import com.aobei.train.model.OperateLog;
import com.aobei.train.model.OperateLogExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogMapper extends MbgReadMapper<Long, OperateLog, OperateLog, OperateLogExample>, MbgWriteMapper<Long, OperateLog, OperateLog, OperateLogExample> {
}