package com.aobei.train.mapper;

import com.aobei.train.model.Course;
import com.aobei.train.model.CourseExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends MbgReadMapper<Long, Course, Course, CourseExample>, MbgWriteMapper<Long, Course, Course, CourseExample> {
}