package com.aobei.train.service;

import com.aobei.train.model.CouponAndCouponEnvExample;
import com.aobei.train.model.CouponAndCouponEnv;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CouponAndCouponEnvService extends MbgReadService<Long, CouponAndCouponEnv, CouponAndCouponEnv, CouponAndCouponEnvExample>,MbgWriteService<Long, CouponAndCouponEnv, CouponAndCouponEnv, CouponAndCouponEnvExample>,MbgUpsertService<Long, CouponAndCouponEnv, CouponAndCouponEnv, CouponAndCouponEnvExample>{

}