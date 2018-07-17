package com.aobei.train.mapper;

import com.aobei.train.model.BillBatch;
import com.aobei.train.model.BillBatchExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BillBatchMapper extends MbgReadMapper<String, BillBatch, BillBatch, BillBatchExample>, MbgWriteMapper<String, BillBatch, BillBatch, BillBatchExample> {
}