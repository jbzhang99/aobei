package com.aobei.train.mapper;

import com.aobei.train.model.JdOrder;
import com.aobei.train.model.JdOrderExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JdOrderMapper extends MbgReadMapper<String, JdOrder, JdOrder, JdOrderExample>, MbgWriteMapper<String, JdOrder, JdOrder, JdOrderExample> {
}