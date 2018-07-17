package com.aobei.train.service;

import com.aobei.train.model.PartnerFallintoExample;
import com.aobei.train.model.PartnerFallinto;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface PartnerFallintoService extends MbgReadService<Long, PartnerFallinto, PartnerFallinto, PartnerFallintoExample>,MbgWriteService<Long, PartnerFallinto, PartnerFallinto, PartnerFallintoExample>,MbgUpsertService<Long, PartnerFallinto, PartnerFallinto, PartnerFallintoExample>{

}