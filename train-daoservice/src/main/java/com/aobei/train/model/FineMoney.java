package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class FineMoney implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fine_money.fine_money_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private Long fine_money_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fine_money.pay_order_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private String pay_order_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fine_money.serviceunit_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private Long serviceunit_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fine_money.partner_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private Long partner_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fine_money.fine_amount
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private Integer fine_amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fine_money.fine_info
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private String fine_info;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fine_money.create_datetime
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private Date create_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fine_money.fine_operator
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private Long fine_operator;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fine_money.fine_status
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private Integer fine_status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fine_money.confirm_datetime
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private Date confirm_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fine_money.confirm_operator
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private Long confirm_operator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table fine_money
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table fine_money
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fine_money.fine_money_id
     *
     * @return the value of fine_money.fine_money_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public Long getFine_money_id() {
        return fine_money_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fine_money.fine_money_id
     *
     * @param fine_money_id the value for fine_money.fine_money_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void setFine_money_id(Long fine_money_id) {
        this.fine_money_id = fine_money_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fine_money.pay_order_id
     *
     * @return the value of fine_money.pay_order_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public String getPay_order_id() {
        return pay_order_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fine_money.pay_order_id
     *
     * @param pay_order_id the value for fine_money.pay_order_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id == null ? null : pay_order_id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fine_money.serviceunit_id
     *
     * @return the value of fine_money.serviceunit_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public Long getServiceunit_id() {
        return serviceunit_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fine_money.serviceunit_id
     *
     * @param serviceunit_id the value for fine_money.serviceunit_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void setServiceunit_id(Long serviceunit_id) {
        this.serviceunit_id = serviceunit_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fine_money.partner_id
     *
     * @return the value of fine_money.partner_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public Long getPartner_id() {
        return partner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fine_money.partner_id
     *
     * @param partner_id the value for fine_money.partner_id
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void setPartner_id(Long partner_id) {
        this.partner_id = partner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fine_money.fine_amount
     *
     * @return the value of fine_money.fine_amount
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public Integer getFine_amount() {
        return fine_amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fine_money.fine_amount
     *
     * @param fine_amount the value for fine_money.fine_amount
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void setFine_amount(Integer fine_amount) {
        this.fine_amount = fine_amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fine_money.fine_info
     *
     * @return the value of fine_money.fine_info
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public String getFine_info() {
        return fine_info;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fine_money.fine_info
     *
     * @param fine_info the value for fine_money.fine_info
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void setFine_info(String fine_info) {
        this.fine_info = fine_info == null ? null : fine_info.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fine_money.create_datetime
     *
     * @return the value of fine_money.create_datetime
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public Date getCreate_datetime() {
        return create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fine_money.create_datetime
     *
     * @param create_datetime the value for fine_money.create_datetime
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fine_money.fine_operator
     *
     * @return the value of fine_money.fine_operator
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public Long getFine_operator() {
        return fine_operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fine_money.fine_operator
     *
     * @param fine_operator the value for fine_money.fine_operator
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void setFine_operator(Long fine_operator) {
        this.fine_operator = fine_operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fine_money.fine_status
     *
     * @return the value of fine_money.fine_status
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public Integer getFine_status() {
        return fine_status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fine_money.fine_status
     *
     * @param fine_status the value for fine_money.fine_status
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void setFine_status(Integer fine_status) {
        this.fine_status = fine_status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fine_money.confirm_datetime
     *
     * @return the value of fine_money.confirm_datetime
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public Date getConfirm_datetime() {
        return confirm_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fine_money.confirm_datetime
     *
     * @param confirm_datetime the value for fine_money.confirm_datetime
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void setConfirm_datetime(Date confirm_datetime) {
        this.confirm_datetime = confirm_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fine_money.confirm_operator
     *
     * @return the value of fine_money.confirm_operator
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public Long getConfirm_operator() {
        return confirm_operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fine_money.confirm_operator
     *
     * @param confirm_operator the value for fine_money.confirm_operator
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void setConfirm_operator(Long confirm_operator) {
        this.confirm_operator = confirm_operator;
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fine_money
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fine_money
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fine_money
     *
     * @mbg.generated Wed Jun 20 16:07:23 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fine_money_id=").append(fine_money_id);
        sb.append(", pay_order_id=").append(pay_order_id);
        sb.append(", serviceunit_id=").append(serviceunit_id);
        sb.append(", partner_id=").append(partner_id);
        sb.append(", fine_amount=").append(fine_amount);
        sb.append(", fine_info=").append(fine_info);
        sb.append(", create_datetime=").append(create_datetime);
        sb.append(", fine_operator=").append(fine_operator);
        sb.append(", fine_status=").append(fine_status);
        sb.append(", confirm_datetime=").append(confirm_datetime);
        sb.append(", confirm_operator=").append(confirm_operator);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}