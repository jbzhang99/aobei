package com.aobei.train.service;

import com.aobei.train.model.AuthRoleResExample;
import com.aobei.train.model.AuthRoleRes;
import com.aobei.train.model.AuthRoleResKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface AuthRoleResService extends MbgReadService<AuthRoleResKey, AuthRoleRes, AuthRoleRes, AuthRoleResExample>,MbgWriteService<AuthRoleResKey, AuthRoleRes, AuthRoleRes, AuthRoleResExample>,MbgUpsertService<AuthRoleResKey, AuthRoleRes, AuthRoleRes, AuthRoleResExample>{

}