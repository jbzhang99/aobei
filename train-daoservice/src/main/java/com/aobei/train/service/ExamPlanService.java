package com.aobei.train.service;

import com.aobei.train.model.ExamPlanExample;
import com.aobei.train.model.ExamPlan;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface ExamPlanService extends MbgReadService<Long, ExamPlan, ExamPlan, ExamPlanExample>,MbgWriteService<Long, ExamPlan, ExamPlan, ExamPlanExample>,MbgUpsertService<Long, ExamPlan, ExamPlan, ExamPlanExample>{

}