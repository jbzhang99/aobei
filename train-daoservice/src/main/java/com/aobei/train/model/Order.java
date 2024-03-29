package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class Order implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.pay_order_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String pay_order_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.name
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.uid
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Long uid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.channel
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String channel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.client_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String client_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.price_total
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer price_total;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.price_discount
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer price_discount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.price_pay
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer price_pay;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.discount_data
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String discount_data;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.pay_type
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer pay_type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.pay_status
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer pay_status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.create_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Date create_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.pay_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Date pay_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.r_fee
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer r_fee;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.r_status
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer r_status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.r_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Date r_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.r_finish_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Date r_finish_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.cus_username
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String cus_username;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.customer_address_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Long customer_address_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.cus_phone
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String cus_phone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.cus_province
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer cus_province;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.cus_city
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer cus_city;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.cus_area
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer cus_area;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.cus_address
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String cus_address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.lbs_lat
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String lbs_lat;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.lbs_lng
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String lbs_lng;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.remark
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String remark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.remark_cancel
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String remark_cancel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.sys_remark
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String sys_remark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.status_active
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer status_active;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.expire_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Date expire_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.group_tag
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Long group_tag;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.proxyed
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer proxyed;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.proxyed_uid
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Long proxyed_uid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order.out_order_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private String out_order_id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table pay_order
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table pay_order
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.pay_order_id
     *
     * @return the value of pay_order.pay_order_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getPay_order_id() {
        return pay_order_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.pay_order_id
     *
     * @param pay_order_id the value for pay_order.pay_order_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id == null ? null : pay_order_id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.name
     *
     * @return the value of pay_order.name
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.name
     *
     * @param name the value for pay_order.name
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.uid
     *
     * @return the value of pay_order.uid
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Long getUid() {
        return uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.uid
     *
     * @param uid the value for pay_order.uid
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.channel
     *
     * @return the value of pay_order.channel
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getChannel() {
        return channel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.channel
     *
     * @param channel the value for pay_order.channel
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.client_id
     *
     * @return the value of pay_order.client_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getClient_id() {
        return client_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.client_id
     *
     * @param client_id the value for pay_order.client_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setClient_id(String client_id) {
        this.client_id = client_id == null ? null : client_id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.price_total
     *
     * @return the value of pay_order.price_total
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getPrice_total() {
        return price_total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.price_total
     *
     * @param price_total the value for pay_order.price_total
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setPrice_total(Integer price_total) {
        this.price_total = price_total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.price_discount
     *
     * @return the value of pay_order.price_discount
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getPrice_discount() {
        return price_discount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.price_discount
     *
     * @param price_discount the value for pay_order.price_discount
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setPrice_discount(Integer price_discount) {
        this.price_discount = price_discount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.price_pay
     *
     * @return the value of pay_order.price_pay
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getPrice_pay() {
        return price_pay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.price_pay
     *
     * @param price_pay the value for pay_order.price_pay
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setPrice_pay(Integer price_pay) {
        this.price_pay = price_pay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.discount_data
     *
     * @return the value of pay_order.discount_data
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getDiscount_data() {
        return discount_data;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.discount_data
     *
     * @param discount_data the value for pay_order.discount_data
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setDiscount_data(String discount_data) {
        this.discount_data = discount_data == null ? null : discount_data.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.pay_type
     *
     * @return the value of pay_order.pay_type
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getPay_type() {
        return pay_type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.pay_type
     *
     * @param pay_type the value for pay_order.pay_type
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setPay_type(Integer pay_type) {
        this.pay_type = pay_type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.pay_status
     *
     * @return the value of pay_order.pay_status
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getPay_status() {
        return pay_status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.pay_status
     *
     * @param pay_status the value for pay_order.pay_status
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setPay_status(Integer pay_status) {
        this.pay_status = pay_status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.create_datetime
     *
     * @return the value of pay_order.create_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Date getCreate_datetime() {
        return create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.create_datetime
     *
     * @param create_datetime the value for pay_order.create_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.pay_datetime
     *
     * @return the value of pay_order.pay_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Date getPay_datetime() {
        return pay_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.pay_datetime
     *
     * @param pay_datetime the value for pay_order.pay_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setPay_datetime(Date pay_datetime) {
        this.pay_datetime = pay_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.r_fee
     *
     * @return the value of pay_order.r_fee
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getR_fee() {
        return r_fee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.r_fee
     *
     * @param r_fee the value for pay_order.r_fee
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setR_fee(Integer r_fee) {
        this.r_fee = r_fee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.r_status
     *
     * @return the value of pay_order.r_status
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getR_status() {
        return r_status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.r_status
     *
     * @param r_status the value for pay_order.r_status
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setR_status(Integer r_status) {
        this.r_status = r_status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.r_datetime
     *
     * @return the value of pay_order.r_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Date getR_datetime() {
        return r_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.r_datetime
     *
     * @param r_datetime the value for pay_order.r_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setR_datetime(Date r_datetime) {
        this.r_datetime = r_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.r_finish_datetime
     *
     * @return the value of pay_order.r_finish_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Date getR_finish_datetime() {
        return r_finish_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.r_finish_datetime
     *
     * @param r_finish_datetime the value for pay_order.r_finish_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setR_finish_datetime(Date r_finish_datetime) {
        this.r_finish_datetime = r_finish_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.cus_username
     *
     * @return the value of pay_order.cus_username
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getCus_username() {
        return cus_username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.cus_username
     *
     * @param cus_username the value for pay_order.cus_username
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setCus_username(String cus_username) {
        this.cus_username = cus_username == null ? null : cus_username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.customer_address_id
     *
     * @return the value of pay_order.customer_address_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Long getCustomer_address_id() {
        return customer_address_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.customer_address_id
     *
     * @param customer_address_id the value for pay_order.customer_address_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setCustomer_address_id(Long customer_address_id) {
        this.customer_address_id = customer_address_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.cus_phone
     *
     * @return the value of pay_order.cus_phone
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getCus_phone() {
        return cus_phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.cus_phone
     *
     * @param cus_phone the value for pay_order.cus_phone
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setCus_phone(String cus_phone) {
        this.cus_phone = cus_phone == null ? null : cus_phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.cus_province
     *
     * @return the value of pay_order.cus_province
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getCus_province() {
        return cus_province;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.cus_province
     *
     * @param cus_province the value for pay_order.cus_province
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setCus_province(Integer cus_province) {
        this.cus_province = cus_province;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.cus_city
     *
     * @return the value of pay_order.cus_city
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getCus_city() {
        return cus_city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.cus_city
     *
     * @param cus_city the value for pay_order.cus_city
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setCus_city(Integer cus_city) {
        this.cus_city = cus_city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.cus_area
     *
     * @return the value of pay_order.cus_area
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getCus_area() {
        return cus_area;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.cus_area
     *
     * @param cus_area the value for pay_order.cus_area
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setCus_area(Integer cus_area) {
        this.cus_area = cus_area;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.cus_address
     *
     * @return the value of pay_order.cus_address
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getCus_address() {
        return cus_address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.cus_address
     *
     * @param cus_address the value for pay_order.cus_address
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setCus_address(String cus_address) {
        this.cus_address = cus_address == null ? null : cus_address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.lbs_lat
     *
     * @return the value of pay_order.lbs_lat
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getLbs_lat() {
        return lbs_lat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.lbs_lat
     *
     * @param lbs_lat the value for pay_order.lbs_lat
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setLbs_lat(String lbs_lat) {
        this.lbs_lat = lbs_lat == null ? null : lbs_lat.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.lbs_lng
     *
     * @return the value of pay_order.lbs_lng
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getLbs_lng() {
        return lbs_lng;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.lbs_lng
     *
     * @param lbs_lng the value for pay_order.lbs_lng
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setLbs_lng(String lbs_lng) {
        this.lbs_lng = lbs_lng == null ? null : lbs_lng.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.remark
     *
     * @return the value of pay_order.remark
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.remark
     *
     * @param remark the value for pay_order.remark
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.remark_cancel
     *
     * @return the value of pay_order.remark_cancel
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getRemark_cancel() {
        return remark_cancel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.remark_cancel
     *
     * @param remark_cancel the value for pay_order.remark_cancel
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setRemark_cancel(String remark_cancel) {
        this.remark_cancel = remark_cancel == null ? null : remark_cancel.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.sys_remark
     *
     * @return the value of pay_order.sys_remark
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getSys_remark() {
        return sys_remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.sys_remark
     *
     * @param sys_remark the value for pay_order.sys_remark
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setSys_remark(String sys_remark) {
        this.sys_remark = sys_remark == null ? null : sys_remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.status_active
     *
     * @return the value of pay_order.status_active
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getStatus_active() {
        return status_active;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.status_active
     *
     * @param status_active the value for pay_order.status_active
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setStatus_active(Integer status_active) {
        this.status_active = status_active;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.expire_datetime
     *
     * @return the value of pay_order.expire_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Date getExpire_datetime() {
        return expire_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.expire_datetime
     *
     * @param expire_datetime the value for pay_order.expire_datetime
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setExpire_datetime(Date expire_datetime) {
        this.expire_datetime = expire_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.group_tag
     *
     * @return the value of pay_order.group_tag
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Long getGroup_tag() {
        return group_tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.group_tag
     *
     * @param group_tag the value for pay_order.group_tag
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setGroup_tag(Long group_tag) {
        this.group_tag = group_tag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.proxyed
     *
     * @return the value of pay_order.proxyed
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer getProxyed() {
        return proxyed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.proxyed
     *
     * @param proxyed the value for pay_order.proxyed
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setProxyed(Integer proxyed) {
        this.proxyed = proxyed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.proxyed_uid
     *
     * @return the value of pay_order.proxyed_uid
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Long getProxyed_uid() {
        return proxyed_uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.proxyed_uid
     *
     * @param proxyed_uid the value for pay_order.proxyed_uid
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setProxyed_uid(Long proxyed_uid) {
        this.proxyed_uid = proxyed_uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order.out_order_id
     *
     * @return the value of pay_order.out_order_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public String getOut_order_id() {
        return out_order_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order.out_order_id
     *
     * @param out_order_id the value for pay_order.out_order_id
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void setOut_order_id(String out_order_id) {
        this.out_order_id = out_order_id == null ? null : out_order_id.trim();
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pay_order
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pay_order
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pay_order
     *
     * @mbg.generated Thu Jul 12 21:00:23 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", pay_order_id=").append(pay_order_id);
        sb.append(", name=").append(name);
        sb.append(", uid=").append(uid);
        sb.append(", channel=").append(channel);
        sb.append(", client_id=").append(client_id);
        sb.append(", price_total=").append(price_total);
        sb.append(", price_discount=").append(price_discount);
        sb.append(", price_pay=").append(price_pay);
        sb.append(", discount_data=").append(discount_data);
        sb.append(", pay_type=").append(pay_type);
        sb.append(", pay_status=").append(pay_status);
        sb.append(", create_datetime=").append(create_datetime);
        sb.append(", pay_datetime=").append(pay_datetime);
        sb.append(", r_fee=").append(r_fee);
        sb.append(", r_status=").append(r_status);
        sb.append(", r_datetime=").append(r_datetime);
        sb.append(", r_finish_datetime=").append(r_finish_datetime);
        sb.append(", cus_username=").append(cus_username);
        sb.append(", customer_address_id=").append(customer_address_id);
        sb.append(", cus_phone=").append(cus_phone);
        sb.append(", cus_province=").append(cus_province);
        sb.append(", cus_city=").append(cus_city);
        sb.append(", cus_area=").append(cus_area);
        sb.append(", cus_address=").append(cus_address);
        sb.append(", lbs_lat=").append(lbs_lat);
        sb.append(", lbs_lng=").append(lbs_lng);
        sb.append(", remark=").append(remark);
        sb.append(", remark_cancel=").append(remark_cancel);
        sb.append(", sys_remark=").append(sys_remark);
        sb.append(", status_active=").append(status_active);
        sb.append(", expire_datetime=").append(expire_datetime);
        sb.append(", group_tag=").append(group_tag);
        sb.append(", proxyed=").append(proxyed);
        sb.append(", proxyed_uid=").append(proxyed_uid);
        sb.append(", out_order_id=").append(out_order_id);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}