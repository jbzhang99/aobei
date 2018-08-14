package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class RejectRecord implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.reject_record_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private Long reject_record_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.pay_order_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private String pay_order_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.serviceunit_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private Long serviceunit_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.create_datetime
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private Date create_datetime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.server_name
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private String server_name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.cus_username
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private String cus_username;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.cus_phone
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private String cus_phone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.cus_address
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private String cus_address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.price_total
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private Integer price_total;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.price_pay
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private Integer price_pay;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.reject_type
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private Integer reject_type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.reject_info
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private String reject_info;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.partner_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private Long partner_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reject_record.server_datetime
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private Date server_datetime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table reject_record
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table reject_record
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.reject_record_id
     *
     * @return the value of reject_record.reject_record_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public Long getReject_record_id() {
        return reject_record_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.reject_record_id
     *
     * @param reject_record_id the value for reject_record.reject_record_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setReject_record_id(Long reject_record_id) {
        this.reject_record_id = reject_record_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.pay_order_id
     *
     * @return the value of reject_record.pay_order_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public String getPay_order_id() {
        return pay_order_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.pay_order_id
     *
     * @param pay_order_id the value for reject_record.pay_order_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id == null ? null : pay_order_id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.serviceunit_id
     *
     * @return the value of reject_record.serviceunit_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public Long getServiceunit_id() {
        return serviceunit_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.serviceunit_id
     *
     * @param serviceunit_id the value for reject_record.serviceunit_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setServiceunit_id(Long serviceunit_id) {
        this.serviceunit_id = serviceunit_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.create_datetime
     *
     * @return the value of reject_record.create_datetime
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public Date getCreate_datetime() {
        return create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.create_datetime
     *
     * @param create_datetime the value for reject_record.create_datetime
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setCreate_datetime(Date create_datetime) {
        this.create_datetime = create_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.server_name
     *
     * @return the value of reject_record.server_name
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public String getServer_name() {
        return server_name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.server_name
     *
     * @param server_name the value for reject_record.server_name
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setServer_name(String server_name) {
        this.server_name = server_name == null ? null : server_name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.cus_username
     *
     * @return the value of reject_record.cus_username
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public String getCus_username() {
        return cus_username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.cus_username
     *
     * @param cus_username the value for reject_record.cus_username
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setCus_username(String cus_username) {
        this.cus_username = cus_username == null ? null : cus_username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.cus_phone
     *
     * @return the value of reject_record.cus_phone
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public String getCus_phone() {
        return cus_phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.cus_phone
     *
     * @param cus_phone the value for reject_record.cus_phone
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setCus_phone(String cus_phone) {
        this.cus_phone = cus_phone == null ? null : cus_phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.cus_address
     *
     * @return the value of reject_record.cus_address
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public String getCus_address() {
        return cus_address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.cus_address
     *
     * @param cus_address the value for reject_record.cus_address
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setCus_address(String cus_address) {
        this.cus_address = cus_address == null ? null : cus_address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.price_total
     *
     * @return the value of reject_record.price_total
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public Integer getPrice_total() {
        return price_total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.price_total
     *
     * @param price_total the value for reject_record.price_total
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setPrice_total(Integer price_total) {
        this.price_total = price_total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.price_pay
     *
     * @return the value of reject_record.price_pay
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public Integer getPrice_pay() {
        return price_pay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.price_pay
     *
     * @param price_pay the value for reject_record.price_pay
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setPrice_pay(Integer price_pay) {
        this.price_pay = price_pay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.reject_type
     *
     * @return the value of reject_record.reject_type
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public Integer getReject_type() {
        return reject_type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.reject_type
     *
     * @param reject_type the value for reject_record.reject_type
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setReject_type(Integer reject_type) {
        this.reject_type = reject_type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.reject_info
     *
     * @return the value of reject_record.reject_info
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public String getReject_info() {
        return reject_info;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.reject_info
     *
     * @param reject_info the value for reject_record.reject_info
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setReject_info(String reject_info) {
        this.reject_info = reject_info == null ? null : reject_info.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.partner_id
     *
     * @return the value of reject_record.partner_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public Long getPartner_id() {
        return partner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.partner_id
     *
     * @param partner_id the value for reject_record.partner_id
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setPartner_id(Long partner_id) {
        this.partner_id = partner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reject_record.server_datetime
     *
     * @return the value of reject_record.server_datetime
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public Date getServer_datetime() {
        return server_datetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reject_record.server_datetime
     *
     * @param server_datetime the value for reject_record.server_datetime
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void setServer_datetime(Date server_datetime) {
        this.server_datetime = server_datetime;
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reject_record
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reject_record
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reject_record
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", reject_record_id=").append(reject_record_id);
        sb.append(", pay_order_id=").append(pay_order_id);
        sb.append(", serviceunit_id=").append(serviceunit_id);
        sb.append(", create_datetime=").append(create_datetime);
        sb.append(", server_name=").append(server_name);
        sb.append(", cus_username=").append(cus_username);
        sb.append(", cus_phone=").append(cus_phone);
        sb.append(", cus_address=").append(cus_address);
        sb.append(", price_total=").append(price_total);
        sb.append(", price_pay=").append(price_pay);
        sb.append(", reject_type=").append(reject_type);
        sb.append(", reject_info=").append(reject_info);
        sb.append(", partner_id=").append(partner_id);
        sb.append(", server_datetime=").append(server_datetime);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}