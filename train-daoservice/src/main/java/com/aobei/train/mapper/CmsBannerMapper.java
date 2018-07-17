package com.aobei.train.mapper;

import com.aobei.train.model.CmsBanner;
import com.aobei.train.model.CmsBannerExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CmsBannerMapper extends MbgReadMapper<Long, CmsBanner, CmsBanner, CmsBannerExample>, MbgWriteMapper<Long, CmsBanner, CmsBanner, CmsBannerExample> {
}