package com.aobei.train.service;

import com.aobei.train.model.FallintoExample;
import com.aobei.train.model.Fallinto;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface FallintoService extends MbgReadService<Long, Fallinto, Fallinto, FallintoExample>,MbgWriteService<Long, Fallinto, Fallinto, FallintoExample>,MbgUpsertService<Long, Fallinto, Fallinto, FallintoExample>{

}