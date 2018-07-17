package com.aobei.train.service;

import com.aobei.train.model.FallintoCompensationExample;
import com.aobei.train.model.FallintoCompensation;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface FallintoCompensationService extends MbgReadService<Long, FallintoCompensation, FallintoCompensation, FallintoCompensationExample>,MbgWriteService<Long, FallintoCompensation, FallintoCompensation, FallintoCompensationExample>,MbgUpsertService<Long, FallintoCompensation, FallintoCompensation, FallintoCompensationExample>{

    String selectMaxBalanceCycle();
}