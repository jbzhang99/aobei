package com.aobei.train.service;

import com.aobei.train.model.OssImgExample;

import java.util.Map;

import com.aobei.train.model.OssImg;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface OssImgService extends MbgReadService<Long, OssImg, OssImg, OssImgExample>,MbgWriteService<Long, OssImg, OssImg, OssImgExample>,MbgUpsertService<Long, OssImg, OssImg, OssImgExample>{
	OssImg xInsertOssImg(Map<String, String> params,String effect,String privileges);
}