package custom.bean;

import com.aobei.train.model.ProEvaluate;

/**
 * Created by adminL on 2018/5/7.
 */
public class ProEvaList extends ProEvaluate{

    String pro_name;//商品名称

    String psku_name;//商品sku名称

    String cus_name;//顾客名称

    public String getCus_name() {
        return cus_name;
    }

    public String getPsku_name() {
        return psku_name;
    }

    public void setPsku_name(String psku_name) {
        this.psku_name = psku_name;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }
}
