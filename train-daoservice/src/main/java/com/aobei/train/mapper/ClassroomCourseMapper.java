package com.aobei.train.mapper;

import com.aobei.train.model.ClassroomCourse;
import com.aobei.train.model.ClassroomCourseExample;
import com.aobei.train.model.ClassroomCourseKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClassroomCourseMapper extends MbgReadMapper<ClassroomCourseKey, ClassroomCourse, ClassroomCourse, ClassroomCourseExample>, MbgWriteMapper<ClassroomCourseKey, ClassroomCourse, ClassroomCourse, ClassroomCourseExample> {
}