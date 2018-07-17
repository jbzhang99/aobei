package com.aobei.train.service;

import com.aobei.train.model.FallintoRefundExample;
import com.aobei.train.model.FallintoRefund;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface FallintoRefundService extends MbgReadService<Long, FallintoRefund, FallintoRefund, FallintoRefundExample>,MbgWriteService<Long, FallintoRefund, FallintoRefund, FallintoRefundExample>,MbgUpsertService<Long, FallintoRefund, FallintoRefund, FallintoRefundExample>{

    String selectMaxBalanceCycle();
}