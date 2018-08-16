package com.aobei.train.mapper;

import com.aobei.train.model.GroupPurchase;
import com.aobei.train.model.GroupPurchaseExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupPurchaseMapper extends MbgReadMapper<Long, GroupPurchase, GroupPurchase, GroupPurchaseExample>, MbgWriteMapper<Long, GroupPurchase, GroupPurchase, GroupPurchaseExample> {
}