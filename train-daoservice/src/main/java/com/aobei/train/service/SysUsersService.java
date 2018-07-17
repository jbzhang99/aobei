package com.aobei.train.service;

import com.aobei.train.model.SysUsersExample;
import com.aobei.train.model.SysUsers;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface SysUsersService extends MbgReadService<Long, SysUsers, SysUsers, SysUsersExample>,MbgWriteService<Long, SysUsers, SysUsers, SysUsersExample>,MbgUpsertService<Long, SysUsers, SysUsers, SysUsersExample>{

}