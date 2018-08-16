package com.aobei.train.service;

import com.aobei.train.model.GroupPurchaseExample;
import com.aobei.train.model.GroupPurchase;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface GroupPurchaseService extends MbgReadService<Long, GroupPurchase, GroupPurchase, GroupPurchaseExample>,MbgWriteService<Long, GroupPurchase, GroupPurchase, GroupPurchaseExample>,MbgUpsertService<Long, GroupPurchase, GroupPurchase, GroupPurchaseExample>{

}