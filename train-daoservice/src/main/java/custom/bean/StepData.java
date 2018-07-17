package custom.bean;

/**
 * Created by xk873 on 2018/6/14.
 */
public class StepData implements Comparable<StepData>{
    private int d;
    private String v;

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    @Override
    public int compareTo(StepData o) {
        return Integer.valueOf(this.getD()).compareTo(Integer.valueOf(o.getD()));
    }
}
