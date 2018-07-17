package com.aobei.train.mapper;

import com.aobei.train.model.FallintoDeductMoney;
import com.aobei.train.model.FallintoDeductMoneyExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FallintoDeductMoneyMapper extends MbgReadMapper<Long, FallintoDeductMoney, FallintoDeductMoney, FallintoDeductMoneyExample>, MbgWriteMapper<Long, FallintoDeductMoney, FallintoDeductMoney, FallintoDeductMoneyExample> {
    String selectMaxBalanceCycle();
}