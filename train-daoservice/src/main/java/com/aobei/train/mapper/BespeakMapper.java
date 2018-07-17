package com.aobei.train.mapper;

import com.aobei.train.model.Bespeak;
import com.aobei.train.model.BespeakExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BespeakMapper extends MbgReadMapper<Integer, Bespeak, Bespeak, BespeakExample>, MbgWriteMapper<Integer, Bespeak, Bespeak, BespeakExample> {
}