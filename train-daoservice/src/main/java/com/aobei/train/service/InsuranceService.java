package com.aobei.train.service;

import com.aobei.train.model.InsuranceExample;
import com.aobei.train.model.Insurance;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

import java.util.List;

public interface InsuranceService extends MbgReadService<Long, Insurance, Insurance, InsuranceExample>,MbgWriteService<Long, Insurance, Insurance, InsuranceExample>,MbgUpsertService<Long, Insurance, Insurance, InsuranceExample>{

    int xAddInsurance(Insurance insurance, String startDatetime, String endDatetime);

    int xEditInsurance(Insurance insurance, String startDatetime, String endDatetime);

}