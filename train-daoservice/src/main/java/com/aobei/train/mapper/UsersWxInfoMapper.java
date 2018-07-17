package com.aobei.train.mapper;

import com.aobei.train.model.UsersWxInfo;
import com.aobei.train.model.UsersWxInfoExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersWxInfoMapper extends MbgReadMapper<String, UsersWxInfo, UsersWxInfo, UsersWxInfoExample>, MbgWriteMapper<String, UsersWxInfo, UsersWxInfo, UsersWxInfoExample> {
}