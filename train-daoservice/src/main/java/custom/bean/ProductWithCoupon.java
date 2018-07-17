package custom.bean;

import com.aobei.train.model.Product;

public class ProductWithCoupon extends Product {

    private static final long serialVersionUID = 8646440724066808712L;
    Long coupon_id;
    Long coupon_receive_id;
    String coupon_name;
    int programme_type;  //1 折扣，2满减
    boolean recevied;
    boolean have ;  //true 又要领取的券。false 没有要领取的券
    String unit;
    public ProductWithCoupon(){

    }
    public Long getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Long coupon_id) {
        this.coupon_id = coupon_id;
    }

    public Long getCoupon_receive_id() {
        return coupon_receive_id;
    }

    public void setCoupon_receive_id(Long coupon_receive_id) {
        this.coupon_receive_id = coupon_receive_id;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }

    public boolean isRecevied() {
        return recevied;
    }

    public void setRecevied(boolean recevied) {
        this.recevied = recevied;
    }

    public int getProgramme_type() {
        return programme_type;
    }

    public void setProgramme_type(int programme_type) {
        this.programme_type = programme_type;
    }

    public boolean isHave() {
        return have;
    }

    public void setHave(boolean have) {
        this.have = have;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
