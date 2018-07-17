package com.aobei.train.mapper;

import com.aobei.train.model.TeacherCourse;
import com.aobei.train.model.TeacherCourseExample;
import com.aobei.train.model.TeacherCourseKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherCourseMapper extends MbgReadMapper<TeacherCourseKey, TeacherCourse, TeacherCourse, TeacherCourseExample>, MbgWriteMapper<TeacherCourseKey, TeacherCourse, TeacherCourse, TeacherCourseExample> {
}