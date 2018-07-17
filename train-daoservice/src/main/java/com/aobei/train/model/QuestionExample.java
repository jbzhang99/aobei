package com.aobei.train.model;

import com.aobei.train.model.QuestionExample.Criteria;
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
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class QuestionExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public QuestionExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table question
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

        public Criteria andQuestion_idIsNull() {
            addCriterion("question_id is null");
            return (Criteria) this;
        }

        public Criteria andQuestion_idIsNotNull() {
            addCriterion("question_id is not null");
            return (Criteria) this;
        }

        public Criteria andQuestion_idEqualTo(Long value) {
            addCriterion("question_id =", value, "question_id");
            return (Criteria) this;
        }

        public Criteria andQuestion_idNotEqualTo(Long value) {
            addCriterion("question_id <>", value, "question_id");
            return (Criteria) this;
        }

        public Criteria andQuestion_idGreaterThan(Long value) {
            addCriterion("question_id >", value, "question_id");
            return (Criteria) this;
        }

        public Criteria andQuestion_idGreaterThanOrEqualTo(Long value) {
            addCriterion("question_id >=", value, "question_id");
            return (Criteria) this;
        }

        public Criteria andQuestion_idLessThan(Long value) {
            addCriterion("question_id <", value, "question_id");
            return (Criteria) this;
        }

        public Criteria andQuestion_idLessThanOrEqualTo(Long value) {
            addCriterion("question_id <=", value, "question_id");
            return (Criteria) this;
        }

        public Criteria andQuestion_idIn(List<Long> values) {
            addCriterion("question_id in", values, "question_id");
            return (Criteria) this;
        }

        public Criteria andQuestion_idNotIn(List<Long> values) {
            addCriterion("question_id not in", values, "question_id");
            return (Criteria) this;
        }

        public Criteria andQuestion_idBetween(Long value1, Long value2) {
            addCriterion("question_id between", value1, value2, "question_id");
            return (Criteria) this;
        }

        public Criteria andQuestion_idNotBetween(Long value1, Long value2) {
            addCriterion("question_id not between", value1, value2, "question_id");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idIsNull() {
            addCriterion("exam_subject_id is null");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idIsNotNull() {
            addCriterion("exam_subject_id is not null");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idEqualTo(Long value) {
            addCriterion("exam_subject_id =", value, "exam_subject_id");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idNotEqualTo(Long value) {
            addCriterion("exam_subject_id <>", value, "exam_subject_id");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idGreaterThan(Long value) {
            addCriterion("exam_subject_id >", value, "exam_subject_id");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idGreaterThanOrEqualTo(Long value) {
            addCriterion("exam_subject_id >=", value, "exam_subject_id");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idLessThan(Long value) {
            addCriterion("exam_subject_id <", value, "exam_subject_id");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idLessThanOrEqualTo(Long value) {
            addCriterion("exam_subject_id <=", value, "exam_subject_id");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idIn(List<Long> values) {
            addCriterion("exam_subject_id in", values, "exam_subject_id");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idNotIn(List<Long> values) {
            addCriterion("exam_subject_id not in", values, "exam_subject_id");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idBetween(Long value1, Long value2) {
            addCriterion("exam_subject_id between", value1, value2, "exam_subject_id");
            return (Criteria) this;
        }

        public Criteria andExam_subject_idNotBetween(Long value1, Long value2) {
            addCriterion("exam_subject_id not between", value1, value2, "exam_subject_id");
            return (Criteria) this;
        }

        public Criteria andTopicIsNull() {
            addCriterion("topic is null");
            return (Criteria) this;
        }

        public Criteria andTopicIsNotNull() {
            addCriterion("topic is not null");
            return (Criteria) this;
        }

        public Criteria andTopicEqualTo(String value) {
            addCriterion("topic =", value, "topic");
            return (Criteria) this;
        }

        public Criteria andTopicNotEqualTo(String value) {
            addCriterion("topic <>", value, "topic");
            return (Criteria) this;
        }

        public Criteria andTopicGreaterThan(String value) {
            addCriterion("topic >", value, "topic");
            return (Criteria) this;
        }

        public Criteria andTopicGreaterThanOrEqualTo(String value) {
            addCriterion("topic >=", value, "topic");
            return (Criteria) this;
        }

        public Criteria andTopicLessThan(String value) {
            addCriterion("topic <", value, "topic");
            return (Criteria) this;
        }

        public Criteria andTopicLessThanOrEqualTo(String value) {
            addCriterion("topic <=", value, "topic");
            return (Criteria) this;
        }

        public Criteria andTopicLike(String value) {
            addCriterion("topic like", value, "topic");
            return (Criteria) this;
        }

        public Criteria andTopicNotLike(String value) {
            addCriterion("topic not like", value, "topic");
            return (Criteria) this;
        }

        public Criteria andTopicIn(List<String> values) {
            addCriterion("topic in", values, "topic");
            return (Criteria) this;
        }

        public Criteria andTopicNotIn(List<String> values) {
            addCriterion("topic not in", values, "topic");
            return (Criteria) this;
        }

        public Criteria andTopicBetween(String value1, String value2) {
            addCriterion("topic between", value1, value2, "topic");
            return (Criteria) this;
        }

        public Criteria andTopicNotBetween(String value1, String value2) {
            addCriterion("topic not between", value1, value2, "topic");
            return (Criteria) this;
        }

        public Criteria andOption_jsonIsNull() {
            addCriterion("option_json is null");
            return (Criteria) this;
        }

        public Criteria andOption_jsonIsNotNull() {
            addCriterion("option_json is not null");
            return (Criteria) this;
        }

        public Criteria andOption_jsonEqualTo(String value) {
            addCriterion("option_json =", value, "option_json");
            return (Criteria) this;
        }

        public Criteria andOption_jsonNotEqualTo(String value) {
            addCriterion("option_json <>", value, "option_json");
            return (Criteria) this;
        }

        public Criteria andOption_jsonGreaterThan(String value) {
            addCriterion("option_json >", value, "option_json");
            return (Criteria) this;
        }

        public Criteria andOption_jsonGreaterThanOrEqualTo(String value) {
            addCriterion("option_json >=", value, "option_json");
            return (Criteria) this;
        }

        public Criteria andOption_jsonLessThan(String value) {
            addCriterion("option_json <", value, "option_json");
            return (Criteria) this;
        }

        public Criteria andOption_jsonLessThanOrEqualTo(String value) {
            addCriterion("option_json <=", value, "option_json");
            return (Criteria) this;
        }

        public Criteria andOption_jsonLike(String value) {
            addCriterion("option_json like", value, "option_json");
            return (Criteria) this;
        }

        public Criteria andOption_jsonNotLike(String value) {
            addCriterion("option_json not like", value, "option_json");
            return (Criteria) this;
        }

        public Criteria andOption_jsonIn(List<String> values) {
            addCriterion("option_json in", values, "option_json");
            return (Criteria) this;
        }

        public Criteria andOption_jsonNotIn(List<String> values) {
            addCriterion("option_json not in", values, "option_json");
            return (Criteria) this;
        }

        public Criteria andOption_jsonBetween(String value1, String value2) {
            addCriterion("option_json between", value1, value2, "option_json");
            return (Criteria) this;
        }

        public Criteria andOption_jsonNotBetween(String value1, String value2) {
            addCriterion("option_json not between", value1, value2, "option_json");
            return (Criteria) this;
        }

        public Criteria andAnswerIsNull() {
            addCriterion("answer is null");
            return (Criteria) this;
        }

        public Criteria andAnswerIsNotNull() {
            addCriterion("answer is not null");
            return (Criteria) this;
        }

        public Criteria andAnswerEqualTo(String value) {
            addCriterion("answer =", value, "answer");
            return (Criteria) this;
        }

        public Criteria andAnswerNotEqualTo(String value) {
            addCriterion("answer <>", value, "answer");
            return (Criteria) this;
        }

        public Criteria andAnswerGreaterThan(String value) {
            addCriterion("answer >", value, "answer");
            return (Criteria) this;
        }

        public Criteria andAnswerGreaterThanOrEqualTo(String value) {
            addCriterion("answer >=", value, "answer");
            return (Criteria) this;
        }

        public Criteria andAnswerLessThan(String value) {
            addCriterion("answer <", value, "answer");
            return (Criteria) this;
        }

        public Criteria andAnswerLessThanOrEqualTo(String value) {
            addCriterion("answer <=", value, "answer");
            return (Criteria) this;
        }

        public Criteria andAnswerLike(String value) {
            addCriterion("answer like", value, "answer");
            return (Criteria) this;
        }

        public Criteria andAnswerNotLike(String value) {
            addCriterion("answer not like", value, "answer");
            return (Criteria) this;
        }

        public Criteria andAnswerIn(List<String> values) {
            addCriterion("answer in", values, "answer");
            return (Criteria) this;
        }

        public Criteria andAnswerNotIn(List<String> values) {
            addCriterion("answer not in", values, "answer");
            return (Criteria) this;
        }

        public Criteria andAnswerBetween(String value1, String value2) {
            addCriterion("answer between", value1, value2, "answer");
            return (Criteria) this;
        }

        public Criteria andAnswerNotBetween(String value1, String value2) {
            addCriterion("answer not between", value1, value2, "answer");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeIsNull() {
            addCriterion("create_datetime is null");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeIsNotNull() {
            addCriterion("create_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeEqualTo(Date value) {
            addCriterion("create_datetime =", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeNotEqualTo(Date value) {
            addCriterion("create_datetime <>", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeGreaterThan(Date value) {
            addCriterion("create_datetime >", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_datetime >=", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeLessThan(Date value) {
            addCriterion("create_datetime <", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeLessThanOrEqualTo(Date value) {
            addCriterion("create_datetime <=", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeIn(List<Date> values) {
            addCriterion("create_datetime in", values, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeNotIn(List<Date> values) {
            addCriterion("create_datetime not in", values, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeBetween(Date value1, Date value2) {
            addCriterion("create_datetime between", value1, value2, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeNotBetween(Date value1, Date value2) {
            addCriterion("create_datetime not between", value1, value2, "create_datetime");
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

        public Criteria andTopicLikeInsensitive(String value) {
            addCriterion("upper(topic) like", value.toUpperCase(), "topic");
            return (Criteria) this;
        }

        public Criteria andOption_jsonLikeInsensitive(String value) {
            addCriterion("upper(option_json) like", value.toUpperCase(), "option_json");
            return (Criteria) this;
        }

        public Criteria andAnswerLikeInsensitive(String value) {
            addCriterion("upper(answer) like", value.toUpperCase(), "answer");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table question
     *
     * @mbg.generated do_not_delete_during_merge Thu Feb 01 11:23:32 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table question
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	题库
		//--------------------------------------------------
		/** 课程id */									question_id(1,Types.BIGINT,false),
		/** 课程类型；0为全部，1为单选，2为多选，3为判断 */
														type(2,Types.INTEGER,false),
		/** 考试科目id */								exam_subject_id(2,Types.BIGINT,false),
		/** 题目 */										topic(2,Types.VARCHAR,false),
		/** 选择题 选项JSON 数据 */						option_json(2,Types.VARCHAR,false),
		/** 答案 */										answer(2,Types.VARCHAR,false),					
														create_datetime(2,Types.TIMESTAMP,false),
		/** 0 未删除，1 已删除 [DV=>0] */				deleted(2,Types.INTEGER,false);

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