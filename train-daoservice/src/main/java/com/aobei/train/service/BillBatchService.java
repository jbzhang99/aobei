package com.aobei.train.service;

import com.aobei.train.model.BillBatchExample;
import com.aobei.train.model.BillBatch;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface BillBatchService extends MbgReadService<String, BillBatch, BillBatch, BillBatchExample>,MbgWriteService<String, BillBatch, BillBatch, BillBatchExample>,MbgUpsertService<String, BillBatch, BillBatch, BillBatchExample>{

}