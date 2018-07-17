package com.aobei.train.service;

import com.aobei.train.model.ServiceitemExample;
import com.aobei.train.model.Serviceitem;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface ServiceitemService extends MbgReadService<Long, Serviceitem, Serviceitem, ServiceitemExample>,MbgWriteService<Long, Serviceitem, Serviceitem, ServiceitemExample>,MbgUpsertService<Long, Serviceitem, Serviceitem, ServiceitemExample>{

}