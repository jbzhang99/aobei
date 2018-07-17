package com.aobei.train.service;

import com.aobei.train.model.PlanStudentExample;
import com.aobei.train.model.PlanStudent;
import com.aobei.train.model.PlanStudentKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface PlanStudentService extends MbgReadService<PlanStudentKey, PlanStudent, PlanStudent, PlanStudentExample>,MbgWriteService<PlanStudentKey, PlanStudent, PlanStudent, PlanStudentExample>,MbgUpsertService<PlanStudentKey, PlanStudent, PlanStudent, PlanStudentExample>{

}