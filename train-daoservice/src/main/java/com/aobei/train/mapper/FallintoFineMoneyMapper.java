package com.aobei.train.mapper;

import com.aobei.train.model.FallintoFineMoney;
import com.aobei.train.model.FallintoFineMoneyExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FallintoFineMoneyMapper extends MbgReadMapper<Long, FallintoFineMoney, FallintoFineMoney, FallintoFineMoneyExample>, MbgWriteMapper<Long, FallintoFineMoney, FallintoFineMoney, FallintoFineMoneyExample> {
    String selectMaxBalanceCycle();
}