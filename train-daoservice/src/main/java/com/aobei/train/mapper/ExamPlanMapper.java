package com.aobei.train.mapper;

import com.aobei.train.model.ExamPlan;
import com.aobei.train.model.ExamPlanExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExamPlanMapper extends MbgReadMapper<Long, ExamPlan, ExamPlan, ExamPlanExample>, MbgWriteMapper<Long, ExamPlan, ExamPlan, ExamPlanExample> {
}