package com.aobei.train.mapper;

import com.aobei.train.model.Channel;
import com.aobei.train.model.ChannelExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChannelMapper extends MbgReadMapper<Integer, Channel, Channel, ChannelExample>, MbgWriteMapper<Integer, Channel, Channel, ChannelExample> {
}