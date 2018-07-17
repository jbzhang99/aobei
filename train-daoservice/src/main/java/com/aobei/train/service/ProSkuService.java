package com.aobei.train.service;

import com.aobei.train.model.ProSkuExample;
import com.aobei.train.model.ProSku;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface ProSkuService extends MbgReadService<Long, ProSku, ProSku, ProSkuExample>,MbgWriteService<Long, ProSku, ProSku, ProSkuExample>,MbgUpsertService<Long, ProSku, ProSku, ProSkuExample>{


}