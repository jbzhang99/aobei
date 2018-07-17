package com.aobei.train.mapper;

import com.aobei.train.model.TeacherExamSubject;
import com.aobei.train.model.TeacherExamSubjectExample;
import com.aobei.train.model.TeacherExamSubjectKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherExamSubjectMapper extends MbgReadMapper<TeacherExamSubjectKey, TeacherExamSubject, TeacherExamSubject, TeacherExamSubjectExample>, MbgWriteMapper<TeacherExamSubjectKey, TeacherExamSubject, TeacherExamSubject, TeacherExamSubjectExample> {
}