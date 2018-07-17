package com.aobei.train.mapper;

import com.aobei.train.model.PlanStudent;
import com.aobei.train.model.PlanStudentExample;
import com.aobei.train.model.PlanStudentKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanStudentMapper extends MbgReadMapper<PlanStudentKey, PlanStudent, PlanStudent, PlanStudentExample>, MbgWriteMapper<PlanStudentKey, PlanStudent, PlanStudent, PlanStudentExample> {
}