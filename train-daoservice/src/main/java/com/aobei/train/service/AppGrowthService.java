package com.aobei.train.service;

import com.aobei.train.model.AppGrowthExample;
import com.aobei.train.model.AppGrowth;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;

public interface AppGrowthService extends MbgReadService<Integer, AppGrowth, AppGrowth, AppGrowthExample>,MbgWriteService<Integer, AppGrowth, AppGrowth, AppGrowthExample>{

}