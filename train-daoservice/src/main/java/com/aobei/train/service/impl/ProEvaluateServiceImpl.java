package com.aobei.train.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.aobei.train.handler.CacheReloadHandler;
import com.aobei.train.mapper.ProEvaluateMapper;
import com.aobei.train.model.ProEvaluate;
import com.aobei.train.model.ProEvaluateExample;
import com.aobei.train.service.ProEvaluateService;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ProEvaluateServiceImpl extends MbgServiceSupport<ProEvaluateMapper, Long, ProEvaluate, ProEvaluate, ProEvaluateExample> implements ProEvaluateService {

    @Autowired
    private ProEvaluateMapper proEvaluateMapper;

    @Autowired
    private ProEvaluateService proEvaluateService;

    @Autowired
    CacheReloadHandler cacheReloadHandler;

    @Autowired
    private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory) {
        super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(proEvaluateMapper);
    }

    @Override
    public Double xAvgScore(ProEvaluateExample example) {
        return proEvaluateMapper.xAvgScore(example);
    }

    @Override
    public int xVerify(Long product_evaluate_id, int ver) {
        HashMap<String, String> map = new HashMap<>();
        if (product_evaluate_id != null) {
            ProEvaluate proEvaluate = new ProEvaluate();
            proEvaluate.setProduct_evaluate_id(product_evaluate_id);
            proEvaluate.setVerify(ver);
            int i = proEvaluateService.updateByPrimaryKeySelective(proEvaluate);
            ProEvaluate pro = proEvaluateService.selectByPrimaryKey(product_evaluate_id);
            cacheReloadHandler.productEvaluateBaseReload(pro.getProduct_id());
            cacheReloadHandler.productEvaluatesListReload(pro.getProduct_id());
            return i;
        } else {
            return 0;
        }
    }

    @Override
    public HashMap xAllVerify(String list_proEva_ids, int ver) {
        HashMap<String, String> map = new HashMap<>();
        List<Long> longs = JSONArray.parseArray(list_proEva_ids, Long.class);
        int i = 0;
        for (Long data : longs) {
            if (data != null) {
                ProEvaluate proEvaluate = new ProEvaluate();
                proEvaluate.setProduct_evaluate_id(data);
                proEvaluate.setVerify(ver);
                int s = proEvaluateService.updateByPrimaryKeySelective(proEvaluate);
                i++;
            }
            ProEvaluate pro = proEvaluateService.selectByPrimaryKey(data);
            cacheReloadHandler.productEvaluateBaseReload(pro.getProduct_id());
            cacheReloadHandler.productEvaluatesListReload(pro.getProduct_id());
        }

        map.put("result", i + "条评价审核成功");
        return map;
    }

}