package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;

public class CouponAndCouponEnv implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon__coupon_env.coupon_env_id
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    private Long coupon_env_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon__coupon_env.coupon_id
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    private Long coupon_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column coupon__coupon_env.none
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    private String none;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table coupon__coupon_env
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table coupon__coupon_env
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon__coupon_env.coupon_env_id
     *
     * @return the value of coupon__coupon_env.coupon_env_id
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    public Long getCoupon_env_id() {
        return coupon_env_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon__coupon_env.coupon_env_id
     *
     * @param coupon_env_id the value for coupon__coupon_env.coupon_env_id
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    public void setCoupon_env_id(Long coupon_env_id) {
        this.coupon_env_id = coupon_env_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon__coupon_env.coupon_id
     *
     * @return the value of coupon__coupon_env.coupon_id
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    public Long getCoupon_id() {
        return coupon_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon__coupon_env.coupon_id
     *
     * @param coupon_id the value for coupon__coupon_env.coupon_id
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    public void setCoupon_id(Long coupon_id) {
        this.coupon_id = coupon_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column coupon__coupon_env.none
     *
     * @return the value of coupon__coupon_env.none
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    public String getNone() {
        return none;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column coupon__coupon_env.none
     *
     * @param none the value for coupon__coupon_env.none
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    public void setNone(String none) {
        this.none = none == null ? null : none.trim();
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon__coupon_env
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon__coupon_env
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coupon__coupon_env
     *
     * @mbg.generated Tue Jul 24 16:41:31 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", coupon_env_id=").append(coupon_env_id);
        sb.append(", coupon_id=").append(coupon_id);
        sb.append(", none=").append(none);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}