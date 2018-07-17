package com.aobei.train.mapper;

import com.aobei.train.model.RefundWxNotify;
import com.aobei.train.model.RefundWxNotifyExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefundWxNotifyMapper extends MbgReadMapper<String, RefundWxNotify, RefundWxNotify, RefundWxNotifyExample>, MbgWriteMapper<String, RefundWxNotify, RefundWxNotify, RefundWxNotifyExample> {
}