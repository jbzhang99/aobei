package com.aobei.train.service;

import com.aobei.train.model.CourseServiceitemExample;
import com.aobei.train.model.CourseServiceitem;
import com.aobei.train.model.CourseServiceitemKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface CourseServiceitemService extends MbgReadService<CourseServiceitemKey, CourseServiceitem, CourseServiceitem, CourseServiceitemExample>,MbgWriteService<CourseServiceitemKey, CourseServiceitem, CourseServiceitem, CourseServiceitemExample>,MbgUpsertService<CourseServiceitemKey, CourseServiceitem, CourseServiceitem, CourseServiceitemExample>{

}