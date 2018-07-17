package com.aobei.train.service;

import com.aobei.train.model.BespeakExample;

import java.util.List;

import com.aobei.train.model.Bespeak;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;

public interface BespeakService extends MbgReadService<Integer, Bespeak, Bespeak, BespeakExample>,MbgWriteService<Integer, Bespeak, Bespeak, BespeakExample>{

	
	int xDelete(int bespeak_id);
	
	int xInsert(String name,String list);
	
	List getBespeakList(Integer bespeak_id);
	
	int xUpdate(String name,String list,int id);
}