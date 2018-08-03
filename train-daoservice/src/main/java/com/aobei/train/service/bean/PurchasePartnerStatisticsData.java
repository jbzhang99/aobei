package com.aobei.train.service.bean;

import com.aobei.train.model.DataStatisticsSinglePartnerData;

import java.io.Serializable;
import java.util.List;

/**
 * 数据统计 顾客数据统计 消费顾客数据量
 */
public class PurchasePartnerStatisticsData implements Serializable {

    private static final long serialVersionUID = 2686927402336727171L;

    private String dateStr;

    private Long totalCustomNum;       // 合伙人员总数

    private Long sendOrdersTotalNum;   // 派单总数

    private Long singleOrdersTotalNum; //拒单总数

    private String orderRate;  //接单率

    private List<DataStatisticsSinglePartnerData> list;  //对应合伙人

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getTotalCustomNum() {
        return totalCustomNum;
    }

    public void setTotalCustomNum(Long totalCustomNum) {
        this.totalCustomNum = totalCustomNum;
    }

    public Long getSendOrdersTotalNum() {
        return sendOrdersTotalNum;
    }

    public void setSendOrdersTotalNum(Long sendOrdersTotalNum) {
        this.sendOrdersTotalNum = sendOrdersTotalNum;
    }

    public Long getSingleOrdersTotalNum() {
        return singleOrdersTotalNum;
    }

    public void setSingleOrdersTotalNum(Long singleOrdersTotalNum) {
        this.singleOrdersTotalNum = singleOrdersTotalNum;
    }

    public String getOrderRate() {
        return orderRate;
    }

    public void setOrderRate(String orderRate) {
        this.orderRate = orderRate;
    }

    public List<DataStatisticsSinglePartnerData> getList() {
        return list;
    }

    public void setList(List<DataStatisticsSinglePartnerData> list) {
        this.list = list;
    }
}
