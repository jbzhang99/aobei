package com.aobei.train.mapper;

import com.aobei.train.model.Customer;
import com.aobei.train.model.CustomerExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends MbgReadMapper<Long, Customer, Customer, CustomerExample>, MbgWriteMapper<Long, Customer, Customer, CustomerExample> {
}