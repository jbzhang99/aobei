package com.aobei.train.service;

import com.aobei.train.model.TrainCityExample;
import com.aobei.train.model.TrainCity;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface TrainCityService extends MbgReadService<String, TrainCity, TrainCity, TrainCityExample>,MbgWriteService<String, TrainCity, TrainCity, TrainCityExample>,MbgUpsertService<String, TrainCity, TrainCity, TrainCityExample>{

	TrainCity xSelectCityById(String id);
	TrainCity xSelectCityByName(String name);
}