package com.aobei.train.model;

import com.github.liyiorg.mbg.support.model.ModelUpsert;
import java.io.Serializable;
import java.util.Date;

public class OrderLog implements ModelUpsert, Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_log.pay_order_log_id
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    private Long pay_order_log_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_log.pay_order_id
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    private String pay_order_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_log.log_text
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    private String log_text;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_log.user_id
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    private Long user_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_log.operator_name
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    private String operator_name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_log.create_time
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    private Date create_time;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table pay_order_log
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    private Integer _sqlImprovement_;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table pay_order_log
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_log.pay_order_log_id
     *
     * @return the value of pay_order_log.pay_order_log_id
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public Long getPay_order_log_id() {
        return pay_order_log_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_log.pay_order_log_id
     *
     * @param pay_order_log_id the value for pay_order_log.pay_order_log_id
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public void setPay_order_log_id(Long pay_order_log_id) {
        this.pay_order_log_id = pay_order_log_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_log.pay_order_id
     *
     * @return the value of pay_order_log.pay_order_id
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public String getPay_order_id() {
        return pay_order_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_log.pay_order_id
     *
     * @param pay_order_id the value for pay_order_log.pay_order_id
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id == null ? null : pay_order_id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_log.log_text
     *
     * @return the value of pay_order_log.log_text
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public String getLog_text() {
        return log_text;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_log.log_text
     *
     * @param log_text the value for pay_order_log.log_text
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public void setLog_text(String log_text) {
        this.log_text = log_text == null ? null : log_text.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_log.user_id
     *
     * @return the value of pay_order_log.user_id
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public Long getUser_id() {
        return user_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_log.user_id
     *
     * @param user_id the value for pay_order_log.user_id
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_log.operator_name
     *
     * @return the value of pay_order_log.operator_name
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public String getOperator_name() {
        return operator_name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_log.operator_name
     *
     * @param operator_name the value for pay_order_log.operator_name
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name == null ? null : operator_name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_log.create_time
     *
     * @return the value of pay_order_log.create_time
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public Date getCreate_time() {
        return create_time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_log.create_time
     *
     * @param create_time the value for pay_order_log.create_time
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void upsert() {
        this._sqlImprovement_ = 2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pay_order_log
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public void sqlImprovement(Integer sqlImprovement) {
        this._sqlImprovement_ = sqlImprovement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pay_order_log
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    public Integer get_sqlImprovement_() {
        return this._sqlImprovement_;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pay_order_log
     *
     * @mbg.generated Fri Feb 23 15:06:43 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", pay_order_log_id=").append(pay_order_log_id);
        sb.append(", pay_order_id=").append(pay_order_id);
        sb.append(", log_text=").append(log_text);
        sb.append(", user_id=").append(user_id);
        sb.append(", operator_name=").append(operator_name);
        sb.append(", create_time=").append(create_time);
        sb.append(", _sqlImprovement_=").append(_sqlImprovement_);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}