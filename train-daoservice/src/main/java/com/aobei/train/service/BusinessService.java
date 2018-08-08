package com.aobei.train.service;

import com.aobei.train.model.BusinessExample;
import com.aobei.train.model.Business;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface BusinessService extends MbgReadService<String, Business, Business, BusinessExample>,MbgWriteService<String, Business, Business, BusinessExample>,MbgUpsertService<String, Business, Business, BusinessExample>{

}