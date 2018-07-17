package com.aobei.train.service;

import com.aobei.train.model.BillExample;
import com.aobei.train.model.Bill;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface BillService extends MbgReadService<Long, Bill, Bill, BillExample>,MbgWriteService<Long, Bill, Bill, BillExample>,MbgUpsertService<Long, Bill, Bill, BillExample>{

    int xInsert(Bill bill);

    int xUpdate(Bill bill);
}