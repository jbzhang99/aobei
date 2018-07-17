package com.aobei.train.service;

import com.aobei.train.model.SchoolExample;
import com.aobei.train.model.School;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface SchoolService extends MbgReadService<Long, School, School, SchoolExample>,MbgWriteService<Long, School, School, SchoolExample>,MbgUpsertService<Long, School, School, SchoolExample>{
	
	/**
	 * 插入新的学校信息
	 */
	int xInsertSchool(School school);
	
	/**
	 * 根据主键更新学校信息
	 */
	int xUpdateByPrimaryKey(School school);
	
	/**
	 * 根据主键删除学校信息
	 */
	int xDeleteByPrimaryKey(Long school_id);
}