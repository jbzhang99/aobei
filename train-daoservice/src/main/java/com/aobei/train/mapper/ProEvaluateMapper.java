package com.aobei.train.mapper;

import com.aobei.train.model.ProEvaluate;
import com.aobei.train.model.ProEvaluateExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProEvaluateMapper extends MbgReadMapper<Long, ProEvaluate, ProEvaluate, ProEvaluateExample>, MbgWriteMapper<Long, ProEvaluate, ProEvaluate, ProEvaluateExample> {
    Double xAvgScore(ProEvaluateExample example);
}