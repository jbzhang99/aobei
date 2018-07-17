package com.aobei.train.service;

import com.aobei.train.model.MetadataExample;
import com.aobei.train.model.Metadata;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface MetadataService extends MbgReadService<String, Metadata, Metadata, MetadataExample>,MbgWriteService<String, Metadata, Metadata, MetadataExample>,MbgUpsertService<String, Metadata, Metadata, MetadataExample>{

}