package com.aobei.train.mapper;

import com.aobei.train.model.OssVideo;
import com.aobei.train.model.OssVideoExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OssVideoMapper extends MbgReadMapper<Long, OssVideo, OssVideo, OssVideoExample>, MbgWriteMapper<Long, OssVideo, OssVideo, OssVideoExample> {
}