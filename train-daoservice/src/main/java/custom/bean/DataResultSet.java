package custom.bean;

import java.io.Serializable;

/**
 * Created by mr_bl on 2018/8/1.
 */
public class DataResultSet implements Serializable{

    private String dateStr;

    private long num;

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
}
