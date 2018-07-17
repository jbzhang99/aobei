package com.aobei.train.mapper;

import com.aobei.train.model.BalanceOrder;
import com.aobei.train.model.BalanceOrderExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BalanceOrderMapper extends MbgReadMapper<Long, BalanceOrder, BalanceOrder, BalanceOrderExample>, MbgWriteMapper<Long, BalanceOrder, BalanceOrder, BalanceOrderExample> {
    String selectMaxBalanceCycle();
}