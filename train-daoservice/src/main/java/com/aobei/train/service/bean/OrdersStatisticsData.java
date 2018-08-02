package com.aobei.train.service.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by mr_bl on 2018/8/2.
 */
public class OrdersStatisticsData implements Serializable {

    private String dateStr;

    private Long gmv;

    private Long num;

    private Map<String,Long> clientNumMap;

    private Long completeNum;

    private Long waitServiceNum;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getGmv() {
        return gmv;
    }

    public void setGmv(Long gmv) {
        this.gmv = gmv;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Map<String, Long> getClientNumMap() {
        return clientNumMap;
    }

    public void setClientNumMap(Map<String, Long> clientNumMap) {
        this.clientNumMap = clientNumMap;
    }

    public Long getCompleteNum() {
        return completeNum;
    }

    public void setCompleteNum(Long completeNum) {
        this.completeNum = completeNum;
    }

    public Long getWaitServiceNum() {
        return waitServiceNum;
    }

    public void setWaitServiceNum(Long waitServiceNum) {
        this.waitServiceNum = waitServiceNum;
    }
}
