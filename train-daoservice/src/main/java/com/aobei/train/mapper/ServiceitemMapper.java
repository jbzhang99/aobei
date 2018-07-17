package com.aobei.train.mapper;

import com.aobei.train.model.Serviceitem;
import com.aobei.train.model.ServiceitemExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ServiceitemMapper extends MbgReadMapper<Long, Serviceitem, Serviceitem, ServiceitemExample>, MbgWriteMapper<Long, Serviceitem, Serviceitem, ServiceitemExample> {
}