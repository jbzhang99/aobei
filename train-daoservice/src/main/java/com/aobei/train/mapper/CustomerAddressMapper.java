package com.aobei.train.mapper;

import com.aobei.train.model.CustomerAddress;
import com.aobei.train.model.CustomerAddressExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerAddressMapper extends MbgReadMapper<Long, CustomerAddress, CustomerAddress, CustomerAddressExample>, MbgWriteMapper<Long, CustomerAddress, CustomerAddress, CustomerAddressExample> {
}