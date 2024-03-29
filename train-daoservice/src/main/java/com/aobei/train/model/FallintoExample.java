package com.aobei.train.model;

import com.aobei.train.model.FallintoExample.Criteria;
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
public class FallintoExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fallinto
     *
     * @mbg.generated Fri Jul 20 20:40:00 CST 2018
     */
    public FallintoExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fallinto
     *
     * @mbg.generated Fri Jul 20 20:40:00 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fallinto
     *
     * @mbg.generated Fri Jul 20 20:40:00 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fallinto
     *
     * @mbg.generated Fri Jul 20 20:40:00 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table fallinto
     *
     * @mbg.generated Fri Jul 20 20:40:00 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

        public Criteria andFallinto_idIsNull() {
            addCriterion("fallinto_id is null");
            return (Criteria) this;
        }

        public Criteria andFallinto_idIsNotNull() {
            addCriterion("fallinto_id is not null");
            return (Criteria) this;
        }

        public Criteria andFallinto_idEqualTo(Long value) {
            addCriterion("fallinto_id =", value, "fallinto_id");
            return (Criteria) this;
        }

        public Criteria andFallinto_idNotEqualTo(Long value) {
            addCriterion("fallinto_id <>", value, "fallinto_id");
            return (Criteria) this;
        }

        public Criteria andFallinto_idGreaterThan(Long value) {
            addCriterion("fallinto_id >", value, "fallinto_id");
            return (Criteria) this;
        }

        public Criteria andFallinto_idGreaterThanOrEqualTo(Long value) {
            addCriterion("fallinto_id >=", value, "fallinto_id");
            return (Criteria) this;
        }

        public Criteria andFallinto_idLessThan(Long value) {
            addCriterion("fallinto_id <", value, "fallinto_id");
            return (Criteria) this;
        }

        public Criteria andFallinto_idLessThanOrEqualTo(Long value) {
            addCriterion("fallinto_id <=", value, "fallinto_id");
            return (Criteria) this;
        }

        public Criteria andFallinto_idIn(List<Long> values) {
            addCriterion("fallinto_id in", values, "fallinto_id");
            return (Criteria) this;
        }

        public Criteria andFallinto_idNotIn(List<Long> values) {
            addCriterion("fallinto_id not in", values, "fallinto_id");
            return (Criteria) this;
        }

        public Criteria andFallinto_idBetween(Long value1, Long value2) {
            addCriterion("fallinto_id between", value1, value2, "fallinto_id");
            return (Criteria) this;
        }

        public Criteria andFallinto_idNotBetween(Long value1, Long value2) {
            addCriterion("fallinto_id not between", value1, value2, "fallinto_id");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameIsNull() {
            addCriterion("fallinto_name is null");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameIsNotNull() {
            addCriterion("fallinto_name is not null");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameEqualTo(String value) {
            addCriterion("fallinto_name =", value, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameNotEqualTo(String value) {
            addCriterion("fallinto_name <>", value, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameGreaterThan(String value) {
            addCriterion("fallinto_name >", value, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameGreaterThanOrEqualTo(String value) {
            addCriterion("fallinto_name >=", value, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameLessThan(String value) {
            addCriterion("fallinto_name <", value, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameLessThanOrEqualTo(String value) {
            addCriterion("fallinto_name <=", value, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameLike(String value) {
            addCriterion("fallinto_name like", value, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameNotLike(String value) {
            addCriterion("fallinto_name not like", value, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameIn(List<String> values) {
            addCriterion("fallinto_name in", values, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameNotIn(List<String> values) {
            addCriterion("fallinto_name not in", values, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameBetween(String value1, String value2) {
            addCriterion("fallinto_name between", value1, value2, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameNotBetween(String value1, String value2) {
            addCriterion("fallinto_name not between", value1, value2, "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andPercentIsNull() {
            addCriterion("percent is null");
            return (Criteria) this;
        }

        public Criteria andPercentIsNotNull() {
            addCriterion("percent is not null");
            return (Criteria) this;
        }

        public Criteria andPercentEqualTo(Integer value) {
            addCriterion("percent =", value, "percent");
            return (Criteria) this;
        }

        public Criteria andPercentNotEqualTo(Integer value) {
            addCriterion("percent <>", value, "percent");
            return (Criteria) this;
        }

        public Criteria andPercentGreaterThan(Integer value) {
            addCriterion("percent >", value, "percent");
            return (Criteria) this;
        }

        public Criteria andPercentGreaterThanOrEqualTo(Integer value) {
            addCriterion("percent >=", value, "percent");
            return (Criteria) this;
        }

        public Criteria andPercentLessThan(Integer value) {
            addCriterion("percent <", value, "percent");
            return (Criteria) this;
        }

        public Criteria andPercentLessThanOrEqualTo(Integer value) {
            addCriterion("percent <=", value, "percent");
            return (Criteria) this;
        }

        public Criteria andPercentIn(List<Integer> values) {
            addCriterion("percent in", values, "percent");
            return (Criteria) this;
        }

        public Criteria andPercentNotIn(List<Integer> values) {
            addCriterion("percent not in", values, "percent");
            return (Criteria) this;
        }

        public Criteria andPercentBetween(Integer value1, Integer value2) {
            addCriterion("percent between", value1, value2, "percent");
            return (Criteria) this;
        }

        public Criteria andPercentNotBetween(Integer value1, Integer value2) {
            addCriterion("percent not between", value1, value2, "percent");
            return (Criteria) this;
        }

        public Criteria andFloor_priceIsNull() {
            addCriterion("floor_price is null");
            return (Criteria) this;
        }

        public Criteria andFloor_priceIsNotNull() {
            addCriterion("floor_price is not null");
            return (Criteria) this;
        }

        public Criteria andFloor_priceEqualTo(Integer value) {
            addCriterion("floor_price =", value, "floor_price");
            return (Criteria) this;
        }

        public Criteria andFloor_priceNotEqualTo(Integer value) {
            addCriterion("floor_price <>", value, "floor_price");
            return (Criteria) this;
        }

        public Criteria andFloor_priceGreaterThan(Integer value) {
            addCriterion("floor_price >", value, "floor_price");
            return (Criteria) this;
        }

        public Criteria andFloor_priceGreaterThanOrEqualTo(Integer value) {
            addCriterion("floor_price >=", value, "floor_price");
            return (Criteria) this;
        }

        public Criteria andFloor_priceLessThan(Integer value) {
            addCriterion("floor_price <", value, "floor_price");
            return (Criteria) this;
        }

        public Criteria andFloor_priceLessThanOrEqualTo(Integer value) {
            addCriterion("floor_price <=", value, "floor_price");
            return (Criteria) this;
        }

        public Criteria andFloor_priceIn(List<Integer> values) {
            addCriterion("floor_price in", values, "floor_price");
            return (Criteria) this;
        }

        public Criteria andFloor_priceNotIn(List<Integer> values) {
            addCriterion("floor_price not in", values, "floor_price");
            return (Criteria) this;
        }

        public Criteria andFloor_priceBetween(Integer value1, Integer value2) {
            addCriterion("floor_price between", value1, value2, "floor_price");
            return (Criteria) this;
        }

        public Criteria andFloor_priceNotBetween(Integer value1, Integer value2) {
            addCriterion("floor_price not between", value1, value2, "floor_price");
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

        public Criteria andFallinto_typeIsNull() {
            addCriterion("fallinto_type is null");
            return (Criteria) this;
        }

        public Criteria andFallinto_typeIsNotNull() {
            addCriterion("fallinto_type is not null");
            return (Criteria) this;
        }

        public Criteria andFallinto_typeEqualTo(Integer value) {
            addCriterion("fallinto_type =", value, "fallinto_type");
            return (Criteria) this;
        }

        public Criteria andFallinto_typeNotEqualTo(Integer value) {
            addCriterion("fallinto_type <>", value, "fallinto_type");
            return (Criteria) this;
        }

        public Criteria andFallinto_typeGreaterThan(Integer value) {
            addCriterion("fallinto_type >", value, "fallinto_type");
            return (Criteria) this;
        }

        public Criteria andFallinto_typeGreaterThanOrEqualTo(Integer value) {
            addCriterion("fallinto_type >=", value, "fallinto_type");
            return (Criteria) this;
        }

        public Criteria andFallinto_typeLessThan(Integer value) {
            addCriterion("fallinto_type <", value, "fallinto_type");
            return (Criteria) this;
        }

        public Criteria andFallinto_typeLessThanOrEqualTo(Integer value) {
            addCriterion("fallinto_type <=", value, "fallinto_type");
            return (Criteria) this;
        }

        public Criteria andFallinto_typeIn(List<Integer> values) {
            addCriterion("fallinto_type in", values, "fallinto_type");
            return (Criteria) this;
        }

        public Criteria andFallinto_typeNotIn(List<Integer> values) {
            addCriterion("fallinto_type not in", values, "fallinto_type");
            return (Criteria) this;
        }

        public Criteria andFallinto_typeBetween(Integer value1, Integer value2) {
            addCriterion("fallinto_type between", value1, value2, "fallinto_type");
            return (Criteria) this;
        }

        public Criteria andFallinto_typeNotBetween(Integer value1, Integer value2) {
            addCriterion("fallinto_type not between", value1, value2, "fallinto_type");
            return (Criteria) this;
        }

        public Criteria andStep_dataIsNull() {
            addCriterion("step_data is null");
            return (Criteria) this;
        }

        public Criteria andStep_dataIsNotNull() {
            addCriterion("step_data is not null");
            return (Criteria) this;
        }

        public Criteria andStep_dataEqualTo(String value) {
            addCriterion("step_data =", value, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_dataNotEqualTo(String value) {
            addCriterion("step_data <>", value, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_dataGreaterThan(String value) {
            addCriterion("step_data >", value, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_dataGreaterThanOrEqualTo(String value) {
            addCriterion("step_data >=", value, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_dataLessThan(String value) {
            addCriterion("step_data <", value, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_dataLessThanOrEqualTo(String value) {
            addCriterion("step_data <=", value, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_dataLike(String value) {
            addCriterion("step_data like", value, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_dataNotLike(String value) {
            addCriterion("step_data not like", value, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_dataIn(List<String> values) {
            addCriterion("step_data in", values, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_dataNotIn(List<String> values) {
            addCriterion("step_data not in", values, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_dataBetween(String value1, String value2) {
            addCriterion("step_data between", value1, value2, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_dataNotBetween(String value1, String value2) {
            addCriterion("step_data not between", value1, value2, "step_data");
            return (Criteria) this;
        }

        public Criteria andStep_typeIsNull() {
            addCriterion("step_type is null");
            return (Criteria) this;
        }

        public Criteria andStep_typeIsNotNull() {
            addCriterion("step_type is not null");
            return (Criteria) this;
        }

        public Criteria andStep_typeEqualTo(Integer value) {
            addCriterion("step_type =", value, "step_type");
            return (Criteria) this;
        }

        public Criteria andStep_typeNotEqualTo(Integer value) {
            addCriterion("step_type <>", value, "step_type");
            return (Criteria) this;
        }

        public Criteria andStep_typeGreaterThan(Integer value) {
            addCriterion("step_type >", value, "step_type");
            return (Criteria) this;
        }

        public Criteria andStep_typeGreaterThanOrEqualTo(Integer value) {
            addCriterion("step_type >=", value, "step_type");
            return (Criteria) this;
        }

        public Criteria andStep_typeLessThan(Integer value) {
            addCriterion("step_type <", value, "step_type");
            return (Criteria) this;
        }

        public Criteria andStep_typeLessThanOrEqualTo(Integer value) {
            addCriterion("step_type <=", value, "step_type");
            return (Criteria) this;
        }

        public Criteria andStep_typeIn(List<Integer> values) {
            addCriterion("step_type in", values, "step_type");
            return (Criteria) this;
        }

        public Criteria andStep_typeNotIn(List<Integer> values) {
            addCriterion("step_type not in", values, "step_type");
            return (Criteria) this;
        }

        public Criteria andStep_typeBetween(Integer value1, Integer value2) {
            addCriterion("step_type between", value1, value2, "step_type");
            return (Criteria) this;
        }

        public Criteria andStep_typeNotBetween(Integer value1, Integer value2) {
            addCriterion("step_type not between", value1, value2, "step_type");
            return (Criteria) this;
        }

        public Criteria andActivedIsNull() {
            addCriterion("actived is null");
            return (Criteria) this;
        }

        public Criteria andActivedIsNotNull() {
            addCriterion("actived is not null");
            return (Criteria) this;
        }

        public Criteria andActivedEqualTo(Integer value) {
            addCriterion("actived =", value, "actived");
            return (Criteria) this;
        }

        public Criteria andActivedNotEqualTo(Integer value) {
            addCriterion("actived <>", value, "actived");
            return (Criteria) this;
        }

        public Criteria andActivedGreaterThan(Integer value) {
            addCriterion("actived >", value, "actived");
            return (Criteria) this;
        }

        public Criteria andActivedGreaterThanOrEqualTo(Integer value) {
            addCriterion("actived >=", value, "actived");
            return (Criteria) this;
        }

        public Criteria andActivedLessThan(Integer value) {
            addCriterion("actived <", value, "actived");
            return (Criteria) this;
        }

        public Criteria andActivedLessThanOrEqualTo(Integer value) {
            addCriterion("actived <=", value, "actived");
            return (Criteria) this;
        }

        public Criteria andActivedIn(List<Integer> values) {
            addCriterion("actived in", values, "actived");
            return (Criteria) this;
        }

        public Criteria andActivedNotIn(List<Integer> values) {
            addCriterion("actived not in", values, "actived");
            return (Criteria) this;
        }

        public Criteria andActivedBetween(Integer value1, Integer value2) {
            addCriterion("actived between", value1, value2, "actived");
            return (Criteria) this;
        }

        public Criteria andActivedNotBetween(Integer value1, Integer value2) {
            addCriterion("actived not between", value1, value2, "actived");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNull() {
            addCriterion("deleted is null");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNotNull() {
            addCriterion("deleted is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedEqualTo(Integer value) {
            addCriterion("deleted =", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotEqualTo(Integer value) {
            addCriterion("deleted <>", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThan(Integer value) {
            addCriterion("deleted >", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThanOrEqualTo(Integer value) {
            addCriterion("deleted >=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThan(Integer value) {
            addCriterion("deleted <", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThanOrEqualTo(Integer value) {
            addCriterion("deleted <=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedIn(List<Integer> values) {
            addCriterion("deleted in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotIn(List<Integer> values) {
            addCriterion("deleted not in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedBetween(Integer value1, Integer value2) {
            addCriterion("deleted between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotBetween(Integer value1, Integer value2) {
            addCriterion("deleted not between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andCreate_nameIsNull() {
            addCriterion("create_name is null");
            return (Criteria) this;
        }

        public Criteria andCreate_nameIsNotNull() {
            addCriterion("create_name is not null");
            return (Criteria) this;
        }

        public Criteria andCreate_nameEqualTo(String value) {
            addCriterion("create_name =", value, "create_name");
            return (Criteria) this;
        }

        public Criteria andCreate_nameNotEqualTo(String value) {
            addCriterion("create_name <>", value, "create_name");
            return (Criteria) this;
        }

        public Criteria andCreate_nameGreaterThan(String value) {
            addCriterion("create_name >", value, "create_name");
            return (Criteria) this;
        }

        public Criteria andCreate_nameGreaterThanOrEqualTo(String value) {
            addCriterion("create_name >=", value, "create_name");
            return (Criteria) this;
        }

        public Criteria andCreate_nameLessThan(String value) {
            addCriterion("create_name <", value, "create_name");
            return (Criteria) this;
        }

        public Criteria andCreate_nameLessThanOrEqualTo(String value) {
            addCriterion("create_name <=", value, "create_name");
            return (Criteria) this;
        }

        public Criteria andCreate_nameLike(String value) {
            addCriterion("create_name like", value, "create_name");
            return (Criteria) this;
        }

        public Criteria andCreate_nameNotLike(String value) {
            addCriterion("create_name not like", value, "create_name");
            return (Criteria) this;
        }

        public Criteria andCreate_nameIn(List<String> values) {
            addCriterion("create_name in", values, "create_name");
            return (Criteria) this;
        }

        public Criteria andCreate_nameNotIn(List<String> values) {
            addCriterion("create_name not in", values, "create_name");
            return (Criteria) this;
        }

        public Criteria andCreate_nameBetween(String value1, String value2) {
            addCriterion("create_name between", value1, value2, "create_name");
            return (Criteria) this;
        }

        public Criteria andCreate_nameNotBetween(String value1, String value2) {
            addCriterion("create_name not between", value1, value2, "create_name");
            return (Criteria) this;
        }

        public Criteria andIs_activedIsNull() {
            addCriterion("is_actived is null");
            return (Criteria) this;
        }

        public Criteria andIs_activedIsNotNull() {
            addCriterion("is_actived is not null");
            return (Criteria) this;
        }

        public Criteria andIs_activedEqualTo(Integer value) {
            addCriterion("is_actived =", value, "is_actived");
            return (Criteria) this;
        }

        public Criteria andIs_activedNotEqualTo(Integer value) {
            addCriterion("is_actived <>", value, "is_actived");
            return (Criteria) this;
        }

        public Criteria andIs_activedGreaterThan(Integer value) {
            addCriterion("is_actived >", value, "is_actived");
            return (Criteria) this;
        }

        public Criteria andIs_activedGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_actived >=", value, "is_actived");
            return (Criteria) this;
        }

        public Criteria andIs_activedLessThan(Integer value) {
            addCriterion("is_actived <", value, "is_actived");
            return (Criteria) this;
        }

        public Criteria andIs_activedLessThanOrEqualTo(Integer value) {
            addCriterion("is_actived <=", value, "is_actived");
            return (Criteria) this;
        }

        public Criteria andIs_activedIn(List<Integer> values) {
            addCriterion("is_actived in", values, "is_actived");
            return (Criteria) this;
        }

        public Criteria andIs_activedNotIn(List<Integer> values) {
            addCriterion("is_actived not in", values, "is_actived");
            return (Criteria) this;
        }

        public Criteria andIs_activedBetween(Integer value1, Integer value2) {
            addCriterion("is_actived between", value1, value2, "is_actived");
            return (Criteria) this;
        }

        public Criteria andIs_activedNotBetween(Integer value1, Integer value2) {
            addCriterion("is_actived not between", value1, value2, "is_actived");
            return (Criteria) this;
        }

        public Criteria andFallinto_nameLikeInsensitive(String value) {
            addCriterion("upper(fallinto_name) like", value.toUpperCase(), "fallinto_name");
            return (Criteria) this;
        }

        public Criteria andStep_dataLikeInsensitive(String value) {
            addCriterion("upper(step_data) like", value.toUpperCase(), "step_data");
            return (Criteria) this;
        }

        public Criteria andCreate_nameLikeInsensitive(String value) {
            addCriterion("upper(create_name) like", value.toUpperCase(), "create_name");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table fallinto
     *
     * @mbg.generated do_not_delete_during_merge Fri Jul 20 20:40:00 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table fallinto
     *
     * @mbg.generated Fri Jul 20 20:40:00 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	结算策略
		//--------------------------------------------------					
														fallinto_id(1,Types.BIGINT,false),					
														fallinto_name(2,Types.VARCHAR,false),
		/** 千分比 1-1000 */							percent(2,Types.INTEGER,false),
		/** 底价   单位分 */							floor_price(2,Types.INTEGER,false),
		/** 创建时间 */									create_datetime(2,Types.TIMESTAMP,false),
		/** 结算类型  1 底价  2 分成比例 3 单数阶梯 4 金额阶梯 5 客单价阶梯 */
														fallinto_type(2,Types.INTEGER,false),
		/** 阶梯json 数据 [{"d":100,"v":20.05}] */		step_data(2,Types.VARCHAR,false),
		/** 阶梯类型  1 按底价  2 比例 */				step_type(2,Types.INTEGER,false),
		/** 是否生效  1 已生效 0 未生效 */				actived(2,Types.INTEGER,false),
		/** 1 已删除 0 未删除 */						deleted(2,Types.INTEGER,false),
		/** 创建者 */									create_name(2,Types.VARCHAR,false),
		/** 是否生效过 [DV=>0] */						is_actived(2,Types.INTEGER,false);

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