package com.aobei.train.service;

import com.aobei.train.model.CancleStrategyExample;
import com.aobei.train.model.CancleStrategy;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import org.springframework.security.core.Authentication;

public interface CancleStrategyService extends MbgReadService<Integer, CancleStrategy, CancleStrategy, CancleStrategyExample>,MbgWriteService<Integer, CancleStrategy, CancleStrategy, CancleStrategyExample>{

    Integer xInsertCancleStratrgy(String list,CancleStrategy strategy,Authentication authentication);

    Integer xUpdateCancleStratrgy(String list,CancleStrategy strategy,Authentication authentication);
}