package com.aobei.train.service;

import com.aobei.train.model.VideoContentExample;
import com.aobei.train.model.VideoContent;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface VideoContentService extends MbgReadService<Long, VideoContent, VideoContent, VideoContentExample>,MbgWriteService<Long, VideoContent, VideoContent, VideoContentExample>,MbgUpsertService<Long, VideoContent, VideoContent, VideoContentExample>{

}