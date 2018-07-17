package com.aobei.train.model;

import com.aobei.train.model.CompleteApplyExample.Criteria;
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
public class CompleteApplyExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table complete_apply
     *
     * @mbg.generated Wed Jun 13 14:14:16 CST 2018
     */
    public CompleteApplyExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table complete_apply
     *
     * @mbg.generated Wed Jun 13 14:14:16 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table complete_apply
     *
     * @mbg.generated Wed Jun 13 14:14:16 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table complete_apply
     *
     * @mbg.generated Wed Jun 13 14:14:16 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table complete_apply
     *
     * @mbg.generated Wed Jun 13 14:14:16 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

        public Criteria andComplete_apply_idIsNull() {
            addCriterion("complete_apply_id is null");
            return (Criteria) this;
        }

        public Criteria andComplete_apply_idIsNotNull() {
            addCriterion("complete_apply_id is not null");
            return (Criteria) this;
        }

        public Criteria andComplete_apply_idEqualTo(Long value) {
            addCriterion("complete_apply_id =", value, "complete_apply_id");
            return (Criteria) this;
        }

        public Criteria andComplete_apply_idNotEqualTo(Long value) {
            addCriterion("complete_apply_id <>", value, "complete_apply_id");
            return (Criteria) this;
        }

        public Criteria andComplete_apply_idGreaterThan(Long value) {
            addCriterion("complete_apply_id >", value, "complete_apply_id");
            return (Criteria) this;
        }

        public Criteria andComplete_apply_idGreaterThanOrEqualTo(Long value) {
            addCriterion("complete_apply_id >=", value, "complete_apply_id");
            return (Criteria) this;
        }

        public Criteria andComplete_apply_idLessThan(Long value) {
            addCriterion("complete_apply_id <", value, "complete_apply_id");
            return (Criteria) this;
        }

        public Criteria andComplete_apply_idLessThanOrEqualTo(Long value) {
            addCriterion("complete_apply_id <=", value, "complete_apply_id");
            return (Criteria) this;
        }

        public Criteria andComplete_apply_idIn(List<Long> values) {
            addCriterion("complete_apply_id in", values, "complete_apply_id");
            return (Criteria) this;
        }

        public Criteria andComplete_apply_idNotIn(List<Long> values) {
            addCriterion("complete_apply_id not in", values, "complete_apply_id");
            return (Criteria) this;
        }

        public Criteria andComplete_apply_idBetween(Long value1, Long value2) {
            addCriterion("complete_apply_id between", value1, value2, "complete_apply_id");
            return (Criteria) this;
        }

        public Criteria andComplete_apply_idNotBetween(Long value1, Long value2) {
            addCriterion("complete_apply_id not between", value1, value2, "complete_apply_id");
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

        public Criteria andServiceunit_idIsNull() {
            addCriterion("serviceunit_id is null");
            return (Criteria) this;
        }

        public Criteria andServiceunit_idIsNotNull() {
            addCriterion("serviceunit_id is not null");
            return (Criteria) this;
        }

        public Criteria andServiceunit_idEqualTo(Long value) {
            addCriterion("serviceunit_id =", value, "serviceunit_id");
            return (Criteria) this;
        }

        public Criteria andServiceunit_idNotEqualTo(Long value) {
            addCriterion("serviceunit_id <>", value, "serviceunit_id");
            return (Criteria) this;
        }

        public Criteria andServiceunit_idGreaterThan(Long value) {
            addCriterion("serviceunit_id >", value, "serviceunit_id");
            return (Criteria) this;
        }

        public Criteria andServiceunit_idGreaterThanOrEqualTo(Long value) {
            addCriterion("serviceunit_id >=", value, "serviceunit_id");
            return (Criteria) this;
        }

        public Criteria andServiceunit_idLessThan(Long value) {
            addCriterion("serviceunit_id <", value, "serviceunit_id");
            return (Criteria) this;
        }

        public Criteria andServiceunit_idLessThanOrEqualTo(Long value) {
            addCriterion("serviceunit_id <=", value, "serviceunit_id");
            return (Criteria) this;
        }

        public Criteria andServiceunit_idIn(List<Long> values) {
            addCriterion("serviceunit_id in", values, "serviceunit_id");
            return (Criteria) this;
        }

        public Criteria andServiceunit_idNotIn(List<Long> values) {
            addCriterion("serviceunit_id not in", values, "serviceunit_id");
            return (Criteria) this;
        }

        public Criteria andServiceunit_idBetween(Long value1, Long value2) {
            addCriterion("serviceunit_id between", value1, value2, "serviceunit_id");
            return (Criteria) this;
        }

        public Criteria andServiceunit_idNotBetween(Long value1, Long value2) {
            addCriterion("serviceunit_id not between", value1, value2, "serviceunit_id");
            return (Criteria) this;
        }

        public Criteria andComplete_infoIsNull() {
            addCriterion("complete_info is null");
            return (Criteria) this;
        }

        public Criteria andComplete_infoIsNotNull() {
            addCriterion("complete_info is not null");
            return (Criteria) this;
        }

        public Criteria andComplete_infoEqualTo(String value) {
            addCriterion("complete_info =", value, "complete_info");
            return (Criteria) this;
        }

        public Criteria andComplete_infoNotEqualTo(String value) {
            addCriterion("complete_info <>", value, "complete_info");
            return (Criteria) this;
        }

        public Criteria andComplete_infoGreaterThan(String value) {
            addCriterion("complete_info >", value, "complete_info");
            return (Criteria) this;
        }

        public Criteria andComplete_infoGreaterThanOrEqualTo(String value) {
            addCriterion("complete_info >=", value, "complete_info");
            return (Criteria) this;
        }

        public Criteria andComplete_infoLessThan(String value) {
            addCriterion("complete_info <", value, "complete_info");
            return (Criteria) this;
        }

        public Criteria andComplete_infoLessThanOrEqualTo(String value) {
            addCriterion("complete_info <=", value, "complete_info");
            return (Criteria) this;
        }

        public Criteria andComplete_infoLike(String value) {
            addCriterion("complete_info like", value, "complete_info");
            return (Criteria) this;
        }

        public Criteria andComplete_infoNotLike(String value) {
            addCriterion("complete_info not like", value, "complete_info");
            return (Criteria) this;
        }

        public Criteria andComplete_infoIn(List<String> values) {
            addCriterion("complete_info in", values, "complete_info");
            return (Criteria) this;
        }

        public Criteria andComplete_infoNotIn(List<String> values) {
            addCriterion("complete_info not in", values, "complete_info");
            return (Criteria) this;
        }

        public Criteria andComplete_infoBetween(String value1, String value2) {
            addCriterion("complete_info between", value1, value2, "complete_info");
            return (Criteria) this;
        }

        public Criteria andComplete_infoNotBetween(String value1, String value2) {
            addCriterion("complete_info not between", value1, value2, "complete_info");
            return (Criteria) this;
        }

        public Criteria andOperator_applyIsNull() {
            addCriterion("operator_apply is null");
            return (Criteria) this;
        }

        public Criteria andOperator_applyIsNotNull() {
            addCriterion("operator_apply is not null");
            return (Criteria) this;
        }

        public Criteria andOperator_applyEqualTo(Long value) {
            addCriterion("operator_apply =", value, "operator_apply");
            return (Criteria) this;
        }

        public Criteria andOperator_applyNotEqualTo(Long value) {
            addCriterion("operator_apply <>", value, "operator_apply");
            return (Criteria) this;
        }

        public Criteria andOperator_applyGreaterThan(Long value) {
            addCriterion("operator_apply >", value, "operator_apply");
            return (Criteria) this;
        }

        public Criteria andOperator_applyGreaterThanOrEqualTo(Long value) {
            addCriterion("operator_apply >=", value, "operator_apply");
            return (Criteria) this;
        }

        public Criteria andOperator_applyLessThan(Long value) {
            addCriterion("operator_apply <", value, "operator_apply");
            return (Criteria) this;
        }

        public Criteria andOperator_applyLessThanOrEqualTo(Long value) {
            addCriterion("operator_apply <=", value, "operator_apply");
            return (Criteria) this;
        }

        public Criteria andOperator_applyIn(List<Long> values) {
            addCriterion("operator_apply in", values, "operator_apply");
            return (Criteria) this;
        }

        public Criteria andOperator_applyNotIn(List<Long> values) {
            addCriterion("operator_apply not in", values, "operator_apply");
            return (Criteria) this;
        }

        public Criteria andOperator_applyBetween(Long value1, Long value2) {
            addCriterion("operator_apply between", value1, value2, "operator_apply");
            return (Criteria) this;
        }

        public Criteria andOperator_applyNotBetween(Long value1, Long value2) {
            addCriterion("operator_apply not between", value1, value2, "operator_apply");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeIsNull() {
            addCriterion("create_datetime is null");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeIsNotNull() {
            addCriterion("create_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeEqualTo(Date value) {
            addCriterion("create_datetime =", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeNotEqualTo(Date value) {
            addCriterion("create_datetime <>", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeGreaterThan(Date value) {
            addCriterion("create_datetime >", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_datetime >=", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeLessThan(Date value) {
            addCriterion("create_datetime <", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeLessThanOrEqualTo(Date value) {
            addCriterion("create_datetime <=", value, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeIn(List<Date> values) {
            addCriterion("create_datetime in", values, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeNotIn(List<Date> values) {
            addCriterion("create_datetime not in", values, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeBetween(Date value1, Date value2) {
            addCriterion("create_datetime between", value1, value2, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andCreate_datetimeNotBetween(Date value1, Date value2) {
            addCriterion("create_datetime not between", value1, value2, "create_datetime");
            return (Criteria) this;
        }

        public Criteria andApply_statusIsNull() {
            addCriterion("apply_status is null");
            return (Criteria) this;
        }

        public Criteria andApply_statusIsNotNull() {
            addCriterion("apply_status is not null");
            return (Criteria) this;
        }

        public Criteria andApply_statusEqualTo(Integer value) {
            addCriterion("apply_status =", value, "apply_status");
            return (Criteria) this;
        }

        public Criteria andApply_statusNotEqualTo(Integer value) {
            addCriterion("apply_status <>", value, "apply_status");
            return (Criteria) this;
        }

        public Criteria andApply_statusGreaterThan(Integer value) {
            addCriterion("apply_status >", value, "apply_status");
            return (Criteria) this;
        }

        public Criteria andApply_statusGreaterThanOrEqualTo(Integer value) {
            addCriterion("apply_status >=", value, "apply_status");
            return (Criteria) this;
        }

        public Criteria andApply_statusLessThan(Integer value) {
            addCriterion("apply_status <", value, "apply_status");
            return (Criteria) this;
        }

        public Criteria andApply_statusLessThanOrEqualTo(Integer value) {
            addCriterion("apply_status <=", value, "apply_status");
            return (Criteria) this;
        }

        public Criteria andApply_statusIn(List<Integer> values) {
            addCriterion("apply_status in", values, "apply_status");
            return (Criteria) this;
        }

        public Criteria andApply_statusNotIn(List<Integer> values) {
            addCriterion("apply_status not in", values, "apply_status");
            return (Criteria) this;
        }

        public Criteria andApply_statusBetween(Integer value1, Integer value2) {
            addCriterion("apply_status between", value1, value2, "apply_status");
            return (Criteria) this;
        }

        public Criteria andApply_statusNotBetween(Integer value1, Integer value2) {
            addCriterion("apply_status not between", value1, value2, "apply_status");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeIsNull() {
            addCriterion("confirm_datetime is null");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeIsNotNull() {
            addCriterion("confirm_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeEqualTo(Date value) {
            addCriterion("confirm_datetime =", value, "confirm_datetime");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeNotEqualTo(Date value) {
            addCriterion("confirm_datetime <>", value, "confirm_datetime");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeGreaterThan(Date value) {
            addCriterion("confirm_datetime >", value, "confirm_datetime");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("confirm_datetime >=", value, "confirm_datetime");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeLessThan(Date value) {
            addCriterion("confirm_datetime <", value, "confirm_datetime");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeLessThanOrEqualTo(Date value) {
            addCriterion("confirm_datetime <=", value, "confirm_datetime");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeIn(List<Date> values) {
            addCriterion("confirm_datetime in", values, "confirm_datetime");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeNotIn(List<Date> values) {
            addCriterion("confirm_datetime not in", values, "confirm_datetime");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeBetween(Date value1, Date value2) {
            addCriterion("confirm_datetime between", value1, value2, "confirm_datetime");
            return (Criteria) this;
        }

        public Criteria andConfirm_datetimeNotBetween(Date value1, Date value2) {
            addCriterion("confirm_datetime not between", value1, value2, "confirm_datetime");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorIsNull() {
            addCriterion("confirm_operator is null");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorIsNotNull() {
            addCriterion("confirm_operator is not null");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorEqualTo(Long value) {
            addCriterion("confirm_operator =", value, "confirm_operator");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorNotEqualTo(Long value) {
            addCriterion("confirm_operator <>", value, "confirm_operator");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorGreaterThan(Long value) {
            addCriterion("confirm_operator >", value, "confirm_operator");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorGreaterThanOrEqualTo(Long value) {
            addCriterion("confirm_operator >=", value, "confirm_operator");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorLessThan(Long value) {
            addCriterion("confirm_operator <", value, "confirm_operator");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorLessThanOrEqualTo(Long value) {
            addCriterion("confirm_operator <=", value, "confirm_operator");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorIn(List<Long> values) {
            addCriterion("confirm_operator in", values, "confirm_operator");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorNotIn(List<Long> values) {
            addCriterion("confirm_operator not in", values, "confirm_operator");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorBetween(Long value1, Long value2) {
            addCriterion("confirm_operator between", value1, value2, "confirm_operator");
            return (Criteria) this;
        }

        public Criteria andConfirm_operatorNotBetween(Long value1, Long value2) {
            addCriterion("confirm_operator not between", value1, value2, "confirm_operator");
            return (Criteria) this;
        }

        public Criteria andPay_order_idLikeInsensitive(String value) {
            addCriterion("upper(pay_order_id) like", value.toUpperCase(), "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andComplete_infoLikeInsensitive(String value) {
            addCriterion("upper(complete_info) like", value.toUpperCase(), "complete_info");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table complete_apply
     *
     * @mbg.generated do_not_delete_during_merge Wed Jun 13 14:14:16 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table complete_apply
     *
     * @mbg.generated Wed Jun 13 14:14:16 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	订单服务完成申请表
		//--------------------------------------------------					
														complete_apply_id(1,Types.BIGINT,false),
		/** 订单id */									pay_order_id(2,Types.VARCHAR,false),
		/** 服务单id */									serviceunit_id(2,Types.BIGINT,false),
		/** 申请原因 */									complete_info(2,Types.VARCHAR,false),
		/** 服务完成申请人 */							operator_apply(2,Types.BIGINT,false),
		/** 申请时间 */									create_datetime(2,Types.TIMESTAMP,false),
		/** 申请状态  1 待处理 2 完成 3 驳回 */			apply_status(2,Types.INTEGER,false),
		/** 确认时间 */									confirm_datetime(2,Types.TIMESTAMP,false),
		/** 确认人 */									confirm_operator(2,Types.BIGINT,false);

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