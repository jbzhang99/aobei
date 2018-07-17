package com.aobei.train.mapper;

import com.aobei.train.model.Msgtext;
import com.aobei.train.model.MsgtextExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MsgtextMapper extends MbgReadMapper<String, Msgtext, Msgtext, MsgtextExample>, MbgWriteMapper<String, Msgtext, Msgtext, MsgtextExample> {
}