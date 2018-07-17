package com.aobei.train.service;

import com.aobei.train.model.CouponEnvExample;
import com.aobei.train.model.CouponEnv;
import com.aobei.train.model.Customer;
import com.aobei.train.model.ProSku;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;
import custom.bean.DiscountData;

public interface CouponEnvService extends MbgReadService<Long, CouponEnv, CouponEnv, CouponEnvExample>,MbgWriteService<Long, CouponEnv, CouponEnv, CouponEnvExample>,MbgUpsertService<Long, CouponEnv, CouponEnv, CouponEnvExample>{

    DiscountData xDiscountPolicy(Customer customer, ProSku proSku,int num);
}