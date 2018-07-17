package com.aobei.train.mapper;

import com.aobei.train.model.TrainSchedule;
import com.aobei.train.model.TrainScheduleExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrainScheduleMapper extends MbgReadMapper<Long, TrainSchedule, TrainSchedule, TrainScheduleExample>, MbgWriteMapper<Long, TrainSchedule, TrainSchedule, TrainScheduleExample> {
}