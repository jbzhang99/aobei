package com.aobei.train.mapper;

import com.aobei.train.model.AuthRoleRes;
import com.aobei.train.model.AuthRoleResExample;
import com.aobei.train.model.AuthRoleResKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleResMapper extends MbgReadMapper<AuthRoleResKey, AuthRoleRes, AuthRoleRes, AuthRoleResExample>, MbgWriteMapper<AuthRoleResKey, AuthRoleRes, AuthRoleRes, AuthRoleResExample> {
}