package com.aobei.train.mapper;

import com.aobei.train.model.CourseEvaluate;
import com.aobei.train.model.CourseEvaluateExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseEvaluateMapper extends MbgReadMapper<Long, CourseEvaluate, CourseEvaluate, CourseEvaluateExample>, MbgWriteMapper<Long, CourseEvaluate, CourseEvaluate, CourseEvaluateExample> {
}