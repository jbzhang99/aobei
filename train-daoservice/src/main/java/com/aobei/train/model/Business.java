package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class Business implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business.business_id
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    private String business_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business.user
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    private String user;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business.createTime
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business.action
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    private String action;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business.stepName
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    private String stepName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business.flowInfo
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    private String flowInfo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business.pay_order_id
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    private String pay_order_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business.cus_phone
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    private String cus_phone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table business
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table business
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business.business_id
     *
     * @return the value of business.business_id
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public String getBusiness_id() {
        return business_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business.business_id
     *
     * @param business_id the value for business.business_id
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public void setBusiness_id(String business_id) {
        this.business_id = business_id == null ? null : business_id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business.user
     *
     * @return the value of business.user
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public String getUser() {
        return user;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business.user
     *
     * @param user the value for business.user
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public void setUser(String user) {
        this.user = user == null ? null : user.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business.createTime
     *
     * @return the value of business.createTime
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business.createTime
     *
     * @param createTime the value for business.createTime
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business.action
     *
     * @return the value of business.action
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public String getAction() {
        return action;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business.action
     *
     * @param action the value for business.action
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business.stepName
     *
     * @return the value of business.stepName
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public String getStepName() {
        return stepName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business.stepName
     *
     * @param stepName the value for business.stepName
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public void setStepName(String stepName) {
        this.stepName = stepName == null ? null : stepName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business.flowInfo
     *
     * @return the value of business.flowInfo
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public String getFlowInfo() {
        return flowInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business.flowInfo
     *
     * @param flowInfo the value for business.flowInfo
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public void setFlowInfo(String flowInfo) {
        this.flowInfo = flowInfo == null ? null : flowInfo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business.pay_order_id
     *
     * @return the value of business.pay_order_id
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public String getPay_order_id() {
        return pay_order_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business.pay_order_id
     *
     * @param pay_order_id the value for business.pay_order_id
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id == null ? null : pay_order_id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business.cus_phone
     *
     * @return the value of business.cus_phone
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public String getCus_phone() {
        return cus_phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business.cus_phone
     *
     * @param cus_phone the value for business.cus_phone
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public void setCus_phone(String cus_phone) {
        this.cus_phone = cus_phone == null ? null : cus_phone.trim();
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", business_id=").append(business_id);
        sb.append(", user=").append(user);
        sb.append(", createTime=").append(createTime);
        sb.append(", action=").append(action);
        sb.append(", stepName=").append(stepName);
        sb.append(", flowInfo=").append(flowInfo);
        sb.append(", pay_order_id=").append(pay_order_id);
        sb.append(", cus_phone=").append(cus_phone);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}