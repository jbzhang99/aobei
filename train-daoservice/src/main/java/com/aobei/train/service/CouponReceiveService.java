package com.aobei.train.service;

import com.aobei.train.model.CouponReceiveExample;
import com.aobei.train.model.CouponReceive;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CouponReceiveService extends MbgReadService<Long, CouponReceive, CouponReceive, CouponReceiveExample>,MbgWriteService<Long, CouponReceive, CouponReceive, CouponReceiveExample>,MbgUpsertService<Long, CouponReceive, CouponReceive, CouponReceiveExample>{

}