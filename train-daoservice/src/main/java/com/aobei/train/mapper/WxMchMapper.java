package com.aobei.train.mapper;

import com.aobei.train.model.WxMch;
import com.aobei.train.model.WxMchExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WxMchMapper extends MbgReadMapper<String, WxMch, WxMch, WxMchExample>, MbgWriteMapper<String, WxMch, WxMch, WxMchExample> {
}