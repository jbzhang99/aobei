package com.aobei.train.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aobei.train.model.Question;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aobei.train.IdGenerator;
import com.aobei.train.mapper.QuestionMapper;
import com.aobei.train.model.QuestionExample;
import com.aobei.train.service.QuestionService;
import com.github.liyiorg.mbg.support.service.MbgServiceSupport;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;

import custom.bean.Option;
import custom.bean.Status;


@Service
@Transactional
public class QuestionServiceImpl extends MbgServiceSupport<QuestionMapper, Long, Question, Question, QuestionExample> implements QuestionService{

	@Autowired
	private QuestionMapper questionMapper;
	
	

	@Autowired
	private void initService(MbgMapperTemplateFactory mbgMapperTemplateFactory){
		super.mbgMapperTemplate = mbgMapperTemplateFactory.getMbgMapperTemplate(questionMapper);
	}
	/**
	 * 插入题数据
	 */
	@Transactional(timeout = 5)
	@Override
	public int xInsert(String topic, String subject_id, String answer, String option_json, Question question) {
		question.setQuestion_id(IdGenerator.generateId());
		question.setDeleted(Status.DeleteStatus.no.value);
		question.setTopic(topic);
		question.setExam_subject_id(Long.parseLong(subject_id));
		question.setAnswer(answer);
		question.setOption_json(option_json);
		question.setCreate_datetime((new Date()));
		return questionMapper.insertSelective(question);
	}
	@Transactional(timeout = 5)
	@Override
	public int xDeleteById(Long question_id) {
		Question question = new Question();
		question.setQuestion_id(question_id);
		question.setDeleted(Status.DeleteStatus.yes.value);
		return questionMapper.updateByPrimaryKey(question);
	}
	@Transactional(timeout = 5)
	@Override
	public int xUpdate(String topic, String subject_id, String answer, String option_json, Question question,Long questionId) {
		question.setQuestion_id(questionId);
		question.setDeleted(Status.DeleteStatus.no.value);
		question.setTopic(topic);
		question.setExam_subject_id(Long.parseLong(subject_id));
		question.setAnswer(answer);
		question.setOption_json(option_json);
		//question.setCreate_datetime(questionMapper.selectByPrimaryKey(questionId).getCreate_datetime());
		return questionMapper.updateByPrimaryKeySelective(question);
	}
	@Override
	public List<HashMap<Long, List<Option>>> getOptionList(List<Question> list) {
		List<HashMap<Long, List<Option>>> listOption = list.stream().map(n ->{
			Long question_id = n.getQuestion_id();
			HashMap<Long, List<Option>> map = new HashMap<>();
			String option_json = n.getOption_json();
			JSONArray parseArray = JSONObject.parseArray(option_json);
			List<Option> list_option = parseArray.toJavaList(Option.class);
			map.put(question_id, list_option);
			return map;
			
		}).collect(Collectors.toList());
		return listOption;
	}
	
	

}