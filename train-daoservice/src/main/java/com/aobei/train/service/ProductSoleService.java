package com.aobei.train.service;

import com.aobei.train.model.ProductSoleExample;
import com.aobei.train.model.ProductSole;
import com.aobei.train.model.ProductSoleKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface ProductSoleService extends MbgReadService<ProductSoleKey, ProductSole, ProductSole, ProductSoleExample>,MbgWriteService<ProductSoleKey, ProductSole, ProductSole, ProductSoleExample>,MbgUpsertService<ProductSoleKey, ProductSole, ProductSole, ProductSoleExample>{

}