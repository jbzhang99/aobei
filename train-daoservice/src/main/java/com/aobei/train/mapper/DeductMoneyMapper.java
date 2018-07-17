package com.aobei.train.mapper;

import com.aobei.train.model.DeductMoney;
import com.aobei.train.model.DeductMoneyExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeductMoneyMapper extends MbgReadMapper<Long, DeductMoney, DeductMoney, DeductMoneyExample>, MbgWriteMapper<Long, DeductMoney, DeductMoney, DeductMoneyExample> {
}