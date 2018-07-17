package com.aobei.train.service;

import com.aobei.train.model.TrainScheduleExample;
import com.aobei.train.model.TrainSchedule;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface TrainScheduleService extends MbgReadService<Long, TrainSchedule, TrainSchedule, TrainScheduleExample>,MbgWriteService<Long, TrainSchedule, TrainSchedule, TrainScheduleExample>,MbgUpsertService<Long, TrainSchedule, TrainSchedule, TrainScheduleExample>{

}