package com.aobei.train.mapper;

import com.aobei.train.model.ExamApply;
import com.aobei.train.model.ExamApplyExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExamApplyMapper extends MbgReadMapper<Long, ExamApply, ExamApply, ExamApplyExample>, MbgWriteMapper<Long, ExamApply, ExamApply, ExamApplyExample> {
}