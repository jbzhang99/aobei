package custom.bean;

import java.io.Serializable;

/**
 * 数据统计 优惠卷表格数据
 */
public class CouponTableStatisticsData implements Serializable {

    private static final long serialVersionUID = 4816854928672741479L;

    private String dateStr;

    private Long planMoney;      // 预算金额

    private Long numTotal;        // 优惠卷数量

    private Long numUsed;        // 优惠卷使用数量

    private String type;           // 类型

    private String useStartDatetime;  // 优惠卷开始时间

    private String useEndDatetime;  // 优惠卷结束时间

    private Long gmv;

    private Long regUserCount;    // 拉新用户数

    private Long totalUsedMoney;  // 优惠卷 使用金额

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getNumTotal() {
        return numTotal;
    }

    public void setNumTotal(Long numTotal) {
        this.numTotal = numTotal;
    }

    public Long getNumUsed() {
        return numUsed;
    }

    public void setNumUsed(Long numUsed) {
        this.numUsed = numUsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUseStartDatetime() {
        return useStartDatetime;
    }

    public void setUseStartDatetime(String useStartDatetime) {
        this.useStartDatetime = useStartDatetime;
    }

    public String getUseEndDatetime() {
        return useEndDatetime;
    }

    public void setUseEndDatetime(String useEndDatetime) {
        this.useEndDatetime = useEndDatetime;
    }

    public Long getGmv() {
        return gmv;
    }

    public void setGmv(Long gmv) {
        this.gmv = gmv;
    }

    public Long getRegUserCount() {
        return regUserCount;
    }

    public void setRegUserCount(Long regUserCount) {
        this.regUserCount = regUserCount;
    }

    public Long getTotalUsedMoney() {
        return totalUsedMoney;
    }

    public void setTotalUsedMoney(Long totalUsedMoney) {
        this.totalUsedMoney = totalUsedMoney;
    }

    public Long getPlanMoney() {
        return planMoney;
    }

    public void setPlanMoney(Long planMoney) {
        this.planMoney = planMoney;
    }
}
