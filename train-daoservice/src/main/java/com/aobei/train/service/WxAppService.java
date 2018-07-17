package com.aobei.train.service;

import com.aobei.train.model.WxAppExample;
import com.aobei.train.model.WxApp;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface WxAppService extends MbgReadService<String, WxApp, WxApp, WxAppExample>,MbgWriteService<String, WxApp, WxApp, WxAppExample>,MbgUpsertService<String, WxApp, WxApp, WxAppExample>{

}