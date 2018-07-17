package com.aobei.train.mapper;

import com.aobei.train.model.TrainCity;
import com.aobei.train.model.TrainCityExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrainCityMapper extends MbgReadMapper<String, TrainCity, TrainCity, TrainCityExample>, MbgWriteMapper<String, TrainCity, TrainCity, TrainCityExample> {
}