package com.aobei.train.mapper;

import com.aobei.train.model.Partner;
import com.aobei.train.model.PartnerExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PartnerMapper extends MbgReadMapper<Long, Partner, Partner, PartnerExample>, MbgWriteMapper<Long, Partner, Partner, PartnerExample> {
}