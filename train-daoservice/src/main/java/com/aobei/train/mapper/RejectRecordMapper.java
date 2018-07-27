package com.aobei.train.mapper;

import com.aobei.train.model.RejectRecord;
import com.aobei.train.model.RejectRecordExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RejectRecordMapper extends MbgReadMapper<Long, RejectRecord, RejectRecord, RejectRecordExample>, MbgWriteMapper<Long, RejectRecord, RejectRecord, RejectRecordExample> {
}