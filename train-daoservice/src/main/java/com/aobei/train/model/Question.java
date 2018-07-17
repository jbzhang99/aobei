package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class Question implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column question.question_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Long question_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column question.type
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column question.exam_subject_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Long exam_subject_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column question.topic
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private String topic;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column question.option_json
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private String option_json;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column question.answer
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private String answer;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column question.create_datetime
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Date create_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column question.deleted
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Integer deleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table question
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table question
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column question.question_id
     *
     * @return the value of question.question_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Long getQuestion_id() {
        return question_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column question.question_id
     *
     * @param question_id the value for question.question_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column question.type
     *
     * @return the value of question.type
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column question.type
     *
     * @param type the value for question.type
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column question.exam_subject_id
     *
     * @return the value of question.exam_subject_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Long getExam_subject_id() {
        return exam_subject_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column question.exam_subject_id
     *
     * @param exam_subject_id the value for question.exam_subject_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setExam_subject_id(Long exam_subject_id) {
        this.exam_subject_id = exam_subject_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column question.topic
     *
     * @return the value of question.topic
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public String getTopic() {
        return topic;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column question.topic
     *
     * @param topic the value for question.topic
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setTopic(String topic) {
        this.topic = topic == null ? null : topic.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column question.option_json
     *
     * @return the value of question.option_json
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public String getOption_json() {
        return option_json;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column question.option_json
     *
     * @param option_json the value for question.option_json
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setOption_json(String option_json) {
        this.option_json = option_json == null ? null : option_json.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column question.answer
     *
     * @return the value of question.answer
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column question.answer
     *
     * @param answer the value for question.answer
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column question.create_datetime
     *
     * @return the value of question.create_datetime
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Date getCreate_datetime() {
        return create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column question.create_datetime
     *
     * @param create_datetime the value for question.create_datetime
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column question.deleted
     *
     * @return the value of question.deleted
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column question.deleted
     *
     * @param deleted the value for question.deleted
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table question
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", question_id=").append(question_id);
        sb.append(", type=").append(type);
        sb.append(", exam_subject_id=").append(exam_subject_id);
        sb.append(", topic=").append(topic);
        sb.append(", option_json=").append(option_json);
        sb.append(", answer=").append(answer);
        sb.append(", create_datetime=").append(create_datetime);
        sb.append(", deleted=").append(deleted);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}