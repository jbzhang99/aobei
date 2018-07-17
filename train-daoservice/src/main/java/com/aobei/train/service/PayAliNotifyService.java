package com.aobei.train.service;

import com.aobei.train.model.PayAliNotifyExample;
import com.aobei.train.model.PayAliNotify;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface PayAliNotifyService extends MbgReadService<String, PayAliNotify, PayAliNotify, PayAliNotifyExample>,MbgWriteService<String, PayAliNotify, PayAliNotify, PayAliNotifyExample>,MbgUpsertService<String, PayAliNotify, PayAliNotify, PayAliNotifyExample>{

}