package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class Refund implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.refund_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Long refund_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.pay_order_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private String pay_order_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.fee
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Integer fee;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.info
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private String info;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.refund_date
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Date refund_date;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.create_date
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Date create_date;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.status
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Integer status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.rtype
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Integer rtype;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.user_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Long user_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.r_info
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private String r_info;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.r_date
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Date r_date;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.r_user_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Long r_user_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.apply_type
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Integer apply_type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.order_name
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private String order_name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.cuname
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private String cuname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.uphone
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private String uphone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.cus_address
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private String cus_address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.price_total
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Integer price_total;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.price_pay
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Integer price_pay;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.partner_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Long partner_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column refund.student_name
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private String student_name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table refund
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table refund
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.refund_id
     *
     * @return the value of refund.refund_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Long getRefund_id() {
        return refund_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.refund_id
     *
     * @param refund_id the value for refund.refund_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setRefund_id(Long refund_id) {
        this.refund_id = refund_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.pay_order_id
     *
     * @return the value of refund.pay_order_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public String getPay_order_id() {
        return pay_order_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.pay_order_id
     *
     * @param pay_order_id the value for refund.pay_order_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id == null ? null : pay_order_id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.fee
     *
     * @return the value of refund.fee
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Integer getFee() {
        return fee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.fee
     *
     * @param fee the value for refund.fee
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setFee(Integer fee) {
        this.fee = fee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.info
     *
     * @return the value of refund.info
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public String getInfo() {
        return info;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.info
     *
     * @param info the value for refund.info
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.refund_date
     *
     * @return the value of refund.refund_date
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Date getRefund_date() {
        return refund_date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.refund_date
     *
     * @param refund_date the value for refund.refund_date
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setRefund_date(Date refund_date) {
        this.refund_date = refund_date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.create_date
     *
     * @return the value of refund.create_date
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Date getCreate_date() {
        return create_date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.create_date
     *
     * @param create_date the value for refund.create_date
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.status
     *
     * @return the value of refund.status
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.status
     *
     * @param status the value for refund.status
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.rtype
     *
     * @return the value of refund.rtype
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Integer getRtype() {
        return rtype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.rtype
     *
     * @param rtype the value for refund.rtype
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setRtype(Integer rtype) {
        this.rtype = rtype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.user_id
     *
     * @return the value of refund.user_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Long getUser_id() {
        return user_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.user_id
     *
     * @param user_id the value for refund.user_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.r_info
     *
     * @return the value of refund.r_info
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public String getR_info() {
        return r_info;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.r_info
     *
     * @param r_info the value for refund.r_info
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setR_info(String r_info) {
        this.r_info = r_info == null ? null : r_info.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.r_date
     *
     * @return the value of refund.r_date
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Date getR_date() {
        return r_date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.r_date
     *
     * @param r_date the value for refund.r_date
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setR_date(Date r_date) {
        this.r_date = r_date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.r_user_id
     *
     * @return the value of refund.r_user_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Long getR_user_id() {
        return r_user_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.r_user_id
     *
     * @param r_user_id the value for refund.r_user_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setR_user_id(Long r_user_id) {
        this.r_user_id = r_user_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.apply_type
     *
     * @return the value of refund.apply_type
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Integer getApply_type() {
        return apply_type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.apply_type
     *
     * @param apply_type the value for refund.apply_type
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setApply_type(Integer apply_type) {
        this.apply_type = apply_type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.order_name
     *
     * @return the value of refund.order_name
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public String getOrder_name() {
        return order_name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.order_name
     *
     * @param order_name the value for refund.order_name
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setOrder_name(String order_name) {
        this.order_name = order_name == null ? null : order_name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.cuname
     *
     * @return the value of refund.cuname
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public String getCuname() {
        return cuname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.cuname
     *
     * @param cuname the value for refund.cuname
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setCuname(String cuname) {
        this.cuname = cuname == null ? null : cuname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.uphone
     *
     * @return the value of refund.uphone
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public String getUphone() {
        return uphone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.uphone
     *
     * @param uphone the value for refund.uphone
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setUphone(String uphone) {
        this.uphone = uphone == null ? null : uphone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.cus_address
     *
     * @return the value of refund.cus_address
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public String getCus_address() {
        return cus_address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.cus_address
     *
     * @param cus_address the value for refund.cus_address
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setCus_address(String cus_address) {
        this.cus_address = cus_address == null ? null : cus_address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.price_total
     *
     * @return the value of refund.price_total
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Integer getPrice_total() {
        return price_total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.price_total
     *
     * @param price_total the value for refund.price_total
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setPrice_total(Integer price_total) {
        this.price_total = price_total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.price_pay
     *
     * @return the value of refund.price_pay
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Integer getPrice_pay() {
        return price_pay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.price_pay
     *
     * @param price_pay the value for refund.price_pay
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setPrice_pay(Integer price_pay) {
        this.price_pay = price_pay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.partner_id
     *
     * @return the value of refund.partner_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Long getPartner_id() {
        return partner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.partner_id
     *
     * @param partner_id the value for refund.partner_id
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setPartner_id(Long partner_id) {
        this.partner_id = partner_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column refund.student_name
     *
     * @return the value of refund.student_name
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public String getStudent_name() {
        return student_name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column refund.student_name
     *
     * @param student_name the value for refund.student_name
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void setStudent_name(String student_name) {
        this.student_name = student_name == null ? null : student_name.trim();
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table refund
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table refund
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table refund
     *
     * @mbg.generated Sun Apr 08 11:15:25 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", refund_id=").append(refund_id);
        sb.append(", pay_order_id=").append(pay_order_id);
        sb.append(", fee=").append(fee);
        sb.append(", info=").append(info);
        sb.append(", refund_date=").append(refund_date);
        sb.append(", create_date=").append(create_date);
        sb.append(", status=").append(status);
        sb.append(", rtype=").append(rtype);
        sb.append(", user_id=").append(user_id);
        sb.append(", r_info=").append(r_info);
        sb.append(", r_date=").append(r_date);
        sb.append(", r_user_id=").append(r_user_id);
        sb.append(", apply_type=").append(apply_type);
        sb.append(", order_name=").append(order_name);
        sb.append(", cuname=").append(cuname);
        sb.append(", uphone=").append(uphone);
        sb.append(", cus_address=").append(cus_address);
        sb.append(", price_total=").append(price_total);
        sb.append(", price_pay=").append(price_pay);
        sb.append(", partner_id=").append(partner_id);
        sb.append(", student_name=").append(student_name);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}