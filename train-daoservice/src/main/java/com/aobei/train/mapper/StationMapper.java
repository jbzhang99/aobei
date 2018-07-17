package com.aobei.train.mapper;

import com.aobei.train.model.Station;
import com.aobei.train.model.StationExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StationMapper extends MbgReadMapper<Long, Station, Station, StationExample>, MbgWriteMapper<Long, Station, Station, StationExample> {
}