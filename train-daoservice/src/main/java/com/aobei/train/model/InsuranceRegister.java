package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class InsuranceRegister implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insurance_register.insurance_register_id
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    private Long insurance_register_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insurance_register.insurance_id
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    private Long insurance_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insurance_register.insurance_name
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    private String insurance_name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insurance_register.uid
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    private Long uid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insurance_register.type
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insurance_register.create_datetime
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    private Date create_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insurance_register.start_datetime
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    private Date start_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column insurance_register.end_datetime
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    private Date end_datetime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table insurance_register
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table insurance_register
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insurance_register.insurance_register_id
     *
     * @return the value of insurance_register.insurance_register_id
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public Long getInsurance_register_id() {
        return insurance_register_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insurance_register.insurance_register_id
     *
     * @param insurance_register_id the value for insurance_register.insurance_register_id
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public void setInsurance_register_id(Long insurance_register_id) {
        this.insurance_register_id = insurance_register_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insurance_register.insurance_id
     *
     * @return the value of insurance_register.insurance_id
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public Long getInsurance_id() {
        return insurance_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insurance_register.insurance_id
     *
     * @param insurance_id the value for insurance_register.insurance_id
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public void setInsurance_id(Long insurance_id) {
        this.insurance_id = insurance_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insurance_register.insurance_name
     *
     * @return the value of insurance_register.insurance_name
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public String getInsurance_name() {
        return insurance_name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insurance_register.insurance_name
     *
     * @param insurance_name the value for insurance_register.insurance_name
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public void setInsurance_name(String insurance_name) {
        this.insurance_name = insurance_name == null ? null : insurance_name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insurance_register.uid
     *
     * @return the value of insurance_register.uid
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public Long getUid() {
        return uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insurance_register.uid
     *
     * @param uid the value for insurance_register.uid
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insurance_register.type
     *
     * @return the value of insurance_register.type
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insurance_register.type
     *
     * @param type the value for insurance_register.type
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insurance_register.create_datetime
     *
     * @return the value of insurance_register.create_datetime
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public Date getCreate_datetime() {
        return create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insurance_register.create_datetime
     *
     * @param create_datetime the value for insurance_register.create_datetime
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insurance_register.start_datetime
     *
     * @return the value of insurance_register.start_datetime
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public Date getStart_datetime() {
        return start_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insurance_register.start_datetime
     *
     * @param start_datetime the value for insurance_register.start_datetime
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public void setStart_datetime(Date start_datetime) {
        this.start_datetime = start_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column insurance_register.end_datetime
     *
     * @return the value of insurance_register.end_datetime
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public Date getEnd_datetime() {
        return end_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column insurance_register.end_datetime
     *
     * @param end_datetime the value for insurance_register.end_datetime
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public void setEnd_datetime(Date end_datetime) {
        this.end_datetime = end_datetime;
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insurance_register
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insurance_register
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table insurance_register
     *
     * @mbg.generated Mon May 07 17:31:07 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", insurance_register_id=").append(insurance_register_id);
        sb.append(", insurance_id=").append(insurance_id);
        sb.append(", insurance_name=").append(insurance_name);
        sb.append(", uid=").append(uid);
        sb.append(", type=").append(type);
        sb.append(", create_datetime=").append(create_datetime);
        sb.append(", start_datetime=").append(start_datetime);
        sb.append(", end_datetime=").append(end_datetime);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}