package com.aobei.train.mapper;

import com.aobei.train.model.Product;
import com.aobei.train.model.ProductExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadBLOBsMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteBLOBsMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends MbgReadBLOBsMapper<Long, Product, Product, ProductExample>, MbgWriteBLOBsMapper<Long, Product, Product, ProductExample> {
}