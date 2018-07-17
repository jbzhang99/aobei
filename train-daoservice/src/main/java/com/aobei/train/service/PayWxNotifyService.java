package com.aobei.train.service;

import com.aobei.train.model.PayWxNotifyExample;
import com.aobei.train.model.PayWxNotify;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface PayWxNotifyService extends MbgReadService<String, PayWxNotify, PayWxNotify, PayWxNotifyExample>,MbgWriteService<String, PayWxNotify, PayWxNotify, PayWxNotifyExample>,MbgUpsertService<String, PayWxNotify, PayWxNotify, PayWxNotifyExample>{

}