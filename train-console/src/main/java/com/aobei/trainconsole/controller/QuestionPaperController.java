package com.aobei.trainconsole.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.OperateLogService;
import com.aobei.train.service.UsersService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.aobei.train.service.ExamSubjectService;
import com.aobei.train.service.QuestionPaperService;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.Option;
import custom.bean.Paper;
import custom.bean.QuestionPaperJson;
import custom.bean.Status;

@Controller
@RequestMapping("/questionPaperManager/questionPaper")
public class QuestionPaperController {

	@Autowired
	private QuestionPaperService questionPaperService;
	@Autowired
	private ExamSubjectService examSubjectService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private OperateLogService operateLogService;


	private static Logger logger = LoggerFactory.getLogger(QuestionPaperController.class);
	
	@RequestMapping("/questionPaperList")
	public String questionPaperList(Model model, @RequestParam(defaultValue = "1") Integer p,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(defaultValue = "10") Integer ps) {
		QuestionPaperExample questionPaperExample = new QuestionPaperExample();
		questionPaperExample.or()
				.andDeletedEqualTo(Status.DeleteStatus.no.value);
		Page<QuestionPaper> page = questionPaperService.selectByExample(questionPaperExample,p, ps);
		
		ExamSubjectExample examSubjectExample = new ExamSubjectExample();
		examSubjectExample.or()
				.andTypeEqualTo(2)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<ExamSubject> list_examSubject = examSubjectService.selectByExample(examSubjectExample);
		// 试卷号序号递增
		int pageSize = page.getPageSize();
		int pageNo = page.getPageNo(); 
		int number = pageSize*(pageNo-1);
		model.addAttribute("list_examSubject", list_examSubject);
		model.addAttribute("number", number);
		model.addAttribute("current", p);
		model.addAttribute("ps", ps);
		model.addAttribute("questionPaper", page.getList());
		model.addAttribute("page", page);
		return "questionPaper/questionPaper_list";
	}

	/**
	 * 跳转到添加页面
	 * @param model
	 * @param current
	 * @return
	 */
	@RequestMapping("/gotoAddQuestionPaper")
	public String gotoAddQuestionPaper(Model model, @RequestParam(value = "p", required = false) Integer current) {
//		QuestionBankManagerExample example1 = new QuestionBankManagerExample();
//		example1.or().andQuestion_typeEqualTo(1);
//		List<QuestionBankManager> list_singleSelect = questionBankManagerService.selectByExample(example1);
//
//		QuestionBankManagerExample example2 = new QuestionBankManagerExample();
//		example2.or().andQuestion_typeEqualTo(2);
//		List<QuestionBankManager> list_multiple = questionBankManagerService.selectByExample(example2);
//
//		QuestionBankManagerExample example3 = new QuestionBankManagerExample();
//		example3.or().andQuestion_typeEqualTo(3);
//		List<QuestionBankManager> list_checking = questionBankManagerService.selectByExample(example3);
//
//		List<CourseTeam> list_course = courseTeamService.selectByExample(new CourseTeamExample());
//		model.addAttribute("list_singleSelect", list_singleSelect);
//		model.addAttribute("list_multiple", list_multiple);
//		model.addAttribute("list_checking", list_checking);
//		model.addAttribute("list_course", list_course);
//		model.addAttribute("current", current);
		return "questionPaper/questionPaper_add";
	}

