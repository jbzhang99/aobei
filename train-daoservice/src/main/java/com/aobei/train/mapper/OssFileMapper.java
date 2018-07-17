package com.aobei.train.mapper;

import com.aobei.train.model.OssFile;
import com.aobei.train.model.OssFileExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OssFileMapper extends MbgReadMapper<Long, OssFile, OssFile, OssFileExample>, MbgWriteMapper<Long, OssFile, OssFile, OssFileExample> {
}