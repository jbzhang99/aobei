package com.aobei.train.model;

import java.io.Serializable;

public class PlanStudentKey implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan__student.train_plan_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Long train_plan_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column train_plan__student.student_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private Long student_id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table train_plan__student
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan__student.train_plan_id
     *
     * @return the value of train_plan__student.train_plan_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Long getTrain_plan_id() {
        return train_plan_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan__student.train_plan_id
     *
     * @param train_plan_id the value for train_plan__student.train_plan_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setTrain_plan_id(Long train_plan_id) {
        this.train_plan_id = train_plan_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column train_plan__student.student_id
     *
     * @return the value of train_plan__student.student_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public Long getStudent_id() {
        return student_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column train_plan__student.student_id
     *
     * @param student_id the value for train_plan__student.student_id
     *
     * @mbg.generated Thu Feb 01 11:23:32 CST 2018
     */
    public void setStudent_id(Long student_id) {
        this.student_id = student_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table train_plan__student
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
        sb.append(", student_id=").append(student_id);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}