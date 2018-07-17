package com.aobei.train.mapper;

import com.aobei.train.model.School;
import com.aobei.train.model.SchoolExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchoolMapper extends MbgReadMapper<Long, School, School, SchoolExample>, MbgWriteMapper<Long, School, School, SchoolExample> {
}