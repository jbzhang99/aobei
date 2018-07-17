package com.aobei.train.service;

import com.aobei.train.model.FallintoFineMoneyExample;
import com.aobei.train.model.FallintoFineMoney;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface FallintoFineMoneyService extends MbgReadService<Long, FallintoFineMoney, FallintoFineMoney, FallintoFineMoneyExample>,MbgWriteService<Long, FallintoFineMoney, FallintoFineMoney, FallintoFineMoneyExample>,MbgUpsertService<Long, FallintoFineMoney, FallintoFineMoney, FallintoFineMoneyExample>{

    String selectMaxBalanceCycle();
}