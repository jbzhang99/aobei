package custom.bean;

import java.io.Serializable;

/**
 * 数据统计 优惠卷数据统计
 */
public class PurchaseCouponStatisticsData implements Serializable {


    private static final long serialVersionUID = -5646983791067324311L;

    private String dateStr;

    private Long totalPlanMoney;        // 优惠卷预算金额

    private Long totalUsedMoney;        // 优惠卷使用金额

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getTotalPlanMoney() {
        return totalPlanMoney;
    }

    public void setTotalPlanMoney(Long totalPlanMoney) {
        this.totalPlanMoney = totalPlanMoney;
    }

    public Long getTotalUsedMoney() {
        return totalUsedMoney;
    }

    public void setTotalUsedMoney(Long totalUsedMoney) {
        this.totalUsedMoney = totalUsedMoney;
    }
}
