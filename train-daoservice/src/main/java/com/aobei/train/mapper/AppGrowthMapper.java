package com.aobei.train.mapper;

import com.aobei.train.model.AppGrowth;
import com.aobei.train.model.AppGrowthExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppGrowthMapper extends MbgReadMapper<Integer, AppGrowth, AppGrowth, AppGrowthExample>, MbgWriteMapper<Integer, AppGrowth, AppGrowth, AppGrowthExample> {
}