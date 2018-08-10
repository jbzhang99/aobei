package com.aobei.trainapi.schema.input;

public class StudentOrderInput {

	String pay_order_id;
    Long product_id;
    Long psku_id;
    String begin_datetime;
    String end_datatime;
    Long customer_address_id;
    int num ;
    String remark;
    int type;
    //服务单 1  收费单 2
	int continuousSingleState;

	public int getContinuousSingleState() {
		return continuousSingleState;
	}

	public void setContinuousSingleState(int continuousSingleState) {
		this.continuousSingleState = continuousSingleState;
	}

	public String getPay_order_id() {
		return pay_order_id;
	}
	public void setPay_order_id(String pay_order_id) {
		this.pay_order_id = pay_order_id;
	}
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
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "StudentOrderInput [pay_order_id=" + pay_order_id + ", product_id=" + product_id + ", psku_id=" + psku_id
				+ ", begin_datetime=" + begin_datetime + ", end_datatime=" + end_datatime + ", customer_address_id="
				+ customer_address_id + ", num=" + num + ", remark=" + remark + ", type=" + type + "]";
	}
	
	
}
