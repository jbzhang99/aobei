package com.aobei.train.service.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.aobei.train.model.Question;
import com.aobei.train.model.QuestionExample;
import com.aobei.train.model.QuestionPaper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.QuestionPaperMapper;
import com.aobei.train.model.QuestionPaperExample;
import com.aobei.train.model.QuestionExample.Criteria;
import com.aobei.train.service.QuestionPaperService;
import com.aobei.train.service.QuestionService;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Option;
import custom.bean.Paper;
import custom.bean.QuestionPaperJson;
import custom.bean.Status;

@Service
@Transactional
public class QuestionPaperServiceImpl extends MbgServiceSupport<QuestionPaperMapper, Long, QuestionPaper, QuestionPaper, QuestionPaperExample> implements QuestionPaperService{

	@Autowired
	private QuestionPaperMapper questionPaperMapper;
	@Autowired
	private QuestionService questionService;

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(questionPaperMapper);
	}

	@Transactional(timeout = 5)
	@Override
	public int xDelete(Long testPaerId) {
		QuestionPaper questionPaper = questionPaperMapper.selectByPrimaryKey(testPaerId);
		questionPaper.setDeleted(Status.DeleteStatus.yes.value);
		return questionPaperMapper.updateByPrimaryKeySelective(questionPaper);
	}
	@Transactional(timeout = 5)
	@Override
	public HashMap<String, String> xInsert(String singleCount,String multipartCount,String train_course,Model model,String name) {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		Paper paper = new Paper();
		if((Integer.parseInt(singleCount)+Integer.parseInt(multipartCount))>50){
			resultMap.put("message", "题数之和不能大于50");
			return resultMap;
		}
		
		//最终拿到试卷的所有题集合，然后转化为json数据存储
		//试卷的单选题集合
		List<QuestionPaperJson> list_t1 = new ArrayList<QuestionPaperJson>();
		//试卷的多选题集合
		List<QuestionPaperJson> list_t2 = new ArrayList<QuestionPaperJson>();
		
		//paper.setT3(null);
		//开始随机抽取试卷，单选题数量为singleCount和多选题multipartCount。从题库抽取试卷后随机
		//查询出该试卷科目类型对应的所有单选题
		QuestionExample questionExample1 = new QuestionExample();
		Criteria criteria2 = questionExample1.or();
		criteria2.andExam_subject_idEqualTo(Long.parseLong(train_course)).andTypeEqualTo(1).andDeletedEqualTo(Status.DeleteStatus.no.value);
		List<Question> list_single = questionService.selectByExample(questionExample1);
		model.addAttribute("single_count", list_single.size());
		Collections.shuffle(list_single);
		//获取随机后的页面需要的单选题
		if(Integer.parseInt(singleCount)<=list_single.size()){			
			for (int i = 0 ; i<=Integer.parseInt(singleCount)-1;i++){
				Question question = list_single.get(i);
				QuestionPaperJson questionPaperJson = new QuestionPaperJson();
				questionPaperJson.setId(question.getQuestion_id());
				questionPaperJson.setTopic(question.getTopic());
				questionPaperJson.setAnswer(question.getAnswer());
				questionPaperJson.setOption_json(JSONObject.parseArray(question.getOption_json(), Option.class));
				list_t1.add(questionPaperJson);
			}
		}else{
			resultMap.put("message", "单选题数量不能大于题库数量，题库数量为"+list_single.size());
			return resultMap;
		}
		//查询出该试卷科目类型对应的所有多选题
		QuestionExample questionExample2 = new QuestionExample();
		Criteria criteria = questionExample2.or();
		criteria.andExam_subject_idEqualTo(Long.parseLong(train_course)).andTypeEqualTo(2).andDeletedEqualTo(Status.DeleteStatus.no.value);
		
		List<Question> list_multipart = questionService.selectByExample(questionExample2);
		model.addAttribute("multipart_count", list_multipart.size());
		Collections.shuffle(list_multipart);
		if(Integer.parseInt(multipartCount)<=list_multipart.size()){			
			//获取随机后的页面需要的多选题
			for(int i=0;i<=Integer.parseInt(multipartCount)-1;i++){
				Question question = list_multipart.get(i);
				QuestionPaperJson questionPaperJson = new QuestionPaperJson();
				questionPaperJson.setAnswer(question.getAnswer());
				questionPaperJson.setId(question.getQuestion_id());
				questionPaperJson.setTopic(question.getTopic());
				questionPaperJson.setOption_json(JSONObject.parseArray(question.getOption_json(), Option.class));
				list_t2.add(questionPaperJson);
			}
		}else{
			resultMap.put("messsage", "多选题数量不能大于题库数量，题库数量为"+list_multipart.size());
			return resultMap;
		}
		paper.setT1(list_t1);
		paper.setT2(list_t2);
		
		String object_to_json = null;
		try {
			object_to_json = JSON.toJSONString(paper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		QuestionPaper questionPaper = new QuestionPaper();
		questionPaper.setCreate_datetime(new Date());
		questionPaper.setDeleted(Status.DeleteStatus.no.value);
		//科目id
		questionPaper.setExam_subject_id(Long.parseLong(train_course));
		//试卷id
		questionPaper.setQuestion_paper_id(IdGenerator.generateId());
		questionPaper.setName(name);
		questionPaper.setT1_count(list_t1.size());
		questionPaper.setT2_count(list_t2.size());
		questionPaper.setT3_count(0);
		questionPaper.setQuestion_json(object_to_json.toString());
		int i = questionPaperMapper.insertSelective(questionPaper);
		
		resultMap.put("message", String.format("【"+name+"】试卷生成"+(list_t1.size()+list_t2.size())+"道题%s", i > 0 ? "成功":"失败"));
		return resultMap;
	}

	
}