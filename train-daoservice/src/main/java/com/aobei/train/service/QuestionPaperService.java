package com.aobei.train.service;

import com.aobei.train.model.QuestionPaperExample;

import java.util.HashMap;

import org.springframework.ui.Model;

import com.aobei.train.model.QuestionPaper;
import com.github.liyiorg.mbg.support.service.MbgReadBLOBsService;
import com.github.liyiorg.mbg.support.service.MbgWriteBLOBsService;


import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface QuestionPaperService extends MbgReadBLOBsService<Long, QuestionPaper, QuestionPaper, QuestionPaperExample>,MbgWriteBLOBsService<Long, QuestionPaper, QuestionPaper, QuestionPaperExample>,MbgUpsertService<Long, QuestionPaper, QuestionPaper, QuestionPaperExample>{

	
	int xDelete(Long testPaerId);
	
	HashMap<String, String> xInsert(String singleCount,String multipartCount,
			String train_course,Model model,String name);
}