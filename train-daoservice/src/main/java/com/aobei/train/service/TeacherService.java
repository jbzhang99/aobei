package com.aobei.train.service;

import com.aobei.train.model.TeacherExample;

import java.util.List;

import com.aobei.train.model.Teacher;
import com.aobei.train.model.TeacherCourse;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface TeacherService extends MbgReadService<Long, Teacher, Teacher, TeacherExample>,MbgWriteService<Long, Teacher, Teacher, TeacherExample>,MbgUpsertService<Long, Teacher, Teacher, TeacherExample>{

	/**
	 * 删除方法（修改为删除状态）
	 * @param teacher_id
	 * @return
	 */
	int xDelete(Long teacher_id);
	/**
	 * 添加教师
	 * @param str 省市区地址
	 * @param icon_id 头像id
	 * @param certification 资格证图片id
	 * @param teacherInfo
	 * @param courses 课程集合
	 * @return
	 */
	int xInsert(StringBuffer str,Long icon_id,Long certification,Teacher teacherInfo,Long[] courses);
}