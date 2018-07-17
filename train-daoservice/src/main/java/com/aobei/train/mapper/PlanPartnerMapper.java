package com.aobei.train.mapper;

import com.aobei.train.model.PlanPartner;
import com.aobei.train.model.PlanPartnerExample;
import com.aobei.train.model.PlanPartnerKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanPartnerMapper extends MbgReadMapper<PlanPartnerKey, PlanPartner, PlanPartner, PlanPartnerExample>, MbgWriteMapper<PlanPartnerKey, PlanPartner, PlanPartner, PlanPartnerExample> {
}