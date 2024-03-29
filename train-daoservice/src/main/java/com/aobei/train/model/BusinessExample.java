package com.aobei.train.model;

import com.aobei.train.model.BusinessExample.Criteria;
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
import java.util.Date;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class BusinessExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public BusinessExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table business
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table business
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

        public Criteria andBusiness_idIsNull() {
            addCriterion("business_id is null");
            return (Criteria) this;
        }

        public Criteria andBusiness_idIsNotNull() {
            addCriterion("business_id is not null");
            return (Criteria) this;
        }

        public Criteria andBusiness_idEqualTo(String value) {
            addCriterion("business_id =", value, "business_id");
            return (Criteria) this;
        }

        public Criteria andBusiness_idNotEqualTo(String value) {
            addCriterion("business_id <>", value, "business_id");
            return (Criteria) this;
        }

        public Criteria andBusiness_idGreaterThan(String value) {
            addCriterion("business_id >", value, "business_id");
            return (Criteria) this;
        }

        public Criteria andBusiness_idGreaterThanOrEqualTo(String value) {
            addCriterion("business_id >=", value, "business_id");
            return (Criteria) this;
        }

        public Criteria andBusiness_idLessThan(String value) {
            addCriterion("business_id <", value, "business_id");
            return (Criteria) this;
        }

        public Criteria andBusiness_idLessThanOrEqualTo(String value) {
            addCriterion("business_id <=", value, "business_id");
            return (Criteria) this;
        }

        public Criteria andBusiness_idLike(String value) {
            addCriterion("business_id like", value, "business_id");
            return (Criteria) this;
        }

        public Criteria andBusiness_idNotLike(String value) {
            addCriterion("business_id not like", value, "business_id");
            return (Criteria) this;
        }

        public Criteria andBusiness_idIn(List<String> values) {
            addCriterion("business_id in", values, "business_id");
            return (Criteria) this;
        }

        public Criteria andBusiness_idNotIn(List<String> values) {
            addCriterion("business_id not in", values, "business_id");
            return (Criteria) this;
        }

        public Criteria andBusiness_idBetween(String value1, String value2) {
            addCriterion("business_id between", value1, value2, "business_id");
            return (Criteria) this;
        }

        public Criteria andBusiness_idNotBetween(String value1, String value2) {
            addCriterion("business_id not between", value1, value2, "business_id");
            return (Criteria) this;
        }

        public Criteria andUserIsNull() {
            addCriterion("user is null");
            return (Criteria) this;
        }

        public Criteria andUserIsNotNull() {
            addCriterion("user is not null");
            return (Criteria) this;
        }

        public Criteria andUserEqualTo(String value) {
            addCriterion("user =", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserNotEqualTo(String value) {
            addCriterion("user <>", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserGreaterThan(String value) {
            addCriterion("user >", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserGreaterThanOrEqualTo(String value) {
            addCriterion("user >=", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserLessThan(String value) {
            addCriterion("user <", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserLessThanOrEqualTo(String value) {
            addCriterion("user <=", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserLike(String value) {
            addCriterion("user like", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserNotLike(String value) {
            addCriterion("user not like", value, "user");
            return (Criteria) this;
        }

        public Criteria andUserIn(List<String> values) {
            addCriterion("user in", values, "user");
            return (Criteria) this;
        }

        public Criteria andUserNotIn(List<String> values) {
            addCriterion("user not in", values, "user");
            return (Criteria) this;
        }

        public Criteria andUserBetween(String value1, String value2) {
            addCriterion("user between", value1, value2, "user");
            return (Criteria) this;
        }

        public Criteria andUserNotBetween(String value1, String value2) {
            addCriterion("user not between", value1, value2, "user");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("createTime is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("createTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("createTime =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("createTime <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("createTime >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("createTime >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("createTime <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("createTime <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("createTime in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("createTime not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("createTime between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("createTime not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andActionIsNull() {
            addCriterion("action is null");
            return (Criteria) this;
        }

        public Criteria andActionIsNotNull() {
            addCriterion("action is not null");
            return (Criteria) this;
        }

        public Criteria andActionEqualTo(String value) {
            addCriterion("action =", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionNotEqualTo(String value) {
            addCriterion("action <>", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionGreaterThan(String value) {
            addCriterion("action >", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionGreaterThanOrEqualTo(String value) {
            addCriterion("action >=", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionLessThan(String value) {
            addCriterion("action <", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionLessThanOrEqualTo(String value) {
            addCriterion("action <=", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionLike(String value) {
            addCriterion("action like", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionNotLike(String value) {
            addCriterion("action not like", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionIn(List<String> values) {
            addCriterion("action in", values, "action");
            return (Criteria) this;
        }

        public Criteria andActionNotIn(List<String> values) {
            addCriterion("action not in", values, "action");
            return (Criteria) this;
        }

        public Criteria andActionBetween(String value1, String value2) {
            addCriterion("action between", value1, value2, "action");
            return (Criteria) this;
        }

        public Criteria andActionNotBetween(String value1, String value2) {
            addCriterion("action not between", value1, value2, "action");
            return (Criteria) this;
        }

        public Criteria andStepNameIsNull() {
            addCriterion("stepName is null");
            return (Criteria) this;
        }

        public Criteria andStepNameIsNotNull() {
            addCriterion("stepName is not null");
            return (Criteria) this;
        }

        public Criteria andStepNameEqualTo(String value) {
            addCriterion("stepName =", value, "stepName");
            return (Criteria) this;
        }

        public Criteria andStepNameNotEqualTo(String value) {
            addCriterion("stepName <>", value, "stepName");
            return (Criteria) this;
        }

        public Criteria andStepNameGreaterThan(String value) {
            addCriterion("stepName >", value, "stepName");
            return (Criteria) this;
        }

        public Criteria andStepNameGreaterThanOrEqualTo(String value) {
            addCriterion("stepName >=", value, "stepName");
            return (Criteria) this;
        }

        public Criteria andStepNameLessThan(String value) {
            addCriterion("stepName <", value, "stepName");
            return (Criteria) this;
        }

        public Criteria andStepNameLessThanOrEqualTo(String value) {
            addCriterion("stepName <=", value, "stepName");
            return (Criteria) this;
        }

        public Criteria andStepNameLike(String value) {
            addCriterion("stepName like", value, "stepName");
            return (Criteria) this;
        }

        public Criteria andStepNameNotLike(String value) {
            addCriterion("stepName not like", value, "stepName");
            return (Criteria) this;
        }

        public Criteria andStepNameIn(List<String> values) {
            addCriterion("stepName in", values, "stepName");
            return (Criteria) this;
        }

        public Criteria andStepNameNotIn(List<String> values) {
            addCriterion("stepName not in", values, "stepName");
            return (Criteria) this;
        }

        public Criteria andStepNameBetween(String value1, String value2) {
            addCriterion("stepName between", value1, value2, "stepName");
            return (Criteria) this;
        }

        public Criteria andStepNameNotBetween(String value1, String value2) {
            addCriterion("stepName not between", value1, value2, "stepName");
            return (Criteria) this;
        }

        public Criteria andFlowInfoIsNull() {
            addCriterion("flowInfo is null");
            return (Criteria) this;
        }

        public Criteria andFlowInfoIsNotNull() {
            addCriterion("flowInfo is not null");
            return (Criteria) this;
        }

        public Criteria andFlowInfoEqualTo(String value) {
            addCriterion("flowInfo =", value, "flowInfo");
            return (Criteria) this;
        }

        public Criteria andFlowInfoNotEqualTo(String value) {
            addCriterion("flowInfo <>", value, "flowInfo");
            return (Criteria) this;
        }

        public Criteria andFlowInfoGreaterThan(String value) {
            addCriterion("flowInfo >", value, "flowInfo");
            return (Criteria) this;
        }

        public Criteria andFlowInfoGreaterThanOrEqualTo(String value) {
            addCriterion("flowInfo >=", value, "flowInfo");
            return (Criteria) this;
        }

        public Criteria andFlowInfoLessThan(String value) {
            addCriterion("flowInfo <", value, "flowInfo");
            return (Criteria) this;
        }

        public Criteria andFlowInfoLessThanOrEqualTo(String value) {
            addCriterion("flowInfo <=", value, "flowInfo");
            return (Criteria) this;
        }

        public Criteria andFlowInfoLike(String value) {
            addCriterion("flowInfo like", value, "flowInfo");
            return (Criteria) this;
        }

        public Criteria andFlowInfoNotLike(String value) {
            addCriterion("flowInfo not like", value, "flowInfo");
            return (Criteria) this;
        }

        public Criteria andFlowInfoIn(List<String> values) {
            addCriterion("flowInfo in", values, "flowInfo");
            return (Criteria) this;
        }

        public Criteria andFlowInfoNotIn(List<String> values) {
            addCriterion("flowInfo not in", values, "flowInfo");
            return (Criteria) this;
        }

        public Criteria andFlowInfoBetween(String value1, String value2) {
            addCriterion("flowInfo between", value1, value2, "flowInfo");
            return (Criteria) this;
        }

        public Criteria andFlowInfoNotBetween(String value1, String value2) {
            addCriterion("flowInfo not between", value1, value2, "flowInfo");
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

        public Criteria andCus_phoneIsNull() {
            addCriterion("cus_phone is null");
            return (Criteria) this;
        }

        public Criteria andCus_phoneIsNotNull() {
            addCriterion("cus_phone is not null");
            return (Criteria) this;
        }

        public Criteria andCus_phoneEqualTo(String value) {
            addCriterion("cus_phone =", value, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_phoneNotEqualTo(String value) {
            addCriterion("cus_phone <>", value, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_phoneGreaterThan(String value) {
            addCriterion("cus_phone >", value, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_phoneGreaterThanOrEqualTo(String value) {
            addCriterion("cus_phone >=", value, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_phoneLessThan(String value) {
            addCriterion("cus_phone <", value, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_phoneLessThanOrEqualTo(String value) {
            addCriterion("cus_phone <=", value, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_phoneLike(String value) {
            addCriterion("cus_phone like", value, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_phoneNotLike(String value) {
            addCriterion("cus_phone not like", value, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_phoneIn(List<String> values) {
            addCriterion("cus_phone in", values, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_phoneNotIn(List<String> values) {
            addCriterion("cus_phone not in", values, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_phoneBetween(String value1, String value2) {
            addCriterion("cus_phone between", value1, value2, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_phoneNotBetween(String value1, String value2) {
            addCriterion("cus_phone not between", value1, value2, "cus_phone");
            return (Criteria) this;
        }

        public Criteria andBusiness_idLikeInsensitive(String value) {
            addCriterion("upper(business_id) like", value.toUpperCase(), "business_id");
            return (Criteria) this;
        }

        public Criteria andUserLikeInsensitive(String value) {
            addCriterion("upper(user) like", value.toUpperCase(), "user");
            return (Criteria) this;
        }

        public Criteria andActionLikeInsensitive(String value) {
            addCriterion("upper(action) like", value.toUpperCase(), "action");
            return (Criteria) this;
        }

        public Criteria andStepNameLikeInsensitive(String value) {
            addCriterion("upper(stepName) like", value.toUpperCase(), "stepName");
            return (Criteria) this;
        }

        public Criteria andFlowInfoLikeInsensitive(String value) {
            addCriterion("upper(flowInfo) like", value.toUpperCase(), "flowInfo");
            return (Criteria) this;
        }

        public Criteria andPay_order_idLikeInsensitive(String value) {
            addCriterion("upper(pay_order_id) like", value.toUpperCase(), "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andCus_phoneLikeInsensitive(String value) {
            addCriterion("upper(cus_phone) like", value.toUpperCase(), "cus_phone");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table business
     *
     * @mbg.generated do_not_delete_during_merge Wed Aug 08 10:21:57 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table business
     *
     * @mbg.generated Wed Aug 08 10:21:57 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	工单信息记录表（七陌）
		//--------------------------------------------------
		/** 工单编号 */						business_id(1,Types.VARCHAR,false),
		/** 坐席号 */						user(2,Types.VARCHAR,false),
		/** 创建时间 */						createTime(2,Types.TIMESTAMP,false),
		/** 执行的动作名称 */				action(2,Types.VARCHAR,false),
		/** 当前步骤的名称 */				stepName(2,Types.VARCHAR,false),
		/** 当前步骤的的步骤信息 */			flowInfo(2,Types.VARCHAR,false),
		/** 订单号 */						pay_order_id(2,Types.VARCHAR,false),
		/** 顾客电话 */						cus_phone(2,Types.VARCHAR,false);

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