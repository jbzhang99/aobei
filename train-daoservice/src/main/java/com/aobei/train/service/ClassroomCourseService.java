package com.aobei.train.service;

import com.aobei.train.model.ClassroomCourseExample;
import com.aobei.train.model.ClassroomCourse;
import com.aobei.train.model.ClassroomCourseKey;
import com.github.liyiorg.mbg.support.service.MbgReadService;
import com.github.liyiorg.mbg.support.service.MbgWriteService;
import com.github.liyiorg.mbg.support.service.MbgUpsertService;

public interface ClassroomCourseService extends MbgReadService<ClassroomCourseKey, ClassroomCourse, ClassroomCourse, ClassroomCourseExample>,MbgWriteService<ClassroomCourseKey, ClassroomCourse, ClassroomCourse, ClassroomCourseExample>,MbgUpsertService<ClassroomCourseKey, ClassroomCourse, ClassroomCourse, ClassroomCourseExample>{

}