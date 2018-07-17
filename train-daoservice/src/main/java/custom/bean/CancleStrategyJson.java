package custom.bean;

/**
 * Created by liqizhen on 2018/6/25.
 */
public class CancleStrategyJson implements Comparable<CancleStrategyJson>{
    //小时
    private Integer beforeHour;
    //金额
    private String  value;
    //是否允许
    private Boolean allow;

    public Integer getBeforeHour() {
        return beforeHour;
    }

    public void setBeforeHour(Integer beforeHour) {
        this.beforeHour = beforeHour;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getAllow() {
        return allow;
    }

    public void setAllow(Boolean allow) {
        this.allow = allow;
    }

    @Override
    public int compareTo(CancleStrategyJson o) {
        int a  = this.getBeforeHour().compareTo(o.beforeHour);
        if(a==0){
            if(this.getAllow()){
                return 1;
            }else {
                return -1;
            }
        }
        return a;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CancleStrategyJson{");
        sb.append("beforeHour=").append(beforeHour);
        sb.append(", value='").append(value).append('\'');
        sb.append(", allow=").append(allow);
        sb.append('}');
        return sb.toString();
    }

}
