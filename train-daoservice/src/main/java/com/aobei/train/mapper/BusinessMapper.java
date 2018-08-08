package com.aobei.train.mapper;

import com.aobei.train.model.Business;
import com.aobei.train.model.BusinessExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusinessMapper extends MbgReadMapper<String, Business, Business, BusinessExample>, MbgWriteMapper<String, Business, Business, BusinessExample> {
}