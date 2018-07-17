package com.aobei.train.service;

import com.aobei.train.model.CourseExamSubjectExample;
import com.aobei.train.model.CourseExamSubject;
import com.aobei.train.model.CourseExamSubjectKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CourseExamSubjectService extends MbgReadService<CourseExamSubjectKey, CourseExamSubject, CourseExamSubject, CourseExamSubjectExample>,MbgWriteService<CourseExamSubjectKey, CourseExamSubject, CourseExamSubject, CourseExamSubjectExample>,MbgUpsertService<CourseExamSubjectKey, CourseExamSubject, CourseExamSubject, CourseExamSubjectExample>{

}