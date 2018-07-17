package com.aobei.train.service;

import com.aobei.train.model.QuestionExample;

import java.util.HashMap;
import java.util.List;

import com.aobei.train.model.Question;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;

import custom.bean.Option;

import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface QuestionService extends MbgReadService<Long, Question, Question, QuestionExample>,MbgWriteService<Long, Question, Question, QuestionExample>,MbgUpsertService<Long, Question, Question, QuestionExample>{

	/**
	 * 返回每题选项集合数据
	 * @param list
	 * @return
	 */
	List<HashMap<Long, List<Option>>> getOptionList(List<Question> list);
	/**
	 * 插入数据
	 * @param topic
	 * @param subject_id
	 * @param anwser
	 * @param option_json
	 * @param question
	 * @return
	 */
	int xInsert(String topic,String subject_id,String anwser,String option_json,Question question);
	/**
	 * 删除方法（修改状态）
	 * @param question_id
	 * @return
	 */
	int xDeleteById(Long question_id);
	/**
	 * 修改方法
	 * @param topic
	 * @param subject_id
	 * @param answer
	 * @param option_json
	 * @param question
	 * @param questionId
	 * @return
	 */
	int xUpdate(String topic, String subject_id, String answer, String option_json, Question question,Long questionId);
}