package com.aobei.train.service;

import com.aobei.train.model.RefundWxNotifyExample;
import com.aobei.train.model.RefundWxNotify;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface RefundWxNotifyService extends MbgReadService<String, RefundWxNotify, RefundWxNotify, RefundWxNotifyExample>,MbgWriteService<String, RefundWxNotify, RefundWxNotify, RefundWxNotifyExample>,MbgUpsertService<String, RefundWxNotify, RefundWxNotify, RefundWxNotifyExample>{

}