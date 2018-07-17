package com.aobei.train.service;

import com.aobei.train.model.ServiceUnitExample;
import com.aobei.train.model.ServiceUnit;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface ServiceUnitService extends MbgReadService<Long, ServiceUnit, ServiceUnit, ServiceUnitExample>,MbgWriteService<Long, ServiceUnit, ServiceUnit, ServiceUnitExample>,MbgUpsertService<Long, ServiceUnit, ServiceUnit, ServiceUnitExample>{

}