package com.aobei.train.service;

import com.aobei.train.model.RejectRecordExample;
import com.aobei.train.model.RejectRecord;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface RejectRecordService extends MbgReadService<Long, RejectRecord, RejectRecord, RejectRecordExample>,MbgWriteService<Long, RejectRecord, RejectRecord, RejectRecordExample>,MbgUpsertService<Long, RejectRecord, RejectRecord, RejectRecordExample>{

}