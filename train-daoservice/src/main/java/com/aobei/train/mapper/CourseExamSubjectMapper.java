package com.aobei.train.mapper;

import com.aobei.train.model.CourseExamSubject;
import com.aobei.train.model.CourseExamSubjectExample;
import com.aobei.train.model.CourseExamSubjectKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseExamSubjectMapper extends MbgReadMapper<CourseExamSubjectKey, CourseExamSubject, CourseExamSubject, CourseExamSubjectExample>, MbgWriteMapper<CourseExamSubjectKey, CourseExamSubject, CourseExamSubject, CourseExamSubjectExample> {
}