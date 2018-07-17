package custom.bean;

import java.io.Serializable;

public class OrderPrice implements Serializable{
    private static final long serialVersionUID = 1225170706721057262L;

    int payPrice;
    int totalPrice;
    int discountPrice;

    public int getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(int payPrice) {
        this.payPrice = payPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }
}
