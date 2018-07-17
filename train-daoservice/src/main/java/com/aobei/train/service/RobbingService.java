package com.aobei.train.service;

import com.aobei.train.model.RobbingExample;
import com.aobei.train.model.Robbing;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;

public interface RobbingService extends MbgReadService<Long, Robbing, Robbing, RobbingExample>,MbgWriteService<Long, Robbing, Robbing, RobbingExample>{

    void  xStartRobbing(String pay_order_id);
}