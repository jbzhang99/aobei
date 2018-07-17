package custom.bean;

/**
 * 取消策略方法
 * Created by liqizhen on 2018/6/25.
 */
public class CancleStrategyMethod {
    //扣除金额
    private Integer deductionPrice;
    //退款原因
    private String feeRemark;
    //退款金额
    private Integer feePrice;

    public Integer getDeductionPrice() {
        return deductionPrice;
    }

    public void setDeductionPrice(Integer deductionPrice) {
        this.deductionPrice = deductionPrice;
    }

    public String getFeeRemark() {
        return feeRemark;
    }

    public void setFeeRemark(String feeRemark) {
        this.feeRemark = feeRemark;
    }

    public Integer getFeePrice() {
        return feePrice;
    }

    public void setFeePrice(Integer feePrice) {
        this.feePrice = feePrice;
    }
}
