package custom.bean;

/**
 * Created by mr_bl on 2018/8/15.
 */
public class EffectiveOrder {

    private String dateStr;

    private long num;

    /**
     * 增长率/有效订单比
     */
    private double incrRate;

    private long stuNum;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public double getIncrRate() {
        return incrRate;
    }

    public void setIncrRate(double incrRate) {
        this.incrRate = incrRate;
    }

    public long getStuNum() {
        return stuNum;
    }

    public void setStuNum(long stuNum) {
        this.stuNum = stuNum;
    }
}
