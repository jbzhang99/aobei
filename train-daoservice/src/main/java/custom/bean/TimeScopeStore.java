package custom.bean;

/**
 * Created by mr_bl on 2018/6/12.
 */
public class TimeScopeStore {

    /**
     * 服务时间范围  s - e
     */
    private SkuTime skuTime;

    /**
     * 库存
     */
    private int store;

    public SkuTime getSkuTime() {
        return skuTime;
    }

    public void setSkuTime(SkuTime skuTime) {
        this.skuTime = skuTime;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }
}
