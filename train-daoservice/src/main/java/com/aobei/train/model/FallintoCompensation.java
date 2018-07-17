package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class FallintoCompensation implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.fallinto_compensation_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Long fallinto_compensation_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.balance_cycle
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private String balance_cycle;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.compensation_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Long compensation_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.pay_order_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private String pay_order_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.serviceunit_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Long serviceunit_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.order_name
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private String order_name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.compensation_create_datetime
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Date compensation_create_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.compensation_confirm_datetime
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Date compensation_confirm_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.partner_bear_amount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Integer partner_bear_amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.partner_bear_coupon_amount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Integer partner_bear_coupon_amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.coupon_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Long coupon_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.coupon_receive_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Long coupon_receive_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.price_total
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Integer price_total;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.price_discount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Integer price_discount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.price_pay
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Integer price_pay;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.amount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Integer amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.partner_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Long partner_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.partner_name
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private String partner_name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.partner_level
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Integer partner_level;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.cooperation_start
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Date cooperation_start;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.cooperation_end
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Date cooperation_end;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.status
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Integer status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_compensation.order_create_datetime
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Date order_create_datetime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table fallinto_compensation
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table fallinto_compensation
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.fallinto_compensation_id
     *
     * @return the value of fallinto_compensation.fallinto_compensation_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Long getFallinto_compensation_id() {
        return fallinto_compensation_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.fallinto_compensation_id
     *
     * @param fallinto_compensation_id the value for fallinto_compensation.fallinto_compensation_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setFallinto_compensation_id(Long fallinto_compensation_id) {
        this.fallinto_compensation_id = fallinto_compensation_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.balance_cycle
     *
     * @return the value of fallinto_compensation.balance_cycle
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public String getBalance_cycle() {
        return balance_cycle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.balance_cycle
     *
     * @param balance_cycle the value for fallinto_compensation.balance_cycle
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setBalance_cycle(String balance_cycle) {
        this.balance_cycle = balance_cycle == null ? null : balance_cycle.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.compensation_id
     *
     * @return the value of fallinto_compensation.compensation_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Long getCompensation_id() {
        return compensation_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.compensation_id
     *
     * @param compensation_id the value for fallinto_compensation.compensation_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setCompensation_id(Long compensation_id) {
        this.compensation_id = compensation_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.pay_order_id
     *
     * @return the value of fallinto_compensation.pay_order_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public String getPay_order_id() {
        return pay_order_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.pay_order_id
     *
     * @param pay_order_id the value for fallinto_compensation.pay_order_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id == null ? null : pay_order_id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.serviceunit_id
     *
     * @return the value of fallinto_compensation.serviceunit_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Long getServiceunit_id() {
        return serviceunit_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.serviceunit_id
     *
     * @param serviceunit_id the value for fallinto_compensation.serviceunit_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setServiceunit_id(Long serviceunit_id) {
        this.serviceunit_id = serviceunit_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.order_name
     *
     * @return the value of fallinto_compensation.order_name
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public String getOrder_name() {
        return order_name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.order_name
     *
     * @param order_name the value for fallinto_compensation.order_name
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setOrder_name(String order_name) {
        this.order_name = order_name == null ? null : order_name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.compensation_create_datetime
     *
     * @return the value of fallinto_compensation.compensation_create_datetime
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Date getCompensation_create_datetime() {
        return compensation_create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.compensation_create_datetime
     *
     * @param compensation_create_datetime the value for fallinto_compensation.compensation_create_datetime
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setCompensation_create_datetime(Date compensation_create_datetime) {
        this.compensation_create_datetime = compensation_create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.compensation_confirm_datetime
     *
     * @return the value of fallinto_compensation.compensation_confirm_datetime
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Date getCompensation_confirm_datetime() {
        return compensation_confirm_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.compensation_confirm_datetime
     *
     * @param compensation_confirm_datetime the value for fallinto_compensation.compensation_confirm_datetime
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setCompensation_confirm_datetime(Date compensation_confirm_datetime) {
        this.compensation_confirm_datetime = compensation_confirm_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.partner_bear_amount
     *
     * @return the value of fallinto_compensation.partner_bear_amount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Integer getPartner_bear_amount() {
        return partner_bear_amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.partner_bear_amount
     *
     * @param partner_bear_amount the value for fallinto_compensation.partner_bear_amount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setPartner_bear_amount(Integer partner_bear_amount) {
        this.partner_bear_amount = partner_bear_amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.partner_bear_coupon_amount
     *
     * @return the value of fallinto_compensation.partner_bear_coupon_amount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Integer getPartner_bear_coupon_amount() {
        return partner_bear_coupon_amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.partner_bear_coupon_amount
     *
     * @param partner_bear_coupon_amount the value for fallinto_compensation.partner_bear_coupon_amount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setPartner_bear_coupon_amount(Integer partner_bear_coupon_amount) {
        this.partner_bear_coupon_amount = partner_bear_coupon_amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.coupon_id
     *
     * @return the value of fallinto_compensation.coupon_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Long getCoupon_id() {
        return coupon_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.coupon_id
     *
     * @param coupon_id the value for fallinto_compensation.coupon_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setCoupon_id(Long coupon_id) {
        this.coupon_id = coupon_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.coupon_receive_id
     *
     * @return the value of fallinto_compensation.coupon_receive_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Long getCoupon_receive_id() {
        return coupon_receive_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.coupon_receive_id
     *
     * @param coupon_receive_id the value for fallinto_compensation.coupon_receive_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setCoupon_receive_id(Long coupon_receive_id) {
        this.coupon_receive_id = coupon_receive_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.price_total
     *
     * @return the value of fallinto_compensation.price_total
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Integer getPrice_total() {
        return price_total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.price_total
     *
     * @param price_total the value for fallinto_compensation.price_total
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setPrice_total(Integer price_total) {
        this.price_total = price_total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.price_discount
     *
     * @return the value of fallinto_compensation.price_discount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Integer getPrice_discount() {
        return price_discount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.price_discount
     *
     * @param price_discount the value for fallinto_compensation.price_discount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setPrice_discount(Integer price_discount) {
        this.price_discount = price_discount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.price_pay
     *
     * @return the value of fallinto_compensation.price_pay
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Integer getPrice_pay() {
        return price_pay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.price_pay
     *
     * @param price_pay the value for fallinto_compensation.price_pay
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setPrice_pay(Integer price_pay) {
        this.price_pay = price_pay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.amount
     *
     * @return the value of fallinto_compensation.amount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.amount
     *
     * @param amount the value for fallinto_compensation.amount
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.partner_id
     *
     * @return the value of fallinto_compensation.partner_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Long getPartner_id() {
        return partner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.partner_id
     *
     * @param partner_id the value for fallinto_compensation.partner_id
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setPartner_id(Long partner_id) {
        this.partner_id = partner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.partner_name
     *
     * @return the value of fallinto_compensation.partner_name
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public String getPartner_name() {
        return partner_name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.partner_name
     *
     * @param partner_name the value for fallinto_compensation.partner_name
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name == null ? null : partner_name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.partner_level
     *
     * @return the value of fallinto_compensation.partner_level
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Integer getPartner_level() {
        return partner_level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.partner_level
     *
     * @param partner_level the value for fallinto_compensation.partner_level
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setPartner_level(Integer partner_level) {
        this.partner_level = partner_level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.cooperation_start
     *
     * @return the value of fallinto_compensation.cooperation_start
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Date getCooperation_start() {
        return cooperation_start;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.cooperation_start
     *
     * @param cooperation_start the value for fallinto_compensation.cooperation_start
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setCooperation_start(Date cooperation_start) {
        this.cooperation_start = cooperation_start;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.cooperation_end
     *
     * @return the value of fallinto_compensation.cooperation_end
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Date getCooperation_end() {
        return cooperation_end;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.cooperation_end
     *
     * @param cooperation_end the value for fallinto_compensation.cooperation_end
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setCooperation_end(Date cooperation_end) {
        this.cooperation_end = cooperation_end;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.status
     *
     * @return the value of fallinto_compensation.status
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.status
     *
     * @param status the value for fallinto_compensation.status
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_compensation.order_create_datetime
     *
     * @return the value of fallinto_compensation.order_create_datetime
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Date getOrder_create_datetime() {
        return order_create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_compensation.order_create_datetime
     *
     * @param order_create_datetime the value for fallinto_compensation.order_create_datetime
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void setOrder_create_datetime(Date order_create_datetime) {
        this.order_create_datetime = order_create_datetime;
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fallinto_compensation
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fallinto_compensation
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fallinto_compensation
     *
     * @mbg.generated Wed Jun 27 13:58:59 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fallinto_compensation_id=").append(fallinto_compensation_id);
        sb.append(", balance_cycle=").append(balance_cycle);
        sb.append(", compensation_id=").append(compensation_id);
        sb.append(", pay_order_id=").append(pay_order_id);
        sb.append(", serviceunit_id=").append(serviceunit_id);
        sb.append(", order_name=").append(order_name);
        sb.append(", compensation_create_datetime=").append(compensation_create_datetime);
        sb.append(", compensation_confirm_datetime=").append(compensation_confirm_datetime);
        sb.append(", partner_bear_amount=").append(partner_bear_amount);
        sb.append(", partner_bear_coupon_amount=").append(partner_bear_coupon_amount);
        sb.append(", coupon_id=").append(coupon_id);
        sb.append(", coupon_receive_id=").append(coupon_receive_id);
        sb.append(", price_total=").append(price_total);
        sb.append(", price_discount=").append(price_discount);
        sb.append(", price_pay=").append(price_pay);
        sb.append(", amount=").append(amount);
        sb.append(", partner_id=").append(partner_id);
        sb.append(", partner_name=").append(partner_name);
        sb.append(", partner_level=").append(partner_level);
        sb.append(", cooperation_start=").append(cooperation_start);
        sb.append(", cooperation_end=").append(cooperation_end);
        sb.append(", status=").append(status);
        sb.append(", order_create_datetime=").append(order_create_datetime);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}