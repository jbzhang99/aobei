package com.aobei.train.mapper;

import com.aobei.train.model.PartnerServiceitem;
import com.aobei.train.model.PartnerServiceitemExample;
import com.aobei.train.model.PartnerServiceitemKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PartnerServiceitemMapper extends MbgReadMapper<PartnerServiceitemKey, PartnerServiceitem, PartnerServiceitem, PartnerServiceitemExample>, MbgWriteMapper<PartnerServiceitemKey, PartnerServiceitem, PartnerServiceitem, PartnerServiceitemExample> {
}