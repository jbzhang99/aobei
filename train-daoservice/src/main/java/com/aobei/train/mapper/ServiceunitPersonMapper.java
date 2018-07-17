package com.aobei.train.mapper;

import com.aobei.train.model.ServiceunitPerson;
import com.aobei.train.model.ServiceunitPersonExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ServiceunitPersonMapper extends MbgReadMapper<Long, ServiceunitPerson, ServiceunitPerson, ServiceunitPersonExample>, MbgWriteMapper<Long, ServiceunitPerson, ServiceunitPerson, ServiceunitPersonExample> {
}