package com.aobei.train.service;

import com.aobei.train.model.TeacherExamSubjectExample;
import com.aobei.train.model.TeacherExamSubject;
import com.aobei.train.model.TeacherExamSubjectKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface TeacherExamSubjectService extends MbgReadService<TeacherExamSubjectKey, TeacherExamSubject, TeacherExamSubject, TeacherExamSubjectExample>,MbgWriteService<TeacherExamSubjectKey, TeacherExamSubject, TeacherExamSubject, TeacherExamSubjectExample>,MbgUpsertService<TeacherExamSubjectKey, TeacherExamSubject, TeacherExamSubject, TeacherExamSubjectExample>{

}