package com.aobei.trainapi.schema.input;

public class OrderInput {


    Long product_id;
    Long psku_id;
    String begin_datetime;
    String end_datatime;
    Long customer_address_id;
    Long coupon_receive_id;
    int num ;
    int pay_type;
    String remark;

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getPsku_id() {
        return psku_id;
    }

    public void setPsku_id(Long psku_id) {
        this.psku_id = psku_id;
    }

    public String getBegin_datetime() {
        return begin_datetime;
    }

    public void setBegin_datetime(String begin_datetime) {
        this.begin_datetime = begin_datetime;
    }

    public String getEnd_datatime() {
        return end_datatime;
    }

    public void setEnd_datatime(String end_datatime) {
        this.end_datatime = end_datatime;
    }

    public Long getCustomer_address_id() {
        return customer_address_id;
    }

    public void setCustomer_address_id(Long customer_address_id) {
        this.customer_address_id = customer_address_id;
    }

    public Long getCoupon_receive_id() {
        return coupon_receive_id;
    }

    public void setCoupon_receive_id(Long coupon_receive_id) {
        this.coupon_receive_id = coupon_receive_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "OrderInput{" +
                "product_id=" + product_id +
                ", psku_id=" + psku_id +
                ", begin_datetime='" + begin_datetime + '\'' +
                ", end_datatime='" + end_datatime + '\'' +
                ", customer_address_id=" + customer_address_id +
                ", coupon_receive_id=" + coupon_receive_id +
                ", num=" + num +
                ", pay_type=" + pay_type +
                ", remark='" + remark + '\'' +
                '}';
    }
}
