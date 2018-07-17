package custom.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mr_bl on 2018/5/14.
 */
public class BillLog implements Serializable{

    private Date bill_datetime;

    private int bill_status;

    public Date getBill_datetime() {
        return bill_datetime;
    }

    public void setBill_datetime(Date bill_datetime) {
        this.bill_datetime = bill_datetime;
    }

    public int getBill_status() {
        return bill_status;
    }

    public void setBill_status(int bill_status) {
        this.bill_status = bill_status;
    }
}
