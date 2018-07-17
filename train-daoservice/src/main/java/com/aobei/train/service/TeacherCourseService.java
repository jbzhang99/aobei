package com.aobei.train.service;

import com.aobei.train.model.TeacherCourseExample;
import com.aobei.train.model.TeacherCourse;
import com.aobei.train.model.TeacherCourseKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface TeacherCourseService extends MbgReadService<TeacherCourseKey, TeacherCourse, TeacherCourse, TeacherCourseExample>,MbgWriteService<TeacherCourseKey, TeacherCourse, TeacherCourse, TeacherCourseExample>,MbgUpsertService<TeacherCourseKey, TeacherCourse, TeacherCourse, TeacherCourseExample>{

}