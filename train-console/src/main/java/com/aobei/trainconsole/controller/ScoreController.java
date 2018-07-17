package com.aobei.trainconsole.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aobei.train.model.ExamApplyExample.Criteria;
import com.aobei.trainconsole.util.PoiExcelExport;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.Score;
import custom.bean.Status;
 
@Controller
@RequestMapping("/scoreInfoManager/scoreInfo")
public class ScoreController {
	
	@Autowired
	private PartnerService partnerService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private ExamApplyService examApplyService;
	@Autowired
	private PlanStudentService planStudentService;
	@Autowired
	private ExamSubjectService examSubjectService;
	@Autowired
	private CourseExamSubjectService courseExamSubjectService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private PlanService planService;
	@Autowired
	private OperateLogService operateLogService;


	private static Logger logger = LoggerFactory.getLogger(ScoreController.class);
	
	
	/**
	 * 成绩信息列表展示
	 * @param model
	 * @param p
	 * @param partnerId
	 * @param course
	 * @param conditions
	 * @param ps
	 * @return
	 */
	@RequestMapping("/scoreInfoList")
	public String scoreInfoList(
			Model model,
			String conditions,
			@RequestParam(defaultValue="1") Integer p,
			@RequestParam(defaultValue="0")Long partnerId,
			@RequestParam(defaultValue="0")Long course,
			@RequestParam(defaultValue="10") Integer ps){

		List<Score> listScore = new ArrayList<>();
		//Page<Score> page = null;
		ExamApplyExample examApplyExample = new ExamApplyExample();
		Criteria cri = examApplyExample.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		if(partnerId!=0){
			cri.andPartner_idEqualTo(partnerId);
		}
		if(course!=0){
			CourseExamSubjectExample courseExamSubjectExample = new CourseExamSubjectExample();
			courseExamSubjectExample.or()
					.andCourse_idEqualTo(course);
			CourseExamSubject courseExamSubject = DataAccessUtils.singleResult(courseExamSubjectService.selectByExample(courseExamSubjectExample));
			if(courseExamSubject!=null){
				cri.andExam_subject_idEqualTo(courseExamSubject.getExam_subject_id());
			}
		}
		if(conditions!=null){
			StudentExample studentExample = new StudentExample();
			studentExample.or()
					.andNameLike("%"+conditions+"%");
			List<Student> students = studentService.selectByExample(studentExample);
			List<Long> ids = students.stream().map(n->{
				Long student_id = n.getStudent_id();
				return student_id;
			}).collect(Collectors.toList());
			cri.andStudent_idIn(ids);

		}


		Page<ExamApply> page = examApplyService.selectByExample(examApplyExample,p,ps);
		for (ExamApply examApplys : page.getList()) {
			//根据学生id和培训计划表确定一条数据
			PlanStudentExample example = new PlanStudentExample();
			com.aobei.train.model.PlanStudentExample.Criteria criteria = example.or();
			criteria.andStudent_idEqualTo(examApplys.getStudent_id())
					.andTrain_endEqualTo(examApplys.getApply_datetime())
					.andTrain_plan_idEqualTo(examApplys.getExam_plan_id());

			CourseExamSubjectExample courseExamSubjectExample = new CourseExamSubjectExample();
			courseExamSubjectExample.or()
					.andExam_subject_idEqualTo(examApplys.getExam_subject_id());
			CourseExamSubject courseExamSubject = DataAccessUtils.singleResult(courseExamSubjectService.selectByExample(courseExamSubjectExample));

			PlanExample planExample = new PlanExample();
			planExample.or()
					.andCourse_idEqualTo(courseExamSubject.getCourse_id())
					.andTrain_endEqualTo(examApplys.getApply_datetime())
					.andTrain_plan_idEqualTo(examApplys.getTrain_plan_id());
			Plan plan = DataAccessUtils.singleResult(planService.selectByExample(planExample));

			//if(plan!=null){
				Student student = studentService.selectByPrimaryKey(examApplys.getStudent_id());

				if(student!=null ){	//学员在职
					//列表展示对象，放到集合
					Score score = new Score();
					score.setScore_manager_id(examApplys.getExam_apply_id());
					score.setName(student.getName());
					score.setSex(student.getSex());
					score.setId_card(student.getIdentity_card());
					score.setPhone(student.getPhone());
					score.setAge(student.getAge());
					score.setCourse_name(courseExamSubject.getCourse_id());
					if(plan!=null) {
						score.setTrain_time(plan.getTrain_begin());
					}
					score.setFrom_partner(examApplys.getPartner_id());
					/* ---------------------------- */

					if(examApplys.getScore()==null){
						score.setTest_score(null);
						score.setIf_qualified(null);
					}else{
						score.setIf_qualified((int)examApplys.getPassed());
						score.setTest_score(examApplys.getScore());
					}
					//加此段代码会过滤学员已删除的课程信息
					/*CourseExample courseExample = new CourseExample();
					courseExample.or()
							.andCourse_idEqualTo(score.getCourse_name())
							.andDeletedEqualTo(0);
					List<Course> list_score_course = courseService.selectByExample(courseExample);*/

						listScore.add(score);
						//page.setList(listScore);
					//}
				}


			//}
		}
		//page = new Page<>(listScore,listScore.size(),p,ps);
		//合伙人所有信息
		/*PartnerExample partnerExample = new PartnerExample();
		partnerExample.or()
				.andStateEqualTo(1)//上线
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0*/
		List<Partner> list_partner = partnerService.selectByExample(new PartnerExample());
		//课程所有信息
		CourseExample courseExample = new CourseExample();
		courseExample.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<Course> list_course = courseService.selectByExample(courseExample);

		model.addAttribute("conditions", conditions);
		model.addAttribute("list_partner", list_partner);
		model.addAttribute("page", page);
		model.addAttribute("current", p);
		model.addAttribute("course", course);
		model.addAttribute("partnerId", partnerId);
		model.addAttribute("list_score", listScore);

		model.addAttribute("list_course", list_course);
		model.addAttribute("number", (page.getPageNo()-1)*page.getPageSize());
		return "scoreManager/scoreManage_list"; 
	}
	/**
	 * 删除方法
	 * @param scoreId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteScore")
	public Object deleteScore(@RequestParam(value="scoreId")Long scoreId,Authentication authentication){
		int i = examApplyService.xDelete(scoreId);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[score] F[delete_score] U[{}] param[scoreId:{}];  result:{}",
				users.getUser_id(),scoreId, String.format("删除成绩信息%s" , i > 0 ? "成功":"失败"));
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("delMessage", String.format("删除信息%s" , i > 0 ? "成功":"失败"));
		return map;
	}
	/**
	 * 跳转编辑页面，指定一个对象
	 * @param model
	 * @param scoreId
	 * @param current
	 * @return
	 */
	@RequestMapping("/gotoEditScore")
	public String gotoEditScore(Model model,
			@RequestParam(value="scoreId",required=false)Long scoreId,
			@RequestParam(value="p") Integer current){
		ExamApplyExample applyExample = new ExamApplyExample();
		Criteria criteria = applyExample.or();
		criteria
				.andExam_apply_idEqualTo(scoreId)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		ExamApply examApply = DataAccessUtils.singleResult(examApplyService.selectByExample(applyExample));
		
		PartnerExample partnerExample = new PartnerExample();
		com.aobei.train.model.PartnerExample.Criteria criteria2 = partnerExample.or();
		criteria2
				.andPartner_idEqualTo(examApply.getPartner_id())
				.andStateEqualTo(1)//上线
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		Partner partner = DataAccessUtils.singleResult(partnerService.selectByExample(partnerExample));
		
		
		ExamSubjectExample examSubjectExample = new ExamSubjectExample();
		com.aobei.train.model.ExamSubjectExample.Criteria criteria3 = examSubjectExample.or();
		criteria3
				.andExam_subject_idEqualTo(examApply.getExam_subject_id())
				.andDeletedEqualTo(Status.DeleteStatus.no.value)
				.andTypeEqualTo(2);
		ExamSubject examSubject = DataAccessUtils.singleResult(examSubjectService.selectByExample(examSubjectExample));
		Score score = new Score();
		if(partner!=null){
			model.addAttribute("partner", partner);
		}else{
			model.addAttribute("partner", null);
		}
		if(examSubject!=null) {
			CourseExamSubjectExample courseExamSubjectExample = new CourseExamSubjectExample();
			courseExamSubjectExample.or()
					.andExam_subject_idEqualTo(examSubject.getExam_subject_id());
			CourseExamSubject singleResult = DataAccessUtils.singleResult(courseExamSubjectService.selectByExample(courseExamSubjectExample));

			CourseExample courseExample = new CourseExample();
			courseExample.or()
					.andCourse_idEqualTo(singleResult.getCourse_id());
			Course course = DataAccessUtils.singleResult(courseService.selectByExample(courseExample));
			score.setCourse_name(singleResult.getCourse_id());
			model.addAttribute("course", course);
		}else{
			//score.setCourse_name(null);
			model.addAttribute("course", null);
		}
			//if(course!=null){
				StudentExample studentExample = new StudentExample();
				com.aobei.train.model.StudentExample.Criteria criteria4 = studentExample.or();
				criteria4.andStudent_idEqualTo(examApply.getStudent_id())
						 .andStateEqualTo(Status.DeleteStatus.yes.value);//1
				Student student = DataAccessUtils.singleResult(studentService.selectByExample(studentExample));

				score.setScore_manager_id(examApply.getExam_apply_id());
				score.setName(student.getName());
				score.setAge(student.getAge());

				score.setId_card(student.getIdentity_card());
				score.setPhone(student.getPhone());
				score.setTest_score(examApply.getScore());
				score.setTrain_time(examApply.getApply_datetime());
				//score.setFrom_partner(partner.getPartner_id());
				score.setSex(student.getSex());
				
				model.addAttribute("score", score);
				model.addAttribute("current", current);
				model.addAttribute("examApply", examApply);
				model.addAttribute("examSubject", examSubject);
				//model.addAttribute("course", course);

				model.addAttribute("student", student);
			//}

		return "scoreManager/scoreManage_edit";
	}
	
