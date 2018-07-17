package com.aobei.train.mapper;

import com.aobei.train.model.CourseServiceitem;
import com.aobei.train.model.CourseServiceitemExample;
import com.aobei.train.model.CourseServiceitemKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseServiceitemMapper extends MbgReadMapper<CourseServiceitemKey, CourseServiceitem, CourseServiceitem, CourseServiceitemExample>, MbgWriteMapper<CourseServiceitemKey, CourseServiceitem, CourseServiceitem, CourseServiceitemExample> {
}