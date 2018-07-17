package com.aobei.trainapi.schema;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.aobei.train.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*import com.aobei.train.model.CourseEvaluate;
import com.aobei.train.model.CourseTeam;*/
/*import com.aobei.train.model.MessageInfo;*/
/*import com.aobei.train.model.TeacherInfo;*/
import com.aobei.trainapi.server.ApiService;
import com.aobei.trainapi.server.bean.CourseInfoEvaluate;
import com.aobei.trainapi.server.bean.ExamApplyResult;
/*import com.aobei.trainapi.server.bean.CourseInfoEvaluate;
import com.aobei.trainapi.server.bean.ExamApplyResult;*/
import com.aobei.trainapi.server.bean.ExamScoreCounts;
import com.aobei.trainapi.server.bean.Image;
import com.aobei.trainapi.server.bean.StudentInfo;
import com.aobei.trainapi.server.bean.StudingTrainCourseInfo;
import com.aobei.trainapi.server.bean.TrainScheduleInfo;
import com.aobei.trainapi.server.bean.TrainedInfo;
/*import com.aobei.trainapi.server.bean.StudingTrainCourseInfo;
import com.aobei.trainapi.server.bean.TrainScheduleInfo;
import com.aobei.trainapi.server.bean.TrainedInfo;*/
import com.coxautodev.graphql.tools.GraphQLQueryResolver;

@Component
public class Query implements GraphQLQueryResolver {

	@Autowired
	private TokenUtil TOKEN;

	@Autowired
	private ApiService apiService;


	/**
	 * 绑定的学员信息
	 * @return
	 */
	public StudentInfo my_student_bindinfo(){
		StudentInfo student = apiService.studentInfoByUserId(TOKEN.getUuid());
		if(student == null){
			Errors._40101.throwError();
		}
		return student;
	}

	/**
	 * 学员正在培训中的课程列表
	 * @return 
	 */
	public List<StudingTrainCourseInfo> my_student_course_team_active_list(int count){
		Student student = my_student_bindinfo();
		return apiService.studentCourseTeamPlan(student.getStudent_id(),count);
	}

	/**
	 * 获取学员的课程排期列表
	 * @param count
	 * @return
	 */
	public List<TrainScheduleInfo> my_student_courseinfo_active_list(int count){
		Student student = my_student_bindinfo();
		return apiService.studentCourseInfoActiveList(student.getStudent_id(), count);
	}
	
	/**
	 * 获取课程排期详情
	 * @param count
	 * @return
	 */
	public TrainScheduleInfo train_schedule_info(Long train_schedule_id){
		TrainScheduleInfo courseInfo = apiService.trainScheduleInfo(train_schedule_id);
		if(courseInfo == null){
			Errors._40001.throwError();
		}
		return courseInfo;
	}
	
	/**
	 * 获取学员整体考试记录
	 * @return
	 */
	public List<ExamApplyResult> my_student_exam_apply_result(){
		Student student = my_student_bindinfo();
		return apiService.examApplyInfoList(student.getStudent_id());
	}
	
	/**
	 * 获取学员考试成绩数量
	 * @return
	 */
	public ExamScoreCounts my_student_exam_score_counts(){
		Student student = my_student_bindinfo();
		return apiService.examScoreCounts(student.getStudent_id());
	}
	
	/**
	 * 获取学员培训记录
	 * @return 
	 */
	public List<CourseInfoEvaluate> my_student_trained_courseinfo_list(){
		//学员
		Student student = my_student_bindinfo();
		//培训记录
		List<TrainScheduleInfo> list = apiService.studentTrainedCourseInfoList(student.getStudent_id());
		//培训评价
		List<CourseEvaluate> evaluateList = apiService.studentCourseEvaluate(student.getStudent_id());
		Map<Long,CourseEvaluate> evaluateMap = evaluateList.stream().collect(Collectors.toMap(CourseEvaluate::getTrain_schedule_id,Function.identity()));
		return list.stream().map(n -> {
			CourseInfoEvaluate  cie = new CourseInfoEvaluate();
			cie.setTrainScheduleInfo(n);
			cie.setCourseEvaluate(evaluateMap.get(n.getTrainSchedule().getTrain_schedule_id()));
			return cie;
		}).collect(Collectors.toList());
	}
	
	/**
	 * 获取用户的消息列表
	 * @param startDate
	 * @return
	 */
	/*public List<MessageInfo> user_message_info_list(Date startDate){
		return apiService.userMessageInfoList(TOKEN.getUuid(), startDate);
	}*/
	
	/**
	 * 老师端 bannar 图片
	 * @return
	 */
	public List<CmsBanner> teacher_cms_bannar_online_list(){
		return apiService.cmsBannerOnlineList();
	}
	
	/**
	 * 获取图片信息
	 * @param ids
	 * @return
	 */
	public List<Image> images(List<Long> ids){
		return apiService.images(ids.toArray(new Long[ids.size()]));
	}
	
	/**
	 * 绑定的老师信息
	 * @return
	 */
	public Teacher my_teacher_bindinfo(){
		Teacher teacher = apiService.teacherInfoByUserId(TOKEN.getUuid());
		if(teacher == null){
			Errors._40110.throwError();
		}
		return teacher;
	}
	
