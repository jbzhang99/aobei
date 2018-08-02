package com.aobei.train.model;

import java.io.Serializable;

public class DataStatisticsSinglePartnerData implements Serializable {

    private static final long serialVersionUID = 2952689074105947934L;

    private String dateStr;  //日期

    private Long sendNum;   //派单数量

    private Long id;   //合伙人id

    private String name;  //合伙人名称

    private Long singleNum;   //拒单数量

    private String orderRate;  //接单率

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getSendNum() {
        return sendNum;
    }

    public void setSendNum(Long sendNum) {
        this.sendNum = sendNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSingleNum() {
        return singleNum;
    }

    public void setSingleNum(Long singleNum) {
        this.singleNum = singleNum;
    }

    public String getOrderRate() {
        return orderRate;
    }

    public void setOrderRate(String orderRate) {
        this.orderRate = orderRate;
    }
}
