package com.aobei.train.mapper;

import com.aobei.train.model.PartnerFallinto;
import com.aobei.train.model.PartnerFallintoExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PartnerFallintoMapper extends MbgReadMapper<Long, PartnerFallinto, PartnerFallinto, PartnerFallintoExample>, MbgWriteMapper<Long, PartnerFallinto, PartnerFallinto, PartnerFallintoExample> {
}