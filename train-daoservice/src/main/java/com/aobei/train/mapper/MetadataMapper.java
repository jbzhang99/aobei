package com.aobei.train.mapper;

import com.aobei.train.model.Metadata;
import com.aobei.train.model.MetadataExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MetadataMapper extends MbgReadMapper<String, Metadata, Metadata, MetadataExample>, MbgWriteMapper<String, Metadata, Metadata, MetadataExample> {
}