package com.aobei.train.model;

import com.aobei.train.model.OrderItemExample.Criteria;
import com.github.liyiorg.mbg.support.example.CInterface;
import com.github.liyiorg.mbg.support.example.CItem;
import com.github.liyiorg.mbg.support.example.ColumnListAble;
import com.github.liyiorg.mbg.support.example.MbgExample;
import com.github.liyiorg.mbg.support.example.MbgGeneratedCriteria;
import com.github.liyiorg.mbg.support.example.PaginationAble;
import com.github.liyiorg.mbg.util.CUtil;
import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class OrderItemExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pay_order_item
     *
     * @mbg.generated Fri Feb 23 09:47:32 CST 2018
     */
    public OrderItemExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pay_order_item
     *
     * @mbg.generated Fri Feb 23 09:47:32 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pay_order_item
     *
     * @mbg.generated Fri Feb 23 09:47:32 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pay_order_item
     *
     * @mbg.generated Fri Feb 23 09:47:32 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table pay_order_item
     *
     * @mbg.generated Fri Feb 23 09:47:32 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

        public Criteria andPay_order_item_idIsNull() {
            addCriterion("pay_order_item_id is null");
            return (Criteria) this;
        }

        public Criteria andPay_order_item_idIsNotNull() {
            addCriterion("pay_order_item_id is not null");
            return (Criteria) this;
        }

        public Criteria andPay_order_item_idEqualTo(Long value) {
            addCriterion("pay_order_item_id =", value, "pay_order_item_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_item_idNotEqualTo(Long value) {
            addCriterion("pay_order_item_id <>", value, "pay_order_item_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_item_idGreaterThan(Long value) {
            addCriterion("pay_order_item_id >", value, "pay_order_item_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_item_idGreaterThanOrEqualTo(Long value) {
            addCriterion("pay_order_item_id >=", value, "pay_order_item_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_item_idLessThan(Long value) {
            addCriterion("pay_order_item_id <", value, "pay_order_item_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_item_idLessThanOrEqualTo(Long value) {
            addCriterion("pay_order_item_id <=", value, "pay_order_item_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_item_idIn(List<Long> values) {
            addCriterion("pay_order_item_id in", values, "pay_order_item_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_item_idNotIn(List<Long> values) {
            addCriterion("pay_order_item_id not in", values, "pay_order_item_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_item_idBetween(Long value1, Long value2) {
            addCriterion("pay_order_item_id between", value1, value2, "pay_order_item_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_item_idNotBetween(Long value1, Long value2) {
            addCriterion("pay_order_item_id not between", value1, value2, "pay_order_item_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idIsNull() {
            addCriterion("pay_order_id is null");
            return (Criteria) this;
        }

        public Criteria andPay_order_idIsNotNull() {
            addCriterion("pay_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andPay_order_idEqualTo(String value) {
            addCriterion("pay_order_id =", value, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idNotEqualTo(String value) {
            addCriterion("pay_order_id <>", value, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idGreaterThan(String value) {
            addCriterion("pay_order_id >", value, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idGreaterThanOrEqualTo(String value) {
            addCriterion("pay_order_id >=", value, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idLessThan(String value) {
            addCriterion("pay_order_id <", value, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idLessThanOrEqualTo(String value) {
            addCriterion("pay_order_id <=", value, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idLike(String value) {
            addCriterion("pay_order_id like", value, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idNotLike(String value) {
            addCriterion("pay_order_id not like", value, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idIn(List<String> values) {
            addCriterion("pay_order_id in", values, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idNotIn(List<String> values) {
            addCriterion("pay_order_id not in", values, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idBetween(String value1, String value2) {
            addCriterion("pay_order_id between", value1, value2, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andPay_order_idNotBetween(String value1, String value2) {
            addCriterion("pay_order_id not between", value1, value2, "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andProduct_idIsNull() {
            addCriterion("product_id is null");
            return (Criteria) this;
        }

        public Criteria andProduct_idIsNotNull() {
            addCriterion("product_id is not null");
            return (Criteria) this;
        }

        public Criteria andProduct_idEqualTo(Long value) {
            addCriterion("product_id =", value, "product_id");
            return (Criteria) this;
        }

        public Criteria andProduct_idNotEqualTo(Long value) {
            addCriterion("product_id <>", value, "product_id");
            return (Criteria) this;
        }

        public Criteria andProduct_idGreaterThan(Long value) {
            addCriterion("product_id >", value, "product_id");
            return (Criteria) this;
        }

        public Criteria andProduct_idGreaterThanOrEqualTo(Long value) {
            addCriterion("product_id >=", value, "product_id");
            return (Criteria) this;
        }

        public Criteria andProduct_idLessThan(Long value) {
            addCriterion("product_id <", value, "product_id");
            return (Criteria) this;
        }

        public Criteria andProduct_idLessThanOrEqualTo(Long value) {
            addCriterion("product_id <=", value, "product_id");
            return (Criteria) this;
        }

        public Criteria andProduct_idIn(List<Long> values) {
            addCriterion("product_id in", values, "product_id");
            return (Criteria) this;
        }

        public Criteria andProduct_idNotIn(List<Long> values) {
            addCriterion("product_id not in", values, "product_id");
            return (Criteria) this;
        }

        public Criteria andProduct_idBetween(Long value1, Long value2) {
            addCriterion("product_id between", value1, value2, "product_id");
            return (Criteria) this;
        }

        public Criteria andProduct_idNotBetween(Long value1, Long value2) {
            addCriterion("product_id not between", value1, value2, "product_id");
            return (Criteria) this;
        }

        public Criteria andPsku_idIsNull() {
            addCriterion("psku_id is null");
            return (Criteria) this;
        }

        public Criteria andPsku_idIsNotNull() {
            addCriterion("psku_id is not null");
            return (Criteria) this;
        }

        public Criteria andPsku_idEqualTo(Long value) {
            addCriterion("psku_id =", value, "psku_id");
            return (Criteria) this;
        }

        public Criteria andPsku_idNotEqualTo(Long value) {
            addCriterion("psku_id <>", value, "psku_id");
            return (Criteria) this;
        }

        public Criteria andPsku_idGreaterThan(Long value) {
            addCriterion("psku_id >", value, "psku_id");
            return (Criteria) this;
        }

        public Criteria andPsku_idGreaterThanOrEqualTo(Long value) {
            addCriterion("psku_id >=", value, "psku_id");
            return (Criteria) this;
        }

        public Criteria andPsku_idLessThan(Long value) {
            addCriterion("psku_id <", value, "psku_id");
            return (Criteria) this;
        }

        public Criteria andPsku_idLessThanOrEqualTo(Long value) {
            addCriterion("psku_id <=", value, "psku_id");
            return (Criteria) this;
        }

        public Criteria andPsku_idIn(List<Long> values) {
            addCriterion("psku_id in", values, "psku_id");
            return (Criteria) this;
        }

        public Criteria andPsku_idNotIn(List<Long> values) {
            addCriterion("psku_id not in", values, "psku_id");
            return (Criteria) this;
        }

        public Criteria andPsku_idBetween(Long value1, Long value2) {
            addCriterion("psku_id between", value1, value2, "psku_id");
            return (Criteria) this;
        }

        public Criteria andPsku_idNotBetween(Long value1, Long value2) {
            addCriterion("psku_id not between", value1, value2, "psku_id");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(Integer value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(Integer value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(Integer value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(Integer value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(Integer value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(Integer value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<Integer> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<Integer> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(Integer value1, Integer value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(Integer value1, Integer value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andNumIsNull() {
            addCriterion("num is null");
            return (Criteria) this;
        }

        public Criteria andNumIsNotNull() {
            addCriterion("num is not null");
            return (Criteria) this;
        }

        public Criteria andNumEqualTo(Integer value) {
            addCriterion("num =", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotEqualTo(Integer value) {
            addCriterion("num <>", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumGreaterThan(Integer value) {
            addCriterion("num >", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("num >=", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumLessThan(Integer value) {
            addCriterion("num <", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumLessThanOrEqualTo(Integer value) {
            addCriterion("num <=", value, "num");
            return (Criteria) this;
        }

        public Criteria andNumIn(List<Integer> values) {
            addCriterion("num in", values, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotIn(List<Integer> values) {
            addCriterion("num not in", values, "num");
            return (Criteria) this;
        }

        public Criteria andNumBetween(Integer value1, Integer value2) {
            addCriterion("num between", value1, value2, "num");
            return (Criteria) this;
        }

        public Criteria andNumNotBetween(Integer value1, Integer value2) {
            addCriterion("num not between", value1, value2, "num");
            return (Criteria) this;
        }

        public Criteria andPay_order_idLikeInsensitive(String value) {
            addCriterion("upper(pay_order_id) like", value.toUpperCase(), "pay_order_id");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table pay_order_item
     *
     * @mbg.generated do_not_delete_during_merge Fri Feb 23 09:47:32 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table pay_order_item
     *
     * @mbg.generated Fri Feb 23 09:47:32 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	订单包含的商品sku
		//--------------------------------------------------					
									pay_order_item_id(1,Types.BIGINT,false),
		/** 订单号 */				pay_order_id(2,Types.VARCHAR,false),
		/** 商品id */				product_id(2,Types.BIGINT,false),
		/** 商品sku_id */			psku_id(2,Types.BIGINT,false),
		/** 价格 */					price(2,Types.INTEGER,false),
		/** 购买数量 */				num(2,Types.INTEGER,false);

        private final CItem C_I_T_E_M;

        private C(int type, int jdbcType, boolean delimited) {
            C_I_T_E_M = new CItem(type, jdbcType, delimited, name(), null , "`", "`");
        }

        public int getType() {
            return C_I_T_E_M.getType();
        }

        public int getJdbcType() {
            return C_I_T_E_M.getJdbcType();
        }

        public boolean isDelimited() {
            return C_I_T_E_M.isDelimited();
        }

        public String delimitedName() {
            return C_I_T_E_M.delimitedName();
        }

        public String delimitedAliasName() {
            return C_I_T_E_M.delimitedAliasName();
        }
    }
}