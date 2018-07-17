package com.aobei.train.mapper;

import com.aobei.train.model.AuthRes;
import com.aobei.train.model.AuthResExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthResMapper extends MbgReadMapper<Integer, AuthRes, AuthRes, AuthResExample>, MbgWriteMapper<Integer, AuthRes, AuthRes, AuthResExample> {
}