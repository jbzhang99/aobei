package com.aobei.train.mapper;

import com.aobei.train.model.PayWxNotify;
import com.aobei.train.model.PayWxNotifyExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayWxNotifyMapper extends MbgReadMapper<String, PayWxNotify, PayWxNotify, PayWxNotifyExample>, MbgWriteMapper<String, PayWxNotify, PayWxNotify, PayWxNotifyExample> {
}