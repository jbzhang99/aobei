package com.aobei.train.service;

import com.aobei.train.model.UsersWxExample;
import com.aobei.train.model.UsersWx;
import com.aobei.train.model.UsersWxKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface UsersWxService extends MbgReadService<UsersWxKey, UsersWx, UsersWx, UsersWxExample>,MbgWriteService<UsersWxKey, UsersWx, UsersWx, UsersWxExample>,MbgUpsertService<UsersWxKey, UsersWx, UsersWx, UsersWxExample>{

}