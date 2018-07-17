package com.aobei.train.service;

import com.aobei.train.model.AliPayExample;
import com.aobei.train.model.AliPay;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface AliPayService extends MbgReadService<String, AliPay, AliPay, AliPayExample>,MbgWriteService<String, AliPay, AliPay, AliPayExample>,MbgUpsertService<String, AliPay, AliPay, AliPayExample>{

}