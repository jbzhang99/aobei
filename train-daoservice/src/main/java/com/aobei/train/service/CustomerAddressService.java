package com.aobei.train.service;

import com.aobei.train.model.CustomerAddressExample;
import com.aobei.train.model.CustomerAddress;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CustomerAddressService extends MbgReadService<Long, CustomerAddress, CustomerAddress, CustomerAddressExample>,MbgWriteService<Long, CustomerAddress, CustomerAddress, CustomerAddressExample>,MbgUpsertService<Long, CustomerAddress, CustomerAddress, CustomerAddressExample>{

}