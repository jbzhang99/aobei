package com.aobei.train.mapper;

import com.aobei.train.model.ProductSole;
import com.aobei.train.model.ProductSoleExample;
import com.aobei.train.model.ProductSoleKey;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductSoleMapper extends MbgReadMapper<ProductSoleKey, ProductSole, ProductSole, ProductSoleExample>, MbgWriteMapper<ProductSoleKey, ProductSole, ProductSole, ProductSoleExample> {
}