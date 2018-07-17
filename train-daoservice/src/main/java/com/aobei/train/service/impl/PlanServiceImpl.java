package com.aobei.train.service.impl;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.aobei.train.model.Chapter;
import com.aobei.train.model.ChapterExample;
import com.aobei.train.model.Classroom;
import com.aobei.train.model.ClassroomCourse;
import com.aobei.train.model.ClassroomCourseExample;
import com.aobei.train.model.ClassroomExample;
import com.aobei.train.model.Course;
import com.aobei.train.model.CourseExamSubject;
import com.aobei.train.model.CourseExamSubjectExample;
import com.aobei.train.model.CourseExample;
import com.aobei.train.model.ExamApply;
import com.aobei.train.model.ExamApplyExample;
import com.aobei.train.model.ExamPlan;
import com.aobei.train.model.ExamPlanExample;
import com.aobei.train.model.ExamSubject;
import com.aobei.train.model.ExamSubjectExample;
import com.aobei.train.model.Message;
import com.aobei.train.model.MessageExample;
import com.aobei.train.model.Partner;
import com.aobei.train.model.PartnerExample;
import com.aobei.train.model.Plan;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.PlanMapper;
import com.aobei.train.model.PlanExample;
import com.aobei.train.model.PlanInfo;
import com.aobei.train.model.PlanPartner;
import com.aobei.train.model.PlanPartnerExample;
import com.aobei.train.model.PlanStudent;
import com.aobei.train.model.PlanStudentExample;
import com.aobei.train.model.Schedule;
import com.aobei.train.model.School;
import com.aobei.train.model.Se;
import com.aobei.train.model.Student;
import com.aobei.train.model.StudentExample;
import com.aobei.train.model.Teacher;
import com.aobei.train.model.TeacherCourse;
import com.aobei.train.model.TeacherCourseExample;
import com.aobei.train.model.TeacherExample;
import com.aobei.train.model.TrainSchedule;
import com.aobei.train.model.TrainScheduleExample;
import com.aobei.train.model.TrainScheduleStudent;
import com.aobei.train.model.TrainScheduleStudentExample;
import com.aobei.train.model.StudentExample.Criteria;
import com.aobei.train.service.ChapterService;
import com.aobei.train.service.ClassroomCourseService;
import com.aobei.train.service.ClassroomService;
import com.aobei.train.service.CourseExamSubjectService;
import com.aobei.train.service.CourseService;
import com.aobei.train.service.ExamApplyService;
import com.aobei.train.service.ExamPlanService;
import com.aobei.train.service.ExamSubjectService;
import com.aobei.train.service.MessageService;
import com.aobei.train.service.PartnerService;
import com.aobei.train.service.PlanPartnerService;
import com.aobei.train.service.PlanService;
import com.aobei.train.service.PlanStudentService;
import com.aobei.train.service.SchoolService;
import com.aobei.train.service.StudentService;
import com.aobei.train.service.TeacherCourseService;
import com.aobei.train.service.TeacherService;
import com.aobei.train.service.TrainScheduleService;
import com.aobei.train.service.TrainScheduleStudentService;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Status;

@Service
public class PlanServiceImpl extends MbgServiceSupport<PlanMapper, Long, Plan, Plan, PlanExample> implements PlanService{

	@Autowired
	private PlanMapper planMapper;
	
	@Autowired
	private PlanService planService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseExamSubjectService courseExamSubjectService;
	
	@Autowired
	private ExamSubjectService examSubjectService;
	
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private ClassroomService classroomService;
	
	@Autowired
	private ClassroomCourseService classroomCourseService;
	
	@Autowired
	private PartnerService partnerService;
	
	@Autowired
	private PlanPartnerService planPartnerService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private PlanStudentService planStudentService;
	
	@Autowired
	private TrainScheduleService trainScheduleService;
	
	@Autowired
	private TrainScheduleStudentService trainScheduleStudentService;
	
	@Autowired
	private ExamPlanService examPlanService;
	
