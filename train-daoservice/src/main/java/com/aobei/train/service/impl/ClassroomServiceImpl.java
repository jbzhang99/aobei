package com.aobei.train.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.model.Classroom;
import com.aobei.train.model.ClassroomCourse;
import com.aobei.train.model.ClassroomCourseExample;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.ClassroomMapper;
import com.aobei.train.model.ClassroomExample;
import com.aobei.train.model.ClassroomInfo;
import com.aobei.train.model.Course;
import com.aobei.train.model.CourseExample;
import com.aobei.train.model.SchoolExample;
import com.aobei.train.service.ClassroomCourseService;
import com.aobei.train.service.ClassroomService;
import com.aobei.train.service.CourseService;
import com.aobei.train.service.SchoolService;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Status;

@Service
public class ClassroomServiceImpl extends MbgServiceSupport<ClassroomMapper, Long, Classroom, Classroom, ClassroomExample> implements ClassroomService{

	@Autowired
	private ClassroomMapper classroomMapper;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private ClassroomCourseService classroomCourseService;
	
	@Autowired
	private CourseService courseService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(classroomMapper);
	}
	
	/**
	 * 公共的获取课程名集合的方法
	 * @param list
	 * @return
	 */
	public List<String> getCourses(List<ClassroomCourse> list){
		return list.stream().map(classroomCourse ->{
			CourseExample courseExample = new CourseExample();
			courseExample.or().andCourse_idEqualTo(classroomCourse.getCourse_id());
			Course course = DataAccessUtils.
					singleResult(courseService.selectByExample(courseExample));
			String name = null;
			if(course != null) {
				name = course.getName();
			}
			return name;
		}).collect(Collectors.toList());
	}
	
	/**
	 * 封装classroomInfo
	 * @param classroom
	 * @param school_name
	 * @param courses
	 * @return
	 */
	public ClassroomInfo pottingClassroom(Classroom classroom,String school_name,List<String> courses) {
		ClassroomInfo classroomInfo = new ClassroomInfo();
		classroomInfo.setClassroom_id(classroom.getClassroom_id());
		classroomInfo.setSchool_id(classroom.getSchool_id());
		classroomInfo.setBlock_number(classroom.getBlock_number());
		classroomInfo.setTimescope_json(classroom.getTimescope_json());
		classroomInfo.setCapacity(classroom.getCapacity());
		classroomInfo.setUseable(classroom.getUseable());
		classroomInfo.setCreate_time(classroom.getCreate_time());
		classroomInfo.setSchool_name(school_name);
		classroomInfo.setCourses(courses);
		return classroomInfo;
	}
	
	/**
	 * 获取教室信息列表
	 */
	@Override
	public Page<ClassroomInfo> xSelectClassroomList(ClassroomExample classroomExample, int pageNo, int pageSize) {
		Page<Classroom> page = this.selectByExample(classroomExample,pageNo,pageSize);
		List<Classroom> classroom_list = page.getList();
		//删除最后一页只有一条数据时
		if(classroom_list.size() == 0 && pageNo > 1) {
			page = this.selectByExample(classroomExample,pageNo,pageSize);
			classroom_list = page.getList();
		}
		
		List<ClassroomInfo> classroomInfo_list = classroom_list.stream().map(classroom -> {
			ClassroomInfo classroomInfo = new ClassroomInfo();
			String school_name = schoolService.selectByPrimaryKey(classroom.getSchool_id()).getName();
			ClassroomCourseExample cce = new ClassroomCourseExample();
			cce.or().andClassroom_idEqualTo(classroom.getClassroom_id());
			List<ClassroomCourse> list = classroomCourseService.selectByExample(cce);
			List<String> courses = getCourses(list);
			BeanUtils.copyProperties(classroom,classroomInfo);
			classroomInfo.setSchool_name(school_name);
			classroomInfo.setCourses(courses);
			return classroomInfo;
		}).collect(Collectors.toList());
		
		return new Page<>(classroomInfo_list, page.getTotal(), pageNo, pageSize);
	}
	
	/**
	 * 插入新的教室信息
	 */
	@Override
	@Transactional(timeout = 5)
	public int xInsertClassroom(Classroom classroom, String[] support_course_ids) {
		long id = IdGenerator.generateId();
		classroom.setClassroom_id(id);
		Stream.of(support_course_ids).forEach(string ->{
			ClassroomCourse classroomCourse = new ClassroomCourse();
			classroomCourse.setClassroom_id(id);
			classroomCourse.setCourse_id(Long.valueOf(string));
			classroomCourseService.insertSelective(classroomCourse);
		});
		return this.insertSelective(classroom);
	}
	
	/**
	 * 获取封装好的教室信息
	 */
	@Override
	public ClassroomInfo getClassroomInfo(Classroom classroom) {
		SchoolExample example = new SchoolExample();
		example.or().andSchool_idEqualTo(classroom.getSchool_id());
		example.includeColumns(SchoolExample.C.name);
		String school_name = DataAccessUtils.
				singleResult(schoolService.selectByExample(example)).getName();
		
		ClassroomCourseExample cce = new ClassroomCourseExample();
		cce.or().andClassroom_idEqualTo(classroom.getClassroom_id());
		List<ClassroomCourse> list = classroomCourseService.selectByExample(cce);
		return pottingClassroom(classroom, school_name, getCourses(list));
	}
	
	/**
	 * 更新教室信息
	 */
	@Override
	@Transactional(timeout = 5)
	public int xUpdateClassroom(Classroom classroom, String[] support_course_ids) {
		ClassroomCourseExample classroomCourseExample = new ClassroomCourseExample();
		classroomCourseExample.or().andClassroom_idEqualTo(classroom.getClassroom_id());
		classroomCourseService.deleteByExample(classroomCourseExample);
		Stream.of(support_course_ids).forEach(string ->{
			ClassroomCourse classroomCourse = new ClassroomCourse();
			classroomCourse.setClassroom_id(classroom.getClassroom_id());
			classroomCourse.setCourse_id(Long.valueOf(string));
			classroomCourseService.insertSelective(classroomCourse);
		});
		return this.updateByPrimaryKeySelective(classroom);
	}

	/**
	 * 根据主键删除教室信息
	 */
	@Override
	@Transactional(timeout = 5)
	public int xDeleteClassroom(Long classroom_id) {
		Classroom classroom = new Classroom();
		classroom.setClassroom_id(classroom_id);
		classroom.setDeleted(Status.DeleteStatus.yes.value);
		ClassroomCourseExample classroomCourseExample = new ClassroomCourseExample();
		classroomCourseExample.or().andClassroom_idEqualTo(classroom_id);
		classroomCourseService.deleteByExample(classroomCourseExample);
		return this.updateByPrimaryKeySelective(classroom);
	}
}