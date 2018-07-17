package com.aobei.train.service;

import com.aobei.train.model.InsuranceRegisterExample;
import com.aobei.train.model.InsuranceRegister;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface InsuranceRegisterService extends MbgReadService<Long, InsuranceRegister, InsuranceRegister, InsuranceRegisterExample>,MbgWriteService<Long, InsuranceRegister, InsuranceRegister, InsuranceRegisterExample>,MbgUpsertService<Long, InsuranceRegister, InsuranceRegister, InsuranceRegisterExample>{

}