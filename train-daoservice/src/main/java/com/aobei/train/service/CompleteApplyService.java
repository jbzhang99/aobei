package com.aobei.train.service;

import com.aobei.train.model.CompleteApplyExample;
import com.aobei.train.model.CompleteApply;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CompleteApplyService extends MbgReadService<Long, CompleteApply, CompleteApply, CompleteApplyExample>,MbgWriteService<Long, CompleteApply, CompleteApply, CompleteApplyExample>,MbgUpsertService<Long, CompleteApply, CompleteApply, CompleteApplyExample>{

    CompleteApplyExample generateDownloadTaskAndPottingParam(String str, String username, long id);
}