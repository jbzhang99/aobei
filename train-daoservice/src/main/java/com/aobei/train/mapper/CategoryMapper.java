package com.aobei.train.mapper;

import com.aobei.train.model.Category;
import com.aobei.train.model.CategoryExample;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends MbgReadMapper<Long, Category, Category, CategoryExample>, MbgWriteMapper<Long, Category, Category, CategoryExample> {
	String selectMaxCategoryId();

	String selectPidFindMaxSonId(String pid);
}