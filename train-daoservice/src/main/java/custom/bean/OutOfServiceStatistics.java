package custom.bean;

import com.aobei.train.model.OutOfService;

import java.io.Serializable;
import java.util.List;

public class OutOfServiceStatistics implements Serializable {
    private static final long serialVersionUID = 3148921855628777533L;

    private String yearAndMonth;

    private Integer workingDays;
    private Integer outDays;
    private Integer totalDays;
    List<OutOfService> outOfServices;

    public String getYearAndMonth() {
        return yearAndMonth;
    }

    public void setYearAndMonth(String yearAndMonth) {
        this.yearAndMonth = yearAndMonth;
    }

    public Integer getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Integer workingDays) {
        this.workingDays = workingDays;
    }

    public Integer getOutDays() {
        return outDays;
    }

    public void setOutDays(Integer outDays) {
        this.outDays = outDays;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public List<OutOfService> getOutOfServices() {
        return outOfServices;
    }

    public void setOutOfServices(List<OutOfService> outOfServices) {
        this.outOfServices = outOfServices;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OutOfServiceStatistics{");
        sb.append("yearAndMonth='").append(yearAndMonth).append('\'');
        sb.append(", workingDays=").append(workingDays);
        sb.append(", outDays=").append(outDays);
        sb.append(", totalDays=").append(totalDays);
        sb.append(", outOfServices=").append(outOfServices);
        sb.append('}');
        return sb.toString();
    }
}
