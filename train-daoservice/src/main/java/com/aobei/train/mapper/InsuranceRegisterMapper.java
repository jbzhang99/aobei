package com.aobei.train.mapper;

import com.aobei.train.model.InsuranceRegister;
import com.aobei.train.model.InsuranceRegisterExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InsuranceRegisterMapper extends MbgReadMapper<Long, InsuranceRegister, InsuranceRegister, InsuranceRegisterExample>, MbgWriteMapper<Long, InsuranceRegister, InsuranceRegister, InsuranceRegisterExample> {
}