	/**
	 * 获取老师的课程表
	 * @param count
	 * @return
	 */
	public List<TrainScheduleInfo> my_teacher_courseinfo_active_list(int count){
		Teacher teacher = my_teacher_bindinfo();
		return apiService.teacherCourseInfoActiveList(teacher.getTeacher_id(), count);
	}
	
	/**
	 * 获取老师的课程表
	 * @param count
	 * @return
	 */
	public List<TrainScheduleInfo> my_teacher_courseinfo_list(Date dateStart, Date dateEnd, int count) {
		Teacher teacher = my_teacher_bindinfo();
		return apiService.teacherCourseInfoList(teacher.getTeacher_id(), dateStart, dateEnd, count);
	}
	
	/** 
	 * 学员已学习课程
	 * @return
	 */
	public List<TrainedInfo> my_student_trainedinfo_list() {
		Student student = my_student_bindinfo();
		return apiService.studentTrainedInfoList(student.getStudent_id());
	}
	
	/**
	 * 根据user_id查询老师信息
	 * @return
	 */
	public Teacher id_teacher_info(Long user_id ){
		if(user_id == null){
			Errors._40111.throwError();
		}
		return  apiService.teacherInfoByUserId(user_id);
	}
	
	/**
	 * 根据user_id查询学生信息
	 * @return
	 */
	public Student id_student_info(Long user_id){
		if(user_id == null){
			Errors._40111.throwError();
		}
		return apiService.studentInfoByUserId(user_id);
	}
	
	/**
	 * 根据student_id查询学生信息
	 */
	public Student id_student_info_by_student_id(Long student_id){
		if(student_id == null){
			Errors._40112.throwError();
		}
		return apiService.traineeInformation(student_id);
	}
	
	/**
	 * 根据teacher_id查询老师信息
	 */
	public Teacher id_teacher_info_by_teacher_id(Long teacher_id){
		if(teacher_id == null){
			Errors._40113.throwError();
		}
		return apiService.teacherinformation(teacher_id);
	}
	
	/**
	 * 根据course_id查询出课程信息
	 */
	public Course id_course_information_by_team_Id(Long course_id){
		if(course_id == null){
			Errors._40114.throwError();
		}
		return apiService.courseTeaminformation(course_id);
	}
	
	/**
	 * 根据train_plan_id查询培训计划
	 */
	public Plan id_training_plan_by_plan_id(Long train_plan_id){
		if(train_plan_id == null){
			Errors._40115.throwError();
		}
		return apiService.planinformation(train_plan_id);
	}
	
	/**
	 * 根据exam_plan_id查询考试安排
	 */
	public ExamPlan id_examPlan_arrangement_by_id(Long exam_plan_id){
		if(exam_plan_id == null){
			Errors._40114.throwError();
		}
		return apiService.examPlaninformation(exam_plan_id);
	}
	
	/**
	 * 根据exam_apply_id查询考试信息
	 */
	public ExamApply id_examApply_information_by_id(Long exam_apply_id){
		if(exam_apply_id == null){
			Errors._40114.throwError();
		}
		return apiService.examApplyinformation(exam_apply_id);
	}
	
	/**
	 * 根据id查询课程评价
	 */
	public CourseEvaluate id_courseEvaluate_by_id(Long id){
		if(id == null){
			Errors._40114.throwError();
		}
		return apiService.courseEvaluateinformation(id);
	}
	
	/**
	 * 根据id查询消息
	 */
	/*public MessageInfo id_messageInfo_information_by_id(Long id){
		return apiService.messageinformation(id);
	}*/
	
	/**
	 * 根据cms_bannar_id查询CmsBanner
	 */
	public CmsBanner id_cmsBanner_information_by_content_id(Long cms_bannar_id){
		if(cms_bannar_id == null){
			Errors._40114.throwError();
		}
		return apiService.cmsBannerinformation(cms_bannar_id);
	}
	
	/**
	 * 根据school_id查询学校信息
	 */
	public School id_school_information_by_school_id(Long school_id){
		if(school_id == null){
			Errors._40114.throwError();
		}
		return apiService.schoolinformation(school_id);
	}
	
	/**
	 * 根据classroom_id查询教室信息
	 */
	public Classroom id_classroom_information_by_classroom_id(Long classroom_id){
		if(classroom_id == null){
			Errors._40114.throwError();
		}
		return apiService.classRoominformation(classroom_id);
	}
	
	/**
	 * 根据train_schedule_id查询上课排期
	 */
	public TrainSchedule id_trainSchedule_information_by_id(Long train_schedule_id){
		if(train_schedule_id == null){
			Errors._40114.throwError();
		}
		return apiService.trainScheduleinformation(train_schedule_id);
	}
	
	/**
	 * 根据chapter_id查询课程章节信息
	 */
	public Chapter id_chapter_section_information_by_id(Long chapter_id){
		if(chapter_id == null){
			Errors._40114.throwError();
		}
		return apiService.courseSectionInformation(chapter_id);
	}
	
	
	
}
