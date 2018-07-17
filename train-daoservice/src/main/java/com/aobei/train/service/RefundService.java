package com.aobei.train.service;

import com.aobei.train.model.RefundExample;
import com.aobei.train.model.Refund;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface RefundService extends MbgReadService<Long, Refund, Refund, RefundExample>,MbgWriteService<Long, Refund, Refund, RefundExample>,MbgUpsertService<Long, Refund, Refund, RefundExample>{
	
}