package com.aobei.train.service;

import com.aobei.train.model.AuthRoleUserExample;
import com.aobei.train.model.AuthRoleUser;
import com.aobei.train.model.AuthRoleUserKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface AuthRoleUserService extends MbgReadService<AuthRoleUserKey, AuthRoleUser, AuthRoleUser, AuthRoleUserExample>,MbgWriteService<AuthRoleUserKey, AuthRoleUser, AuthRoleUser, AuthRoleUserExample>,MbgUpsertService<AuthRoleUserKey, AuthRoleUser, AuthRoleUser, AuthRoleUserExample>{

}