package com.aobei.train.mapper;

import com.aobei.train.model.Insurance;
import com.aobei.train.model.InsuranceExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InsuranceMapper extends MbgReadMapper<Long, Insurance, Insurance, InsuranceExample>, MbgWriteMapper<Long, Insurance, Insurance, InsuranceExample> {
}