package custom.bean;


import java.io.Serializable;
import java.util.List;

public class EvaluateBase implements Serializable{

    private static final long serialVersionUID = 4977583779621922432L;
    Double avgScore;
    Long total;
    List<Evaluate> evaluates;
    int percent;
    String percentStr;
    public Double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Double avgScore) {
        this.avgScore = avgScore;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Evaluate> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(List<Evaluate> evaluates) {
        this.evaluates = evaluates;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getPercentStr() {
        return percentStr;
    }

    public void setPercentStr(String percentStr) {
        this.percentStr = percentStr;
    }
}
