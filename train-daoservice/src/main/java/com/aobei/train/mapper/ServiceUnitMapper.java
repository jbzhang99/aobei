package com.aobei.train.mapper;

import com.aobei.train.model.ServiceUnit;
import com.aobei.train.model.ServiceUnitExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ServiceUnitMapper extends MbgReadMapper<Long, ServiceUnit, ServiceUnit, ServiceUnitExample>, MbgWriteMapper<Long, ServiceUnit, ServiceUnit, ServiceUnitExample> {
}