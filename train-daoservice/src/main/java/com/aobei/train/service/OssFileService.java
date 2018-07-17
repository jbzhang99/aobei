package com.aobei.train.service;

import com.aobei.train.model.OssFileExample;
import com.aobei.train.model.OssFile;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface OssFileService extends MbgReadService<Long, OssFile, OssFile, OssFileExample>,MbgWriteService<Long, OssFile, OssFile, OssFileExample>,MbgUpsertService<Long, OssFile, OssFile, OssFileExample>{

}