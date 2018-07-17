package com.aobei.train.mapper;

import com.aobei.train.model.OutOfService;
import com.aobei.train.model.OutOfServiceExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OutOfServiceMapper extends MbgReadMapper<Long, OutOfService, OutOfService, OutOfServiceExample>, MbgWriteMapper<Long, OutOfService, OutOfService, OutOfServiceExample> {
}