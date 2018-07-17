package com.aobei.trainconsole.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.aobei.train.IdGenerator;
import com.aobei.train.model.*;
import com.aobei.train.service.OperateLogService;
import com.aobei.train.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.service.ExamSubjectService;
import com.aobei.train.service.QuestionService;
import com.github.liyiorg.mbg.bean.Page;

import custom.bean.Option;
import custom.bean.Status;

@Controller
@RequestMapping("/questionBankManager/question")
public class QuestionBankController {
	
	@Autowired
	private QuestionService questionService;
	@Autowired
	private ExamSubjectService examSubjectService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private OperateLogService operateLogService;


	private static Logger logger = LoggerFactory.getLogger(QuestionBankController.class);
	/**
	 * 查询试题集合展示
	 * @param question_type
	 * @param course_name
	 * @param model
	 * @return 
	 */
	@RequestMapping("/questionBankList")
	public String questionBankDetail(@RequestParam(defaultValue="0") Integer question_type,
			@RequestParam(defaultValue="0") Long course_name,Model model,
			@RequestParam(defaultValue="1")Integer p,
			@RequestParam(defaultValue="10")Integer ps){
		QuestionExample example = new QuestionExample();
		QuestionExample.Criteria citeria = example.or();
		//做一个排序操作
		if(question_type !=0){
			citeria.andTypeEqualTo(question_type);
		}
		if(course_name !=0){
			citeria.andExam_subject_idEqualTo(course_name);
		}
		citeria
			.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		example.setOrderByClause(QuestionExample.C.create_datetime+" desc");
		Page<Question> page = questionService.selectByExample(example, p, ps);
		ExamSubjectExample examSubjectExample = new ExamSubjectExample();
		examSubjectExample.or()
			.andTypeEqualTo(2)
			.andDeletedEqualTo(Status.DeleteStatus.no.value);//0	
		List<ExamSubject> list_examSubject = examSubjectService.selectByExample(examSubjectExample);
		//题号根据页数顺序自增
		int number = page.getPageSize()*(page.getPageNo()-1);
		List<HashMap<Long,List<Option>>> list = questionService.getOptionList(page.getList());
		model.addAttribute("list_option", list);
		model.addAttribute("number", number);
		model.addAttribute("ps", ps);
		model.addAttribute("pageList", page.getList());
		model.addAttribute("page", page);
		model.addAttribute("list_examSubject", list_examSubject);
		model.addAttribute("current", p);
		model.addAttribute("course_name", course_name);
		model.addAttribute("question_type", question_type);
		
		return "questionBank/questionBank_list";
	}
	/**
	 * 执行添加题库操作
	 * @param model
	 * @return
	 */
	@ResponseBody
	@Transactional
	@RequestMapping("/addQuestionBank")
	public Object addQuestionBank(Model model,Question question,HttpServletRequest request,Authentication authentication){
		String option_json = request.getParameter("param");
		String subject_id = request.getParameter("course");
		String topic = request.getParameter("topic");
		String answer = request.getParameter("answer");
		int i = questionService.xInsert(topic, subject_id, answer, option_json, question);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[question] F[add_question] U[{}]; param[科目id：subject_id{},选项集合json数据：option_json{},topic{},answer{},question{}]; result:{}",
				users.getUser_id(),subject_id,option_json,topic,answer,question,String.format("题库信息添加%s!", i > 0 ? "成功":"失败"));
		ExamSubjectExample examSubjectExample = new ExamSubjectExample();
		examSubjectExample.or()
				.andTypeEqualTo(2)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<ExamSubject> list_examSubject = examSubjectService.selectByExample(examSubjectExample);
		model.addAttribute("list_examSubject", list_examSubject );
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("addMessage",String.format("题库信息添加%s!", i > 0 ? "成功":"失败"));
		return map;
		//"redirect:/questionBankDetail"
	}
	//跳转到添加页面
	@RequestMapping("/gotoAddQuestionBank")
	public String gotoAdd(Model model,@RequestParam(value="p")Integer current){
		//课程数据回显
		ExamSubjectExample examSubjectExample = new ExamSubjectExample();
		examSubjectExample.or()
				.andTypeEqualTo(2)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<ExamSubject> list_examSubject = examSubjectService.selectByExample(examSubjectExample);
		model.addAttribute("list_examSubject", list_examSubject );
		model.addAttribute("current", current);
		return "questionBank/questionBank_add";
	}
	/**
	 * 刪除题库信息
	 * @param question_id
	 * @return
	 */
	@ResponseBody
	@Transactional
	@RequestMapping("/deleteQuestionBank")
	public Object deleteQuestionBank(@RequestParam()Long question_id,Authentication authentication){
		int i = questionService.xDeleteById(question_id);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[question] F[delete_question] U[{}]; param[题号id:question_id{}]; result:{}",
				users.getUser_id(),question_id,String.format("题库信息删除%s", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<String,String>();
		map.put("message", String.format("题库信息删除%s", i > 0 ? "成功" : "失败"));
		return map;
	}
	//跳转到修改信息页面
	@RequestMapping("/gotoEditQuestionBank")
	public String gotoEditQuestionBank(Model model,@RequestParam()Long question_id,@RequestParam(value="p")Integer current){
		Question editQuestion = questionService.selectByPrimaryKey(question_id);
		JSONArray parseArray = JSONObject.parseArray(editQuestion.getOption_json());
		for (int i = 0; i < parseArray.size(); i++) {
			Option option = parseArray.getObject(0, Option.class);
			Option option1 = parseArray.getObject(1, Option.class);
			Option option2 = parseArray.getObject(2, Option.class);
			Option option3 = parseArray.getObject(3, Option.class);
			model.addAttribute("option", option);
			model.addAttribute("option1", option1);
			model.addAttribute("option2", option2);
			model.addAttribute("option3", option3);
		}
		ExamSubjectExample examSubjectExample = new ExamSubjectExample();
		examSubjectExample.or()
				.andTypeEqualTo(2)
				.andDeletedEqualTo(Status.DeleteStatus.no.value);//0
		List<ExamSubject> list_examSubject = examSubjectService.selectByExample(examSubjectExample);
		model.addAttribute("list_examSubject", list_examSubject);
		model.addAttribute("editQuestion", editQuestion);
		model.addAttribute("current", current);
		return "questionBank/questionBank_edit";
	}
	/**
	 * 修改题库信息
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateQuestionBank")
	@Transactional
	public Object updateQuestionBank(HttpServletRequest request,Question question,@RequestParam(value="question_id")Long questionId,
									 Authentication authentication){
		String option_json = request.getParameter("param");
		String subject_id = request.getParameter("course");
		String topic = request.getParameter("topic");
		String answer = request.getParameter("answer");
		int i = questionService.xUpdate(topic,subject_id,  answer,  option_json, question,questionId);
		Users users = usersService.xSelectUserByUsername(authentication.getName());
		logger.info("M[question] F[update_question] U[{}]; param[科目id：subject_id{},选项集合json数据：option_json{},topic{},answer{},question{},题号id:questionId{}]; result:{}",
				users.getUser_id(),subject_id,option_json,topic,answer,question,questionId,String.format("题库信息修改%s", i > 0 ? "成功" : "失败"));
		Map<String,String> map = new HashMap<String,String>();
		map.put("editMessage", String.format("题库信息修改%s", i > 0 ? "成功" : "失败"));
		return map;
	}
}
