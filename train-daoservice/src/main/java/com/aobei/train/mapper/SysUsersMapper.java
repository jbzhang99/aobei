package com.aobei.train.mapper;

import com.aobei.train.model.SysUsers;
import com.aobei.train.model.SysUsersExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUsersMapper extends MbgReadMapper<Long, SysUsers, SysUsers, SysUsersExample>, MbgWriteMapper<Long, SysUsers, SysUsers, SysUsersExample> {
}