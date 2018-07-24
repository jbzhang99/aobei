package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class CouponEnv implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon_env.coupon_env_id
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private Long coupon_env_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon_env.name
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon_env.type
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon_env.condition_env
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private String condition_env;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon_env.coupon_number
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private Integer coupon_number;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon_env.coupon_id
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private Long coupon_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon_env.start_datetime
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private Date start_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon_env.end_datetime
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private Date end_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon_env.coupon_env_type
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private Integer coupon_env_type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon_env.create_datetime
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private Date create_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon_env.status
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table coupon_env
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table coupon_env
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon_env.coupon_env_id
     *
     * @return the value of coupon_env.coupon_env_id
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public Long getCoupon_env_id() {
        return coupon_env_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon_env.coupon_env_id
     *
     * @param coupon_env_id the value for coupon_env.coupon_env_id
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void setCoupon_env_id(Long coupon_env_id) {
        this.coupon_env_id = coupon_env_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon_env.name
     *
     * @return the value of coupon_env.name
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon_env.name
     *
     * @param name the value for coupon_env.name
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon_env.type
     *
     * @return the value of coupon_env.type
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon_env.type
     *
     * @param type the value for coupon_env.type
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon_env.condition_env
     *
     * @return the value of coupon_env.condition_env
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public String getCondition_env() {
        return condition_env;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon_env.condition_env
     *
     * @param condition_env the value for coupon_env.condition_env
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void setCondition_env(String condition_env) {
        this.condition_env = condition_env == null ? null : condition_env.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon_env.coupon_number
     *
     * @return the value of coupon_env.coupon_number
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public Integer getCoupon_number() {
        return coupon_number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon_env.coupon_number
     *
     * @param coupon_number the value for coupon_env.coupon_number
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void setCoupon_number(Integer coupon_number) {
        this.coupon_number = coupon_number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon_env.coupon_id
     *
     * @return the value of coupon_env.coupon_id
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public Long getCoupon_id() {
        return coupon_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon_env.coupon_id
     *
     * @param coupon_id the value for coupon_env.coupon_id
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void setCoupon_id(Long coupon_id) {
        this.coupon_id = coupon_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon_env.start_datetime
     *
     * @return the value of coupon_env.start_datetime
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public Date getStart_datetime() {
        return start_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon_env.start_datetime
     *
     * @param start_datetime the value for coupon_env.start_datetime
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void setStart_datetime(Date start_datetime) {
        this.start_datetime = start_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon_env.end_datetime
     *
     * @return the value of coupon_env.end_datetime
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public Date getEnd_datetime() {
        return end_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon_env.end_datetime
     *
     * @param end_datetime the value for coupon_env.end_datetime
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void setEnd_datetime(Date end_datetime) {
        this.end_datetime = end_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon_env.coupon_env_type
     *
     * @return the value of coupon_env.coupon_env_type
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public Integer getCoupon_env_type() {
        return coupon_env_type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon_env.coupon_env_type
     *
     * @param coupon_env_type the value for coupon_env.coupon_env_type
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void setCoupon_env_type(Integer coupon_env_type) {
        this.coupon_env_type = coupon_env_type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon_env.create_datetime
     *
     * @return the value of coupon_env.create_datetime
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public Date getCreate_datetime() {
        return create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon_env.create_datetime
     *
     * @param create_datetime the value for coupon_env.create_datetime
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon_env.status
     *
     * @return the value of coupon_env.status
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon_env.status
     *
     * @param status the value for coupon_env.status
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_env
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_env
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon_env
     *
     * @mbg.generated Tue Jul 24 18:35:02 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", coupon_env_id=").append(coupon_env_id);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", condition_env=").append(condition_env);
        sb.append(", coupon_number=").append(coupon_number);
        sb.append(", coupon_id=").append(coupon_id);
        sb.append(", start_datetime=").append(start_datetime);
        sb.append(", end_datetime=").append(end_datetime);
        sb.append(", coupon_env_type=").append(coupon_env_type);
        sb.append(", create_datetime=").append(create_datetime);
        sb.append(", status=").append(status);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}