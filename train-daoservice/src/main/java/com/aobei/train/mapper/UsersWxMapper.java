package com.aobei.train.mapper;

import com.aobei.train.model.UsersWx;
import com.aobei.train.model.UsersWxExample;
import com.aobei.train.model.UsersWxKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersWxMapper extends MbgReadMapper<UsersWxKey, UsersWx, UsersWx, UsersWxExample>, MbgWriteMapper<UsersWxKey, UsersWx, UsersWx, UsersWxExample> {
}