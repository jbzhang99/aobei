package com.aobei.train.model;

import com.aobei.train.model.ChannelTypeExample.Criteria;
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
public class ChannelTypeExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_type
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    public ChannelTypeExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_type
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_type
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table channel_type
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table channel_type
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

        public Criteria andChannel_type_idIsNull() {
            addCriterion("channel_type_id is null");
            return (Criteria) this;
        }

        public Criteria andChannel_type_idIsNotNull() {
            addCriterion("channel_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andChannel_type_idEqualTo(Integer value) {
            addCriterion("channel_type_id =", value, "channel_type_id");
            return (Criteria) this;
        }

        public Criteria andChannel_type_idNotEqualTo(Integer value) {
            addCriterion("channel_type_id <>", value, "channel_type_id");
            return (Criteria) this;
        }

        public Criteria andChannel_type_idGreaterThan(Integer value) {
            addCriterion("channel_type_id >", value, "channel_type_id");
            return (Criteria) this;
        }

        public Criteria andChannel_type_idGreaterThanOrEqualTo(Integer value) {
            addCriterion("channel_type_id >=", value, "channel_type_id");
            return (Criteria) this;
        }

        public Criteria andChannel_type_idLessThan(Integer value) {
            addCriterion("channel_type_id <", value, "channel_type_id");
            return (Criteria) this;
        }

        public Criteria andChannel_type_idLessThanOrEqualTo(Integer value) {
            addCriterion("channel_type_id <=", value, "channel_type_id");
            return (Criteria) this;
        }

        public Criteria andChannel_type_idIn(List<Integer> values) {
            addCriterion("channel_type_id in", values, "channel_type_id");
            return (Criteria) this;
        }

        public Criteria andChannel_type_idNotIn(List<Integer> values) {
            addCriterion("channel_type_id not in", values, "channel_type_id");
            return (Criteria) this;
        }

        public Criteria andChannel_type_idBetween(Integer value1, Integer value2) {
            addCriterion("channel_type_id between", value1, value2, "channel_type_id");
            return (Criteria) this;
        }

        public Criteria andChannel_type_idNotBetween(Integer value1, Integer value2) {
            addCriterion("channel_type_id not between", value1, value2, "channel_type_id");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameIsNull() {
            addCriterion("channel_type_name is null");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameIsNotNull() {
            addCriterion("channel_type_name is not null");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameEqualTo(String value) {
            addCriterion("channel_type_name =", value, "channel_type_name");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameNotEqualTo(String value) {
            addCriterion("channel_type_name <>", value, "channel_type_name");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameGreaterThan(String value) {
            addCriterion("channel_type_name >", value, "channel_type_name");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameGreaterThanOrEqualTo(String value) {
            addCriterion("channel_type_name >=", value, "channel_type_name");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameLessThan(String value) {
            addCriterion("channel_type_name <", value, "channel_type_name");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameLessThanOrEqualTo(String value) {
            addCriterion("channel_type_name <=", value, "channel_type_name");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameLike(String value) {
            addCriterion("channel_type_name like", value, "channel_type_name");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameNotLike(String value) {
            addCriterion("channel_type_name not like", value, "channel_type_name");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameIn(List<String> values) {
            addCriterion("channel_type_name in", values, "channel_type_name");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameNotIn(List<String> values) {
            addCriterion("channel_type_name not in", values, "channel_type_name");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameBetween(String value1, String value2) {
            addCriterion("channel_type_name between", value1, value2, "channel_type_name");
            return (Criteria) this;
        }

        public Criteria andChannel_type_nameNotBetween(String value1, String value2) {
            addCriterion("channel_type_name not between", value1, value2, "channel_type_name");
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

        public Criteria andChannel_type_nameLikeInsensitive(String value) {
            addCriterion("upper(channel_type_name) like", value.toUpperCase(), "channel_type_name");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table channel_type
     *
     * @mbg.generated do_not_delete_during_merge Wed Jun 13 14:56:36 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table channel_type
     *
     * @mbg.generated Wed Jun 13 14:56:36 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	渠道类型管理
		//--------------------------------------------------					
						channel_type_id(1,Types.INTEGER,false),					
						channel_type_name(2,Types.VARCHAR,false),					
						create_datetime(2,Types.TIMESTAMP,false);

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