package com.aobei.train.service;

import com.aobei.train.model.ClassroomExample;
import com.aobei.train.model.ClassroomInfo;

import com.aobei.train.model.Classroom;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface ClassroomService extends MbgReadService<Long, Classroom, Classroom, ClassroomExample>,MbgWriteService<Long, Classroom, Classroom, ClassroomExample>,MbgUpsertService<Long, Classroom, Classroom, ClassroomExample>{
	
	/**
	 * 获取教室信息列表
	 */
	Page<ClassroomInfo> xSelectClassroomList(ClassroomExample classroomExample,int pageNo,int pageSize);
	
	/**
	 * 插入新的教室信息
	 */
	int xInsertClassroom(Classroom classroom,String[] support_course_ids);
	
	/**
	 * 获取封装好的教室信息
	 */
	ClassroomInfo getClassroomInfo(Classroom classroom);
	
	/**
	 * 更新教室信息
	 */
	int xUpdateClassroom(Classroom classroom,String[] support_course_ids);
	
	/**
	 * 根据主键删除教室信息
	 */
	int xDeleteClassroom(Long classroom_id);
	
}