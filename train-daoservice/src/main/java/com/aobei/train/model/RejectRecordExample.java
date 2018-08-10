package com.aobei.train.model;

import com.aobei.train.model.RejectRecordExample.Criteria;
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
public class RejectRecordExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reject_record
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public RejectRecordExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reject_record
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reject_record
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reject_record
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table reject_record
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

        public Criteria andReject_record_idIsNull() {
            addCriterion("reject_record_id is null");
            return (Criteria) this;
        }

        public Criteria andReject_record_idIsNotNull() {
            addCriterion("reject_record_id is not null");
            return (Criteria) this;
        }

        public Criteria andReject_record_idEqualTo(Long value) {
            addCriterion("reject_record_id =", value, "reject_record_id");
            return (Criteria) this;
        }

        public Criteria andReject_record_idNotEqualTo(Long value) {
            addCriterion("reject_record_id <>", value, "reject_record_id");
            return (Criteria) this;
        }

        public Criteria andReject_record_idGreaterThan(Long value) {
            addCriterion("reject_record_id >", value, "reject_record_id");
            return (Criteria) this;
        }

        public Criteria andReject_record_idGreaterThanOrEqualTo(Long value) {
            addCriterion("reject_record_id >=", value, "reject_record_id");
            return (Criteria) this;
        }

        public Criteria andReject_record_idLessThan(Long value) {
            addCriterion("reject_record_id <", value, "reject_record_id");
            return (Criteria) this;
        }

        public Criteria andReject_record_idLessThanOrEqualTo(Long value) {
            addCriterion("reject_record_id <=", value, "reject_record_id");
            return (Criteria) this;
        }

        public Criteria andReject_record_idIn(List<Long> values) {
            addCriterion("reject_record_id in", values, "reject_record_id");
            return (Criteria) this;
        }

        public Criteria andReject_record_idNotIn(List<Long> values) {
            addCriterion("reject_record_id not in", values, "reject_record_id");
            return (Criteria) this;
        }

        public Criteria andReject_record_idBetween(Long value1, Long value2) {
            addCriterion("reject_record_id between", value1, value2, "reject_record_id");
            return (Criteria) this;
        }

        public Criteria andReject_record_idNotBetween(Long value1, Long value2) {
            addCriterion("reject_record_id not between", value1, value2, "reject_record_id");
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

        public Criteria andServer_nameIsNull() {
            addCriterion("server_name is null");
            return (Criteria) this;
        }

        public Criteria andServer_nameIsNotNull() {
            addCriterion("server_name is not null");
            return (Criteria) this;
        }

        public Criteria andServer_nameEqualTo(String value) {
            addCriterion("server_name =", value, "server_name");
            return (Criteria) this;
        }

        public Criteria andServer_nameNotEqualTo(String value) {
            addCriterion("server_name <>", value, "server_name");
            return (Criteria) this;
        }

        public Criteria andServer_nameGreaterThan(String value) {
            addCriterion("server_name >", value, "server_name");
            return (Criteria) this;
        }

        public Criteria andServer_nameGreaterThanOrEqualTo(String value) {
            addCriterion("server_name >=", value, "server_name");
            return (Criteria) this;
        }

        public Criteria andServer_nameLessThan(String value) {
            addCriterion("server_name <", value, "server_name");
            return (Criteria) this;
        }

        public Criteria andServer_nameLessThanOrEqualTo(String value) {
            addCriterion("server_name <=", value, "server_name");
            return (Criteria) this;
        }

        public Criteria andServer_nameLike(String value) {
            addCriterion("server_name like", value, "server_name");
            return (Criteria) this;
        }

        public Criteria andServer_nameNotLike(String value) {
            addCriterion("server_name not like", value, "server_name");
            return (Criteria) this;
        }

        public Criteria andServer_nameIn(List<String> values) {
            addCriterion("server_name in", values, "server_name");
            return (Criteria) this;
        }

        public Criteria andServer_nameNotIn(List<String> values) {
            addCriterion("server_name not in", values, "server_name");
            return (Criteria) this;
        }

        public Criteria andServer_nameBetween(String value1, String value2) {
            addCriterion("server_name between", value1, value2, "server_name");
            return (Criteria) this;
        }

        public Criteria andServer_nameNotBetween(String value1, String value2) {
            addCriterion("server_name not between", value1, value2, "server_name");
            return (Criteria) this;
        }

        public Criteria andCus_usernameIsNull() {
            addCriterion("cus_username is null");
            return (Criteria) this;
        }

        public Criteria andCus_usernameIsNotNull() {
            addCriterion("cus_username is not null");
            return (Criteria) this;
        }

        public Criteria andCus_usernameEqualTo(String value) {
            addCriterion("cus_username =", value, "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_usernameNotEqualTo(String value) {
            addCriterion("cus_username <>", value, "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_usernameGreaterThan(String value) {
            addCriterion("cus_username >", value, "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_usernameGreaterThanOrEqualTo(String value) {
            addCriterion("cus_username >=", value, "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_usernameLessThan(String value) {
            addCriterion("cus_username <", value, "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_usernameLessThanOrEqualTo(String value) {
            addCriterion("cus_username <=", value, "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_usernameLike(String value) {
            addCriterion("cus_username like", value, "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_usernameNotLike(String value) {
            addCriterion("cus_username not like", value, "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_usernameIn(List<String> values) {
            addCriterion("cus_username in", values, "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_usernameNotIn(List<String> values) {
            addCriterion("cus_username not in", values, "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_usernameBetween(String value1, String value2) {
            addCriterion("cus_username between", value1, value2, "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_usernameNotBetween(String value1, String value2) {
            addCriterion("cus_username not between", value1, value2, "cus_username");
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

        public Criteria andCus_addressIsNull() {
            addCriterion("cus_address is null");
            return (Criteria) this;
        }

        public Criteria andCus_addressIsNotNull() {
            addCriterion("cus_address is not null");
            return (Criteria) this;
        }

        public Criteria andCus_addressEqualTo(String value) {
            addCriterion("cus_address =", value, "cus_address");
            return (Criteria) this;
        }

        public Criteria andCus_addressNotEqualTo(String value) {
            addCriterion("cus_address <>", value, "cus_address");
            return (Criteria) this;
        }

        public Criteria andCus_addressGreaterThan(String value) {
            addCriterion("cus_address >", value, "cus_address");
            return (Criteria) this;
        }

        public Criteria andCus_addressGreaterThanOrEqualTo(String value) {
            addCriterion("cus_address >=", value, "cus_address");
            return (Criteria) this;
        }

        public Criteria andCus_addressLessThan(String value) {
            addCriterion("cus_address <", value, "cus_address");
            return (Criteria) this;
        }

        public Criteria andCus_addressLessThanOrEqualTo(String value) {
            addCriterion("cus_address <=", value, "cus_address");
            return (Criteria) this;
        }

        public Criteria andCus_addressLike(String value) {
            addCriterion("cus_address like", value, "cus_address");
            return (Criteria) this;
        }

        public Criteria andCus_addressNotLike(String value) {
            addCriterion("cus_address not like", value, "cus_address");
            return (Criteria) this;
        }

        public Criteria andCus_addressIn(List<String> values) {
            addCriterion("cus_address in", values, "cus_address");
            return (Criteria) this;
        }

        public Criteria andCus_addressNotIn(List<String> values) {
            addCriterion("cus_address not in", values, "cus_address");
            return (Criteria) this;
        }

        public Criteria andCus_addressBetween(String value1, String value2) {
            addCriterion("cus_address between", value1, value2, "cus_address");
            return (Criteria) this;
        }

        public Criteria andCus_addressNotBetween(String value1, String value2) {
            addCriterion("cus_address not between", value1, value2, "cus_address");
            return (Criteria) this;
        }

        public Criteria andPrice_totalIsNull() {
            addCriterion("price_total is null");
            return (Criteria) this;
        }

        public Criteria andPrice_totalIsNotNull() {
            addCriterion("price_total is not null");
            return (Criteria) this;
        }

        public Criteria andPrice_totalEqualTo(Integer value) {
            addCriterion("price_total =", value, "price_total");
            return (Criteria) this;
        }

        public Criteria andPrice_totalNotEqualTo(Integer value) {
            addCriterion("price_total <>", value, "price_total");
            return (Criteria) this;
        }

        public Criteria andPrice_totalGreaterThan(Integer value) {
            addCriterion("price_total >", value, "price_total");
            return (Criteria) this;
        }

        public Criteria andPrice_totalGreaterThanOrEqualTo(Integer value) {
            addCriterion("price_total >=", value, "price_total");
            return (Criteria) this;
        }

        public Criteria andPrice_totalLessThan(Integer value) {
            addCriterion("price_total <", value, "price_total");
            return (Criteria) this;
        }

        public Criteria andPrice_totalLessThanOrEqualTo(Integer value) {
            addCriterion("price_total <=", value, "price_total");
            return (Criteria) this;
        }

        public Criteria andPrice_totalIn(List<Integer> values) {
            addCriterion("price_total in", values, "price_total");
            return (Criteria) this;
        }

        public Criteria andPrice_totalNotIn(List<Integer> values) {
            addCriterion("price_total not in", values, "price_total");
            return (Criteria) this;
        }

        public Criteria andPrice_totalBetween(Integer value1, Integer value2) {
            addCriterion("price_total between", value1, value2, "price_total");
            return (Criteria) this;
        }

        public Criteria andPrice_totalNotBetween(Integer value1, Integer value2) {
            addCriterion("price_total not between", value1, value2, "price_total");
            return (Criteria) this;
        }

        public Criteria andPrice_payIsNull() {
            addCriterion("price_pay is null");
            return (Criteria) this;
        }

        public Criteria andPrice_payIsNotNull() {
            addCriterion("price_pay is not null");
            return (Criteria) this;
        }

        public Criteria andPrice_payEqualTo(Integer value) {
            addCriterion("price_pay =", value, "price_pay");
            return (Criteria) this;
        }

        public Criteria andPrice_payNotEqualTo(Integer value) {
            addCriterion("price_pay <>", value, "price_pay");
            return (Criteria) this;
        }

        public Criteria andPrice_payGreaterThan(Integer value) {
            addCriterion("price_pay >", value, "price_pay");
            return (Criteria) this;
        }

        public Criteria andPrice_payGreaterThanOrEqualTo(Integer value) {
            addCriterion("price_pay >=", value, "price_pay");
            return (Criteria) this;
        }

        public Criteria andPrice_payLessThan(Integer value) {
            addCriterion("price_pay <", value, "price_pay");
            return (Criteria) this;
        }

        public Criteria andPrice_payLessThanOrEqualTo(Integer value) {
            addCriterion("price_pay <=", value, "price_pay");
            return (Criteria) this;
        }

        public Criteria andPrice_payIn(List<Integer> values) {
            addCriterion("price_pay in", values, "price_pay");
            return (Criteria) this;
        }

        public Criteria andPrice_payNotIn(List<Integer> values) {
            addCriterion("price_pay not in", values, "price_pay");
            return (Criteria) this;
        }

        public Criteria andPrice_payBetween(Integer value1, Integer value2) {
            addCriterion("price_pay between", value1, value2, "price_pay");
            return (Criteria) this;
        }

        public Criteria andPrice_payNotBetween(Integer value1, Integer value2) {
            addCriterion("price_pay not between", value1, value2, "price_pay");
            return (Criteria) this;
        }

        public Criteria andReject_typeIsNull() {
            addCriterion("reject_type is null");
            return (Criteria) this;
        }

        public Criteria andReject_typeIsNotNull() {
            addCriterion("reject_type is not null");
            return (Criteria) this;
        }

        public Criteria andReject_typeEqualTo(Integer value) {
            addCriterion("reject_type =", value, "reject_type");
            return (Criteria) this;
        }

        public Criteria andReject_typeNotEqualTo(Integer value) {
            addCriterion("reject_type <>", value, "reject_type");
            return (Criteria) this;
        }

        public Criteria andReject_typeGreaterThan(Integer value) {
            addCriterion("reject_type >", value, "reject_type");
            return (Criteria) this;
        }

        public Criteria andReject_typeGreaterThanOrEqualTo(Integer value) {
            addCriterion("reject_type >=", value, "reject_type");
            return (Criteria) this;
        }

        public Criteria andReject_typeLessThan(Integer value) {
            addCriterion("reject_type <", value, "reject_type");
            return (Criteria) this;
        }

        public Criteria andReject_typeLessThanOrEqualTo(Integer value) {
            addCriterion("reject_type <=", value, "reject_type");
            return (Criteria) this;
        }

        public Criteria andReject_typeIn(List<Integer> values) {
            addCriterion("reject_type in", values, "reject_type");
            return (Criteria) this;
        }

        public Criteria andReject_typeNotIn(List<Integer> values) {
            addCriterion("reject_type not in", values, "reject_type");
            return (Criteria) this;
        }

        public Criteria andReject_typeBetween(Integer value1, Integer value2) {
            addCriterion("reject_type between", value1, value2, "reject_type");
            return (Criteria) this;
        }

        public Criteria andReject_typeNotBetween(Integer value1, Integer value2) {
            addCriterion("reject_type not between", value1, value2, "reject_type");
            return (Criteria) this;
        }

        public Criteria andReject_infoIsNull() {
            addCriterion("reject_info is null");
            return (Criteria) this;
        }

        public Criteria andReject_infoIsNotNull() {
            addCriterion("reject_info is not null");
            return (Criteria) this;
        }

        public Criteria andReject_infoEqualTo(String value) {
            addCriterion("reject_info =", value, "reject_info");
            return (Criteria) this;
        }

        public Criteria andReject_infoNotEqualTo(String value) {
            addCriterion("reject_info <>", value, "reject_info");
            return (Criteria) this;
        }

        public Criteria andReject_infoGreaterThan(String value) {
            addCriterion("reject_info >", value, "reject_info");
            return (Criteria) this;
        }

        public Criteria andReject_infoGreaterThanOrEqualTo(String value) {
            addCriterion("reject_info >=", value, "reject_info");
            return (Criteria) this;
        }

        public Criteria andReject_infoLessThan(String value) {
            addCriterion("reject_info <", value, "reject_info");
            return (Criteria) this;
        }

        public Criteria andReject_infoLessThanOrEqualTo(String value) {
            addCriterion("reject_info <=", value, "reject_info");
            return (Criteria) this;
        }

        public Criteria andReject_infoLike(String value) {
            addCriterion("reject_info like", value, "reject_info");
            return (Criteria) this;
        }

        public Criteria andReject_infoNotLike(String value) {
            addCriterion("reject_info not like", value, "reject_info");
            return (Criteria) this;
        }

        public Criteria andReject_infoIn(List<String> values) {
            addCriterion("reject_info in", values, "reject_info");
            return (Criteria) this;
        }

        public Criteria andReject_infoNotIn(List<String> values) {
            addCriterion("reject_info not in", values, "reject_info");
            return (Criteria) this;
        }

        public Criteria andReject_infoBetween(String value1, String value2) {
            addCriterion("reject_info between", value1, value2, "reject_info");
            return (Criteria) this;
        }

        public Criteria andReject_infoNotBetween(String value1, String value2) {
            addCriterion("reject_info not between", value1, value2, "reject_info");
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

        public Criteria andServer_datetimeIsNull() {
            addCriterion("server_datetime is null");
            return (Criteria) this;
        }

        public Criteria andServer_datetimeIsNotNull() {
            addCriterion("server_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andServer_datetimeEqualTo(Date value) {
            addCriterion("server_datetime =", value, "server_datetime");
            return (Criteria) this;
        }

        public Criteria andServer_datetimeNotEqualTo(Date value) {
            addCriterion("server_datetime <>", value, "server_datetime");
            return (Criteria) this;
        }

        public Criteria andServer_datetimeGreaterThan(Date value) {
            addCriterion("server_datetime >", value, "server_datetime");
            return (Criteria) this;
        }

        public Criteria andServer_datetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("server_datetime >=", value, "server_datetime");
            return (Criteria) this;
        }

        public Criteria andServer_datetimeLessThan(Date value) {
            addCriterion("server_datetime <", value, "server_datetime");
            return (Criteria) this;
        }

        public Criteria andServer_datetimeLessThanOrEqualTo(Date value) {
            addCriterion("server_datetime <=", value, "server_datetime");
            return (Criteria) this;
        }

        public Criteria andServer_datetimeIn(List<Date> values) {
            addCriterion("server_datetime in", values, "server_datetime");
            return (Criteria) this;
        }

        public Criteria andServer_datetimeNotIn(List<Date> values) {
            addCriterion("server_datetime not in", values, "server_datetime");
            return (Criteria) this;
        }

        public Criteria andServer_datetimeBetween(Date value1, Date value2) {
            addCriterion("server_datetime between", value1, value2, "server_datetime");
            return (Criteria) this;
        }

        public Criteria andServer_datetimeNotBetween(Date value1, Date value2) {
            addCriterion("server_datetime not between", value1, value2, "server_datetime");
            return (Criteria) this;
        }

        public Criteria andPay_order_idLikeInsensitive(String value) {
            addCriterion("upper(pay_order_id) like", value.toUpperCase(), "pay_order_id");
            return (Criteria) this;
        }

        public Criteria andServer_nameLikeInsensitive(String value) {
            addCriterion("upper(server_name) like", value.toUpperCase(), "server_name");
            return (Criteria) this;
        }

        public Criteria andCus_usernameLikeInsensitive(String value) {
            addCriterion("upper(cus_username) like", value.toUpperCase(), "cus_username");
            return (Criteria) this;
        }

        public Criteria andCus_phoneLikeInsensitive(String value) {
            addCriterion("upper(cus_phone) like", value.toUpperCase(), "cus_phone");
            return (Criteria) this;
        }

        public Criteria andCus_addressLikeInsensitive(String value) {
            addCriterion("upper(cus_address) like", value.toUpperCase(), "cus_address");
            return (Criteria) this;
        }

        public Criteria andReject_infoLikeInsensitive(String value) {
            addCriterion("upper(reject_info) like", value.toUpperCase(), "reject_info");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table reject_record
     *
     * @mbg.generated do_not_delete_during_merge Tue Jul 31 18:49:16 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table reject_record
     *
     * @mbg.generated Tue Jul 31 18:49:16 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	拒单记录表
		//--------------------------------------------------					
														reject_record_id(1,Types.BIGINT,false),
		/** 订单id */									pay_order_id(2,Types.VARCHAR,false),
		/** 服务单id */									serviceunit_id(2,Types.BIGINT,false),
		/** 创建日期 */									create_datetime(2,Types.TIMESTAMP,false),
		/** 订单名称/服务名称 */						server_name(2,Types.VARCHAR,false),
		/** 用户姓名 */									cus_username(2,Types.VARCHAR,false),
		/** 顾客电话 */									cus_phone(2,Types.VARCHAR,false),
		/** 顾客地址/服务地址 */						cus_address(2,Types.VARCHAR,false),
		/** 服务价格 */									price_total(2,Types.INTEGER,false),
		/** 实付金额 */									price_pay(2,Types.INTEGER,false),
		/** 拒单类型 0被动拒单、1主动拒单、2订单改派拒单 */
														reject_type(2,Types.INTEGER,false),
		/** 拒单原因 */									reject_info(2,Types.VARCHAR,false),
		/** 拒单合伙人id */								partner_id(2,Types.BIGINT,false),
		/** 预约服务时间 */								server_datetime(2,Types.TIMESTAMP,false);

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