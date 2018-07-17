package com.aobei.train.mapper;

import com.aobei.train.model.StudentServiceitem;
import com.aobei.train.model.StudentServiceitemExample;
import com.aobei.train.model.StudentServiceitemKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentServiceitemMapper extends MbgReadMapper<StudentServiceitemKey, StudentServiceitem, StudentServiceitem, StudentServiceitemExample>, MbgWriteMapper<StudentServiceitemKey, StudentServiceitem, StudentServiceitem, StudentServiceitemExample> {
}