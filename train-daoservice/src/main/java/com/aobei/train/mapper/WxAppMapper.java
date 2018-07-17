package com.aobei.train.mapper;

import com.aobei.train.model.WxApp;
import com.aobei.train.model.WxAppExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WxAppMapper extends MbgReadMapper<String, WxApp, WxApp, WxAppExample>, MbgWriteMapper<String, WxApp, WxApp, WxAppExample> {
}