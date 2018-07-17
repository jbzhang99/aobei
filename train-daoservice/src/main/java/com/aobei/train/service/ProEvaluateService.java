package com.aobei.train.service;

import com.aobei.train.model.ProEvaluateExample;
import com.aobei.train.model.ProEvaluate;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;
import custom.bean.ProEvaList;

import java.util.HashMap;

public interface ProEvaluateService extends MbgReadService<Long, ProEvaluate, ProEvaluate, ProEvaluateExample>,MbgWriteService<Long, ProEvaluate, ProEvaluate, ProEvaluateExample>,MbgUpsertService<Long, ProEvaluate, ProEvaluate, ProEvaluateExample>{

    Double xAvgScore(ProEvaluateExample example);

    int xVerify(Long product_evaluate_id, int ver);

    HashMap xAllVerify(String list_proEva_ids,int ver);
}