package com.aobei.train.service;

import com.aobei.train.model.PartnerServiceitemExample;
import com.aobei.train.model.PartnerServiceitem;
import com.aobei.train.model.PartnerServiceitemKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface PartnerServiceitemService extends MbgReadService<PartnerServiceitemKey, PartnerServiceitem, PartnerServiceitem, PartnerServiceitemExample>,MbgWriteService<PartnerServiceitemKey, PartnerServiceitem, PartnerServiceitem, PartnerServiceitemExample>,MbgUpsertService<PartnerServiceitemKey, PartnerServiceitem, PartnerServiceitem, PartnerServiceitemExample>{

}