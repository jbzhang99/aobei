package com.aobei.train.mapper;

import com.aobei.train.model.Chapter;
import com.aobei.train.model.ChapterExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChapterMapper extends MbgReadMapper<Long, Chapter, Chapter, ChapterExample>, MbgWriteMapper<Long, Chapter, Chapter, ChapterExample> {
}