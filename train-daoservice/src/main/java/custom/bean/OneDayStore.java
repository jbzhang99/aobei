package custom.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr_bl on 2018/6/12.
 */
public class OneDayStore {

    /**
     * 某天 yyyy-MM-dd
     */
    private String datetime;

    /**
     * 对应某天的各时间范围库存
     */
    private List<TimeScopeStore> scopeStores = new ArrayList<>();

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public List<TimeScopeStore> getScopeStores() {
        return scopeStores;
    }

    public void setScopeStores(List<TimeScopeStore> scopeStores) {
        this.scopeStores = scopeStores;
    }
}
