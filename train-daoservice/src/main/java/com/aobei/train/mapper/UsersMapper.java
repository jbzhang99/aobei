package com.aobei.train.mapper;

import com.aobei.train.model.Users;
import com.aobei.train.model.UsersExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper extends MbgReadMapper<Long, Users, Users, UsersExample>, MbgWriteMapper<Long, Users, Users, UsersExample> {
}