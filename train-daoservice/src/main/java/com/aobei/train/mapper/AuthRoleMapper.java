package com.aobei.train.mapper;

import com.aobei.train.model.AuthRole;
import com.aobei.train.model.AuthRoleExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleMapper extends MbgReadMapper<Long, AuthRole, AuthRole, AuthRoleExample>, MbgWriteMapper<Long, AuthRole, AuthRole, AuthRoleExample> {
}