package com.aobei.train.service;

import com.aobei.train.model.CategoryExample;
import com.aobei.train.model.Serviceitem;

import java.util.Map;

import com.aobei.train.model.Category;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CategoryService extends MbgReadService<Long, Category, Category, CategoryExample>,MbgWriteService<Long, Category, Category, CategoryExample>,MbgUpsertService<Long, Category, Category, CategoryExample>{

	String selectMaxCategoryId();

	String selectPidFindMaxSonId(String pid);

	int xAddCategorySun(Map<String, String> logoimg, String name, String descr,String pid);

	int xDelCategory(long cl_id);

	int xDownServiceItem(long se_id);

	int xUpServiceItem(long se_id);

	int xDownCategory(Category category);

	int xUpCategory(Category category);

	int xAddServiceItem(Serviceitem serviceitem);

}