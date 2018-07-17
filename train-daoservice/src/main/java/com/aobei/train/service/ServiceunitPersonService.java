package com.aobei.train.service;

import com.aobei.train.model.ServiceunitPersonExample;
import com.aobei.train.model.ServiceunitPerson;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface ServiceunitPersonService extends MbgReadService<Long, ServiceunitPerson, ServiceunitPerson, ServiceunitPersonExample>,MbgWriteService<Long, ServiceunitPerson, ServiceunitPerson, ServiceunitPersonExample>,MbgUpsertService<Long, ServiceunitPerson, ServiceunitPerson, ServiceunitPersonExample>{

}