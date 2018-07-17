package com.aobei.train.service;

import com.aobei.train.model.DeductMoneyExample;
import com.aobei.train.model.DeductMoney;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface DeductMoneyService extends MbgReadService<Long, DeductMoney, DeductMoney, DeductMoneyExample>,MbgWriteService<Long, DeductMoney, DeductMoney, DeductMoneyExample>,MbgUpsertService<Long, DeductMoney, DeductMoney, DeductMoneyExample>{

    DeductMoneyExample generateDownloadTaskAndPottingParam(String str,String username,long id);
}