package com.aobei.train.service;

import com.aobei.train.model.FallintoDeductMoneyExample;
import com.aobei.train.model.FallintoDeductMoney;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface FallintoDeductMoneyService extends MbgReadService<Long, FallintoDeductMoney, FallintoDeductMoney, FallintoDeductMoneyExample>,MbgWriteService<Long, FallintoDeductMoney, FallintoDeductMoney, FallintoDeductMoneyExample>,MbgUpsertService<Long, FallintoDeductMoney, FallintoDeductMoney, FallintoDeductMoneyExample>{

    String selectMaxBalanceCycle();
}