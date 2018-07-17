package com.aobei.train.mapper;

import com.aobei.train.model.ExamSubject;
import com.aobei.train.model.ExamSubjectExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExamSubjectMapper extends MbgReadMapper<Long, ExamSubject, ExamSubject, ExamSubjectExample>, MbgWriteMapper<Long, ExamSubject, ExamSubject, ExamSubjectExample> {
}