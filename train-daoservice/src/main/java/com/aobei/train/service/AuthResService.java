package com.aobei.train.service;

import com.aobei.train.model.AuthResExample;
import com.aobei.train.model.AuthRes;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface AuthResService extends MbgReadService<Integer, AuthRes, AuthRes, AuthResExample>,MbgWriteService<Integer, AuthRes, AuthRes, AuthResExample>,MbgUpsertService<Integer, AuthRes, AuthRes, AuthResExample>{

}