	/**
	 * 修改方法，指定一个对象
	 * @param scoreId
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editScoreInfo")
	public Object updateScoreInfo(@RequestParam(value = "scoreId", required = false) Long scoreId, Authentication authentication,
			HttpServletRequest req) {
		String test_score = req.getParameter("test_score");
		HashMap<String,Object> map = examApplyService.xUpdate(scoreId, test_score);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[score] F[edit_score] U[{}] param[scoreId:{},test_score:{}];  result:{}",
				users.getUser_id(),scoreId, test_score,map.get("editMessage"));
		return map;
	}
	/**
	 * 导入方法
	 * @param file
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/upload")
	public Object upload(MultipartFile file, Authentication authentication){
		HashMap<String, Object> map = examApplyService.scoreImport(file);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[score] F[import_score] U[{}] param[multipartFile:{}];  result:{}",
				users.getUser_id(),file,map.get("result"));
		return map;
	}
	/**
	 * 导出方法
	 * @param response
	 */
	@RequestMapping("/exportExcel")
	public void exportExcel(HttpServletResponse response,Authentication authentication){
		
		String titleName[] ={"姓名","性别","身份证号","联系方式","年龄","课程名称","培训日期","隶属合伙人","考试成绩","是否合格"};
		String titleColumn[] = {"name","sex","id_card","phone","age","course_name","train_time","from_partner","test_score","if_qualified"};
		int titleSize[] = {20,20,20,20,20,20,20,20,20,20,20,};
		
		List<ScoreInfoExport> listScore = new ArrayList<ScoreInfoExport>();
		ExamApplyExample examApplyExample = new ExamApplyExample();
		examApplyExample.or().andDeletedEqualTo(0);
		List<ExamApply> list_examApply = examApplyService.selectByExample(examApplyExample);
		for (ExamApply examApplys : list_examApply) {
			// 根据学生id和培训计划表确定一条数据
			PlanStudentExample example = new PlanStudentExample();
			com.aobei.train.model.PlanStudentExample.Criteria criteria = example.or();
			criteria.andStudent_idEqualTo(examApplys.getStudent_id())
					.andTrain_endEqualTo(examApplys.getApply_datetime())
					.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
			// criteria.andTrain_plan_idEqualTo(examApplys.getTrain_plan_id());
			PlanStudent planStudent = DataAccessUtils.singleResult(planStudentService.selectByExample(example));
			if (planStudent != null) {
				// if(examApply.getScore()!=null){
				Student student = studentService.selectByPrimaryKey(examApplys.getStudent_id());
				ExamSubject examSubject = examSubjectService.selectByPrimaryKey(examApplys.getExam_subject_id());

				CourseExamSubjectExample courseExamSubjectExample = new CourseExamSubjectExample();
				courseExamSubjectExample.or()
						.andExam_subject_idEqualTo(examSubject.getExam_subject_id());
				CourseExamSubject singleResult = DataAccessUtils
						.singleResult(courseExamSubjectService.selectByExample(courseExamSubjectExample));

				CourseExample courseExample = new CourseExample();
				courseExample.or()
						.andCourse_idEqualTo(singleResult.getCourse_id());
				Course course = DataAccessUtils.singleResult(courseService.selectByExample(courseExample));
				if (course != null) {
					// 导出成绩封装对象
					ScoreInfoExport se = new ScoreInfoExport();
					se.setScore_manager_id(examApplys.getExam_apply_id());
					se.setName(student.getName());
					se.setSex(student.getSex());
					se.setId_card(student.getIdentity_card());
					se.setPhone(student.getPhone());
					se.setAge(student.getAge());
					se.setCourse_name(course.getName());
					Date train_time = examApplys.getApply_datetime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String format = sdf.format(train_time);
					se.setTrain_time(format);
					Partner partner = partnerService.selectByPrimaryKey(student.getPartner_id());
					se.setFrom_partner(partner.getName());
					if (examApplys.getScore() == null) {
						se.setTest_score(null);
						se.setIf_qualified(null);
					} else {
						se.setTest_score(examApplys.getScore());
						se.setIf_qualified((int) examApplys.getPassed());
					}
					listScore.add(se);
				}
			}
		}
		PoiExcelExport export = new PoiExcelExport(response, "scoreInfo", "sheet1");
		export.wirteExcel(titleColumn, titleName, titleSize, listScore);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[score] F[export_score] U[{}] param[list_data:{}];result:导出数据{}条",
				users.getUser_id(),listScore,listScore.size());
		OperateLog operateLog = new OperateLog();
		operateLog.setOperate_log_id(IdGenerator.generateId());
		operateLog.setOperate("M[score] F[export_score] U["+users.getUsername()+"] D[size:"+listScore.size()+"] ");
		operateLog.setUser_id(users.getUser_id());
		operateLog.setCreate_datetime(new Date());
		operateLogService.insertSelective(operateLog);
	}
	/**
	 * 下载导入模板
	 * @param request
	 * @param response
	 */
	@RequestMapping("/downLoadModel")
	public void downLoadModel(HttpServletRequest request,HttpServletResponse response,Authentication authentication) {
		
		try {
			File file =ResourceUtils.getFile("classpath:templates/scoreManager/成绩信息导入模板.xlsx");
			String path = file.getAbsolutePath();
			InputStream is = new FileInputStream(path);
			XSSFWorkbook workbook;
			try {
				workbook = new XSSFWorkbook(is);
				XSSFSheet sheet = workbook.getSheetAt(0);
				int rowIndex = 0;
				sheet.getRow(rowIndex++);
				rowIndex++;
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
	        	response.setCharacterEncoding("utf-8");
	        	response.setHeader("Content-Disposition","attachment;filename=score.xlsx");
	        	ServletOutputStream out = response.getOutputStream();
	        	workbook.write(out);
	        	out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[score] F[download_template] U[{}] param[无];result:download the scoreExcel template was successful!",
				users.getUser_id());
		OperateLog operateLog = new OperateLog();
		operateLog.setOperate_log_id(IdGenerator.generateId());
		operateLog.setOperate("M[score] F[download_template] U["+users.getUsername()+"] ");
		operateLog.setUser_id(users.getUser_id());
		operateLog.setCreate_datetime(new Date());
		operateLogService.insertSelective(operateLog);
	}
}

