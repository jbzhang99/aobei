package com.aobei.train.mapper;

import com.aobei.train.model.TrainScheduleStudent;
import com.aobei.train.model.TrainScheduleStudentExample;
import com.aobei.train.model.TrainScheduleStudentKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrainScheduleStudentMapper extends MbgReadMapper<TrainScheduleStudentKey, TrainScheduleStudent, TrainScheduleStudent, TrainScheduleStudentExample>, MbgWriteMapper<TrainScheduleStudentKey, TrainScheduleStudent, TrainScheduleStudent, TrainScheduleStudentExample> {
}