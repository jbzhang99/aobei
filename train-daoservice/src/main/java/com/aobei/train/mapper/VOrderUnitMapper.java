package com.aobei.train.mapper;

import com.aobei.train.model.VOrderUnit;
import com.aobei.train.model.VOrderUnitExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.model.NoKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VOrderUnitMapper extends MbgReadMapper<NoKey, VOrderUnit, VOrderUnit, VOrderUnitExample> {
}