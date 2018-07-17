package com.aobei.train.mapper;

import com.aobei.train.model.ChannelType;
import com.aobei.train.model.ChannelTypeExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChannelTypeMapper extends MbgReadMapper<Integer, ChannelType, ChannelType, ChannelTypeExample>, MbgWriteMapper<Integer, ChannelType, ChannelType, ChannelTypeExample> {
}