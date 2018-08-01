package com.aobei.train.service.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * 数据统计 顾客数据统计 消费顾客数据量
 */
public class PurchaseCustomStatisticsData implements Serializable {

    private static final long serialVersionUID = 2686927402336727171L;

    private String dateStr;

    private Long totalCustomNum;       // 顾客总数

    private Long purchaseTotalCustomNum; // 产生消费的顾客总数

    private Long rePurchaseTotalCustomNum; // 复购消费的顾客总数

    private String purchasePercent;          // 复购百分比

    private Map<String, Long> clientNumMap;   // 客户端数量

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

    public Long getPurchaseTotalCustomNum() {
        return purchaseTotalCustomNum;
    }

    public void setPurchaseTotalCustomNum(Long purchaseTotalCustomNum) {
        this.purchaseTotalCustomNum = purchaseTotalCustomNum;
    }

    public Long getRePurchaseTotalCustomNum() {
        return rePurchaseTotalCustomNum;
    }

    public void setRePurchaseTotalCustomNum(Long rePurchaseTotalCustomNum) {
        this.rePurchaseTotalCustomNum = rePurchaseTotalCustomNum;
    }

    public String getPurchasePercent() {
        return purchasePercent;
    }

    public void setPurchasePercent(String purchasePercent) {
        this.purchasePercent = purchasePercent;
    }


    public Map<String, Long> getClientNumMap() {
        return clientNumMap;
    }

    public void setClientNumMap(Map<String, Long> clientNumMap) {
        this.clientNumMap = clientNumMap;
    }
}
