package com.aobei.train.mapper;

import com.aobei.train.model.Message;
import com.aobei.train.model.MessageExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends MbgReadMapper<Long, Message, Message, MessageExample>, MbgWriteMapper<Long, Message, Message, MessageExample> {
}