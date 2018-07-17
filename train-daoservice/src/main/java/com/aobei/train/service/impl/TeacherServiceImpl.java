package com.aobei.train.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.model.Teacher;
import com.aobei.train.model.TeacherCourse;
import com.aobei.train.model.TeacherCourseExample;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.TeacherMapper;
import com.aobei.train.model.TeacherExample;
import com.aobei.train.service.TeacherCourseService;
import com.aobei.train.service.TeacherService;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Status;

@Service
@Transactional
public class TeacherServiceImpl extends MbgServiceSupport<TeacherMapper, Long, Teacher, Teacher, TeacherExample> implements TeacherService{

	@Autowired
	private TeacherMapper teacherMapper;
	@Autowired
	private TeacherCourseService teacherCourseService;
	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(teacherMapper);
	}



	@Transactional(timeout = 5)
	@Override
	public int xDelete(Long teacher_id) {
		Teacher teacher = new Teacher();
		teacher.setTeacher_id(teacher_id);
		teacher.setDeleted(Status.DeleteStatus.yes.value);
		return teacherMapper.deleteByPrimaryKey(teacher_id);
	}
	@Transactional(timeout = 5)
	@Override
	public int xInsert(StringBuffer str, Long icon_id, Long certification_id,Teacher teacherInfo,Long[] courses) {
		teacherInfo.setTeacher_id(IdGenerator.generateId());
		teacherInfo.setTrain_address(str.toString());
		teacherInfo.setCreate_time(new Date());
		teacherInfo.setIcon(String.valueOf(icon_id));
		teacherInfo.setCertification(String.valueOf(certification_id));
		teacherInfo.setDeleted(Status.DeleteStatus.no.value);
		
		for (Long course1 : courses) {
			TeacherCourse tCourse = new TeacherCourse();
			tCourse.setCourse_id(course1);
			tCourse.setTeacher_id(teacherInfo.getTeacher_id());
			teacherCourseService.insert(tCourse);
		}
		return teacherMapper.insert(teacherInfo);
	}
	
	
	
}