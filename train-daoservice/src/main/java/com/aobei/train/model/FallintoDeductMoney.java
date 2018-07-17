package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class FallintoDeductMoney implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.fallinto_deduct_money_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Long fallinto_deduct_money_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.balance_cycle
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private String balance_cycle;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.deduct_money_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Long deduct_money_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.pay_order_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private String pay_order_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.serviceunit_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Long serviceunit_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.order_name
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private String order_name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.order_create_datetime
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Date order_create_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.deduct_money_create_datetime
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Date deduct_money_create_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.deduct_confirm_datetime
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Date deduct_confirm_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.price_total
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Integer price_total;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.price_discount
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Integer price_discount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.price_pay
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Integer price_pay;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.deduct_money
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Integer deduct_money;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.partner_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Long partner_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.partner_name
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private String partner_name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.partner_level
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Integer partner_level;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.cooperation_start
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Date cooperation_start;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.cooperation_end
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Date cooperation_end;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column fallinto_deduct_money.status
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table fallinto_deduct_money
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table fallinto_deduct_money
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.fallinto_deduct_money_id
     *
     * @return the value of fallinto_deduct_money.fallinto_deduct_money_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Long getFallinto_deduct_money_id() {
        return fallinto_deduct_money_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.fallinto_deduct_money_id
     *
     * @param fallinto_deduct_money_id the value for fallinto_deduct_money.fallinto_deduct_money_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setFallinto_deduct_money_id(Long fallinto_deduct_money_id) {
        this.fallinto_deduct_money_id = fallinto_deduct_money_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.balance_cycle
     *
     * @return the value of fallinto_deduct_money.balance_cycle
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public String getBalance_cycle() {
        return balance_cycle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.balance_cycle
     *
     * @param balance_cycle the value for fallinto_deduct_money.balance_cycle
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setBalance_cycle(String balance_cycle) {
        this.balance_cycle = balance_cycle == null ? null : balance_cycle.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.deduct_money_id
     *
     * @return the value of fallinto_deduct_money.deduct_money_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Long getDeduct_money_id() {
        return deduct_money_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.deduct_money_id
     *
     * @param deduct_money_id the value for fallinto_deduct_money.deduct_money_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setDeduct_money_id(Long deduct_money_id) {
        this.deduct_money_id = deduct_money_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.pay_order_id
     *
     * @return the value of fallinto_deduct_money.pay_order_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public String getPay_order_id() {
        return pay_order_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.pay_order_id
     *
     * @param pay_order_id the value for fallinto_deduct_money.pay_order_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id == null ? null : pay_order_id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.serviceunit_id
     *
     * @return the value of fallinto_deduct_money.serviceunit_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Long getServiceunit_id() {
        return serviceunit_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.serviceunit_id
     *
     * @param serviceunit_id the value for fallinto_deduct_money.serviceunit_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setServiceunit_id(Long serviceunit_id) {
        this.serviceunit_id = serviceunit_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.order_name
     *
     * @return the value of fallinto_deduct_money.order_name
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public String getOrder_name() {
        return order_name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.order_name
     *
     * @param order_name the value for fallinto_deduct_money.order_name
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setOrder_name(String order_name) {
        this.order_name = order_name == null ? null : order_name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.order_create_datetime
     *
     * @return the value of fallinto_deduct_money.order_create_datetime
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Date getOrder_create_datetime() {
        return order_create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.order_create_datetime
     *
     * @param order_create_datetime the value for fallinto_deduct_money.order_create_datetime
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setOrder_create_datetime(Date order_create_datetime) {
        this.order_create_datetime = order_create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.deduct_money_create_datetime
     *
     * @return the value of fallinto_deduct_money.deduct_money_create_datetime
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Date getDeduct_money_create_datetime() {
        return deduct_money_create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.deduct_money_create_datetime
     *
     * @param deduct_money_create_datetime the value for fallinto_deduct_money.deduct_money_create_datetime
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setDeduct_money_create_datetime(Date deduct_money_create_datetime) {
        this.deduct_money_create_datetime = deduct_money_create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.deduct_confirm_datetime
     *
     * @return the value of fallinto_deduct_money.deduct_confirm_datetime
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Date getDeduct_confirm_datetime() {
        return deduct_confirm_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.deduct_confirm_datetime
     *
     * @param deduct_confirm_datetime the value for fallinto_deduct_money.deduct_confirm_datetime
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setDeduct_confirm_datetime(Date deduct_confirm_datetime) {
        this.deduct_confirm_datetime = deduct_confirm_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.price_total
     *
     * @return the value of fallinto_deduct_money.price_total
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Integer getPrice_total() {
        return price_total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.price_total
     *
     * @param price_total the value for fallinto_deduct_money.price_total
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setPrice_total(Integer price_total) {
        this.price_total = price_total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.price_discount
     *
     * @return the value of fallinto_deduct_money.price_discount
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Integer getPrice_discount() {
        return price_discount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.price_discount
     *
     * @param price_discount the value for fallinto_deduct_money.price_discount
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setPrice_discount(Integer price_discount) {
        this.price_discount = price_discount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.price_pay
     *
     * @return the value of fallinto_deduct_money.price_pay
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Integer getPrice_pay() {
        return price_pay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.price_pay
     *
     * @param price_pay the value for fallinto_deduct_money.price_pay
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setPrice_pay(Integer price_pay) {
        this.price_pay = price_pay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.deduct_money
     *
     * @return the value of fallinto_deduct_money.deduct_money
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Integer getDeduct_money() {
        return deduct_money;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.deduct_money
     *
     * @param deduct_money the value for fallinto_deduct_money.deduct_money
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setDeduct_money(Integer deduct_money) {
        this.deduct_money = deduct_money;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.partner_id
     *
     * @return the value of fallinto_deduct_money.partner_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Long getPartner_id() {
        return partner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.partner_id
     *
     * @param partner_id the value for fallinto_deduct_money.partner_id
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setPartner_id(Long partner_id) {
        this.partner_id = partner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.partner_name
     *
     * @return the value of fallinto_deduct_money.partner_name
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public String getPartner_name() {
        return partner_name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.partner_name
     *
     * @param partner_name the value for fallinto_deduct_money.partner_name
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name == null ? null : partner_name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.partner_level
     *
     * @return the value of fallinto_deduct_money.partner_level
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Integer getPartner_level() {
        return partner_level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.partner_level
     *
     * @param partner_level the value for fallinto_deduct_money.partner_level
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setPartner_level(Integer partner_level) {
        this.partner_level = partner_level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.cooperation_start
     *
     * @return the value of fallinto_deduct_money.cooperation_start
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Date getCooperation_start() {
        return cooperation_start;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.cooperation_start
     *
     * @param cooperation_start the value for fallinto_deduct_money.cooperation_start
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setCooperation_start(Date cooperation_start) {
        this.cooperation_start = cooperation_start;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.cooperation_end
     *
     * @return the value of fallinto_deduct_money.cooperation_end
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Date getCooperation_end() {
        return cooperation_end;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.cooperation_end
     *
     * @param cooperation_end the value for fallinto_deduct_money.cooperation_end
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setCooperation_end(Date cooperation_end) {
        this.cooperation_end = cooperation_end;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column fallinto_deduct_money.status
     *
     * @return the value of fallinto_deduct_money.status
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column fallinto_deduct_money.status
     *
     * @param status the value for fallinto_deduct_money.status
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fallinto_deduct_money
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fallinto_deduct_money
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fallinto_deduct_money
     *
     * @mbg.generated Wed Jun 27 18:18:06 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fallinto_deduct_money_id=").append(fallinto_deduct_money_id);
        sb.append(", balance_cycle=").append(balance_cycle);
        sb.append(", deduct_money_id=").append(deduct_money_id);
        sb.append(", pay_order_id=").append(pay_order_id);
        sb.append(", serviceunit_id=").append(serviceunit_id);
        sb.append(", order_name=").append(order_name);
        sb.append(", order_create_datetime=").append(order_create_datetime);
        sb.append(", deduct_money_create_datetime=").append(deduct_money_create_datetime);
        sb.append(", deduct_confirm_datetime=").append(deduct_confirm_datetime);
        sb.append(", price_total=").append(price_total);
        sb.append(", price_discount=").append(price_discount);
        sb.append(", price_pay=").append(price_pay);
        sb.append(", deduct_money=").append(deduct_money);
        sb.append(", partner_id=").append(partner_id);
        sb.append(", partner_name=").append(partner_name);
        sb.append(", partner_level=").append(partner_level);
        sb.append(", cooperation_start=").append(cooperation_start);
        sb.append(", cooperation_end=").append(cooperation_end);
        sb.append(", status=").append(status);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}