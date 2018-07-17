package custom.bean;

import com.aobei.train.model.Coupon;
import com.aobei.train.model.ProSku;
import com.aobei.train.model.Product;

import java.io.Serializable;
import java.util.List;

public class ProductInfo implements Serializable {
    private static final long serialVersionUID = -7062272531113439419L;
    Product product;
    List<ProSku> proSkus;
    List<ProductTag> productTags;
    List<CouponResponse> couponResponse;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<CouponResponse> getCouponResponse() {
        return couponResponse;
    }

    public void setCouponResponse(List<CouponResponse> couponResponse) {
        this.couponResponse = couponResponse;
    }

    public List<ProductTag> getProductTags() {
        return productTags;
    }

    public void setProductTags(List<ProductTag> productTags) {
        this.productTags = productTags;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ProSku> getProSkus() {
        return proSkus;
    }

    public void setProSkus(List<ProSku> proSkus) {
        this.proSkus = proSkus;
    }
}
