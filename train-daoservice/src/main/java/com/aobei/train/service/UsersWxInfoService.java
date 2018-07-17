package com.aobei.train.service;

import com.aobei.train.model.UsersWxInfoExample;
import com.aobei.train.model.UsersWxInfo;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface UsersWxInfoService extends MbgReadService<String, UsersWxInfo, UsersWxInfo, UsersWxInfoExample>,MbgWriteService<String, UsersWxInfo, UsersWxInfo, UsersWxInfoExample>,MbgUpsertService<String, UsersWxInfo, UsersWxInfo, UsersWxInfoExample>{

}