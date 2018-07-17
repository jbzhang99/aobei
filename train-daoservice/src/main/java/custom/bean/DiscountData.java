package custom.bean;

import com.aobei.train.model.Coupon;
import com.aobei.train.model.CouponEnv;
import com.aobei.train.model.CouponReceive;

public class DiscountData implements Comparable<DiscountData>{
    Integer payPrice;
    boolean isDiscount;
    String discountData;
    CouponEnv couponEnv;
    Coupon coupon ;
    CouponReceive couponReceive;
    public Integer getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Integer payPrice) {
        this.payPrice = payPrice;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public void setDiscount(boolean discount) {
        isDiscount = discount;
    }

    public String getDiscountData() {
        return discountData;
    }

    public void setDiscountData(String discountData) {
        this.discountData = discountData;
    }

    public CouponEnv getCouponEnv() {
        return couponEnv;
    }

    public void setCouponEnv(CouponEnv couponEnv) {
        this.couponEnv = couponEnv;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public CouponReceive getCouponReceive() {
        return couponReceive;
    }

    public void setCouponReceive(CouponReceive couponReceive) {
        this.couponReceive = couponReceive;
    }

    @Override
    public int compareTo(DiscountData o) {
        return this.payPrice.compareTo(o.getPayPrice());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DiscountData{");
        sb.append("payPrice=").append(payPrice);
        sb.append(", isDiscount=").append(isDiscount);
        sb.append(", discountData='").append(discountData).append('\'');
        sb.append(", couponEnv=").append(couponEnv);
        sb.append(", coupon=").append(coupon);
        sb.append(", couponReceive=").append(couponReceive);
        sb.append('}');
        return sb.toString();
    }
}
