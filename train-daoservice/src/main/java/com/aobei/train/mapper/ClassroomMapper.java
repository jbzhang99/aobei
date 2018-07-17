package com.aobei.train.mapper;

import com.aobei.train.model.Classroom;
import com.aobei.train.model.ClassroomExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClassroomMapper extends MbgReadMapper<Long, Classroom, Classroom, ClassroomExample>, MbgWriteMapper<Long, Classroom, Classroom, ClassroomExample> {
}