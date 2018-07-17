package com.aobei.train.mapper;

import com.aobei.train.model.Student;
import com.aobei.train.model.StudentExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends MbgReadMapper<Long, Student, Student, StudentExample>, MbgWriteMapper<Long, Student, Student, StudentExample> {
}