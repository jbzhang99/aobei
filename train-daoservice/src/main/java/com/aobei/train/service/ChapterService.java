package com.aobei.train.service;

import com.aobei.train.model.ChapterExample;
import com.aobei.train.model.Chapter;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface ChapterService extends MbgReadService<Long, Chapter, Chapter, ChapterExample>,MbgWriteService<Long, Chapter, Chapter, ChapterExample>,MbgUpsertService<Long, Chapter, Chapter, ChapterExample>{

}