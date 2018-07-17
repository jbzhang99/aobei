package com.aobei.train.model;

import com.aobei.train.model.ProductSoleExample.Criteria;
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
public class ProductSoleExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_sole
     *
     * @mbg.generated Fri Apr 13 14:37:01 CST 2018
     */
    public ProductSoleExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_sole
     *
     * @mbg.generated Fri Apr 13 14:37:01 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_sole
     *
     * @mbg.generated Fri Apr 13 14:37:01 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table product_sole
     *
     * @mbg.generated Fri Apr 13 14:37:01 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table product_sole
     *
     * @mbg.generated Fri Apr 13 14:37:01 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

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

        public Criteria andPartner_idIsNull() {
            addCriterion("partner_id is null");
            return (Criteria) this;
        }

        public Criteria andPartner_idIsNotNull() {
            addCriterion("partner_id is not null");
            return (Criteria) this;
        }

        public Criteria andPartner_idEqualTo(Long value) {
            addCriterion("partner_id =", value, "partner_id");
            return (Criteria) this;
        }

        public Criteria andPartner_idNotEqualTo(Long value) {
            addCriterion("partner_id <>", value, "partner_id");
            return (Criteria) this;
        }

        public Criteria andPartner_idGreaterThan(Long value) {
            addCriterion("partner_id >", value, "partner_id");
            return (Criteria) this;
        }

        public Criteria andPartner_idGreaterThanOrEqualTo(Long value) {
            addCriterion("partner_id >=", value, "partner_id");
            return (Criteria) this;
        }

        public Criteria andPartner_idLessThan(Long value) {
            addCriterion("partner_id <", value, "partner_id");
            return (Criteria) this;
        }

        public Criteria andPartner_idLessThanOrEqualTo(Long value) {
            addCriterion("partner_id <=", value, "partner_id");
            return (Criteria) this;
        }

        public Criteria andPartner_idIn(List<Long> values) {
            addCriterion("partner_id in", values, "partner_id");
            return (Criteria) this;
        }

        public Criteria andPartner_idNotIn(List<Long> values) {
            addCriterion("partner_id not in", values, "partner_id");
            return (Criteria) this;
        }

        public Criteria andPartner_idBetween(Long value1, Long value2) {
            addCriterion("partner_id between", value1, value2, "partner_id");
            return (Criteria) this;
        }

        public Criteria andPartner_idNotBetween(Long value1, Long value2) {
            addCriterion("partner_id not between", value1, value2, "partner_id");
            return (Criteria) this;
        }

        public Criteria andNoneIsNull() {
            addCriterion("none is null");
            return (Criteria) this;
        }

        public Criteria andNoneIsNotNull() {
            addCriterion("none is not null");
            return (Criteria) this;
        }

        public Criteria andNoneEqualTo(Integer value) {
            addCriterion("none =", value, "none");
            return (Criteria) this;
        }

        public Criteria andNoneNotEqualTo(Integer value) {
            addCriterion("none <>", value, "none");
            return (Criteria) this;
        }

        public Criteria andNoneGreaterThan(Integer value) {
            addCriterion("none >", value, "none");
            return (Criteria) this;
        }

        public Criteria andNoneGreaterThanOrEqualTo(Integer value) {
            addCriterion("none >=", value, "none");
            return (Criteria) this;
        }

        public Criteria andNoneLessThan(Integer value) {
            addCriterion("none <", value, "none");
            return (Criteria) this;
        }

        public Criteria andNoneLessThanOrEqualTo(Integer value) {
            addCriterion("none <=", value, "none");
            return (Criteria) this;
        }

        public Criteria andNoneIn(List<Integer> values) {
            addCriterion("none in", values, "none");
            return (Criteria) this;
        }

        public Criteria andNoneNotIn(List<Integer> values) {
            addCriterion("none not in", values, "none");
            return (Criteria) this;
        }

        public Criteria andNoneBetween(Integer value1, Integer value2) {
            addCriterion("none between", value1, value2, "none");
            return (Criteria) this;
        }

        public Criteria andNoneNotBetween(Integer value1, Integer value2) {
            addCriterion("none not between", value1, value2, "none");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table product_sole
     *
     * @mbg.generated do_not_delete_during_merge Fri Apr 13 14:37:01 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table product_sole
     *
     * @mbg.generated Fri Apr 13 14:37:01 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	商品独家销售
		//--------------------------------------------------
		/** 商品id */			product_id(1,Types.BIGINT,false),
		/** 合伙人id */			partner_id(1,Types.BIGINT,false),
		/** 无用字段 */			none(2,Types.INTEGER,false);

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