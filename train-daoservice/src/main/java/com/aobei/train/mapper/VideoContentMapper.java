package com.aobei.train.mapper;

import com.aobei.train.model.VideoContent;
import com.aobei.train.model.VideoContentExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VideoContentMapper extends MbgReadMapper<Long, VideoContent, VideoContent, VideoContentExample>, MbgWriteMapper<Long, VideoContent, VideoContent, VideoContentExample> {
}