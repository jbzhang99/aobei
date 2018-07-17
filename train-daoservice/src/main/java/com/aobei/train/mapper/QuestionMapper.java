package com.aobei.train.mapper;

import com.aobei.train.model.Question;
import com.aobei.train.model.QuestionExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper extends MbgReadMapper<Long, Question, Question, QuestionExample>, MbgWriteMapper<Long, Question, Question, QuestionExample> {
}