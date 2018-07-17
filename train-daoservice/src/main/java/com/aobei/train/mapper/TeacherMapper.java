package com.aobei.train.mapper;

import com.aobei.train.model.Teacher;
import com.aobei.train.model.TeacherExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherMapper extends MbgReadMapper<Long, Teacher, Teacher, TeacherExample>, MbgWriteMapper<Long, Teacher, Teacher, TeacherExample> {
}