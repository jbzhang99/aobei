package com.aobei.train.service.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * 数据统计 顾客数据统计 消费顾客数据量
 */
public class PurchaseStudentStatisticsData implements Serializable {

    private static final long serialVersionUID = 2686927402336727171L;

    private String dateStr;

    private Long totalCustomNum;       // 服务人员总数

    private Long serviceunitTotalNum; // 服务单总数

    private Long onJobNum;
    private Long noJobNum;
    private String runoffNum;

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

    public Long getServiceunitTotalNum() {
        return serviceunitTotalNum;
    }

    public void setServiceunitTotalNum(Long serviceunitTotalNum) {
        this.serviceunitTotalNum = serviceunitTotalNum;
    }

    public Long getOnJobNum() {
        return onJobNum;
    }

    public void setOnJobNum(Long onJobNum) {
        this.onJobNum = onJobNum;
    }

    public Long getNoJobNum() {
        return noJobNum;
    }

    public void setNoJobNum(Long noJobNum) {
        this.noJobNum = noJobNum;
    }

    public String getRunoffNum() {
        return runoffNum;
    }

    public void setRunoffNum(String runoffNum) {
        this.runoffNum = runoffNum;
    }
}
