package com.aobei.train.service;

import com.aobei.train.model.CompensationExample;
import com.aobei.train.model.Compensation;
import com.aobei.train.model.CompensationInfo;
import com.aobei.train.model.VOrderUnitExample;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

import java.util.Map;

public interface CompensationService extends MbgReadService<Long, Compensation, Compensation, CompensationExample>,MbgWriteService<Long, Compensation, Compensation, CompensationExample>,MbgUpsertService<Long, Compensation, Compensation, CompensationExample>{

    /**
     * 从订单和服务单视图入手，组装CompensationInfo
     * @param compensationExample
     * @param page
     * @param size
     * @return
     */
    Page<CompensationInfo> compensationInfoList(CompensationExample compensationExample, VOrderUnitExample example, Map<String,String> params, int page, int size);

    /**
     * 赔偿单确认
     * @param compensation
     * @return
     */
    int xUpdateConfirm(Compensation compensation);

    /**
     * 赔偿单驳回
     * @param compensation
     * @return
     */
    int xUpdateReject(Compensation compensation);
}