	@Autowired
	private ExamApplyService examApplyService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private TeacherCourseService teacherCourseService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(planMapper);
	}

	@Override
	public Page<PlanInfo> xSelectPlanList(Date begin_date, Date end_date, Long school_id, Long teacher_id,
			Long partner_id, Long classroom_id,int pageNo,int pageSize) {
		PlanExample example = new PlanExample();
		//条件对象
		PlanExample.Criteria criteria = example.or();
		criteria.andDeletedEqualTo(Status.DeleteStatus.no.value);
		if(begin_date != null & end_date != null) {
			criteria.andTrain_beginGreaterThanOrEqualTo(begin_date)
						.andTrain_endLessThanOrEqualTo(end_date);
		}
		if(school_id != 0) {
			criteria.andSchool_idEqualTo(school_id);
		}
		if(teacher_id != 0) {
			criteria.andTeacher_idEqualTo(teacher_id);
		}
		if(classroom_id != 0) {
			criteria.andClassroom_idEqualTo(classroom_id);
		}
		
		if(partner_id != 0) {
			PlanPartnerExample planPartnerExample = new PlanPartnerExample();
			planPartnerExample.or().andPartner_idEqualTo(partner_id);
			//先根据合伙人到中间表查询该合伙人的培训计划集合
			List<PlanPartner> list = planPartnerService.selectByExample(planPartnerExample);
			/*for (PlanPartner planPartner : list) {
				planids.add(planPartner.getTrain_plan_id());
			}*/
			List<Long> planids = list.stream().map(n -> n.getTrain_plan_id()).collect(Collectors.toList());
			if(planids.size() != 0) {
				//说明有筛选出的培训计划
				criteria.andTrain_plan_idIn(planids);
			} else {
				//没有筛选出的培训计划
				criteria.andTrain_plan_idIsNull();
			}
		}
		example.setOrderByClause(PlanExample.C.train_plan_id + " desc");
		Page<Plan> page = this.selectByExample(example, pageNo, pageSize);
		List<Plan> plan_list = page.getList();
		if(plan_list.size() == 0 & pageNo > 1) {
			page = this.selectByExample(example, pageNo-1, pageSize);
			plan_list = page.getList();
		}
		
		//返回页面的培训计划集合
		List<PlanInfo> p_list = plan_list.stream().map(plan -> {
			//用封装参数的对象将数据封装
			PlanInfo info = new PlanInfo();
			PlanPartnerExample example2 = new PlanPartnerExample();
			example2.or().andTrain_plan_idEqualTo(plan.getTrain_plan_id());
			example2.includeColumns(PlanPartnerExample.C.partner_id);
			List<PlanPartner> planpartners = planPartnerService.selectByExample(example2);
			List<Long> partnerids = planpartners.stream().map(n -> n.getPartner_id()).collect(Collectors.toList());
			//隶属一个培训计划的合伙人集合
			PartnerExample example3 = new PartnerExample();
			List<Partner> partner_list = null;
			if(partnerids.size()>0) {
				example3.or().andPartner_idIn(partnerids);
				partner_list = partnerService.selectByExample(example3);
			}
			
			//将plan的属性封装到继承类中
			info.setTrain_plan_id(plan.getTrain_plan_id());
			info.setTrain_begin(plan.getTrain_begin());
			info.setTrain_end(plan.getTrain_end());
			info.setCourse_id(plan.getCourse_id());
			info.setSchool_id(plan.getSchool_id());
			info.setClassroom_id(plan.getClassroom_id());
			info.setTeacher_id(plan.getTeacher_id());
			info.setAssistant(plan.getAssistant());
			info.setTrain_way(plan.getTrain_way());
			info.setTraining(plan.getTraining());
			info.setTrainers_number(plan.getTrainers_number());
			info.setPartner_list(partner_list);
			return info;
		}).collect(Collectors.toList());
		return new Page<>(p_list, page.getTotal(), pageNo, pageSize);
	}

	@Override
	public Map<String, Object> getSupportSchoolAndClassroomByCourseId(Long course_id) {
		Map<String,Object> map = new HashMap<>();
		List<Classroom> list_classroom = new ArrayList<>();
		Set<School> set_school = new HashSet<>();
		ClassroomExample example = new ClassroomExample();
		example.or().andUseableEqualTo(1).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Classroom> classroom_list = classroomService.selectByExample(example);
		//查询出支持课程course_id的学校和教室
		classroom_list.stream().forEach(classroom -> {
			ClassroomCourseExample cce = new ClassroomCourseExample();
			cce.or().andClassroom_idEqualTo(classroom.getClassroom_id());
			List<ClassroomCourse> cc_list = classroomCourseService.selectByExample(cce);
			cc_list.stream().forEach(classroomCourse ->{
				if(classroomCourse.getCourse_id().equals(course_id)) {
					list_classroom.add(classroom);
					Long school_id = classroom.getSchool_id();
					School school = schoolService.selectByPrimaryKey(school_id);
					set_school.add(school);
				}
			});
		});
		map.put("list_classroom", list_classroom);
		map.put("set_school", set_school);
		return map;
	}

	@Override
	public Map<String, List<Teacher>> getSupportTeacherAndAssistantByCourseId(Long course_id) {
		Map<String, List<Teacher>> map = new HashMap<>();
		List<Teacher> teachers = new ArrayList<>();
		List<Teacher> assistants = new ArrayList<>();
		TeacherExample teacherExample = new TeacherExample();
		teacherExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Teacher> teacher_list = teacherService.selectByExample(teacherExample);
		//查询支持该课程id的培训教师和助教
		teacher_list.stream().forEach(teacher->{
			TeacherCourseExample tce = new TeacherCourseExample();
			tce.or().andTeacher_idEqualTo(teacher.getTeacher_id());
			List<TeacherCourse> tc_list = teacherCourseService.selectByExample(tce);
			tc_list.stream().forEach(teacherCourse->{
				if(teacherCourse.getCourse_id().equals(course_id)) {
					if(teacher.getType().equals(1)) {
						teachers.add(teacher);
					}else if(teacher.getType().equals(0)){
						assistants.add(teacher);
					}
				}
			});
		});
		map.put("teachers", teachers);
		map.put("assistants", assistants);
		return map;
	}

	@Override
	public List<Classroom> getSupportClassroomBySchoolIdAndCourseId(Long school_id, Long course_id) {
		List<Classroom> list_classroom = new ArrayList<>();
		ClassroomExample example = new ClassroomExample();
		example.or().andSchool_idEqualTo(school_id).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Classroom> classroom_list = classroomService.selectByExample(example);
		classroom_list.stream().forEach(classroom ->{
			ClassroomCourseExample cce = new ClassroomCourseExample();
			cce.or().andClassroom_idEqualTo(classroom.getClassroom_id());
			List<ClassroomCourse> cc_list = classroomCourseService.selectByExample(cce);
			cc_list.stream().forEach(classroomCourse ->{
				if(classroomCourse.getCourse_id().equals(course_id)) {
					list_classroom.add(classroom);
				}
			});
		});
		return list_classroom;
	}

	@Override
	public List<Student> getOptionalStudents(Long partner_id, String condition, Date train_begin, Date train_end) {
		List<Long> unadd_stuids = new ArrayList<>();
		if(train_begin != null && train_end != null) {
			//查询出在培训计划时间内的学员id集合
			PlanStudentExample planStudentExample = new PlanStudentExample();
			// where db-s <= begin and begin <= db-e <= end
			planStudentExample.or().andTrain_beginLessThanOrEqualTo(train_begin)
								   .andTrain_endGreaterThanOrEqualTo(train_begin)
								   .andTrain_endLessThanOrEqualTo(train_end)
								   .andDeletedEqualTo(Status.DeleteStatus.no.value);
			// or db-s >= begin and db-e <= end
			planStudentExample.or().andTrain_beginGreaterThanOrEqualTo(train_begin)
								   .andTrain_endLessThanOrEqualTo(train_end)
								   .andDeletedEqualTo(Status.DeleteStatus.no.value);
			// or begin <= db-s <= end and db-e >= end
			planStudentExample.or().andTrain_beginGreaterThanOrEqualTo(train_begin)
								   .andTrain_beginLessThanOrEqualTo(train_end)
								   .andTrain_endGreaterThanOrEqualTo(train_end)
								   .andDeletedEqualTo(Status.DeleteStatus.no.value);
			// or db-s <= begin and db-e >= end 
			planStudentExample.or().andTrain_beginLessThanOrEqualTo(train_begin)
								   .andTrain_endGreaterThanOrEqualTo(train_end)
								   .andDeletedEqualTo(Status.DeleteStatus.no.value);
			List<PlanStudent> plan_stu_list = planStudentService.selectByExample(planStudentExample);
			
			unadd_stuids = plan_stu_list.stream().map(planStu ->planStu.getStudent_id()).collect(Collectors.toList());
		}
		StudentExample example = new StudentExample();
		Criteria criteria = example.or();
		criteria.andDeletedEqualTo(Status.DeleteStatus.no.value).andStateEqualTo(1);
		if(partner_id != null) {
			criteria.andPartner_idEqualTo(partner_id);
		}
		if(unadd_stuids.size() != 0) {
			criteria.andStudent_idNotIn(unadd_stuids);
		}
		if(condition != null) {
			condition = "%" + condition + "%";
			criteria.andInnerOrLike(condition, 
					StudentExample.C.name,
					StudentExample.C.identity_card,
					StudentExample.C.phone);
		}
		return studentService.selectByExample(example);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,timeout = 5)
	public int xInsertPlan(Plan plan, String[] partnerids, String[] students) {
		long id = IdGenerator.generateId();
		plan.setTrain_plan_id(id);
		Stream.of(partnerids).forEach(str ->{
			PlanPartner planPartner = new PlanPartner();
			planPartner.setPartner_id(Long.parseLong(str));
			planPartner.setTrain_plan_id(id);
			planPartnerService.insertSelective(planPartner);
		});
		
		Stream.of(students).forEach(str -> {
			PlanStudent planStudent = new PlanStudent();
			planStudent.setStudent_id(Long.parseLong(str));
			planStudent.setTrain_plan_id(id);
			planStudent.setCourse_id(plan.getCourse_id());
			planStudent.setTrain_begin(plan.getTrain_begin());
			planStudent.setTrain_end(plan.getTrain_end());
			planStudentService.insertSelective(planStudent);
		});
		
		associated_insert(plan,students);
		//插入培训计划associated_insert
		return planService.insertSelective(plan);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,timeout = 5)
	public int xUpdatePlan(Plan plan, String[] partnerids, String[] students) {
		//先将原来的培训计划下的合伙人删除，在插入新数据
		PlanPartnerExample planPartnerExample = new PlanPartnerExample();
		planPartnerExample.or().andTrain_plan_idEqualTo(plan.getTrain_plan_id());
		planPartnerService.deleteByExample(planPartnerExample);
		Stream.of(partnerids).forEach(str ->{
			PlanPartner planPartner = new PlanPartner();
			planPartner.setPartner_id(Long.parseLong(str));
			planPartner.setTrain_plan_id(plan.getTrain_plan_id());
			planPartnerService.insertSelective(planPartner);
		});
		
		//先将原来的培训计划下的学员名单删除，在插入新数据
		PlanStudentExample planStudentExample = new PlanStudentExample();
		planStudentExample.or().andTrain_plan_idEqualTo(plan.getTrain_plan_id());
		planStudentService.deleteByExample(planStudentExample);
		Stream.of(students).forEach(str ->{
			PlanStudent planStudent = new PlanStudent();
			planStudent.setStudent_id(Long.parseLong(str));
			planStudent.setTrain_plan_id(plan.getTrain_plan_id());
			planStudent.setCourse_id(plan.getCourse_id());
			planStudent.setTrain_begin(plan.getTrain_begin());
			planStudent.setTrain_end(plan.getTrain_end());
			planStudentService.insertSelective(planStudent);
		});
		
		//将培训计划下的课程上课排期先删除，再重新插入
		TrainScheduleExample tse = new TrainScheduleExample();
		tse.or().andTrain_plan_idEqualTo(plan.getTrain_plan_id());
		trainScheduleService.deleteByExample(tse);
		
		//将培训计划下的学员课程排期表删除
		TrainScheduleStudentExample tsse = new TrainScheduleStudentExample();
		tsse.or().andTrain_plan_idEqualTo(plan.getTrain_plan_id());
		trainScheduleStudentService.deleteByExample(tsse);
		
		//将培训计划下的考试计划先删除，再重新插入
		PlanExample planExample = new PlanExample();
		planExample.or().andTrain_plan_idEqualTo(plan.getTrain_plan_id());
		List<Plan> list = planService.selectByExample(planExample);
		Plan db_plan = DataAccessUtils.singleResult(list);
		ExamPlanExample example = new ExamPlanExample();
		ExamPlanExample.Criteria criteria = example.or();
		criteria.andClassroom_idEqualTo(db_plan.getClassroom_id());
		
		//获取考试科目的id
		CourseExamSubjectExample cese = new CourseExamSubjectExample();
		cese.or().andCourse_idEqualTo(plan.getCourse_id());
		List<CourseExamSubject> list_sub = courseExamSubjectService.selectByExample(cese);
		List<Long> es_ids = list_sub.stream().map(ces -> ces.getExam_subject_id()).collect(Collectors.toList());
		ExamSubjectExample exam = new ExamSubjectExample();
		ExamSubjectExample.Criteria c = exam.or();
		c.andTypeEqualTo(2);
		c.andExam_subject_idIn(es_ids);
		List<ExamSubject> es_list = examSubjectService.selectByExample(exam);
		ExamSubject examSubject = DataAccessUtils.singleResult(es_list);
		
		criteria.andExam_subject_idEqualTo(examSubject.getExam_subject_id());
		criteria.andExam_dateEqualTo(db_plan.getTrain_end());
		List<ExamPlan> example2 = examPlanService.selectByExample(example);
		ExamPlan examPlan = DataAccessUtils.singleResult(example2);
		examPlan.setDeleted(Status.DeleteStatus.yes.value);
		//删除学员考试申请（报考）
		ExamApplyExample examApplyExample = new ExamApplyExample();
		examApplyExample.or().andExam_plan_idEqualTo(examPlan.getExam_plan_id());
		examApplyService.deleteByExample(examApplyExample);
		//考试删除
		examPlanService.deleteByExample(example);
		
		//删除该培训计划下的培训通知消息
		MessageExample msgExample = new MessageExample();
		msgExample.or().andGroup_idLike(plan.getTrain_plan_id()+"%");
		messageService.deleteByExample(msgExample);
		
		associated_insert(plan,students);
		
		return planService.updateByPrimaryKeySelective(plan);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class,timeout = 5)
	public int xDeletePlanByPrimaryKey(Long train_plan_id) {
		//删除培训计划前，删除与合伙人中间表的数据
		PlanPartnerExample planPartnerExample = new PlanPartnerExample();
		planPartnerExample.or().andTrain_plan_idEqualTo(train_plan_id);
		int del_partner = planPartnerService.deleteByExample(planPartnerExample);
		
		//删除培训计划前，删除与学员中间表的数据
		PlanStudent ps = new PlanStudent();
		ps.setDeleted(Status.DeleteStatus.yes.value);
		PlanStudentExample planStudentExample = new PlanStudentExample();
		planStudentExample.or().andTrain_plan_idEqualTo(train_plan_id);
		int del_stu = planStudentService.updateByExampleSelective(ps, planStudentExample);
		
		//将培训计划下的课程上课排期先删除，再重新插入
		TrainSchedule trainSchedule = new TrainSchedule();
		trainSchedule.setDeleted(Status.DeleteStatus.yes.value);
		TrainScheduleExample tse = new TrainScheduleExample();
		tse.or().andTrain_plan_idEqualTo(train_plan_id);
		int del_trainSch = trainScheduleService.updateByExampleSelective(trainSchedule, tse);
		
		//删除该培训计划下的培训通知消息
		MessageExample msgExample = new MessageExample();
		msgExample.or().andGroup_idLike(train_plan_id+"%");
		Message message = new Message();
		message.setDeleted(Status.DeleteStatus.yes.value);
		int del_msg = messageService.updateByExampleSelective(message, msgExample);
		
		//将培训计划下的学员课程排期表删除
		TrainScheduleStudent tss = new TrainScheduleStudent();
		tss.setDeleted(Status.DeleteStatus.yes.value);
		TrainScheduleStudentExample tsse = new TrainScheduleStudentExample();
		tsse.or().andTrain_plan_idEqualTo(train_plan_id);
		int del_trainSchStu = trainScheduleStudentService.updateByExampleSelective(tss, tsse);
		
		
		//将培训计划下的考试计划先删除
		PlanExample planExample = new PlanExample();
		planExample.or().andTrain_plan_idEqualTo(train_plan_id);
		List<Plan> list = planService.selectByExample(planExample);
		Plan db_plan = DataAccessUtils.singleResult(list);
		ExamPlanExample example = new ExamPlanExample();
		ExamPlanExample.Criteria criteria = example.or();
		criteria.andClassroom_idEqualTo(db_plan.getClassroom_id());
		//获取考试科目的id
		CourseExamSubjectExample cese = new CourseExamSubjectExample();
		Plan plan = planService.selectByPrimaryKey(train_plan_id);
		cese.or().andCourse_idEqualTo(plan.getCourse_id());
		List<CourseExamSubject> list_sub = courseExamSubjectService.selectByExample(cese);
		List<Long> es_ids =  list_sub.stream().map(ces -> ces.getExam_subject_id()).collect(Collectors.toList());
		ExamSubjectExample exam = new ExamSubjectExample();
		ExamSubjectExample.Criteria c = exam.or();
		c.andTypeEqualTo(2);
		c.andExam_subject_idIn(es_ids);
		List<ExamSubject> es_list = examSubjectService.selectByExample(exam);
		ExamSubject examSubject = DataAccessUtils.singleResult(es_list);
		
		criteria.andExam_subject_idEqualTo(examSubject.getExam_subject_id());
		criteria.andExam_dateEqualTo(db_plan.getTrain_end());
		List<ExamPlan> example2 = examPlanService.selectByExample(example);
		ExamPlan examPlan = DataAccessUtils.singleResult(example2);
		examPlan.setDeleted(Status.DeleteStatus.yes.value);
		//删除学员考试申请（报考）
		ExamApply examApply = new ExamApply();
		examApply.setDeleted(Status.DeleteStatus.yes.value);
		ExamApplyExample examApplyExample = new ExamApplyExample();
		examApplyExample.or().andExam_plan_idEqualTo(examPlan.getExam_plan_id());
		int del_examApply = examApplyService.updateByExampleSelective(examApply, examApplyExample);
		//考试删除
		int del_examPlan = examPlanService.updateByPrimaryKeySelective(examPlan);
		int i = 0;
		plan.setDeleted(Status.DeleteStatus.yes.value);
		if(del_partner > 0 && del_stu > 0 && del_trainSch > 0 && del_msg > 0 && del_trainSchStu > 0 && del_examApply > 0 && del_examPlan > 0) {
			i = planService.updateByPrimaryKeySelective(plan);
		}else {
			try {
				throw new Exception("培训计划删除失败！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void associated_insert(Plan plan, String[] students) {
		//查询培训计划相关的培训课程
		Course course = courseService.selectByPrimaryKey(plan.getCourse_id());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//插入培训讲师以及培训助教要培训课程的日程通知
		Message t_train_msg = new Message();
		t_train_msg.setId(IdGenerator.generateId());
		t_train_msg.setType(2);
		t_train_msg.setBis_type(2);
		t_train_msg.setUser_id(plan.getTeacher_id());
		t_train_msg.setUid(plan.getTeacher_id());
		t_train_msg.setMsg_title("开课通知");
		t_train_msg.setMsg_content("你好，您于"+sdf.format(plan.getTrain_begin())+"将开始为"+plan.getTrainers_number()+"名学员开始《"+course.getName()+"》课程的培训，详情请查看培训课程的排期！");
		t_train_msg.setCreate_datetime(new Date());
		t_train_msg.setNotify_datetime(new Date(plan.getTrain_begin().getTime()-3600*24*1000*2l));
		t_train_msg.setGroup_id(plan.getTrain_plan_id()+"-"+plan.getCourse_id()+"-"+plan.getTeacher_id());
		messageService.insertSelective(t_train_msg);//对于培训教师插入一条上课前两天的通知
		if(plan.getAssistant() != null) {
			t_train_msg.setUser_id(plan.getAssistant());
			t_train_msg.setUid(plan.getAssistant());
			t_train_msg.setGroup_id(plan.getTrain_plan_id()+"-"+plan.getCourse_id()+"-"+plan.getAssistant());
			messageService.insertSelective(t_train_msg);
		}
		
		//获取课程节次
		ChapterExample chapterExample = new ChapterExample();
		chapterExample.setOrderByClause(ChapterExample.C.section_name + "");
		chapterExample.or().andCourse_idEqualTo(plan.getCourse_id());
		List<Chapter> chapter_list = chapterService.selectByExample(chapterExample);
		int size = chapter_list.size();
		//安排课程上课排期
		List<List<Date>> schedule = get_schedule(plan.getTrain_begin(),plan.getClassroom_id(),size);
		for (int j = 0; j < chapter_list.size() ; j++) {
			TrainSchedule trainSchedule = new TrainSchedule();
			trainSchedule.setTrain_schedule_id(IdGenerator.generateId());
			trainSchedule.setTrain_plan_id(plan.getTrain_plan_id());//培训计划关联
			trainSchedule.setChapter_id(chapter_list.get(j).getChapter_id());
			trainSchedule.setCourse_id(plan.getCourse_id());
			trainSchedule.setClassroom_id(plan.getClassroom_id());
			trainSchedule.setSchool_id(plan.getSchool_id());
			trainSchedule.setTeacher_id(plan.getTeacher_id());
			trainSchedule.setAssistant(plan.getAssistant());
			trainSchedule.setTrain_way(plan.getTrain_way());
			trainSchedule.setAttendclass_date(schedule.get(j).get(0));
			trainSchedule.setAttendclass_starttime(schedule.get(j).get(1));
			trainSchedule.setAttendclass_endtime(schedule.get(j).get(2));
			//插入课程上课排期
			trainScheduleService.insertSelective(trainSchedule);
			//插入学员上课时间表
			Stream.of(students).forEach(str ->{
				TrainScheduleStudent tss = new TrainScheduleStudent();
				tss.setTrain_schedule_id(trainSchedule.getTrain_schedule_id());
				tss.setStudent_id(Long.valueOf(str));
				tss.setTrain_plan_id(plan.getTrain_plan_id());
				tss.setAttendclass_date(trainSchedule.getAttendclass_date());
				tss.setAttendclass_starttime(trainSchedule.getAttendclass_starttime());
				tss.setAttendclass_endtime(trainSchedule.getAttendclass_endtime());
				trainScheduleStudentService.insertSelective(tss);
			});
		}
		
		//获取考试科目的id
		CourseExamSubjectExample cese = new CourseExamSubjectExample();
		cese.or().andCourse_idEqualTo(plan.getCourse_id());
		List<CourseExamSubject> list_sub = courseExamSubjectService.selectByExample(cese);
		List<Long> es_ids = list_sub.stream().map(ces -> ces.getExam_subject_id()).collect(Collectors.toList());
		ExamSubjectExample example = new ExamSubjectExample();
		ExamSubjectExample.Criteria criteria = example.or();
		criteria.andTypeEqualTo(2);
		criteria.andExam_subject_idIn(es_ids);
		List<ExamSubject> es_list = examSubjectService.selectByExample(example);
		ExamSubject examSubject = DataAccessUtils.singleResult(es_list);
		
		//安排考试计划
		ExamPlan examPlan = new ExamPlan();
		examPlan.setExam_plan_id(IdGenerator.generateId());
		examPlan.setExam_subject_id(examSubject.getExam_subject_id());//设置考试科目
		//默认设置为线上考试
		examPlan.setType(new Byte("1"));//线下考试
		examPlan.setTeacher_id(plan.getTeacher_id());//主考老师
		examPlan.setAssistant(plan.getAssistant());//助考老师
		examPlan.setClassroom_id(plan.getClassroom_id());//关联教室
		//获取试卷id
		CourseExample courseTeamExample = new CourseExample();
		courseTeamExample.or().andCourse_idEqualTo(plan.getCourse_id());
		List<Course> list = courseService.selectByExample(courseTeamExample);
		String name = DataAccessUtils.singleResult(list).getName();
		examPlan.setName(name + "考试");
		examPlan.setExam_date(schedule.get(schedule.size()-1).get(0));
		examPlan.setExam_start_time(schedule.get(schedule.size()-1).get(1));
		examPlan.setExam_end_time(schedule.get(schedule.size()-1).get(2));
		examPlan.setApply_start_datetime(null);
		examPlan.setApply_end_datetime(null);
		examPlan.setApply_number(students.length);//可申请人数  学员人数
		examPlan.setCreate_datetime(new Date());//设置为插入时间
		examPlanService.insertSelective(examPlan);
		
		//教师考试监考通知
		Message t_exam_msg = new Message();
		t_exam_msg.setId(IdGenerator.generateId());
		t_exam_msg.setType(2);
		t_exam_msg.setBis_type(2);
		t_exam_msg.setUser_id(plan.getTeacher_id());
		t_exam_msg.setUid(plan.getTeacher_id());
		t_exam_msg.setMsg_title("开考通知");
		t_exam_msg.setMsg_content("你好，您于"+sdf.format(plan.getTrain_end())+"将进行《"+course.getName()+"》课程的监考，详情请查看考试安排的排期！");
		t_exam_msg.setCreate_datetime(new Date());
		t_exam_msg.setNotify_datetime(new Date(plan.getTrain_end().getTime()-3600*24*1000*2l));
		t_exam_msg.setGroup_id(plan.getTrain_plan_id()+"-"+plan.getCourse_id()+"-"+plan.getTeacher_id());
		messageService.insertSelective(t_exam_msg);//对于培训教师插入一条考试前两天的通知
		if(plan.getAssistant() != null) {
			t_exam_msg.setUser_id(plan.getAssistant());
			t_exam_msg.setUid(plan.getAssistant());
			t_exam_msg.setGroup_id(plan.getTrain_plan_id()+"-"+plan.getCourse_id()+"-"+plan.getAssistant());
			messageService.insertSelective(t_exam_msg);
		}
		
		//插入学员考试申报表
		Stream.of(students).forEach(str ->{
			//考试申请（报考）
			ExamApply examApply = new ExamApply();
			long student_id = Long.parseLong(str);
			Student student = studentService.selectByPrimaryKey(student_id);
			examApply.setExam_apply_id(IdGenerator.generateId());
			examApply.setTrain_plan_id(plan.getTrain_plan_id());
			examApply.setExam_plan_id(examPlan.getExam_plan_id());//关联考试计划
			examApply.setStudent_id(student_id);//关联学员
			examApply.setPartner_id(student.getPartner_id());
			examApply.setExam_subject_id(examSubject.getExam_subject_id());//关联课程
			examApply.setApply_datetime(schedule.get(schedule.size()-1).get(0));
			examApply.setExam_start_time(schedule.get(schedule.size()-1).get(1));
			examApply.setExam_end_time(schedule.get(schedule.size()-1).get(2));
			examApplyService.insertSelective(examApply);
			
			Message msg = new Message();
			msg.setId(IdGenerator.generateId());
			msg.setType(2);
			msg.setBis_type(1);
			msg.setUser_id(Long.valueOf(str));
			msg.setUid(Long.valueOf(str));
			msg.setMsg_title("上课通知");
			msg.setMsg_content("你好，您于"+sdf.format(plan.getTrain_begin())+"将开始《"+course.getName()+"》课程的培训，请注意查看培训课程的排期！");
			msg.setCreate_datetime(new Date());
			msg.setNotify_datetime(new Date(plan.getTrain_begin().getTime()-3600*24*1000l));
			msg.setGroup_id(plan.getTrain_plan_id()+"-"+plan.getCourse_id()+"-"+str);
			messageService.insertSelective(msg);//对于每个学员插入一条上课前一天的通知
			
			msg.setId(IdGenerator.generateId());
			msg.setMsg_title("考试通知");
			msg.setMsg_content("你好，您于"+sdf.format(schedule.get(schedule.size()-1).get(0))+"将进行《"+course.getName()+"》课程的考试，请注意不要错过考试时间！");
			msg.setNotify_datetime(new Date(schedule.get(schedule.size()-1).get(0).getTime()-3600*24*1000l));
			messageService.insertSelective(msg);//对于每个学员插入一条考试前一天的通知
		});
	}
	
	/**
	 * 根据传递的教室id,培训起始日期和课程的章节次数排期一个培训日程表
	 * @param date
	 * @param classroom_id
	 * @param size
	 * @return
	 */
	@Override
	public List<List<Date>> get_schedule(Date date,Long classroom_id,int size) {
		//获取教室的可上课课程表
		Classroom classroom = classroomService.selectByPrimaryKey(classroom_id);
		String json = classroom.getTimescope_json();
		List<Schedule> mList = new ArrayList<>();
		if(!StringUtils.isEmpty(json)) {
			mList = JSONObject.parseArray(json,Schedule.class);
		}
		
		//根据教室的课程表得到规则map
		Map<Integer, List<String>> map = get_rule(mList);
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		LocalTime s_time = null;
		LocalTime e_time = null;
		
		List<List<Date>> re = new ArrayList<>();
		while(size > 0) {
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
			List<String> gz = null;
			if(dayOfWeek == 0) {
				gz = map.get(7);
			}else {
				gz = map.get(dayOfWeek);
			}
			if(gz != null) {
				for (String s : gz) {
					if(!(size <= 0)) {
						List<Date> dd = new ArrayList<>();
						switch(s) {
							case "a" :
								s_time = LocalTime.of(8, 0);
								e_time = LocalTime.of(9, 30);
								break;
							case "b" :
								s_time = LocalTime.of(10, 0);
								e_time = LocalTime.of(11, 30);
								break;
							case "c" :
								s_time = LocalTime.of(14, 0);
								e_time = LocalTime.of(15, 30);
								break;
							case "d" :
								s_time = LocalTime.of(16, 0);
								e_time = LocalTime.of(17, 30);
								break;
						}
						Date time = c.getTime();
						Instant instant = time.toInstant();
						ZoneId zone = ZoneId.systemDefault();
						LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
						LocalDate localDate = localDateTime.toLocalDate();
						
						Instant ti = localDate.atStartOfDay().atZone(zone).toInstant();
					    Date train_day = Date.from(ti);
						
						LocalDateTime localDateTime_s = LocalDateTime.of(localDate, s_time);
						Instant instant_s = localDateTime_s.atZone(zone).toInstant();
						Date date_s = Date.from(instant_s);
						LocalDateTime localDateTime_e = LocalDateTime.of(localDate, e_time);
						Instant instant_e = localDateTime_e.atZone(zone).toInstant();
						Date date_e = Date.from(instant_e);
						
						//根据教室id 培训日期，上课起止时间查询培训安排表
						TrainScheduleExample trainScheduleExample = new TrainScheduleExample();
						TrainScheduleExample.Criteria criteria = trainScheduleExample.or();
						criteria.andClassroom_idEqualTo(classroom_id);
						criteria.andAttendclass_dateEqualTo(train_day);
						criteria.andAttendclass_starttimeEqualTo(date_s);
						criteria.andAttendclass_endtimeEqualTo(date_e);
						criteria.andDeletedEqualTo(Status.DeleteStatus.no.value);
						List<TrainSchedule> list2 = trainScheduleService.selectByExample(trainScheduleExample);
						if(list2.size() == 0) {
							//添加培训日期
							dd.add(train_day);
							//添加上课开始时间
							dd.add(date_s);
							//添加上课结束时间
							dd.add(date_e);
							re.add(dd);
							size = size-1;
						}
					}
				}
			}
			c.add(Calendar.DAY_OF_YEAR, 1);
		}
		return re;
	}
	
	/**
	 * 根据教室的课程表信息制定一个规则map
	 * @param mList
	 * @return
	 */
	public Map<Integer, List<String>> get_rule(List<Schedule> mList) {
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		
		if(mList.size()>0) {
			for (Schedule schedule : mList) {
				//if(schedule.getW().equals("1")) {
				Integer w = Integer.valueOf(schedule.getW());
				List<String> list;
				if(map.containsKey(w)) {
					list = map.get(w);
				}else {
					list = new ArrayList<>();
					map.put(w, list);
				}
				
				List<Se> listse = schedule.getSe();
				for (Se se : listse) {
					String s = se.getS();
					String e = se.getE();
					if(s.equals("96") && e.equals("114")) {
						list.add("a");
					}
					if(s.equals("120") && e.equals("138")) {
						list.add("b");
					}
					if(s.equals("168") && e.equals("186")) {
						list.add("c");
					}
					if(s.equals("192") && e.equals("210")) {
						list.add("d");
					}
				}
			}
		}
		return map;
	}

}