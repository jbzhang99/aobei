package com.aobei.train.service;

import com.aobei.train.model.TrainScheduleStudentExample;
import com.aobei.train.model.TrainScheduleStudent;
import com.aobei.train.model.TrainScheduleStudentKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface TrainScheduleStudentService extends MbgReadService<TrainScheduleStudentKey, TrainScheduleStudent, TrainScheduleStudent, TrainScheduleStudentExample>,MbgWriteService<TrainScheduleStudentKey, TrainScheduleStudent, TrainScheduleStudent, TrainScheduleStudentExample>,MbgUpsertService<TrainScheduleStudentKey, TrainScheduleStudent, TrainScheduleStudent, TrainScheduleStudentExample>{

}