package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class Customer implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.customer_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private Long customer_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.name
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.logo_img
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private String logo_img;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.gender
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private Integer gender;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.phone
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private String phone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.idcard
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private String idcard;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.create_datetime
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private Date create_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.locked
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private Integer locked;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.user_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private Long user_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.channel_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private Integer channel_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.client_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private String client_id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table customer
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table customer
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.customer_id
     *
     * @return the value of customer.customer_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public Long getCustomer_id() {
        return customer_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.customer_id
     *
     * @param customer_id the value for customer.customer_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.name
     *
     * @return the value of customer.name
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.name
     *
     * @param name the value for customer.name
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.logo_img
     *
     * @return the value of customer.logo_img
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public String getLogo_img() {
        return logo_img;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.logo_img
     *
     * @param logo_img the value for customer.logo_img
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void setLogo_img(String logo_img) {
        this.logo_img = logo_img == null ? null : logo_img.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.gender
     *
     * @return the value of customer.gender
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.gender
     *
     * @param gender the value for customer.gender
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.phone
     *
     * @return the value of customer.phone
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.phone
     *
     * @param phone the value for customer.phone
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.idcard
     *
     * @return the value of customer.idcard
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public String getIdcard() {
        return idcard;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.idcard
     *
     * @param idcard the value for customer.idcard
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.create_datetime
     *
     * @return the value of customer.create_datetime
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public Date getCreate_datetime() {
        return create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.create_datetime
     *
     * @param create_datetime the value for customer.create_datetime
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.locked
     *
     * @return the value of customer.locked
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public Integer getLocked() {
        return locked;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.locked
     *
     * @param locked the value for customer.locked
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.user_id
     *
     * @return the value of customer.user_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public Long getUser_id() {
        return user_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.user_id
     *
     * @param user_id the value for customer.user_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.channel_id
     *
     * @return the value of customer.channel_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public Integer getChannel_id() {
        return channel_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.channel_id
     *
     * @param channel_id the value for customer.channel_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void setChannel_id(Integer channel_id) {
        this.channel_id = channel_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.client_id
     *
     * @return the value of customer.client_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public String getClient_id() {
        return client_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.client_id
     *
     * @param client_id the value for customer.client_id
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void setClient_id(String client_id) {
        this.client_id = client_id == null ? null : client_id.trim();
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer
     *
     * @mbg.generated Wed Aug 15 16:03:23 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", customer_id=").append(customer_id);
        sb.append(", name=").append(name);
        sb.append(", logo_img=").append(logo_img);
        sb.append(", gender=").append(gender);
        sb.append(", phone=").append(phone);
        sb.append(", idcard=").append(idcard);
        sb.append(", create_datetime=").append(create_datetime);
        sb.append(", locked=").append(locked);
        sb.append(", user_id=").append(user_id);
        sb.append(", channel_id=").append(channel_id);
        sb.append(", client_id=").append(client_id);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}