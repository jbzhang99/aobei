package custom.bean;

import java.io.Serializable;

public class DataStatisticsCustomData implements Serializable {

    private static final long serialVersionUID = 2952689074105947934L;

    private String dateStr;

    private Long num;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }
}
