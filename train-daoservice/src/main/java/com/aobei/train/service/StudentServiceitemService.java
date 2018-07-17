package com.aobei.train.service;

import com.aobei.train.model.StudentServiceitemExample;
import com.aobei.train.model.StudentServiceitem;
import com.aobei.train.model.StudentServiceitemKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface StudentServiceitemService extends MbgReadService<StudentServiceitemKey, StudentServiceitem, StudentServiceitem, StudentServiceitemExample>,MbgWriteService<StudentServiceitemKey, StudentServiceitem, StudentServiceitem, StudentServiceitemExample>,MbgUpsertService<StudentServiceitemKey, StudentServiceitem, StudentServiceitem, StudentServiceitemExample>{

}