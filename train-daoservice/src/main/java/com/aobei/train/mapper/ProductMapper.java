package com.aobei.train.mapper;

import com.aobei.train.model.Product;
import com.aobei.train.model.ProductExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadBLOBsMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteBLOBsMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper extends MbgReadBLOBsMapper<Long, Product, Product, ProductExample>, MbgWriteBLOBsMapper<Long, Product, Product, ProductExample> {

    /**
     * 获取单品SKU 的商品id
     * @return
     */
    List<Long> xSelectSingleSkuProduct();
}