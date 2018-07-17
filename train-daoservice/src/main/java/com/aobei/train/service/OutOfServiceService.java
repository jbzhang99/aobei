package com.aobei.train.service;

import com.aobei.train.model.OutOfServiceExample;
import com.aobei.train.model.OutOfService;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;

public interface OutOfServiceService extends MbgReadService<Long, OutOfService, OutOfService, OutOfServiceExample>,MbgWriteService<Long, OutOfService, OutOfService, OutOfServiceExample>{

}