package com.aobei.train.mapper;

import com.aobei.train.model.CmsUrl;
import com.aobei.train.model.CmsUrlExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CmsUrlMapper extends MbgReadMapper<Long, CmsUrl, CmsUrl, CmsUrlExample>, MbgWriteMapper<Long, CmsUrl, CmsUrl, CmsUrlExample> {
}