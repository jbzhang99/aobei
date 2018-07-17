package com.aobei.train.mapper;

import com.aobei.train.model.PayAliNotify;
import com.aobei.train.model.PayAliNotifyExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayAliNotifyMapper extends MbgReadMapper<String, PayAliNotify, PayAliNotify, PayAliNotifyExample>, MbgWriteMapper<String, PayAliNotify, PayAliNotify, PayAliNotifyExample> {
}