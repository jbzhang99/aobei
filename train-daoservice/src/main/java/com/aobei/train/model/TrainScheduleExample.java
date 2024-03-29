package com.aobei.train.model;

import com.aobei.train.model.TrainScheduleExample.Criteria;
import com.github.liyiorg.mbg.support.example.CInterface;
import com.github.liyiorg.mbg.support.example.CItem;
import com.github.liyiorg.mbg.support.example.ColumnListAble;
import com.github.liyiorg.mbg.support.example.MbgExample;
import com.github.liyiorg.mbg.support.example.MbgGeneratedCriteria;
import com.github.liyiorg.mbg.support.example.PaginationAble;
import com.github.liyiorg.mbg.util.CUtil;
import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class TrainScheduleExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table train_schedule
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public TrainScheduleExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table train_schedule
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table train_schedule
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table train_schedule
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table train_schedule
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

        public Criteria andTrain_schedule_idIsNull() {
            addCriterion("train_schedule_id is null");
            return (Criteria) this;
        }

        public Criteria andTrain_schedule_idIsNotNull() {
            addCriterion("train_schedule_id is not null");
            return (Criteria) this;
        }

        public Criteria andTrain_schedule_idEqualTo(Long value) {
            addCriterion("train_schedule_id =", value, "train_schedule_id");
            return (Criteria) this;
        }

        public Criteria andTrain_schedule_idNotEqualTo(Long value) {
            addCriterion("train_schedule_id <>", value, "train_schedule_id");
            return (Criteria) this;
        }

        public Criteria andTrain_schedule_idGreaterThan(Long value) {
            addCriterion("train_schedule_id >", value, "train_schedule_id");
            return (Criteria) this;
        }

        public Criteria andTrain_schedule_idGreaterThanOrEqualTo(Long value) {
            addCriterion("train_schedule_id >=", value, "train_schedule_id");
            return (Criteria) this;
        }

        public Criteria andTrain_schedule_idLessThan(Long value) {
            addCriterion("train_schedule_id <", value, "train_schedule_id");
            return (Criteria) this;
        }

        public Criteria andTrain_schedule_idLessThanOrEqualTo(Long value) {
            addCriterion("train_schedule_id <=", value, "train_schedule_id");
            return (Criteria) this;
        }

        public Criteria andTrain_schedule_idIn(List<Long> values) {
            addCriterion("train_schedule_id in", values, "train_schedule_id");
            return (Criteria) this;
        }

        public Criteria andTrain_schedule_idNotIn(List<Long> values) {
            addCriterion("train_schedule_id not in", values, "train_schedule_id");
            return (Criteria) this;
        }

        public Criteria andTrain_schedule_idBetween(Long value1, Long value2) {
            addCriterion("train_schedule_id between", value1, value2, "train_schedule_id");
            return (Criteria) this;
        }

        public Criteria andTrain_schedule_idNotBetween(Long value1, Long value2) {
            addCriterion("train_schedule_id not between", value1, value2, "train_schedule_id");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idIsNull() {
            addCriterion("train_plan_id is null");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idIsNotNull() {
            addCriterion("train_plan_id is not null");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idEqualTo(Long value) {
            addCriterion("train_plan_id =", value, "train_plan_id");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idNotEqualTo(Long value) {
            addCriterion("train_plan_id <>", value, "train_plan_id");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idGreaterThan(Long value) {
            addCriterion("train_plan_id >", value, "train_plan_id");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idGreaterThanOrEqualTo(Long value) {
            addCriterion("train_plan_id >=", value, "train_plan_id");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idLessThan(Long value) {
            addCriterion("train_plan_id <", value, "train_plan_id");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idLessThanOrEqualTo(Long value) {
            addCriterion("train_plan_id <=", value, "train_plan_id");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idIn(List<Long> values) {
            addCriterion("train_plan_id in", values, "train_plan_id");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idNotIn(List<Long> values) {
            addCriterion("train_plan_id not in", values, "train_plan_id");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idBetween(Long value1, Long value2) {
            addCriterion("train_plan_id between", value1, value2, "train_plan_id");
            return (Criteria) this;
        }

        public Criteria andTrain_plan_idNotBetween(Long value1, Long value2) {
            addCriterion("train_plan_id not between", value1, value2, "train_plan_id");
            return (Criteria) this;
        }

        public Criteria andChapter_idIsNull() {
            addCriterion("chapter_id is null");
            return (Criteria) this;
        }

        public Criteria andChapter_idIsNotNull() {
            addCriterion("chapter_id is not null");
            return (Criteria) this;
        }

        public Criteria andChapter_idEqualTo(Long value) {
            addCriterion("chapter_id =", value, "chapter_id");
            return (Criteria) this;
        }

        public Criteria andChapter_idNotEqualTo(Long value) {
            addCriterion("chapter_id <>", value, "chapter_id");
            return (Criteria) this;
        }

        public Criteria andChapter_idGreaterThan(Long value) {
            addCriterion("chapter_id >", value, "chapter_id");
            return (Criteria) this;
        }

        public Criteria andChapter_idGreaterThanOrEqualTo(Long value) {
            addCriterion("chapter_id >=", value, "chapter_id");
            return (Criteria) this;
        }

        public Criteria andChapter_idLessThan(Long value) {
            addCriterion("chapter_id <", value, "chapter_id");
            return (Criteria) this;
        }

        public Criteria andChapter_idLessThanOrEqualTo(Long value) {
            addCriterion("chapter_id <=", value, "chapter_id");
            return (Criteria) this;
        }

        public Criteria andChapter_idIn(List<Long> values) {
            addCriterion("chapter_id in", values, "chapter_id");
            return (Criteria) this;
        }

        public Criteria andChapter_idNotIn(List<Long> values) {
            addCriterion("chapter_id not in", values, "chapter_id");
            return (Criteria) this;
        }

        public Criteria andChapter_idBetween(Long value1, Long value2) {
            addCriterion("chapter_id between", value1, value2, "chapter_id");
            return (Criteria) this;
        }

        public Criteria andChapter_idNotBetween(Long value1, Long value2) {
            addCriterion("chapter_id not between", value1, value2, "chapter_id");
            return (Criteria) this;
        }

        public Criteria andCourse_idIsNull() {
            addCriterion("course_id is null");
            return (Criteria) this;
        }

        public Criteria andCourse_idIsNotNull() {
            addCriterion("course_id is not null");
            return (Criteria) this;
        }

        public Criteria andCourse_idEqualTo(Long value) {
            addCriterion("course_id =", value, "course_id");
            return (Criteria) this;
        }

        public Criteria andCourse_idNotEqualTo(Long value) {
            addCriterion("course_id <>", value, "course_id");
            return (Criteria) this;
        }

        public Criteria andCourse_idGreaterThan(Long value) {
            addCriterion("course_id >", value, "course_id");
            return (Criteria) this;
        }

        public Criteria andCourse_idGreaterThanOrEqualTo(Long value) {
            addCriterion("course_id >=", value, "course_id");
            return (Criteria) this;
        }

        public Criteria andCourse_idLessThan(Long value) {
            addCriterion("course_id <", value, "course_id");
            return (Criteria) this;
        }

        public Criteria andCourse_idLessThanOrEqualTo(Long value) {
            addCriterion("course_id <=", value, "course_id");
            return (Criteria) this;
        }

        public Criteria andCourse_idIn(List<Long> values) {
            addCriterion("course_id in", values, "course_id");
            return (Criteria) this;
        }

        public Criteria andCourse_idNotIn(List<Long> values) {
            addCriterion("course_id not in", values, "course_id");
            return (Criteria) this;
        }

        public Criteria andCourse_idBetween(Long value1, Long value2) {
            addCriterion("course_id between", value1, value2, "course_id");
            return (Criteria) this;
        }

        public Criteria andCourse_idNotBetween(Long value1, Long value2) {
            addCriterion("course_id not between", value1, value2, "course_id");
            return (Criteria) this;
        }

        public Criteria andClassroom_idIsNull() {
            addCriterion("classroom_id is null");
            return (Criteria) this;
        }

        public Criteria andClassroom_idIsNotNull() {
            addCriterion("classroom_id is not null");
            return (Criteria) this;
        }

        public Criteria andClassroom_idEqualTo(Long value) {
            addCriterion("classroom_id =", value, "classroom_id");
            return (Criteria) this;
        }

        public Criteria andClassroom_idNotEqualTo(Long value) {
            addCriterion("classroom_id <>", value, "classroom_id");
            return (Criteria) this;
        }

        public Criteria andClassroom_idGreaterThan(Long value) {
            addCriterion("classroom_id >", value, "classroom_id");
            return (Criteria) this;
        }

        public Criteria andClassroom_idGreaterThanOrEqualTo(Long value) {
            addCriterion("classroom_id >=", value, "classroom_id");
            return (Criteria) this;
        }

        public Criteria andClassroom_idLessThan(Long value) {
            addCriterion("classroom_id <", value, "classroom_id");
            return (Criteria) this;
        }

        public Criteria andClassroom_idLessThanOrEqualTo(Long value) {
            addCriterion("classroom_id <=", value, "classroom_id");
            return (Criteria) this;
        }

        public Criteria andClassroom_idIn(List<Long> values) {
            addCriterion("classroom_id in", values, "classroom_id");
            return (Criteria) this;
        }

        public Criteria andClassroom_idNotIn(List<Long> values) {
            addCriterion("classroom_id not in", values, "classroom_id");
            return (Criteria) this;
        }

        public Criteria andClassroom_idBetween(Long value1, Long value2) {
            addCriterion("classroom_id between", value1, value2, "classroom_id");
            return (Criteria) this;
        }

        public Criteria andClassroom_idNotBetween(Long value1, Long value2) {
            addCriterion("classroom_id not between", value1, value2, "classroom_id");
            return (Criteria) this;
        }

        public Criteria andSchool_idIsNull() {
            addCriterion("school_id is null");
            return (Criteria) this;
        }

        public Criteria andSchool_idIsNotNull() {
            addCriterion("school_id is not null");
            return (Criteria) this;
        }

        public Criteria andSchool_idEqualTo(Long value) {
            addCriterion("school_id =", value, "school_id");
            return (Criteria) this;
        }

        public Criteria andSchool_idNotEqualTo(Long value) {
            addCriterion("school_id <>", value, "school_id");
            return (Criteria) this;
        }

        public Criteria andSchool_idGreaterThan(Long value) {
            addCriterion("school_id >", value, "school_id");
            return (Criteria) this;
        }

        public Criteria andSchool_idGreaterThanOrEqualTo(Long value) {
            addCriterion("school_id >=", value, "school_id");
            return (Criteria) this;
        }

        public Criteria andSchool_idLessThan(Long value) {
            addCriterion("school_id <", value, "school_id");
            return (Criteria) this;
        }

        public Criteria andSchool_idLessThanOrEqualTo(Long value) {
            addCriterion("school_id <=", value, "school_id");
            return (Criteria) this;
        }

        public Criteria andSchool_idIn(List<Long> values) {
            addCriterion("school_id in", values, "school_id");
            return (Criteria) this;
        }

        public Criteria andSchool_idNotIn(List<Long> values) {
            addCriterion("school_id not in", values, "school_id");
            return (Criteria) this;
        }

        public Criteria andSchool_idBetween(Long value1, Long value2) {
            addCriterion("school_id between", value1, value2, "school_id");
            return (Criteria) this;
        }

        public Criteria andSchool_idNotBetween(Long value1, Long value2) {
            addCriterion("school_id not between", value1, value2, "school_id");
            return (Criteria) this;
        }

        public Criteria andTeacher_idIsNull() {
            addCriterion("teacher_id is null");
            return (Criteria) this;
        }

        public Criteria andTeacher_idIsNotNull() {
            addCriterion("teacher_id is not null");
            return (Criteria) this;
        }

        public Criteria andTeacher_idEqualTo(Long value) {
            addCriterion("teacher_id =", value, "teacher_id");
            return (Criteria) this;
        }

        public Criteria andTeacher_idNotEqualTo(Long value) {
            addCriterion("teacher_id <>", value, "teacher_id");
            return (Criteria) this;
        }

        public Criteria andTeacher_idGreaterThan(Long value) {
            addCriterion("teacher_id >", value, "teacher_id");
            return (Criteria) this;
        }

        public Criteria andTeacher_idGreaterThanOrEqualTo(Long value) {
            addCriterion("teacher_id >=", value, "teacher_id");
            return (Criteria) this;
        }

        public Criteria andTeacher_idLessThan(Long value) {
            addCriterion("teacher_id <", value, "teacher_id");
            return (Criteria) this;
        }

        public Criteria andTeacher_idLessThanOrEqualTo(Long value) {
            addCriterion("teacher_id <=", value, "teacher_id");
            return (Criteria) this;
        }

        public Criteria andTeacher_idIn(List<Long> values) {
            addCriterion("teacher_id in", values, "teacher_id");
            return (Criteria) this;
        }

        public Criteria andTeacher_idNotIn(List<Long> values) {
            addCriterion("teacher_id not in", values, "teacher_id");
            return (Criteria) this;
        }

        public Criteria andTeacher_idBetween(Long value1, Long value2) {
            addCriterion("teacher_id between", value1, value2, "teacher_id");
            return (Criteria) this;
        }

        public Criteria andTeacher_idNotBetween(Long value1, Long value2) {
            addCriterion("teacher_id not between", value1, value2, "teacher_id");
            return (Criteria) this;
        }

        public Criteria andAssistantIsNull() {
            addCriterion("assistant is null");
            return (Criteria) this;
        }

        public Criteria andAssistantIsNotNull() {
            addCriterion("assistant is not null");
            return (Criteria) this;
        }

        public Criteria andAssistantEqualTo(Long value) {
            addCriterion("assistant =", value, "assistant");
            return (Criteria) this;
        }

        public Criteria andAssistantNotEqualTo(Long value) {
            addCriterion("assistant <>", value, "assistant");
            return (Criteria) this;
        }

        public Criteria andAssistantGreaterThan(Long value) {
            addCriterion("assistant >", value, "assistant");
            return (Criteria) this;
        }

        public Criteria andAssistantGreaterThanOrEqualTo(Long value) {
            addCriterion("assistant >=", value, "assistant");
            return (Criteria) this;
        }

        public Criteria andAssistantLessThan(Long value) {
            addCriterion("assistant <", value, "assistant");
            return (Criteria) this;
        }

        public Criteria andAssistantLessThanOrEqualTo(Long value) {
            addCriterion("assistant <=", value, "assistant");
            return (Criteria) this;
        }

        public Criteria andAssistantIn(List<Long> values) {
            addCriterion("assistant in", values, "assistant");
            return (Criteria) this;
        }

        public Criteria andAssistantNotIn(List<Long> values) {
            addCriterion("assistant not in", values, "assistant");
            return (Criteria) this;
        }

        public Criteria andAssistantBetween(Long value1, Long value2) {
            addCriterion("assistant between", value1, value2, "assistant");
            return (Criteria) this;
        }

        public Criteria andAssistantNotBetween(Long value1, Long value2) {
            addCriterion("assistant not between", value1, value2, "assistant");
            return (Criteria) this;
        }

        public Criteria andTrain_wayIsNull() {
            addCriterion("train_way is null");
            return (Criteria) this;
        }

        public Criteria andTrain_wayIsNotNull() {
            addCriterion("train_way is not null");
            return (Criteria) this;
        }

        public Criteria andTrain_wayEqualTo(Integer value) {
            addCriterion("train_way =", value, "train_way");
            return (Criteria) this;
        }

        public Criteria andTrain_wayNotEqualTo(Integer value) {
            addCriterion("train_way <>", value, "train_way");
            return (Criteria) this;
        }

        public Criteria andTrain_wayGreaterThan(Integer value) {
            addCriterion("train_way >", value, "train_way");
            return (Criteria) this;
        }

        public Criteria andTrain_wayGreaterThanOrEqualTo(Integer value) {
            addCriterion("train_way >=", value, "train_way");
            return (Criteria) this;
        }

        public Criteria andTrain_wayLessThan(Integer value) {
            addCriterion("train_way <", value, "train_way");
            return (Criteria) this;
        }

        public Criteria andTrain_wayLessThanOrEqualTo(Integer value) {
            addCriterion("train_way <=", value, "train_way");
            return (Criteria) this;
        }

        public Criteria andTrain_wayIn(List<Integer> values) {
            addCriterion("train_way in", values, "train_way");
            return (Criteria) this;
        }

        public Criteria andTrain_wayNotIn(List<Integer> values) {
            addCriterion("train_way not in", values, "train_way");
            return (Criteria) this;
        }

        public Criteria andTrain_wayBetween(Integer value1, Integer value2) {
            addCriterion("train_way between", value1, value2, "train_way");
            return (Criteria) this;
        }

        public Criteria andTrain_wayNotBetween(Integer value1, Integer value2) {
            addCriterion("train_way not between", value1, value2, "train_way");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateIsNull() {
            addCriterion("attendclass_date is null");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateIsNotNull() {
            addCriterion("attendclass_date is not null");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateEqualTo(Date value) {
            addCriterionForJDBCDate("attendclass_date =", value, "attendclass_date");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateNotEqualTo(Date value) {
            addCriterionForJDBCDate("attendclass_date <>", value, "attendclass_date");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateGreaterThan(Date value) {
            addCriterionForJDBCDate("attendclass_date >", value, "attendclass_date");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("attendclass_date >=", value, "attendclass_date");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateLessThan(Date value) {
            addCriterionForJDBCDate("attendclass_date <", value, "attendclass_date");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("attendclass_date <=", value, "attendclass_date");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateIn(List<Date> values) {
            addCriterionForJDBCDate("attendclass_date in", values, "attendclass_date");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateNotIn(List<Date> values) {
            addCriterionForJDBCDate("attendclass_date not in", values, "attendclass_date");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("attendclass_date between", value1, value2, "attendclass_date");
            return (Criteria) this;
        }

        public Criteria andAttendclass_dateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("attendclass_date not between", value1, value2, "attendclass_date");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeIsNull() {
            addCriterion("attendclass_starttime is null");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeIsNotNull() {
            addCriterion("attendclass_starttime is not null");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeEqualTo(Date value) {
            addCriterion("attendclass_starttime =", value, "attendclass_starttime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeNotEqualTo(Date value) {
            addCriterion("attendclass_starttime <>", value, "attendclass_starttime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeGreaterThan(Date value) {
            addCriterion("attendclass_starttime >", value, "attendclass_starttime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeGreaterThanOrEqualTo(Date value) {
            addCriterion("attendclass_starttime >=", value, "attendclass_starttime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeLessThan(Date value) {
            addCriterion("attendclass_starttime <", value, "attendclass_starttime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeLessThanOrEqualTo(Date value) {
            addCriterion("attendclass_starttime <=", value, "attendclass_starttime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeIn(List<Date> values) {
            addCriterion("attendclass_starttime in", values, "attendclass_starttime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeNotIn(List<Date> values) {
            addCriterion("attendclass_starttime not in", values, "attendclass_starttime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeBetween(Date value1, Date value2) {
            addCriterion("attendclass_starttime between", value1, value2, "attendclass_starttime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_starttimeNotBetween(Date value1, Date value2) {
            addCriterion("attendclass_starttime not between", value1, value2, "attendclass_starttime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeIsNull() {
            addCriterion("attendclass_endtime is null");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeIsNotNull() {
            addCriterion("attendclass_endtime is not null");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeEqualTo(Date value) {
            addCriterion("attendclass_endtime =", value, "attendclass_endtime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeNotEqualTo(Date value) {
            addCriterion("attendclass_endtime <>", value, "attendclass_endtime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeGreaterThan(Date value) {
            addCriterion("attendclass_endtime >", value, "attendclass_endtime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("attendclass_endtime >=", value, "attendclass_endtime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeLessThan(Date value) {
            addCriterion("attendclass_endtime <", value, "attendclass_endtime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeLessThanOrEqualTo(Date value) {
            addCriterion("attendclass_endtime <=", value, "attendclass_endtime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeIn(List<Date> values) {
            addCriterion("attendclass_endtime in", values, "attendclass_endtime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeNotIn(List<Date> values) {
            addCriterion("attendclass_endtime not in", values, "attendclass_endtime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeBetween(Date value1, Date value2) {
            addCriterion("attendclass_endtime between", value1, value2, "attendclass_endtime");
            return (Criteria) this;
        }

        public Criteria andAttendclass_endtimeNotBetween(Date value1, Date value2) {
            addCriterion("attendclass_endtime not between", value1, value2, "attendclass_endtime");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNull() {
            addCriterion("deleted is null");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNotNull() {
            addCriterion("deleted is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedEqualTo(Integer value) {
            addCriterion("deleted =", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotEqualTo(Integer value) {
            addCriterion("deleted <>", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThan(Integer value) {
            addCriterion("deleted >", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThanOrEqualTo(Integer value) {
            addCriterion("deleted >=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThan(Integer value) {
            addCriterion("deleted <", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThanOrEqualTo(Integer value) {
            addCriterion("deleted <=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedIn(List<Integer> values) {
            addCriterion("deleted in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotIn(List<Integer> values) {
            addCriterion("deleted not in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedBetween(Integer value1, Integer value2) {
            addCriterion("deleted between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotBetween(Integer value1, Integer value2) {
            addCriterion("deleted not between", value1, value2, "deleted");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table train_schedule
     *
     * @mbg.generated do_not_delete_during_merge Thu Feb 01 11:23:32 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
        
        /**
         * 等于老师或助教
         * @param teacher_id
         * @return
         */
        public Criteria andTeacherAll(Long teacher_id) {
            addCriterion(String.format("teacher_id=%d or assistant=%d",teacher_id,teacher_id));
            return (Criteria) this;
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table train_schedule
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	课程排期
		//--------------------------------------------------					
												train_schedule_id(1,Types.BIGINT,false),
		/** 培训计划id */							train_plan_id(2,Types.BIGINT,false),
		/** 课节id */								chapter_id(2,Types.BIGINT,false),
		/** 课程id */								course_id(2,Types.BIGINT,false),
		/** 教室id */								classroom_id(2,Types.BIGINT,false),
		/** 学校id */								school_id(2,Types.BIGINT,false),
		/** 老师id */								teacher_id(2,Types.BIGINT,false),
		/** 助教老师id */							assistant(2,Types.BIGINT,false),
		/** 培训方式 '0'为面授 '1'为在线 */			train_way(2,Types.INTEGER,false),
		/** 上课日期 */								attendclass_date(2,Types.DATE,false),
		/** 上课开始时间 */							attendclass_starttime(2,Types.TIMESTAMP,false),
		/** 上课结束时间 */							attendclass_endtime(2,Types.TIMESTAMP,false),
		/** 0 未删除，1 已删除 [DV=>0] */			deleted(2,Types.INTEGER,false);

        private final CItem C_I_T_E_M;

        private C(int type, int jdbcType, boolean delimited) {
            C_I_T_E_M = new CItem(type, jdbcType, delimited, name(), null , "`", "`");
        }

        public int getType() {
            return C_I_T_E_M.getType();
        }

        public int getJdbcType() {
            return C_I_T_E_M.getJdbcType();
        }

        public boolean isDelimited() {
            return C_I_T_E_M.isDelimited();
        }

        public String delimitedName() {
            return C_I_T_E_M.delimitedName();
        }

        public String delimitedAliasName() {
            return C_I_T_E_M.delimitedAliasName();
        }
    }
}