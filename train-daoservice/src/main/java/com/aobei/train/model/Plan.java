package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class Plan implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.train_plan_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Long train_plan_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.train_begin
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Date train_begin;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.train_end
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Date train_end;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.course_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Long course_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.school_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Long school_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.classroom_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Long classroom_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.teacher_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Long teacher_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.assistant
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Long assistant;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.train_way
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Integer train_way;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.training
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private String training;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.trainers_number
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Integer trainers_number;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan.deleted
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Integer deleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table train_plan
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table train_plan
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.train_plan_id
     *
     * @return the value of train_plan.train_plan_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Long getTrain_plan_id() {
        return train_plan_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.train_plan_id
     *
     * @param train_plan_id the value for train_plan.train_plan_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setTrain_plan_id(Long train_plan_id) {
        this.train_plan_id = train_plan_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.train_begin
     *
     * @return the value of train_plan.train_begin
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Date getTrain_begin() {
        return train_begin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.train_begin
     *
     * @param train_begin the value for train_plan.train_begin
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setTrain_begin(Date train_begin) {
        this.train_begin = train_begin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.train_end
     *
     * @return the value of train_plan.train_end
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Date getTrain_end() {
        return train_end;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.train_end
     *
     * @param train_end the value for train_plan.train_end
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setTrain_end(Date train_end) {
        this.train_end = train_end;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.course_id
     *
     * @return the value of train_plan.course_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Long getCourse_id() {
        return course_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.course_id
     *
     * @param course_id the value for train_plan.course_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.school_id
     *
     * @return the value of train_plan.school_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Long getSchool_id() {
        return school_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.school_id
     *
     * @param school_id the value for train_plan.school_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setSchool_id(Long school_id) {
        this.school_id = school_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.classroom_id
     *
     * @return the value of train_plan.classroom_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Long getClassroom_id() {
        return classroom_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.classroom_id
     *
     * @param classroom_id the value for train_plan.classroom_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setClassroom_id(Long classroom_id) {
        this.classroom_id = classroom_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.teacher_id
     *
     * @return the value of train_plan.teacher_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Long getTeacher_id() {
        return teacher_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.teacher_id
     *
     * @param teacher_id the value for train_plan.teacher_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setTeacher_id(Long teacher_id) {
        this.teacher_id = teacher_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.assistant
     *
     * @return the value of train_plan.assistant
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Long getAssistant() {
        return assistant;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.assistant
     *
     * @param assistant the value for train_plan.assistant
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setAssistant(Long assistant) {
        this.assistant = assistant;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.train_way
     *
     * @return the value of train_plan.train_way
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Integer getTrain_way() {
        return train_way;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.train_way
     *
     * @param train_way the value for train_plan.train_way
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setTrain_way(Integer train_way) {
        this.train_way = train_way;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.training
     *
     * @return the value of train_plan.training
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public String getTraining() {
        return training;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.training
     *
     * @param training the value for train_plan.training
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setTraining(String training) {
        this.training = training == null ? null : training.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.trainers_number
     *
     * @return the value of train_plan.trainers_number
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Integer getTrainers_number() {
        return trainers_number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.trainers_number
     *
     * @param trainers_number the value for train_plan.trainers_number
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setTrainers_number(Integer trainers_number) {
        this.trainers_number = trainers_number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan.deleted
     *
     * @return the value of train_plan.deleted
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan.deleted
     *
     * @param deleted the value for train_plan.deleted
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
     * This method corresponds to the database table train_plan
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table train_plan
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table train_plan
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", train_plan_id=").append(train_plan_id);
        sb.append(", train_begin=").append(train_begin);
        sb.append(", train_end=").append(train_end);
        sb.append(", course_id=").append(course_id);
        sb.append(", school_id=").append(school_id);
        sb.append(", classroom_id=").append(classroom_id);
        sb.append(", teacher_id=").append(teacher_id);
        sb.append(", assistant=").append(assistant);
        sb.append(", train_way=").append(train_way);
        sb.append(", training=").append(training);
        sb.append(", trainers_number=").append(trainers_number);
        sb.append(", deleted=").append(deleted);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}