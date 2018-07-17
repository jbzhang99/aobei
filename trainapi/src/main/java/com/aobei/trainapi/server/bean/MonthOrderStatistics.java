package com.aobei.trainapi.server.bean;

/**
 * 本月订单统计
 * Created by 15010 on 2018/6/15.
 */
public class MonthOrderStatistics {

    private int doneNum;
    private int waitServiceNum;
    private int cancelNum;
    private int refusedNum;

    public int getDoneNum() {
        return doneNum;
    }

    public void setDoneNum(int doneNum) {
        this.doneNum = doneNum;
    }

    public int getWaitServiceNum() {
        return waitServiceNum;
    }

    public void setWaitServiceNum(int waitServiceNum) {
        this.waitServiceNum = waitServiceNum;
    }

    public int getCancelNum() {
        return cancelNum;
    }

    public void setCancelNum(int cancelNum) {
        this.cancelNum = cancelNum;
    }

    public int getRefusedNum() {
        return refusedNum;
    }

    public void setRefusedNum(int refusedNum) {
        this.refusedNum = refusedNum;
    }
}
