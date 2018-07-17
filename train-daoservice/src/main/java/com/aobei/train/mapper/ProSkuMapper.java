package com.aobei.train.mapper;

import com.aobei.train.model.ProSku;
import com.aobei.train.model.ProSkuExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProSkuMapper extends MbgReadMapper<Long, ProSku, ProSku, ProSkuExample>, MbgWriteMapper<Long, ProSku, ProSku, ProSkuExample> {
}