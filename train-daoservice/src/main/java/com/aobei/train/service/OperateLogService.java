package com.aobei.train.service;

import com.aobei.train.model.OperateLogExample;
import com.aobei.train.model.OperateLog;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface OperateLogService extends MbgReadService<Long, OperateLog, OperateLog, OperateLogExample>,MbgWriteService<Long, OperateLog, OperateLog, OperateLogExample>,MbgUpsertService<Long, OperateLog, OperateLog, OperateLogExample>{

}