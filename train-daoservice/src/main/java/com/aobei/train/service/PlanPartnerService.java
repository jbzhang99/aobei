package com.aobei.train.service;

import com.aobei.train.model.PlanPartnerExample;
import com.aobei.train.model.PlanPartner;
import com.aobei.train.model.PlanPartnerKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface PlanPartnerService extends MbgReadService<PlanPartnerKey, PlanPartner, PlanPartner, PlanPartnerExample>,MbgWriteService<PlanPartnerKey, PlanPartner, PlanPartner, PlanPartnerExample>,MbgUpsertService<PlanPartnerKey, PlanPartner, PlanPartner, PlanPartnerExample>{

}