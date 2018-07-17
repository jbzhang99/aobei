package com.aobei.train.service;

import com.aobei.train.model.DataDownloadExample;
import com.aobei.train.model.DataDownload;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface DataDownloadService extends MbgReadService<Long, DataDownload, DataDownload, DataDownloadExample>,MbgWriteService<Long, DataDownload, DataDownload, DataDownloadExample>,MbgUpsertService<Long, DataDownload, DataDownload, DataDownloadExample>{

}