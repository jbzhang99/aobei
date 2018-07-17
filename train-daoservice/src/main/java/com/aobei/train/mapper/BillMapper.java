package com.aobei.train.mapper;

import com.aobei.train.model.Bill;
import com.aobei.train.model.BillExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BillMapper extends MbgReadMapper<Long, Bill, Bill, BillExample>, MbgWriteMapper<Long, Bill, Bill, BillExample> {
}