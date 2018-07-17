package com.aobei.train.mapper;

import com.aobei.train.model.OssImg;
import com.aobei.train.model.OssImgExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OssImgMapper extends MbgReadMapper<Long, OssImg, OssImg, OssImgExample>, MbgWriteMapper<Long, OssImg, OssImg, OssImgExample> {
}