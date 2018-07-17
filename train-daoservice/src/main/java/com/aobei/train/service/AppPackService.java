package com.aobei.train.service;

import com.aobei.train.model.AppPackExample;
import com.aobei.train.model.AppPack;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

import java.util.List;

public interface AppPackService extends MbgReadService<String, AppPack, AppPack, AppPackExample>,MbgWriteService<String, AppPack, AppPack, AppPackExample>,MbgUpsertService<String, AppPack, AppPack, AppPackExample>{

    List<AppPack> xGetPort(String group_name);

    List<AppPack> xGetPortBanner(String group_name);
}