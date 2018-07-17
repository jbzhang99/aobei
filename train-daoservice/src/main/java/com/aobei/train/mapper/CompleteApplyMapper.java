package com.aobei.train.mapper;

import com.aobei.train.model.CompleteApply;
import com.aobei.train.model.CompleteApplyExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompleteApplyMapper extends MbgReadMapper<Long, CompleteApply, CompleteApply, CompleteApplyExample>, MbgWriteMapper<Long, CompleteApply, CompleteApply, CompleteApplyExample> {
}