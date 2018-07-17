package com.aobei.train.service;

import com.aobei.train.model.ExamSubjectExample;
import com.aobei.train.model.ExamSubject;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface ExamSubjectService extends MbgReadService<Long, ExamSubject, ExamSubject, ExamSubjectExample>,MbgWriteService<Long, ExamSubject, ExamSubject, ExamSubjectExample>,MbgUpsertService<Long, ExamSubject, ExamSubject, ExamSubjectExample>{

}