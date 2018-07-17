package com.aobei.train.mapper;

import com.aobei.train.model.QuestionPaper;
import com.aobei.train.model.QuestionPaperExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadBLOBsMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteBLOBsMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionPaperMapper extends MbgReadBLOBsMapper<Long, QuestionPaper, QuestionPaper, QuestionPaperExample>, MbgWriteBLOBsMapper<Long, QuestionPaper, QuestionPaper, QuestionPaperExample> {
}