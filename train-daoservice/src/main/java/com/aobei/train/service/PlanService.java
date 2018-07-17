package com.aobei.train.service;

import com.aobei.train.model.PlanExample;
import com.aobei.train.model.PlanInfo;
import com.aobei.train.model.Student;
import com.aobei.train.model.Teacher;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aobei.train.model.Classroom;
import com.aobei.train.model.Plan;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface PlanService extends MbgReadService<Long, Plan, Plan, PlanExample>,MbgWriteService<Long, Plan, Plan, PlanExample>,MbgUpsertService<Long, Plan, Plan, PlanExample>{
	
	/**
	 * 根据查询参数查询并封装培训计划信息列表
	 * @param begin_date
	 * @param end_date
	 * @param school_id
	 * @param teacher_id
	 * @param partner_id
	 * @param classroom_id
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<PlanInfo> xSelectPlanList(Date begin_date,Date end_date,Long school_id,Long teacher_id,
			Long partner_id,Long classroom_id,int pageNo,int pageSize);
	
	/**
	 * 根据该课程id获取支持该课程的培训学校和教室信息
	 * @param course_id
	 * @return
	 */
	Map<String, Object> getSupportSchoolAndClassroomByCourseId(Long course_id);
	
	/**
	 * 根据该课程id获取可以培训此课程的培训教师及助教
	 * @param course_id
	 * @return
	 */
	Map<String, List<Teacher>> getSupportTeacherAndAssistantByCourseId(Long course_id);
	
	/**
	 * 根据学校和课程id查找支持该课程的教室
	 * @param school_id
	 * @param course_id
	 * @return
	 */
	List<Classroom> getSupportClassroomBySchoolIdAndCourseId(Long school_id,Long course_id);
	
	/**
	 * 根据查询条件获取可以参加培训计划的学员列表
	 * @param partner_id
	 * @param condition
	 * @param train_begin
	 * @param train_end
	 * @return
	 */
	List<Student> getOptionalStudents(Long partner_id,String condition,Date train_begin,Date train_end);
	
	/**
	 * 插入一条新的培训计划
	 * @param plan
	 * @param partnerids
	 * @param students
	 * @return
	 */
	int xInsertPlan(Plan plan,String[] partnerids,String[] students);
	
	/**
	 * 培训计划所关联的相关信息的插入
	 * 安排课程上课排期
	 * 安排考试计划
	 * 插入学员考试申报表
	 */
	void associated_insert(Plan plan,String[] students);
	
	/**
	 * 根据传递的教室id,培训起始日期和课程的章节次数排期一个培训日程表
	 * @param date
	 * @param classroom_id
	 * @param size
	 * @return
	 */
	List<List<Date>> get_schedule(Date date,Long classroom_id,int size);
	
	/**
	 * 更新培训计划
	 * @param plan
	 * @param partnerids
	 * @param students
	 * @return
	 */
	int xUpdatePlan(Plan plan,String[] partnerids,String[] students);
	
	/**
	 * 根据主键删除培训计划
	 * @param train_plan_id
	 * @return
	 */
	int xDeletePlanByPrimaryKey(Long train_plan_id);
}