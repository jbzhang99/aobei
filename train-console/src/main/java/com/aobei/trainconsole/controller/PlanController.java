package com.aobei.trainconsole.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.aobei.train.model.*;
import com.aobei.train.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aobei.trainconsole.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.Status;


@Controller
@RequestMapping("/planmanager")
public class PlanController {

	private static final Logger logger = LoggerFactory.getLogger(PlanController.class);
	
	@Autowired
	private PlanService planService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private ClassroomService classroomService;
	
	@Autowired
	private PartnerService partnerService;
	
	@Autowired
	private PlanPartnerService planPartnerService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private PlanStudentService planStudentService;

	@Autowired
	private UsersService usersService;
	
	/**
	 * 跳转到培训计划列表
	 * @param map
	 * @param p 查询起始条目数
	 * @param ps 查询条数
	 * @param begin_date 培训开始时间
	 * @param end_date	培训结束时间
	 * @param school_id 学校id
	 * @param teacher_id 教师id
	 * @param partner_id 合伙人id
	 * @param classroom_id 教室id
	 * @return
	 */
	@RequestMapping("/goto_plan_list")
	public String goto_plan_list(Model map,
			@RequestParam(defaultValue="1") Integer p,
			@RequestParam(defaultValue="10") Integer ps,
			@RequestParam(required=false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date begin_date,
			@RequestParam(required=false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date end_date,
			@RequestParam(defaultValue="0") Long school_id,
			@RequestParam(defaultValue="0") Long teacher_id,
			@RequestParam(defaultValue="0") Long partner_id,
			@RequestParam(defaultValue="0") Long classroom_id
			) {
		Page<PlanInfo> page = planService.xSelectPlanList(begin_date, end_date, school_id, teacher_id, partner_id, classroom_id, p, ps);
		List<PlanInfo> p_list = page.getList();
		
		//查询学校信息
		SchoolExample schoolExample = new SchoolExample();
		schoolExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<School> school_list = 
				schoolService.selectByExample(schoolExample);
		//查询合伙人
		PartnerExample partnerExample = new PartnerExample();
		partnerExample.or().andStateEqualTo(1).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Partner> partners = 
				partnerService.selectByExample(partnerExample);
		//查询教师信息
		TeacherExample teacherInfoExample = new TeacherExample();
		teacherInfoExample.or().andTypeEqualTo(1).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Teacher> teacher_list = 
				teacherService.selectByExample(teacherInfoExample);
		//查询课程信息
		CourseExample courseExample = new CourseExample();
		courseExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Course> courses = 
				courseService.selectByExample(courseExample);
		//查询教室信息
		ClassroomExample classroomExample = new ClassroomExample();
		classroomExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Classroom> classroom_list =
				classroomService.selectByExample(classroomExample);
		map.addAttribute("begin_date", begin_date);
		map.addAttribute("end_date", end_date);
		map.addAttribute("school_id", school_id);
		map.addAttribute("teacher_id", teacher_id);
		map.addAttribute("partner_id", partner_id);
		map.addAttribute("classroom_id", classroom_id);
		map.addAttribute("page", page);
		map.addAttribute("plan_list", p_list);
		map.addAttribute("school_list", school_list);
		map.addAttribute("partner_list", partners);
		map.addAttribute("teacher_list", teacher_list);
		map.addAttribute("courses", courses);
		map.addAttribute("classroom_list", classroom_list);
		return "trainplan/plan_list";
	}
	
	/**
	 * 异步查询支持该course_id的学校教室信息
	 * @param course_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get_optional_school_classroom")
	public Object get_optional_school_classroom(Long course_id) {
		Map<String, Object> supportSchoolAndClassroomMap = planService.getSupportSchoolAndClassroomByCourseId(course_id);
		List<Classroom> list_classroom = (List<Classroom>) supportSchoolAndClassroomMap.get("list_classroom");
		Set<School> set_school = (Set<School>) supportSchoolAndClassroomMap.get("set_school");
		
		Map<String, List<Teacher>> supportTeacherAndAssistantMap = planService.getSupportTeacherAndAssistantByCourseId(course_id);
		List<Teacher> teachers = supportTeacherAndAssistantMap.get("teachers");
		List<Teacher> assistants = supportTeacherAndAssistantMap.get("assistants");
		
		Map<String, Object> map = new HashMap<>();
		map.put("classrooms", list_classroom);
		map.put("schools", set_school);
		map.put("teachers", teachers);
		map.put("assistants", assistants);
		return map;
	}
	
	/**
	 * 异步查询选择的学校名称下的教室信息
	 * @param school_id
	 * @param course_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get_optional_classroom")
	public Object get_optional_classroom(Long school_id,Long course_id) {
		return planService.getSupportClassroomBySchoolIdAndCourseId(school_id, course_id);
	}
	
	/**
	 * 跳转到培训计划添加页面
	 * @param map
	 * @param page_current_page
	 * @return
	 */
	@RequestMapping("/train_plan_add")
	public String goto_train_plan_add(Model map,Integer page_current_page) {
		if(page_current_page == 0) {
			page_current_page = page_current_page + 1;
		}
		//查询学校信息
		SchoolExample schoolExample = new SchoolExample();
		schoolExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<School> school_list = 
				schoolService.selectByExample(schoolExample);
		//查询合伙人
		PartnerExample partnerExample = new PartnerExample();
		partnerExample.or().andStateEqualTo(1).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Partner> partner_list = 
				partnerService.selectByExample(partnerExample);
		//查询教师信息
		TeacherExample example = new TeacherExample();
		example.or().andTypeEqualTo(1).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Teacher> teacher_list = 
				teacherService.selectByExample(example);
		//查询助教信息
		TeacherExample exampleAssistant = new TeacherExample();
		exampleAssistant.or().andTypeEqualTo(0).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Teacher> assistant_list = 
				teacherService.selectByExample(exampleAssistant);
		//查询课程信息
		CourseExample courseExample = new CourseExample();
		courseExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Course> course_list = 
				courseService.selectByExample(courseExample);
		map.addAttribute("page_current_page", page_current_page);
		map.addAttribute("school_list", school_list);
		map.addAttribute("teacher_list", teacher_list);
		map.addAttribute("assistant_list", assistant_list);
		map.addAttribute("course_list", course_list);
		map.addAttribute("partner_list", partner_list);
		return "trainplan/plan_add";
	}
	
	/**
	 * 异步查询学生列表 andInnerOrLike方法是重写的
	 * @param partner_id
	 * @param condition
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get_student_list")
	public Object get_student_list(Long partner_id,String condition,
			@DateTimeFormat(pattern="yyyy-MM-dd")Date train_begin,
			@DateTimeFormat(pattern="yyyy-MM-dd")Date train_end) {
		return planService.getOptionalStudents(partner_id, condition, train_begin, train_end);
	}
	
	/**
	 * 保存培训计划
	 * @param plan
	 * @param partnerids
	 * @param students
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save_plan")
	public Object save_plan(Plan plan,
							@RequestParam(value="partnerids")String[] partnerids,
							@RequestParam(value="students")String[] students, Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[trainPlan] F[save_plan] U[{}] , params plan:{},partnerids:{},students{} .",
				users.getUser_id(),plan,partnerids,students);
		//插入培训计划associated_insert
		int i = planService.xInsertPlan(plan, partnerids, students);
		logger.info("M[trainPlan] F[save_plan] U[{}] , execute result:{}",
				users.getUser_id(),String.format("培训计划添加%s!", i > 0 ? "成功" : "失败"));
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("培训计划添加%s!", i > 0 ? "成功" : "失败"));
		return map;
	}
	
	/**
	 * 根据id查询出培训计划信息，并跳转到编辑页面---跳转
	 * 
	 * @param train_plan_id
	 * @param map
	 * @return
	 */
	@RequestMapping("/plan_edit")
	public String plan_edit(Long train_plan_id,Integer page_current_page, Model map) {
		//根据plan_id查询培训计划
		Plan plan = planService.selectByPrimaryKey(train_plan_id);
		
		//根据培训计划id查询其合伙人，页面回显
		PlanPartnerExample planPartnerExample = new PlanPartnerExample();
		planPartnerExample.or().andTrain_plan_idEqualTo(train_plan_id);
		List<PlanPartner> planPartner_list = 
				planPartnerService.selectByExample(planPartnerExample);
		//根据培训计划id查询其学员名单，页面回显
		PlanStudentExample planStudentExample = new PlanStudentExample();
		planStudentExample.or().andTrain_plan_idEqualTo(train_plan_id);
		List<PlanStudent> planStudent_list = 
				planStudentService.selectByExample(planStudentExample);
		List<Long> stu_ids = planStudent_list.stream().map(n -> n.getStudent_id()).collect(Collectors.toList());
		
		//查询全部的学员列表
		StudentExample studentExample = new StudentExample();
		studentExample.or().andStudent_idIn(stu_ids);
		List<Student> student_list = 
				studentService.selectByExample(studentExample);
		//查询合伙人
		PartnerExample partnerExample = new PartnerExample();
		partnerExample.or().andStateEqualTo(1).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Partner> partner_list = 
				partnerService.selectByExample(partnerExample);
		//查询课程信息
		CourseExample courseExample = new CourseExample();
		courseExample.or().andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Course> course_list = 
				courseService.selectByExample(courseExample);
		
		Map<String, Object> supportSchoolAndClassroomMap = planService.getSupportSchoolAndClassroomByCourseId(plan.getCourse_id());
		Set<School> set_school = (Set<School>) supportSchoolAndClassroomMap.get("set_school");
		
		Map<String, List<Teacher>> supportTeacherAndAssistantMap = planService.getSupportTeacherAndAssistantByCourseId(plan.getCourse_id());
		List<Teacher> teachers = supportTeacherAndAssistantMap.get("teachers");
		List<Teacher> assistants = supportTeacherAndAssistantMap.get("assistants");
		
		map.addAttribute("page_current_page", page_current_page);
		map.addAttribute("plan", plan);
		String planPartners = null;
		String students = null;
		try {
			planPartners = JacksonUtil.object_to_json(planPartner_list);
			students = JacksonUtil.object_to_json(student_list);
		} catch (JsonProcessingException e) {
			logger.error("Json解析异常,{}",e);
		}
		map.addAttribute("planPartners", planPartners);
		map.addAttribute("planPartner_list", planPartner_list);
		map.addAttribute("school_list", set_school);
		map.addAttribute("teacher_list", teachers);
		map.addAttribute("assistant_list", assistants);
		map.addAttribute("course_list", course_list);
		map.addAttribute("partner_list", partner_list);
		map.addAttribute("student_list", students);
		return "trainplan/plan_edit";
	}

	/**
	 * 将编辑好的数据更新到数据库
	 * 
	 * @param plan
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/plan_edit_save")
	public Object plan_edit_save(Plan plan,
			@RequestParam(value="partnerids")String[] partnerids,
			@RequestParam(value="students")String[] students, Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[trainPlan] F[plan_edit_save] U[{}] , params plan:{},partnerids:{},students{} .",
				users.getUser_id(),plan,partnerids,students);
		int i = planService.xUpdatePlan(plan, partnerids, students);
		logger.info("M[trainPlan] F[plan_edit_save] U[{}] , execute result:{}",
				users.getUser_id(),String.format("培训计划编辑%s!", i > 0 ? "成功" : "失败"));
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("培训计划信息编辑%s!", i > 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 根据plan_id删除指定培训计划信息
	 */
	@ResponseBody
	@RequestMapping("/plan_delete/{train_plan_id}")
	public Object plan_delete(@PathVariable("train_plan_id") Long train_plan_id, Authentication authentication) {
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[trainPlan] F[plan_delete] U[{}] , params train_plan_id:{} .",
				users.getUser_id(),train_plan_id);
		int i = planService.xDeletePlanByPrimaryKey(train_plan_id);
		logger.info("M[trainPlan] F[plan_delete] U[{}] , execute result:{}",
				users.getUser_id(),String.format("培训计划信息删除%s!", i > 0 ? "成功" : "失败"));
		Map<String, String> map = new HashMap<>();
		map.put("msg", String.format("培训计划信息删除%s!", i > 0 ? "成功" : "失败"));
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/get_train_time")
	public Object get_train_end(@DateTimeFormat(pattern="yyyy-MM-dd")Date train_begin,
			Long course_id,Long classroom_id) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//获取课程节次
		ChapterExample chapterExample = new ChapterExample();
		chapterExample.or().andCourse_idEqualTo(course_id);
		List<Chapter> chapters = chapterService.selectByExample(chapterExample);
		int size = chapters.size();
		//获取培训开始结束日期
		List<List<Date>> schedule_list = planService.get_schedule(train_begin, classroom_id, size);
		List<Date> first_section = schedule_list.get(0);
		Date start_date = first_section.get(0);
		//获取培训计划结束的时间
		List<Date> last_section = schedule_list.get(schedule_list.size()-1);
		Date end_date = last_section.get(0);
		Map<String, String> map = new HashMap<>();
		map.put("train_begin", sdf.format(start_date));
		map.put("train_end", sdf.format(end_date));
		return map;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {  
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	    dateFormat.setLenient(false);  
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
	}
}
