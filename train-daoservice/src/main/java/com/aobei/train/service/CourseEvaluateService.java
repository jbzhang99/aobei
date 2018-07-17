package com.aobei.train.service;

import com.aobei.train.model.CourseEvaluateExample;
import com.aobei.train.model.CourseEvaluate;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CourseEvaluateService extends MbgReadService<Long, CourseEvaluate, CourseEvaluate, CourseEvaluateExample>,MbgWriteService<Long, CourseEvaluate, CourseEvaluate, CourseEvaluateExample>,MbgUpsertService<Long, CourseEvaluate, CourseEvaluate, CourseEvaluateExample>{

}