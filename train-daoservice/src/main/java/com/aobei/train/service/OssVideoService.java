package com.aobei.train.service;

import com.aobei.train.model.OssVideoExample;
import com.aobei.train.model.OssVideo;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface OssVideoService extends MbgReadService<Long, OssVideo, OssVideo, OssVideoExample>,MbgWriteService<Long, OssVideo, OssVideo, OssVideoExample>,MbgUpsertService<Long, OssVideo, OssVideo, OssVideoExample>{

}