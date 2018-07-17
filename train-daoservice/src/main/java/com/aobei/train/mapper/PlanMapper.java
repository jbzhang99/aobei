package com.aobei.train.mapper;

import com.aobei.train.model.Plan;
import com.aobei.train.model.PlanExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanMapper extends MbgReadMapper<Long, Plan, Plan, PlanExample>, MbgWriteMapper<Long, Plan, Plan, PlanExample> {
}