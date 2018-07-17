package com.aobei.train.service;

import com.aobei.train.model.FineMoneyExample;
import com.aobei.train.model.FineMoney;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface FineMoneyService extends MbgReadService<Long, FineMoney, FineMoney, FineMoneyExample>,MbgWriteService<Long, FineMoney, FineMoney, FineMoneyExample>,MbgUpsertService<Long, FineMoney, FineMoney, FineMoneyExample>{

    FineMoneyExample generateDownloadTaskAndPottingParam(String str, String username, long id);
}