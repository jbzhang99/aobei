package com.aobei.train.mapper;

import com.aobei.train.model.FineMoney;
import com.aobei.train.model.FineMoneyExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FineMoneyMapper extends MbgReadMapper<Long, FineMoney, FineMoney, FineMoneyExample>, MbgWriteMapper<Long, FineMoney, FineMoney, FineMoneyExample> {
}