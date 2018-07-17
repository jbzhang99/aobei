package com.aobei.train.mapper;

import com.aobei.train.model.DataDownload;
import com.aobei.train.model.DataDownloadExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DataDownloadMapper extends MbgReadMapper<Long, DataDownload, DataDownload, DataDownloadExample>, MbgWriteMapper<Long, DataDownload, DataDownload, DataDownloadExample> {
}