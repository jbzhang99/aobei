package com.aobei.train.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.model.Classroom;
import com.aobei.train.model.ClassroomExample;
import com.aobei.train.model.School;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.SchoolMapper;
import com.aobei.train.model.SchoolExample;
import com.aobei.train.service.ClassroomService;
import com.aobei.train.service.SchoolService;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Status;

@Service
public class SchoolServiceImpl extends MbgServiceSupport<SchoolMapper, Long, School, School, SchoolExample> implements SchoolService{

	@Autowired
	private SchoolMapper schoolMapper;
	
	@Autowired
	private ClassroomService classroomService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(schoolMapper);
	}
	
	/**
	 * 插入新的学校信息
	 */
	@Override
	@Transactional(timeout = 5)
	public int xInsertSchool(School school) {
		school.setSchool_id(IdGenerator.generateId());
		return this.insertSelective(school);
	}
	
	/**
	 * 根据主键更新学校信息
	 */
	@Override
	@Transactional(timeout = 5)
	public int xUpdateByPrimaryKey(School school) {
		return this.updateByPrimaryKeySelective(school);
	}
	
	/**
	 * 根据主键删除学校信息
	 */
	@Override
	@Transactional(timeout = 5)
	public int xDeleteByPrimaryKey(Long school_id) {
		//删除学校信息前先删除其下所属的教室信息
		ClassroomExample example = new ClassroomExample();
		example.or().andSchool_idEqualTo(school_id);
		List<Classroom> classroom_list = classroomService.selectByExample(example);
		if(classroom_list.size()>0) {
			Classroom classroom = new Classroom();
			classroom.setDeleted(Status.DeleteStatus.yes.value);
			classroomService.updateByExampleSelective(classroom, example);
		}
		// 根据id设置学校信息为已删除状态
		School school = new School();
		school.setDeleted(Status.DeleteStatus.yes.value);
		school.setSchool_id(school_id);
		return xUpdateByPrimaryKey(school);
	}
}