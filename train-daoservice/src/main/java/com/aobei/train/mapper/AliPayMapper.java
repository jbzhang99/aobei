package com.aobei.train.mapper;

import com.aobei.train.model.AliPay;
import com.aobei.train.model.AliPayExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AliPayMapper extends MbgReadMapper<String, AliPay, AliPay, AliPayExample>, MbgWriteMapper<String, AliPay, AliPay, AliPayExample> {
}