	/**
	 * 添加方法，题数据为json格式
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addQuestionPaper")
	public Object addQuestionPaper(HttpServletRequest request,Model model,Authentication authentication) {
		String name = request.getParameter("name");
		String train_course = request.getParameter("train_course");
		String singleCount = request.getParameter("singleCount");
		String multipartCount = request.getParameter("multipartCount");
		//返回map，提示信息
		HashMap<String,String> map = questionPaperService.xInsert(singleCount, multipartCount, train_course, model, name);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[questionPaper] F[add_questionPaper] U[{}]; param[name:{};train_course:{};singleCount:{};multipartCount:{}]; result:{}",
				users.getUser_id(),name,train_course,singleCount,multipartCount,map.get("message"));
		return map;
	}

	/**
	 * 删除试卷信息
	 * @param testPaerId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteQuestionPaper")
	public Object deleteQuestionPaper(@RequestParam(value = "question_paper_id", required = false) Long testPaerId,Authentication authentication) {
		int i = questionPaperService.xDelete(testPaerId);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[questionPaper] F[delete_questionPaper] U[{}];试卷id:testPaerId {}]; result:{}",
				users.getUser_id(),testPaerId,String.format("删除试卷信息%s", i > 0 ? "成功" : "失败"));
		// 删除题信息
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("delMessage", String.format("删除试卷信息%s", i > 0 ? "成功" : "失败"));
		return map;
	}

	/**
	 * 跳转到修改试卷信息页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/gotoEditQuestionPaper")
	public String gotoEditQuestionPaper(Model model,
			@RequestParam(value = "testpaper_id", required = false) Long testPaperId,
			@RequestParam(value = "p", required = false) Integer current) {
		// 回显课程
//		QuestionPaperManager testpaper = questionPaperManagerService.selectByPrimaryKey(testPaperId);
//		String course_name = testpaper.getCourse_name();
//		CourseTeamExample course = new CourseTeamExample();
//		course.or().andTeam_nameEqualTo(course_name);
//		List<CourseTeam> back_course = courseTeamService.selectByExample(course);
//		// 全部课程
//		List<CourseTeam> list_course = courseTeamService.selectByExample(new CourseTeamExample());
//		/*
//		 * for (CourseTeam courseTeam : back_course) { Long team_id =
//		 * courseTeam.getTeam_id(); model.addAttribute("team_id", team_id); }
//		 */
//		// 显示所有题
//		List<QuestionBankManager> allQuestion = questionBankManagerService
//				.selectByExample(new QuestionBankManagerExample());
//		// 所有已经选中的题
//		List<Long> list_allQuestion_checked = new ArrayList<Long>();
//		QuestionFTestPaperExample allExample = new QuestionFTestPaperExample();
//		allExample.or().andTestpaper_idEqualTo(testPaperId);
//		List<QuestionFTestPaper> list_allQuestion_checked_paper = questionFTestPaperService.selectByExample(allExample);
//		for (QuestionFTestPaper questionFTestPaper : list_allQuestion_checked_paper) {
//			Long question_id = questionFTestPaper.getQuestion_id();
//			list_allQuestion_checked.add(question_id);
//		}
//		// 所有题分为对应的三个集合,页面需要分别显示三种题
//		QuestionBankManagerExample example1 = new QuestionBankManagerExample();
//		example1.or().andQuestion_typeEqualTo(1);
//		List<QuestionBankManager> list_singleSelect = questionBankManagerService.selectByExample(example1);
//
//		QuestionBankManagerExample example2 = new QuestionBankManagerExample();
//		example2.or().andQuestion_typeEqualTo(2);
//		List<QuestionBankManager> list_multiple = questionBankManagerService.selectByExample(example2);
//
//		QuestionBankManagerExample example3 = new QuestionBankManagerExample();
//		example3.or().andQuestion_typeEqualTo(3);
//		List<QuestionBankManager> list_checking = questionBankManagerService.selectByExample(example3);
//
//		model.addAttribute("list_singleSelect", list_singleSelect);
//		model.addAttribute("list_multiple", list_multiple);
//		model.addAttribute("list_checking", list_checking);
//		model.addAttribute("allQuestion", allQuestion);
//		model.addAttribute("list_allQuestion_checked", list_allQuestion_checked);
//		model.addAttribute("current", current);
//		model.addAttribute("back_course", back_course);
//		model.addAttribute("list_course", list_course);
//		model.addAttribute("testPaperId", testPaperId + "");
		// 携带当前页参数，所有试题信息，回显已经选择的试题信息。
		return "questionPaper/questionPaper_edit";
	}

	/**
	 * 修改试卷信息
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateQuestionPaper")
	public Object updateQuestionPaper(HttpServletRequest request,
			@RequestParam(value = "testpaper_id") Long testpaperId) {
		// 转换课程id显示为name
//		String courseId = request.getParameter("courseId");
//		CourseTeamExample example = new CourseTeamExample();
//		example.or().andTeam_idEqualTo(Long.parseLong(courseId));
//		List<CourseTeam> list_course = courseTeamService.selectByExample(example);
//		CourseTeam team = list_course.get(0);
//		String teamName = team.getTeam_name();
//
//		String parameter = request.getParameter("result");
//		JSONObject jsonObject = JSONObject.parseObject(parameter);
//		// 将数据json对象封装为map，map的key在前台拼接时指定。
//		Map<String, Object> map = jsonObject;
//		// 拿到所有选择的单选题id
//		List<Long> singles = JSONObject.parseArray(map.get("splist").toString(), Long.class);
//		// 拿到所有已选择的多选题id
//		List<Long> multiples = JSONObject.parseArray(map.get("mplist").toString(), Long.class);
//		// 拿到所有已选择的判断题id
//		List<Long> checkings = JSONObject.parseArray(map.get("clist").toString(), Long.class);
//		int singleSize = singles.size();
//		int multipleSize = multiples.size();
//		int checkingSize = checkings.size();
//		// 创建一个新的集合，把单个对应题型集合遍历放到同一个集合中。
//		List<Long> allQuestion = new ArrayList<Long>();
//		if (singleSize != 0) {
//			for (int i = 0; i < singles.size(); i++) {
//				allQuestion.add(singles.get(i));
//			}
//		}
//		if (multipleSize != 0) {
//			for (int i = 0; i < multiples.size(); i++) {
//				allQuestion.add(multiples.get(i));
//			}
//		}
//		if (checkingSize != 0) {
//			for (int i = 0; i < checkings.size(); i++) {
//				allQuestion.add(checkings.get(i));
//			}
//		}
//		// 创建试卷对象，为试卷对象的每个属性赋值。
//		QuestionPaperManager questionPaperManager = new QuestionPaperManager();
//		questionPaperManager.setTestpaper_id(testpaperId);
//		questionPaperManager.setQuestion_type_SingleSelection(singleSize);
//		questionPaperManager.setQuestion_type_MultipleChoice(multipleSize);
//		questionPaperManager.setQuestion_type_checking(checkingSize);
//		questionPaperManager.setCourse_name(teamName);
//		// 修改试卷信息到试卷表
//		int i = questionPaperManagerService.updateByPrimaryKey(questionPaperManager);
//		// 遍历题号集合，把中间表信息放到list中批量插入。
//		List<QuestionFTestPaper> questionFTestPaperList = allQuestion.stream().map(n -> {
//			QuestionFTestPaper questionFTestPaper = new QuestionFTestPaper();
//			questionFTestPaper.setTestpaper_id(questionPaperManager.getTestpaper_id());
//			questionFTestPaper.setQuestion_id(n);
//			questionFTestPaper.setId(IdGenerator.generateId());
//			return questionFTestPaper;
//		}).collect(Collectors.toList());
//		// 批量插入所有题到中间表
//		int[] k = questionFTestPaperService.batchUpdateByPrimaryKeySelective(
//				questionFTestPaperList.toArray(new QuestionFTestPaper[questionFTestPaperList.size()]));
		HashMap<String, String> result = new HashMap<String, String>();
//		if (i > 0 & k.length == allQuestion.size()) {
//			result.put("editMessage", "试卷修改成功");
//		} else {
//			result.put("editMessage", "试卷修改失败");
//		}
//		// resultMap.put("addMessage", String.format("试卷添加%s", i > 0 ? "成功" :
//		// "失败"));
		return result;
	}

	/**
	 * 试卷导出方法
	 * @param question_paper_id
	 * @param model
	 * @param response
	 * @throws FileNotFoundException
	 */
	@ResponseBody
	@RequestMapping("/printOut")
	public void printOut(@RequestParam(value = "question_paper_id", required = false) Long question_paper_id, Model model,HttpServletResponse response,Authentication authentication) {

		
//		List<Question> list_question = new ArrayList<Question>();
//		List<Question> list_checking = new ArrayList<Question>();
		List<QuestionPaperJson> t1 = null;
		List<QuestionPaperJson> t2 = null;
		
		//查询对应id的试卷，然后遍历json数据，拿到数据导出
		QuestionPaperExample questionPaperExample = new QuestionPaperExample();
		questionPaperExample.or().andQuestion_paper_idEqualTo(question_paper_id);
		List<QuestionPaper> list_questionId = questionPaperService.selectByExampleWithBLOBs(questionPaperExample);
		for (QuestionPaper questionPaper : list_questionId) {
			String question_json = questionPaper.getQuestion_json();
			Paper paper = JSONObject.parseObject(question_json, Paper.class);
			//拿到当前试卷的单集合json
				//单选题集合
				 t1 = paper.getT1();
				 t2 = paper.getT2();
				for (QuestionPaperJson questionPaperJson : t1) {
					List<Option> option_json = questionPaperJson.getOption_json();
					for (Option option : option_json) {
						Question question = new Question();
						question.setAnswer( questionPaperJson.getAnswer());
						question.setTopic(questionPaperJson.getTopic());
						question.setExam_subject_id(questionPaper.getExam_subject_id());
						question.setOption_json(question_json);
					}
				}
			}
		
		
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("akdj");
		HSSFRow createRow = sheet.createRow(0);
		Cell createCell1 = createRow.createCell(0);
		createCell1.setCellValue("一、单选题");
		Cell createCell = createRow.createCell(1);
		createCell.setCellValue("    ");
		for (int j = 0; j < t1.size(); j++) {
			List<Option> list_option = t1.get(j).getOption_json();
				
			// 创建所有行对象
			HSSFRow createRow2 = sheet.createRow(1 + j * 6);
			HSSFRow createRow3 = sheet.createRow(2 + j * 6);
			HSSFRow createRow4 = sheet.createRow(3 + j * 6);
			HSSFRow createRow5 = sheet.createRow(4 + j * 6);
			HSSFRow createRow6 = sheet.createRow(5 + j * 6);
			// 创建所有列对象设置值
			Cell createCell2 = createRow2.createCell(0);
			createCell2.setCellValue(j + 1 + ".");
			Cell createCell21 = createRow2.createCell(1);
			createCell21.setCellValue(t1.get(j).getTopic());
			Cell createCell22 = createRow2.createCell(2);
			createCell22.setCellValue(t1.get(j).getAnswer());
			for (int i =0;i<list_option.size();i++){

			Cell createCell3 = createRow3.createCell(0);
			createCell3.setCellValue("A.");
			HSSFCell createCell23 = createRow3.createCell(1);
			createCell23.setCellValue(list_option.get(0).getO());
			

			Cell createCel24 = createRow4.createCell(0);
			createCel24.setCellValue("B.");
			HSSFCell createCel26 = createRow4.createCell(1);
			createCel26.setCellValue(list_option.get(1).getO());

			Cell createCell25 = createRow5.createCell(0);
			createCell25.setCellValue("C.");
			HSSFCell createCel25 = createRow5.createCell(1);
			createCel25.setCellValue(list_option.get(2).getO());

			Cell createCell26 = createRow6.createCell(0);
			createCell26.setCellValue("D.");
			HSSFCell createCell27 = createRow6.createCell(1);
			createCell27.setCellValue(list_option.get(3).getO());
			}
		}
		int s = sheet.getLastRowNum() + 1;
		HSSFRow createRow7 = sheet.createRow(s);
		Cell createCellM1 = createRow7.createCell(0);
		createCellM1.setCellValue("二、多选题");
		Cell createCellM11 = createRow7.createCell(1);
		createCellM11.setCellValue("    ");
		for (int j = 0; j < t2.size(); j++) {
			List<Option> list_option = t2.get(j).getOption_json();
			for (int i =0;i<list_option.size();i++){
			
			HSSFRow createRow2 = sheet.createRow(s + 1 + j * 6);
			HSSFRow createRow3 = sheet.createRow(s + 2 + j * 6);
			HSSFRow createRow4 = sheet.createRow(s + 3 + j * 6);
			HSSFRow createRow5 = sheet.createRow(s + 4 + j * 6);
			HSSFRow createRow6 = sheet.createRow(s + 5 + j * 6);

			Cell createCell2 = createRow2.createCell(0);
			createCell2.setCellValue(j + 1 + ".");//
			Cell createCell21 = createRow2.createCell(1);
			createCell21.setCellValue(t2.get(j).getTopic());
			Cell createCell22 = createRow2.createCell(2);
			createCell22.setCellValue(t2.get(j).getAnswer());

			Cell createCell3 = createRow3.createCell(0);
			createCell3.setCellValue("A.");
			HSSFCell createCell23 = createRow3.createCell(1);
			createCell23.setCellValue(list_option.get(0).getO());

			Cell createCel24 = createRow4.createCell(0);
			createCel24.setCellValue("B.");
			HSSFCell createCel26 = createRow4.createCell(1);
			createCel26.setCellValue(list_option.get(1).getO());

			Cell createCell25 = createRow5.createCell(0);
			createCell25.setCellValue("C.");
			HSSFCell createCel25 = createRow5.createCell(1);
			createCel25.setCellValue(list_option.get(2).getO());

			Cell createCell26 = createRow6.createCell(0);
			createCell26.setCellValue("D.");
			HSSFCell createCell27 = createRow6.createCell(1);
			createCell27.setCellValue(list_option.get(3).getO());
		}
		}
		/*int lastMultipart = sheet.getLastRowNum();
		HSSFRow createRowC = sheet.createRow(lastMultipart + 1);
		Cell createCellC1 = createRowC.createCell(0);
		createCellC1.setCellValue("三、判断题");
		Cell createCellC11 = createRowC.createCell(1);
		createCellC11.setCellValue("    ");
		for (int i = 0; i < list_checking.size(); i++) {
			HSSFRow createRow2 = sheet.createRow(lastMultipart + 2 + i * 2);

			Cell createCell2 = createRow2.createCell(0);
			createCell2.setCellValue(i + 1 + ".");
			Cell createCell21 = createRow2.createCell(1);
			createCell21.setCellValue(list_checking.get(i).getTopic());
			Cell createCell22 = createRow2.createCell(2);
			createCell22.setCellValue(list_checking.get(i).getAnswer());

		}*/
		for (int i = 0; i < 2; i++) {
			sheet.autoSizeColumn(i);
		}
			try {
				response.setContentType("application/vnd.ms-excel; charset=utf-8");
	        	response.setHeader("Content-Disposition","attachment;filename=testPaper.xls");
	        	response.setCharacterEncoding("utf-8");
	        	ServletOutputStream out = response.getOutputStream();
				workbook.write(out);				
				out.close();
				Users users = usersService.xSelectUserByUsername(authentication.getName());
				logger.info("M[questionPaper] F[export_paper] U[{} param[question_paper_id:{},singleList:{},multipartList:{}] result:导出{}条数据]",
						users.getUser_id(),question_paper_id,t1,t2,t1.size()+t2.size());
			} catch (IOException e) {
				e.printStackTrace();
			}
					
	}
}