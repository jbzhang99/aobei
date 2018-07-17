package com.aobei.train.service;

import com.aobei.train.model.JdOrderExample;
import com.aobei.train.model.JdOrder;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface JdOrderService extends MbgReadService<String, JdOrder, JdOrder, JdOrderExample>,MbgWriteService<String, JdOrder, JdOrder, JdOrderExample>,MbgUpsertService<String, JdOrder, JdOrder, JdOrderExample>{

}