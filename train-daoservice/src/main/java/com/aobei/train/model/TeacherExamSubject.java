package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;

public class TeacherExamSubject extends TeacherExamSubjectKey implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column teacher__exam_subject.none
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Long none;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table teacher__exam_subject
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table teacher__exam_subject
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column teacher__exam_subject.none
     *
     * @return the value of teacher__exam_subject.none
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Long getNone() {
        return none;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column teacher__exam_subject.none
     *
     * @param none the value for teacher__exam_subject.none
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setNone(Long none) {
        this.none = none;
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher__exam_subject
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher__exam_subject
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher__exam_subject
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", none=").append(none);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}