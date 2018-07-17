package custom.bean;

import java.io.Serializable;

import com.aobei.train.model.Coupon;
/**
 * 优惠券列表展示页   加已领用 已下单两个状态
 * @author adminL
 *
 */
public class CouponList extends Coupon implements Serializable{

	
	private static final long serialVersionUID = 1L;
	public CouponList() {
		super();
	}
	private int coupon_receive;
	private int coupon_order;
	private String creater_name;
	private String verifier_name;

	public String getCreater_name() {
		return creater_name;
	}

	public void setCreater_name(String creater_name) {
		this.creater_name = creater_name;
	}

	public String getVerifier_name() {
		return verifier_name;
	}

	public void setVerifier_name(String verifier_name) {
		this.verifier_name = verifier_name;
	}

	public int getCoupon_receive() {
		return coupon_receive;
	}
	public void setCoupon_receive(int coupon_receive) {
		this.coupon_receive = coupon_receive;
	}
	public int getCoupon_order() {
		return coupon_order;
	}
	public void setCoupon_order(int coupon_order) {
		this.coupon_order = coupon_order;
	}
	public CouponList(int coupon_receive, int coupon_order) {
		super();
		this.coupon_receive = coupon_receive;
		this.coupon_order = coupon_order;
	}
	
}
