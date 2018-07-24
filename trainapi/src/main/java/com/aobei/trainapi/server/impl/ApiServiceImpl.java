package com.aobei.trainapi.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.Roles;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import com.aobei.trainapi.server.ApiService;
import com.aobei.trainapi.server.bean.*;
import com.aobei.trainapi.util.JacksonUtil;
import com.aobei.trainapi.util.MyFileHandleUtil;
import com.github.liyiorg.mbg.bean.Page;
import custom.bean.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ApiServiceImpl implements ApiService {

	@Autowired
	private CourseService courseService;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private PlanStudentService planStudentService;

	@Autowired
	private PlanService planService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private TrainScheduleService trainScheduleService;

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private ClassroomService classRoomService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private ExamApplyService examApplyService;

	@Autowired
	private ExamPlanService examPlanService;

	@Autowired
	private ExamSubjectService examSubjectService;

	@Autowired
	private CourseEvaluateService courseEvaluateService;

	@Autowired
	private CmsBannerService cmsBannerService;

	@Autowired
	private OssImgService ossImgService;
	@Autowired
	private CourseExamSubjectService courseExamSubjectService;
	@Autowired
	private MyFileHandleUtil myFileHandleUtil;
	@Autowired
	private StudentServiceitemService studentServiceitemService;
	@Autowired
	private ServiceitemService serviceitemService;


	Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);

	@Cacheable(value = "studentInfoByUserId", key = "'user_id:'+#user_id", unless = "#result == null")
	@Override
	public StudentInfo studentInfoByUserId(Long user_id) {
		logger.info("api-method:studentInfoByUserId:params user_id:{}", user_id);
		List<StudentImgInfo> jobCerts = new ArrayList<>();
		StudentInfo info = null;
		StudentExample studentExample = new StudentExample();
		studentExample.or()
				.andUser_idEqualTo(user_id)
				.andDeletedEqualTo(Status.DeleteStatus.no.value)
		        .andStateEqualTo(1);
		Student student = DataAccessUtils.singleResult(studentService.selectByExample(studentExample));
		logger.info("api-method:studentInfoByUserId:process student:{}", student);
		if (student != null) {
			// 头像url
			if (student.getLogo_img() != null) {
				long log_imd = Long.parseLong(student.getLogo_img());
				OssImg ossImg1 = ossImgService.selectByPrimaryKey(log_imd);
				student.setLogo_img(ossImg1.getUrl());
			}
			// 身份证正面url
			if (student.getCard_just() != null) {
				long card_just = Long.parseLong(student.getCard_just());
				OssImg ossImg2 = ossImgService.selectByPrimaryKey(card_just);
				String justurl = myFileHandleUtil.get_signature_url(ossImg2.getUrl(),
						3600l);
				student.setCard_just(justurl);
				StudentImgInfo studentJusturl = new StudentImgInfo();
				studentJusturl.setName("身份证");
				studentJusturl.setUrl(justurl);
				jobCerts.add(studentJusturl);
			}
			// 身份证反面URL
			if (student.getCard_against() != null) {
				long card_against = Long.parseLong(student.getCard_against());
				OssImg ossImg3 = ossImgService.selectByPrimaryKey(card_against);
				String againsturl = myFileHandleUtil.get_signature_url(ossImg3.getUrl(),
						3600l);
				student.setCard_against(againsturl);
			}
			// 健康证
			if (student.getHealth() != null) {
				long health = Long.parseLong(student.getHealth());
				OssImg ossImg4 = ossImgService.selectByPrimaryKey(health);
				String healthurl = myFileHandleUtil.get_signature_url(ossImg4.getUrl(),
						3600l);
				student.setHealth(healthurl);
				StudentImgInfo studentHealthurl = new StudentImgInfo();
				studentHealthurl.setName("健康证");
				studentHealthurl.setUrl(healthurl);
				jobCerts.add(studentHealthurl);
			}
			// 无犯罪记录
			if (student.getInnocence_proof() != null) {
				JSONObject jsonObject = JSONObject.parseObject(student.getInnocence_proof());
				Img img = jsonObject.toJavaObject(Img.class);
				OssImg ossImg5 = ossImgService.selectByPrimaryKey(img.getId());
				String innocenceProof = myFileHandleUtil.get_signature_url(ossImg5.getUrl(),
                        3600l);
				student.setInnocence_proof(innocenceProof);
				StudentImgInfo studentInnocenceProof = new StudentImgInfo();
				studentInnocenceProof.setName("无犯罪证明");
				studentInnocenceProof.setUrl(innocenceProof);
				jobCerts.add(studentInnocenceProof);
			}
			// 学员服务项
			StudentServiceitemExample studentServiceitemExample = new StudentServiceitemExample();
			studentServiceitemExample.or().andStudent_idEqualTo(student.getStudent_id());
			List<StudentServiceitem> itemList = studentServiceitemService.selectByExample(studentServiceitemExample);
			List<Serviceitem> items = new ArrayList<>();
			if (itemList.size() == 0) {
				items = new ArrayList<>();
			} else {
				List<Long> itemIds = itemList.stream().map(c -> c.getServiceitem_id()).collect(Collectors.toList());
				ServiceitemExample serviceitemExample = new ServiceitemExample();
				serviceitemExample.or().andServiceitem_idIn(itemIds).andStateEqualTo(1).andDeletedEqualTo(0);
				items = serviceitemService.selectByExample(serviceitemExample);
			}
			info = new StudentInfo();
			info.setServiceItems(items);
			BeanUtils.copyProperties(student, info);
			String job_cert = student.getJob_cert();
			List<Img> list = new ArrayList<>();
			if (!StringUtils.isEmpty(job_cert)) {
				try {
					list = JacksonUtil.json_to_object(job_cert, List.class, Img.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			info.setImgs(list);
			//图片信息封装一起
			list.stream().forEach(t->{
				StudentImgInfo studentJobcert = new StudentImgInfo();
				studentJobcert.setName("技能证书");
				studentJobcert.setUrl(t.getUrl());
				jobCerts.add(studentJobcert);
			});
			info.setImgUrl(jobCerts);
			Integer gradeDesc = student.getGrade();
			if (!StringUtils.isEmpty(gradeDesc)){
				switch (gradeDesc){
					case 1:info.setGradeDesc("高级");break;
					case 2:info.setGradeDesc("中级");break;
					case 3:info.setGradeDesc("初级");break;
				}
			}

		}
		return info;
	}

	/**
	 * 学员正在培训中的课程列表
	 *
	 * @return
	 */
	@Override
	public List<StudingTrainCourseInfo> studentCourseTeamPlan(Long student_id, int count) {
		logger.info("api-method:studentCourseTeamPlan:pramas student_id:{},count:{}", student_id, count);
		// 开始日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String s = sdf.format(new Date());
		Date currentDate = null;
		try {
			currentDate = sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		PlanStudentExample planStudentExample = new PlanStudentExample();
		planStudentExample.or().andStudent_idEqualTo(student_id).andTrain_beginLessThanOrEqualTo(currentDate)
				.andTrain_endGreaterThanOrEqualTo(currentDate).andDeletedEqualTo(Status.DeleteStatus.no.value);
		planStudentExample.includeColumns(PlanStudentExample.C.train_plan_id);
		// 获取学员正在培训中的培训计划ids
		List<PlanStudent> list = planStudentService.selectByExample(planStudentExample);
		if (list.size() == 0) {
			return new ArrayList<StudingTrainCourseInfo>();
		}
		List<Long> plan_ids = list.stream().map(n -> n.getTrain_plan_id()).collect(Collectors.toList());

		PlanExample planExample = new PlanExample();
		planExample.or().andTrain_plan_idIn(plan_ids);
		planExample.setOrderByClause(PlanExample.C.train_begin.name());
		// 获取培训ids
		Page<Plan> planList = planService.selectByExample(planExample, 1, count);
		if (planList.getList().size() == 0) {
			return new ArrayList<StudingTrainCourseInfo>();
		}
		List<Long> course_ids = planList.getList().stream().map(n -> n.getCourse_id()).collect(Collectors.toList());
		CourseExample courseExample = new CourseExample();
		courseExample.or().andCourse_idIn(course_ids).andStateEqualTo(1)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);
		courseExample.setOrderByClause(CourseExample.C.cdate.name());
		List<Course> courseList = courseService.selectByExample(courseExample);
		List<Plan> plans = planList.getList();
		return plans.stream().map(n -> {
			StudingTrainCourseInfo studingTrainCourseInfo = new StudingTrainCourseInfo();
			studingTrainCourseInfo.setPlan(n);
			Optional<Course> optional = courseList.stream().filter(cn -> cn.getCourse_id().equals(n.getCourse_id()))
					.findFirst();
			// 数据填充
			optional.ifPresent(e -> studingTrainCourseInfo.setCourse(e));
			studingTrainCourseInfo.setTeacher(teacherService.selectByPrimaryKey(n.getTeacher_id()));
			// 获取已学课程数
			TrainScheduleExample trainScheduleExample = new TrainScheduleExample();
			trainScheduleExample.or().andTrain_plan_idEqualTo(n.getTrain_plan_id())
					.andAttendclass_endtimeLessThan(new Date()).andDeletedEqualTo(Status.DeleteStatus.no.value);
			long studedCount = trainScheduleService.countByExample(trainScheduleExample);
			int studed = (int) (studingTrainCourseInfo.getCourse().getHour()
					/ studingTrainCourseInfo.getCourse().getChapter_count() * studedCount);
			studingTrainCourseInfo.setStuded(studed);
			logger.info("api-method:studentCourseTeamPlan:process studingTrainCourseInfo:{}", studingTrainCourseInfo);
			return studingTrainCourseInfo;
		}).collect(Collectors.toList());
	}

	@Override
	public Student studentByPhoneAndIdentityCard(String phone, String id_num) {
		logger.info("api-method:studentByPhoneAndIdentityCard:pramas phone:{},id_num:{}", phone, id_num);
		StudentExample studentExample = new StudentExample();
		studentExample.or().andPhoneEqualTo(phone).andIdentity_cardEqualTo(id_num)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);
		Student student = DataAccessUtils.singleResult(studentService.selectByExample(studentExample));
		logger.info("api-method:studentByPhoneAndIdentityCard:process student:{}", student);
		return student;
	}

	@Transactional(timeout = 2)
	public int bindStudent(Long user_id, Long student_id) {
		logger.info("api-method:bindStudent:pramas user_id:{},student_id:{}", user_id, student_id);
		Student student = new Student();
		student.setStudent_id(student_id);
		student.setUser_id(user_id);
		int count = studentService.updateByPrimaryKeySelective(student);
		logger.info("api-method:bindStudent:pramas count:{}", count);
		if (count > 0) {
			userAddRole(user_id, Roles.STUDENT.roleName());
		}
		return count;
	}

	@Override
	public int userAddRole(Long user_id, String roleName) {
		logger.info("api-method:userAddRole:pramas user_id:{},roleName:{}", user_id, roleName);
		UsersExample usersExample = new UsersExample();
		usersExample.or().andUser_idEqualTo(user_id).andUser_idIsNotNull();
		usersExample.includeColumns(UsersExample.C.user_id, UsersExample.C.roles);

		// 更新用户角色
		Users users = DataAccessUtils.singleResult(usersService.selectByExample(usersExample));
		String[] roles = (users.getRoles() == null ? roleName : users.getRoles()).split(",");
		Optional<String> optional = Stream.of(roles).filter(n -> n.equals(roleName)).findAny();
		if (!optional.isPresent()) {
			roles = StringUtils.addStringToArray(roles, roleName);
		}
		users.setRoles(StringUtils.arrayToCommaDelimitedString(roles));
		return usersService.updateByPrimaryKeySelective(users);
	}

	/**
	 * 获取学员有效的课程表
	 */
	@Override
	public List<TrainScheduleInfo> studentCourseInfoActiveList(Long student_id, int count) {
		logger.info("api-method:studentCourseInfoActiveList:pramas student_id:{},count:{}", student_id, count);
		PlanStudentExample planStudentExample = new PlanStudentExample();
		// planStudentExample.or().andStudent_idEqualTo(student_id).andTrain_beginGreaterThanOrEqualTo(new
		// Date()).andDeletedEqualTo(0).andTrain_endGreaterThanOrEqualTo(new
		// Date()); //获取学员的培训计划
		planStudentExample.or().andStudent_idEqualTo(student_id).andDeletedEqualTo(Status.DeleteStatus.no.value)
				.andTrain_endGreaterThanOrEqualTo(new Date());
		List<PlanStudent> list = planStudentService.selectByExample(planStudentExample);
		logger.info("api-method:studentCourseInfoActiveList:process list:{}", list);
		if (list.size() == 0) {
			return new ArrayList<TrainScheduleInfo>();
		}
		// 获取培训ids
		List<Long> plan_ids = list.stream().map(n -> n.getTrain_plan_id()).collect(Collectors.toList());
		// 课程排期
		TrainScheduleExample scheduleExample = new TrainScheduleExample();
		scheduleExample.or().andTrain_plan_idIn(plan_ids).andAttendclass_dateGreaterThanOrEqualTo(new Date())
				.andDeletedEqualTo(Status.DeleteStatus.no.value);
		scheduleExample.setOrderByClause(
				TrainScheduleExample.C.attendclass_date + "," + TrainScheduleExample.C.attendclass_starttime);
		// 获取培训课程表
		Page<TrainSchedule> page = trainScheduleService.selectByExample(scheduleExample, 1, count);
		return page.getList().stream().map(n -> {
			// 填充数据到 CourseInfo 类型
			TrainScheduleInfo trainScheduleInfo = new TrainScheduleInfo();
			trainScheduleInfo.setTrainSchedule(n);
			trainScheduleInfo.setTeacherInfo(teacherService.selectByPrimaryKey(n.getTeacher_id()));
			trainScheduleInfo.setClassroom(classRoomService.selectByPrimaryKey(n.getClassroom_id()));
			trainScheduleInfo.setSchool(schoolService.selectByPrimaryKey(n.getSchool_id()));
			trainScheduleInfo.setCourse(courseService.selectByPrimaryKey(n.getCourse_id()));
			trainScheduleInfo.setPlan(planService.selectByPrimaryKey(n.getTrain_plan_id()));
			trainScheduleInfo.setChapter(chapterService.selectByPrimaryKey(n.getChapter_id()));
			logger.info("api-method:studentCourseInfoActiveList:process trainScheduleInfo:{}", trainScheduleInfo);
			return trainScheduleInfo;
		}).collect(Collectors.toList());
	}

	@Override
	public TrainScheduleInfo trainScheduleInfo(Long train_schedule_id) {
		logger.info("api-method:trainScheduleInfo:pramas train_schedule_id:{}", train_schedule_id);
		TrainSchedule trainSchedule = trainScheduleService.selectByPrimaryKey(train_schedule_id);
		if (trainSchedule == null) {
			return new TrainScheduleInfo();
		}
		// 填充数据到 CourseInfo 类型
		TrainScheduleInfo courseInfo = new TrainScheduleInfo();
		courseInfo.setTeacherInfo(teacherService.selectByPrimaryKey(trainSchedule.getTeacher_id()));
		courseInfo.setSchool(schoolService.selectByPrimaryKey(trainSchedule.getSchool_id()));
		courseInfo.setClassroom(classRoomService.selectByPrimaryKey(trainSchedule.getClassroom_id()));
		courseInfo.setCourse(courseService.selectByPrimaryKey(trainSchedule.getCourse_id()));
		courseInfo.setTrainSchedule(trainSchedule);
		courseInfo.setPlan(planService.selectByPrimaryKey(trainSchedule.getTrain_plan_id()));
		courseInfo.setChapter(chapterService.selectByPrimaryKey(trainSchedule.getChapter_id()));
		logger.info("api-method:trainScheduleInfo:process courseInfo:{}", courseInfo);
		return courseInfo;
	}

	/**
	 * 学员考试申请记录
	 */
	@Override
	public List<ExamApply> examApplyList(Long student_id) {
		logger.info("api-method:examApplyList:pramas student_id:{}", student_id);
		ExamApplyExample examApplyExample = new ExamApplyExample();
		examApplyExample.or().andStudent_idEqualTo(student_id).andDeletedEqualTo(Status.DeleteStatus.no.value)
				.andScoreIsNotNull();
		examApplyExample.setOrderByClause(ExamApplyExample.C.apply_datetime + " desc");
		List<ExamApply> examApplyList = examApplyService.selectByExample(examApplyExample);
		logger.info("api-method:examApplyList:process examApplyList:{}", examApplyList);
		return examApplyList;
	}

	/**
	 * 获取学员整体考试记录
	 * 
	 * @return
	 */
	@Override
	public List<ExamApplyResult> examApplyInfoList(Long student_id) {
		logger.info("api-method:examApplyInfoList:pramas student_id:{}", student_id);
		// 获取学员考试申请记录
		List<ExamApply> list = examApplyList(student_id);
		if (list == null) {
			return new ArrayList<>();
		}
		return list.stream().map(n -> {
			// 填充数据
			ExamApplyResult examApplyResult = new ExamApplyResult();
			examApplyResult.setExamApply(n);
			examApplyResult.setExamPlan(examPlanService.selectByPrimaryKey(n.getExam_plan_id()));
			examApplyResult.setCourse(courseService
					.selectByPrimaryKey(planService.selectByPrimaryKey(n.getTrain_plan_id()).getCourse_id()));
			logger.info("api-method:examApplyInfoList:process examApplyResult:{}", examApplyResult);
			return examApplyResult;
		}).sorted((n1, n2) -> {
			return n1.getExamPlan().getExam_date().compareTo(n2.getExamPlan().getExam_date());
		}).collect(Collectors.toList());
	}

	/**
	 * 获取学员考试成绩数量
	 * 
	 * @return
	 */
	@Override
	public ExamScoreCounts examScoreCounts(Long student_id) {
		logger.info("api-method:examScoreCounts:pramas student_id:{}", student_id);
		List<ExamApply> list = examApplyList(student_id);
		long passedCount = list.stream().filter(n -> n.getPassed() != null && n.getPassed() == 1).count();
		ExamScoreCounts examScoreCounts = new ExamScoreCounts();
		examScoreCounts.setTotal(list.size());
		examScoreCounts.setPassed(passedCount);
		examScoreCounts.setFailed(list.size() - passedCount);
		logger.info("api-method:examScoreCounts:process examScoreCounts:{}", examScoreCounts);
		return examScoreCounts;
	}

	/**
	 * 获取学员培训记录
	 * 
	 * @return
	 */
	@Override
	public List<TrainScheduleInfo> studentTrainedCourseInfoList(Long student_id) {
		logger.info("api-method:studentTrainedCourseInfoList:pramas student_id:{}", student_id);
		// 学员培训计划
		PlanStudentExample planStudentExample = new PlanStudentExample();
		planStudentExample.or().andStudent_idEqualTo(student_id);
		// 获取学员的培训计划
		List<PlanStudent> list = planStudentService.selectByExample(planStudentExample);
		if (list.size() == 0) {
			return new ArrayList<TrainScheduleInfo>();
		}
		// 获取到培训ids
		List<Long> plan_ids = list.stream().map(n -> n.getTrain_plan_id()).collect(Collectors.toList());

		TrainScheduleExample scheduleExample = new TrainScheduleExample();
		scheduleExample.or().andTrain_plan_idIn(plan_ids).andAttendclass_endtimeLessThan(new Date())
				.andDeletedEqualTo(Status.DeleteStatus.no.value);
		// 日期降序
		scheduleExample.setOrderByClause(TrainScheduleExample.C.attendclass_date + " desc,"
				+ TrainScheduleExample.C.attendclass_starttime + " desc");
		// 获取培训课程表
		Page<TrainSchedule> page = trainScheduleService.selectByExample(scheduleExample, 1, 1000);
		return page.getList().stream().map(n -> {
			// 填充数据到 CourseInfo类型
			TrainScheduleInfo courseInfo = new TrainScheduleInfo();
			courseInfo.setTrainSchedule(n);
			courseInfo.setTeacherInfo(teacherService.selectByPrimaryKey(n.getTeacher_id()));
			courseInfo.setSchool(schoolService.selectByPrimaryKey(n.getSchool_id()));
			courseInfo.setClassroom(classRoomService.selectByPrimaryKey(n.getClassroom_id()));
			courseInfo.setPlan(planService.selectByPrimaryKey(n.getTrain_plan_id()));
			courseInfo.setCourse(courseService.selectByPrimaryKey(n.getCourse_id()));
			courseInfo.setChapter(chapterService.selectByPrimaryKey(n.getChapter_id()));
			logger.info("api-method:studentTrainedCourseInfoList:pramas courseInfo:{}", courseInfo);
			return courseInfo;
		}).collect(Collectors.toList());

	}

	@Override
	public List<CourseEvaluate> studentCourseEvaluate(Long student_id) {
		logger.info("api-method:studentCourseEvaluate:pramas student_id:{}", student_id);
		CourseEvaluateExample courseEvaluateExample = new CourseEvaluateExample();
		courseEvaluateExample.or().andStudent_idEqualTo(student_id);
		List<CourseEvaluate> courseExaluateList = courseEvaluateService.selectByExample(courseEvaluateExample);
		logger.info("api-method:studentCourseEvaluate:process courseExaluateList:{}", courseExaluateList);
		return courseExaluateList;
	}

	/*
	 * @Override public List<MessageInfo> userMessageInfoList(Long user_id, Date
	 * startDate) { MessageInfoExample messageInfoExample = new
	 * MessageInfoExample(); MessageInfoExample.Criteria criteria =
	 * messageInfoExample.or(); criteria.andUser_idEqualTo(user_id);
	 * if(startDate != null){
	 * criteria.andCreate_datetimeGreaterThanOrEqualTo(startDate); }
	 * messageInfoExample.setOrderByClause(MessageInfoExample.C.create_datetime.
	 * name()); return messageInfoService.selectByExample(messageInfoExample); }
	 */

	@Override
	public List<CmsBanner> cmsBannerOnlineList() {
		CmsBannerExample cmsBannerExample = new CmsBannerExample();
		cmsBannerExample.or().andSignEqualTo(1).andDeletedEqualTo(Status.DeleteStatus.no.value);
		cmsBannerExample.setOrderByClause(CmsBannerExample.C.serial_number.name());
		List<CmsBanner> cmsList = cmsBannerService.selectByExample(cmsBannerExample);
		logger.info("api-method:cmsBannerOnlineList:process cmsList:{}", cmsList);
		return cmsList;
	}

	@Override
	public List<OssImg> cmsBannerOnlineList(String position, String appid) {
		logger.info("api-method:bannerList:params appid:{},position:{}", appid, position);
		CmsBannerExample example = new CmsBannerExample();
		example.or().andAppEqualTo(appid).andSignEqualTo(1).andDeletedEqualTo(Status.DeleteStatus.no.value)
				.andOnline_datetimeLessThanOrEqualTo(new Date()).andOffline_datetimeGreaterThanOrEqualTo(new Date());

		List<CmsBanner> list = cmsBannerService.selectByExample(example);
		return list.stream().map(t -> {
			return ossImgService.selectByPrimaryKey(Long.parseLong(t.getImg_cover() == null ? "0" : t.getImg_cover()));
		}).collect(Collectors.toList());
	}

	@Override
	public Teacher teacherInfoByUserId(Long user_id) {
		TeacherExample teachExample = new TeacherExample();
		teachExample.or().andUser_idEqualTo(user_id).andDeletedEqualTo(Status.DeleteStatus.no.value);
		Teacher teacher = DataAccessUtils.singleResult(teacherService.selectByExample(teachExample));
		if(teacher != null){
			OssImg ossImg  = ossImgService.selectByPrimaryKey(Long.parseLong(teacher.getIcon() == null ? "0" : teacher.getIcon()));
			String url  = myFileHandleUtil.get_signature_url(ossImg.getUrl(),60*60l);
			teacher.setIcon(url);
			logger.info("api-method:teacherInfoByUserId:process teacher:{}", teacher);
		}
		return teacher;
	}

	@Override
	public Teacher teacherInfoByPhoneAndIdentityCard(String phone, String id_num) {
		logger.info("api-method:teacherInfoByPhoneAndIdentityCard:params phone:{},id_num:{}", phone, id_num);
		TeacherExample teacherExample = new TeacherExample();
		teacherExample.or().andPhoneEqualTo(phone).andIdentity_cardEqualTo(id_num)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);
		Teacher teacher = DataAccessUtils.singleResult(teacherService.selectByExample(teacherExample));
		logger.info("api-method:teacherInfoByPhoneAndIdentityCard:process teacher:{}", teacher);
		return teacher;
	}

	@Transactional(timeout = 2)
	public int bindTeacher(Long user_id, Long teacher_id) {
		Teacher teacher = new Teacher();
		teacher.setTeacher_id(teacher_id);
		teacher.setUser_id(user_id);
		int count = teacherService.updateByPrimaryKeySelective(teacher);
		if (count > 0) {
			userAddRole(user_id, Roles.TEACHER.roleName());
		}
		return count;
	}

	@Override
	public List<Image> images(Long[] img_ids) {
		OssImgExample ossImgExample = new OssImgExample();
		ossImgExample.or().andOss_img_idIn(Arrays.asList(img_ids));
		List<OssImg> list = ossImgService.selectByExample(ossImgExample);
		return list.stream().map(n -> {
			Image image = new Image();
			String str = n.getAccess_permissions();
			String url = null;
			if ("private".equals(str)) {
				url = myFileHandleUtil.get_signature_url( n.getUrl(), 3600l);
			} else {
				url = n.getUrl();
			}
			image.setImg_id(n.getOss_img_id());
			image.setPath(url);
			logger.info("api-method:images:process image:{}", image);
			return image;
		}).collect(Collectors.toList());
	}

	@Override
	public int addCourseEvaluate(Long train_schedule_id, Long student_id, Integer score, String comment) {
		TrainSchedule trainSchedule = trainScheduleService.selectByPrimaryKey(train_schedule_id);
		CourseEvaluate courseEvaluate = new CourseEvaluate();
		courseEvaluate.setComment(comment);
		courseEvaluate.setCreate_datetime(new Date());
		courseEvaluate.setScore(score);
		courseEvaluate.setId(IdGenerator.generateId());
		courseEvaluate.setStudent_id(student_id);
		courseEvaluate.setCourse_id(trainSchedule.getCourse_id());
		courseEvaluate.setTeacher_id(trainSchedule.getTeacher_id());
		courseEvaluate.setAssistant(trainSchedule.getAssistant());
		courseEvaluate.setChapter_id(trainSchedule.getChapter_id());
		courseEvaluate.setTrain_schedule_id(train_schedule_id);
		logger.info("api-method:addCourseEvaluate:process courseEvaluate:{}", courseEvaluate);
		return courseEvaluateService.insert(courseEvaluate);
	}

	@Override
	public boolean hasCourseEvaluate(Long train_schedule_id, Long student_id) {
		CourseEvaluateExample courseEvaluateExample = new CourseEvaluateExample();
		courseEvaluateExample.or().andStudent_idEqualTo(student_id).andTrain_schedule_idEqualTo(train_schedule_id);
		return courseEvaluateService.countByExample(courseEvaluateExample) > 0;
	}

	@Override
	public boolean studentAcceptTrainSchedule(Long train_schedule_id, Long student_id) {
		TrainSchedule trainSchedule = trainScheduleService.selectByPrimaryKey(train_schedule_id);
		PlanStudentExample planStudentExample = new PlanStudentExample();
		planStudentExample.or().andTrain_plan_idEqualTo(trainSchedule.getTrain_plan_id())
				.andStudent_idEqualTo(student_id).andStudent_idIsNotNull()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);
		return planStudentService.countByExample(planStudentExample) > 0;
	}

	@Override
	public List<TrainScheduleInfo> teacherCourseInfoActiveList(Long teacher_id, int count) {
		logger.info("api-method:teacherCourseInfoActiveList:params teacher_id:{},count:{}", teacher_id, count);
		TrainScheduleExample scheduleExample = new TrainScheduleExample();
		scheduleExample.or().andTeacher_idEqualTo(teacher_id).andAttendclass_dateGreaterThanOrEqualTo(new Date())
				.andTeacherAll(teacher_id).andDeletedEqualTo(Status.DeleteStatus.no.value);
		scheduleExample.setOrderByClause(
				TrainScheduleExample.C.attendclass_date + "," + TrainScheduleExample.C.attendclass_starttime);
		// 获取培训课程表(课程排期)
		Page<TrainSchedule> page = trainScheduleService.selectByExample(scheduleExample, 1, count);
		return page.getList().stream().map(n -> {
			// 填充数据到 CourseInfo 类型
			TrainScheduleInfo trainScheduleInfo = new TrainScheduleInfo();
			trainScheduleInfo.setTrainSchedule(n);
			trainScheduleInfo.setTeacherInfo(teacherService.selectByPrimaryKey(n.getTeacher_id()));
			trainScheduleInfo.setClassroom(classRoomService.selectByPrimaryKey(n.getClassroom_id()));
			trainScheduleInfo.setSchool(schoolService.selectByPrimaryKey(n.getSchool_id()));
			trainScheduleInfo.setCourse(courseService.selectByPrimaryKey(n.getCourse_id()));
			trainScheduleInfo.setPlan(planService.selectByPrimaryKey(n.getTrain_plan_id()));
			trainScheduleInfo.setChapter(chapterService.selectByPrimaryKey(n.getChapter_id()));
			logger.info("api-method:teacherCourseInfoActiveList:process trainScheduleInfo:{}", trainScheduleInfo);
			return trainScheduleInfo;
		}).collect(Collectors.toList());
	}

	/**
	 * 获取老师的课程表
	 * 
	 * @param count
	 * @return
	 */
	@Override
	public List<TrainScheduleInfo> teacherCourseInfoList(Long teacher_id, Date dateStart, Date dateEnd, int count) {
		logger.info("api-method:teacherCourseInfoList:params teacher_id:{},dateStart:{},dateEnd:{},count:{}",
				teacher_id, dateStart, dateEnd, count);
		TrainScheduleExample scheduleExample = new TrainScheduleExample();
		scheduleExample.or().andTeacher_idEqualTo(teacher_id).andAttendclass_dateBetween(dateStart, dateEnd)
				.andTeacherAll(teacher_id).andDeletedEqualTo(Status.DeleteStatus.no.value);
		// 老师上课日期
		scheduleExample.setOrderByClause(
				TrainScheduleExample.C.attendclass_date + "," + TrainScheduleExample.C.attendclass_starttime);
		// 获取培训课程表(老师)
		Page<TrainSchedule> page = trainScheduleService.selectByExample(scheduleExample, 1, count);
		return page.getList().stream().map(n -> {
			// 填充数据到 CourseInfo 类型
			TrainScheduleInfo trainScheduleInfo = new TrainScheduleInfo();
			trainScheduleInfo.setTrainSchedule(n);
			trainScheduleInfo.setTeacherInfo(teacherService.selectByPrimaryKey(n.getTeacher_id()));
			trainScheduleInfo.setClassroom(classRoomService.selectByPrimaryKey(n.getClassroom_id()));
			trainScheduleInfo.setSchool(schoolService.selectByPrimaryKey(n.getSchool_id()));
			trainScheduleInfo.setCourse(courseService.selectByPrimaryKey(n.getCourse_id()));
			trainScheduleInfo.setPlan(planService.selectByPrimaryKey(n.getTrain_plan_id()));
			trainScheduleInfo.setChapter(chapterService.selectByPrimaryKey(n.getChapter_id()));
			logger.info("api-method:teacherCourseInfoList:process trainScheduleInfo:{}", trainScheduleInfo);
			return trainScheduleInfo;
		}).collect(Collectors.toList());
	}

	/**
	 * 学员已学习课程
	 * 
	 * @return
	 */
	@Override
	public List<TrainedInfo> studentTrainedInfoList(Long student_id) {
		PlanStudentExample planStudentExample = new PlanStudentExample();
		planStudentExample.or().andStudent_idEqualTo(student_id).andTrain_endLessThan(new Date())
				.andDeletedEqualTo(Status.DeleteStatus.no.value);
		planStudentExample.setOrderByClause(PlanStudentExample.C.train_end.name() + " desc");
		// 学员培训计划
		List<PlanStudent> planStudents = planStudentService.selectByExample(planStudentExample);
		logger.info("api-method:studentTrainedInfoList:process planStudents:{}", planStudents);
		return planStudents.stream().map(n -> {
			TrainedInfo trainedInfo = new TrainedInfo();
			// 培训计划
			Plan plan = planService.selectByPrimaryKey(n.getTrain_plan_id());
			trainedInfo.setCourse(courseService.selectByPrimaryKey(plan.getCourse_id()));
			// 获取该类课程的所有考试计划
			// 1.课程考试关联
			// 2.考试科目
			// 3.考试计划
			/*
			 * CourseExamSubjectExample courseExamSubjectExample = new
			 * CourseExamSubjectExample();
			 * courseExamSubjectExample.or().andCourse_idEqualTo(plan.
			 * getCourse_id()); List<CourseExamSubject> list_ces =
			 * courseExamSubjectService.selectByExample(courseExamSubjectExample
			 * ); List<Long> list = list_ces.stream().map(a ->
			 * a.getExam_subject_id()).collect(Collectors.toList());
			 * ExamSubjectExample ese = new ExamSubjectExample();
			 * ese.or().andTypeEqualTo(2).andExam_subject_idIn(list);
			 * List<ExamSubject> list_esj =
			 * examSubjectService.selectByExample(ese); List<Long> list_ids =
			 * list_esj.stream().map(b ->
			 * b.getExam_subject_id()).collect(Collectors.toList());
			 * ExamPlanExample examPlanExam = new ExamPlanExample();
			 * examPlanExam.includeColumns(ExamPlanExample.C.exam_plan_id);
			 * examPlanExam.or().andExam_subject_idIn(list_ids);
			 */
			// 考试计划
			// List<ExamPlan> examPlanList =
			// examPlanService.selectByExample(examPlanExam);
			// 获取考试计划ids
			// List<Long> examPlanIds = examPlanList.stream().map(ep ->
			// ep.getExam_plan_id()).collect(Collectors.toList());
			// 设置学员该课程学习是否已经通过
			// TODO 后期需优化，直接使用 planStudent.passed 字段
			// if(examPlanIds.size()>0){
			ExamApplyExample examApplyExample = new ExamApplyExample();
			examApplyExample.or().andTrain_plan_idEqualTo(n.getTrain_plan_id()).andStudent_idEqualTo(student_id);
			List<ExamApply> examApplies = examApplyService.selectByExample(examApplyExample);
			// int passed = 1;
			/*
			 * for(ExamApply examApply : examApplies){ if(examApply.getPassed()
			 * == null || examApply.getPassed() == 0){ passed = 0; break; } }
			 */
			trainedInfo.setExamApplys(examApplies);
			// n.setPassed(passed);
			/*
			 * }else{ n.setPassed(0); }
			 */
			trainedInfo.setPlanStudent(n);
			trainedInfo.setPlan(plan);
			logger.info("api-method:studentTrainedInfoList:process trainedInfo:{}", trainedInfo);
			return trainedInfo;
		}).collect(Collectors.toList());
	}

	@Override
	public Student traineeInformation(Long student_id) {
		return studentService.selectByPrimaryKey(student_id);
	}

	@Override
	public Teacher teacherinformation(Long teacher_id) {
		return teacherService.selectByPrimaryKey(teacher_id);
	}

	@Override
	public Course courseTeaminformation(Long course_id) {
		return courseService.selectByPrimaryKey(course_id);
	}

	@Override
	public Plan planinformation(Long train_plan_id) {
		return planService.selectByPrimaryKey(train_plan_id);
	}

	@Override
	public TrainSchedule trainScheduleinformation(Long id) {
		return trainScheduleService.selectByPrimaryKey(id);
	}

	@Override
	public ExamPlan examPlaninformation(Long id) {
		return examPlanService.selectByPrimaryKey(id);
	}

	@Override
	public ExamApply examApplyinformation(Long id) {
		return examApplyService.selectByPrimaryKey(id);
	}

	@Override
	public CourseEvaluate courseEvaluateinformation(Long id) {
		return courseEvaluateService.selectByPrimaryKey(id);
	}

	@Override
	public CmsBanner cmsBannerinformation(Long id) {
		return cmsBannerService.selectByPrimaryKey(id);
	}

	@Override
	public School schoolinformation(Long school_id) {
		return schoolService.selectByPrimaryKey(school_id);
	}

	@Override
	public Classroom classRoominformation(Long classroom_id) {
		return classRoomService.selectByPrimaryKey(classroom_id);
	}

	@Override
	public Chapter courseSectionInformation(Long chapter_id) {
		return chapterService.selectByPrimaryKey(chapter_id);
	}


}
