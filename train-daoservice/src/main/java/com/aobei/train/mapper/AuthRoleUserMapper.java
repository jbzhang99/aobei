package com.aobei.train.mapper;

import com.aobei.train.model.AuthRoleUser;
import com.aobei.train.model.AuthRoleUserExample;
import com.aobei.train.model.AuthRoleUserKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleUserMapper extends MbgReadMapper<AuthRoleUserKey, AuthRoleUser, AuthRoleUser, AuthRoleUserExample>, MbgWriteMapper<AuthRoleUserKey, AuthRoleUser, AuthRoleUser, AuthRoleUserExample> {
}