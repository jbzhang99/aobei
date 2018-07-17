package com.aobei.train.mapper;

import com.aobei.train.model.AppPack;
import com.aobei.train.model.AppPackExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppPackMapper extends MbgReadMapper<String, AppPack, AppPack, AppPackExample>, MbgWriteMapper<String, AppPack, AppPack, AppPackExample> {
}