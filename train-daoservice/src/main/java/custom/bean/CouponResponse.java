package custom.bean;

import com.aobei.train.model.Coupon;
import com.aobei.train.model.CouponReceive;

import java.io.Serializable;
import java.util.Date;

public class CouponResponse implements Serializable{

    private static final long serialVersionUID = 1482616702418823732L;
    private Long coupon_id;

    private String name;

    private String descript;

    private Integer plan_money;

    private Integer valid;

    private Integer exclusive;

    private Integer num_limit;

    private Integer num_total;

    private Integer num_able;

    private Integer priority;

    private String condition;

    private Integer condition_type;

    private String programme;

    private Integer programme_type;

    private Date useStartTime;

    private Date useEndTime;

    private Date receive_start_datetime;

    private Date create_time;

    private Date receive_end_datetime;

    private Long coupon_receive_id;

    private Long uid;

    private String pay_order_id;

    private Date receive_datetime;

    private Date use_datetime;

    private Integer status;

    private Integer verification;

    private Integer deleted;

    private boolean expire;

    private int value;

    private String unit;

    private Integer type;

    public Long getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Long coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Integer getPlan_money() {
        return plan_money;
    }

    public void setPlan_money(Integer plan_money) {
        this.plan_money = plan_money;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getExclusive() {
        return exclusive;
    }

    public void setExclusive(Integer exclusive) {
        this.exclusive = exclusive;
    }

    public Integer getNum_limit() {
        return num_limit;
    }

    public void setNum_limit(Integer num_limit) {
        this.num_limit = num_limit;
    }

    public Integer getNum_total() {
        return num_total;
    }

    public void setNum_total(Integer num_total) {
        this.num_total = num_total;
    }

    public Integer getNum_able() {
        return num_able;
    }

    public void setNum_able(Integer num_able) {
        this.num_able = num_able;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Integer getCondition_type() {
        return condition_type;
    }

    public void setCondition_type(Integer condition_type) {
        this.condition_type = condition_type;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public Integer getProgramme_type() {
        return programme_type;
    }

    public void setProgramme_type(Integer programme_type) {
        this.programme_type = programme_type;
    }

    public Date getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(Date useStartTime) {
        this.useStartTime = useStartTime;
    }

    public Date getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(Date useEndTime) {
        this.useEndTime = useEndTime;
    }

    public Date getReceive_start_datetime() {
        return receive_start_datetime;
    }

    public void setReceive_start_datetime(Date receive_start_datetime) {
        this.receive_start_datetime = receive_start_datetime;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getReceive_end_datetime() {
        return receive_end_datetime;
    }

    public void setReceive_end_datetime(Date receive_end_datetime) {
        this.receive_end_datetime = receive_end_datetime;
    }

    public Long getCoupon_receive_id() {
        return coupon_receive_id;
    }

    public void setCoupon_receive_id(Long coupon_receive_id) {
        this.coupon_receive_id = coupon_receive_id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPay_order_id() {
        return pay_order_id;
    }

    public void setPay_order_id(String pay_order_id) {
        this.pay_order_id = pay_order_id;
    }

    public Date getReceive_datetime() {
        return receive_datetime;
    }

    public void setReceive_datetime(Date receive_datetime) {
        this.receive_datetime = receive_datetime;
    }

    public Date getUse_datetime() {
        return use_datetime;
    }

    public void setUse_datetime(Date use_datetime) {
        this.use_datetime = use_datetime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVerification() {
        return verification;
    }

    public void setVerification(Integer verification) {
        this.verification = verification;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public boolean isExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
