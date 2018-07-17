package com.aobei.train.service;

import com.aobei.train.model.WxMchExample;
import com.aobei.train.model.WxMch;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface WxMchService extends MbgReadService<String, WxMch, WxMch, WxMchExample>,MbgWriteService<String, WxMch, WxMch, WxMchExample>,MbgUpsertService<String, WxMch, WxMch, WxMchExample>{

}