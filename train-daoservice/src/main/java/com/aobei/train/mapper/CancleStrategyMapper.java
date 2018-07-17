package com.aobei.train.mapper;

import com.aobei.train.model.CancleStrategy;
import com.aobei.train.model.CancleStrategyExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CancleStrategyMapper extends MbgReadMapper<Integer, CancleStrategy, CancleStrategy, CancleStrategyExample>, MbgWriteMapper<Integer, CancleStrategy, CancleStrategy, CancleStrategyExample> {
}