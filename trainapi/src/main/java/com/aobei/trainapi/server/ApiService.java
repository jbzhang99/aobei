package com.aobei.trainapi.server;

import java.util.Date;
import java.util.List;
import com.aobei.train.model.*;
import com.aobei.trainapi.server.bean.*;


public interface ApiService {

	
	/**
	 * 获取学员培训中的课程信息
	 * @param student_id
	 * @return
	 */
	List<StudingTrainCourseInfo> studentCourseTeamPlan(Long student_id,int count);
	
	
	
	/**
	 * 获取学员信息
	 * @param user_id
	 * @return
	 */
	StudentInfo studentInfoByUserId(Long user_id);
	
	/**
	 * 获取学员信息
	 * @param phone
	 * @param id_num
	 * @return
	 */
	Student studentByPhoneAndIdentityCard(String phone,String id_num);
	
	/**
	 * 绑定学员信息
	 * @param user_id
	 * @param student_id
	 * @return
	 */
	int bindStudent(Long user_id,Long student_id);
	
	/**
	 * 用户添加角色
	 * @param user_id
	 * @param roleName
	 * @return
	 */
	int userAddRole(Long user_id,String roleName);
	
	/**
	 * 获取学员有效的课程表<br>
	 * 未上课的课程
	 * @param student_id
	 * @param count 条数
	 * @return
	 */
	List<TrainScheduleInfo> studentCourseInfoActiveList(Long student_id,int count);
	
	/**
	 * 获取课程表排期详情<br>
	 * @param train_schedule_id id
	 * @return
	 */
	TrainScheduleInfo trainScheduleInfo(Long train_schedule_id);
	
	/**
	 * 获取学员的报考记录
	 * @param student_id
	 * @return
	 */
	List<ExamApply> examApplyList(Long student_id);
	
	/**
	 * 获取学员的考试记录
	 * @param student_id
	 * @return
	 */
	List<ExamApplyResult> examApplyInfoList(Long student_id);
	
	/**
	 * 获取学员成绩统计数量
	 * @param student_id
	 * @return
	 */
	ExamScoreCounts examScoreCounts(Long student_id);
	
	/**
	 * 获取学员 已培训的课程章节信息
	 * @param student_id
	 * @return
	 */
	List<TrainScheduleInfo> studentTrainedCourseInfoList(Long student_id);
	
	/**
	 * 获取学员的评分列表
	 * @param student_id
	 * @return
	 */
	List<CourseEvaluate> studentCourseEvaluate(Long student_id);
	
	/**
	 * 获取用户消息列表
	 * @param user_id
	 * @param startDate
	 * @return
	 */
	/*List<MessageInfo> userMessageInfoList(Long user_id,Date startDate);*/
	
	/**
	 * 获取有效的banner 图
	 * @return
	 */
	List<CmsBanner> cmsBannerOnlineList();
	List<OssImg> cmsBannerOnlineList(String position,String appid);
	/**
	 * 获取老师信息
	 * @param user_id
	 * @return
	 */
	Teacher teacherInfoByUserId(Long user_id);
	
	/**
	 * 获取老师信息
	 * @param phone
	 * @param id_num
	 * @return
	 */
	Teacher teacherInfoByPhoneAndIdentityCard(String phone,String id_num);
	
	/**
	 * 绑定老师信息
	 * @param user_id
	 * @param teacher_id
	 * @return
	 */
	int bindTeacher(Long user_id,Long teacher_id);
	
	/**
	 * 获取图片访问路径
	 * @param img_ids
	 * @return
	 */
	List<Image> images(Long[] img_ids);
	
	/**
	 * 添加培训课程评价
	 * @param train_schedule_id
	 * @param student_id
	 * @param score
	 * @param comment
	 * @return
	 */
	int addCourseEvaluate(Long train_schedule_id,Long student_id,Integer score,String comment);


	/**
	 * 判断是否已经评价
	 * @param train_schedule_id
	 * @param student_id
	 * @return
	 */
	boolean hasCourseEvaluate(Long train_schedule_id,Long student_id);
	
	/**
	 * 判断学员是否参与了这个培训章节
	 * @param train_schedule_id
	 * @param student_id
	 * @return
	 */
	boolean studentAcceptTrainSchedule(Long train_schedule_id,Long student_id);

	/**
	 * 获取老师有效的课程表<br>
	 * 未上课的课程
	 * @param count 条数
	 * @return
	 */
	List<TrainScheduleInfo> teacherCourseInfoActiveList(Long teacher_id,int count);
	
	/**
	 * 获取老师的课程记录
	 * @param teacher_id
	 * @param dateStart
	 * @param dateEnd
	 * @param count
	 * @return
	 */
	List<TrainScheduleInfo> teacherCourseInfoList(Long teacher_id,Date dateStart,Date dateEnd,int count);
	
	/**
	 * 获取学员已培训完成的课程信息及通过情况
	 * @param student_id
	 * @return
	 */
	List<TrainedInfo> studentTrainedInfoList(Long student_id);

	/**
	 * 根据student_id查询学员信息
	 * @param student_id
	 * @return
	 */
	Student traineeInformation(Long student_id);
	
	/**
	 * 根据teacher_id查询学员信息
	 * @param teacher_id
	 * @return
	 */
	Teacher teacherinformation(Long teacher_id);
	
	/**
	 * 根据team_id查询课程信息
	 * @param team_id
	 * @return
	 */
	Course courseTeaminformation(Long team_id);
	
	/**
	 * 根据plan_id查询课程信息
	 * @param plan_id
	 * @return
	 */
	Plan planinformation(Long plan_id);
	
	/**
	 * 根据id查询课程表详情信息
	 * @param id
	 * @return
	 */
	TrainSchedule trainScheduleinformation(Long id);
	
	/**
	 * 根据id查询考试安排
	 * @param id
	 * @return
	 */
	ExamPlan examPlaninformation(Long id);
	
	/**
	 * 根据id查询考试信息
	 * @param id
	 * @return
	 */
	ExamApply examApplyinformation(Long id);
	
	/**
	 * 根据id查询课程评价
	 * @param id
	 * @return
	 */
	CourseEvaluate courseEvaluateinformation(Long id);
	
	/**
	 * 根据id查询消息
	 * @param id
	 * @return
	 */
	/*MessageInfo messageinformation(Long id);*/
	
	/**
	 * 根据id查询 cms_banner
	 * @param id
	 * @return
	 */
	CmsBanner cmsBannerinformation(Long id);
	
	/**
	 * 根据school_id查询学校信息
	 * @param school_id
	 * @return
	 */
	School schoolinformation(Long school_id);
	
	/**
	 * 根据classroom_id查询教室信息
	 * @return
	 */
	Classroom classRoominformation(Long classroom_id);
	
	/**
	 * 根据id查询上课排期
	 * @param id
	 * @return
	 */
	/*TrainSchedule trainScheduleinformation(Long id);*/
	
	/**
	 * 根据course_id查询课程章节信息
	 * @return
	 */
	Chapter courseSectionInformation(Long chapter_id);


}