package com.aobei.train.model;

import com.aobei.train.model.AuthResExample.Criteria;
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
public class AuthResExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_res
     *
     * @mbg.generated Tue Mar 27 15:25:36 CST 2018
     */
    public AuthResExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_res
     *
     * @mbg.generated Tue Mar 27 15:25:36 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_res
     *
     * @mbg.generated Tue Mar 27 15:25:36 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table auth_res
     *
     * @mbg.generated Tue Mar 27 15:25:36 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table auth_res
     *
     * @mbg.generated Tue Mar 27 15:25:36 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

        public Criteria andRes_idIsNull() {
            addCriterion("res_id is null");
            return (Criteria) this;
        }

        public Criteria andRes_idIsNotNull() {
            addCriterion("res_id is not null");
            return (Criteria) this;
        }

        public Criteria andRes_idEqualTo(Integer value) {
            addCriterion("res_id =", value, "res_id");
            return (Criteria) this;
        }

        public Criteria andRes_idNotEqualTo(Integer value) {
            addCriterion("res_id <>", value, "res_id");
            return (Criteria) this;
        }

        public Criteria andRes_idGreaterThan(Integer value) {
            addCriterion("res_id >", value, "res_id");
            return (Criteria) this;
        }

        public Criteria andRes_idGreaterThanOrEqualTo(Integer value) {
            addCriterion("res_id >=", value, "res_id");
            return (Criteria) this;
        }

        public Criteria andRes_idLessThan(Integer value) {
            addCriterion("res_id <", value, "res_id");
            return (Criteria) this;
        }

        public Criteria andRes_idLessThanOrEqualTo(Integer value) {
            addCriterion("res_id <=", value, "res_id");
            return (Criteria) this;
        }

        public Criteria andRes_idIn(List<Integer> values) {
            addCriterion("res_id in", values, "res_id");
            return (Criteria) this;
        }

        public Criteria andRes_idNotIn(List<Integer> values) {
            addCriterion("res_id not in", values, "res_id");
            return (Criteria) this;
        }

        public Criteria andRes_idBetween(Integer value1, Integer value2) {
            addCriterion("res_id between", value1, value2, "res_id");
            return (Criteria) this;
        }

        public Criteria andRes_idNotBetween(Integer value1, Integer value2) {
            addCriterion("res_id not between", value1, value2, "res_id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andRole_keyIsNull() {
            addCriterion("role_key is null");
            return (Criteria) this;
        }

        public Criteria andRole_keyIsNotNull() {
            addCriterion("role_key is not null");
            return (Criteria) this;
        }

        public Criteria andRole_keyEqualTo(String value) {
            addCriterion("role_key =", value, "role_key");
            return (Criteria) this;
        }

        public Criteria andRole_keyNotEqualTo(String value) {
            addCriterion("role_key <>", value, "role_key");
            return (Criteria) this;
        }

        public Criteria andRole_keyGreaterThan(String value) {
            addCriterion("role_key >", value, "role_key");
            return (Criteria) this;
        }

        public Criteria andRole_keyGreaterThanOrEqualTo(String value) {
            addCriterion("role_key >=", value, "role_key");
            return (Criteria) this;
        }

        public Criteria andRole_keyLessThan(String value) {
            addCriterion("role_key <", value, "role_key");
            return (Criteria) this;
        }

        public Criteria andRole_keyLessThanOrEqualTo(String value) {
            addCriterion("role_key <=", value, "role_key");
            return (Criteria) this;
        }

        public Criteria andRole_keyLike(String value) {
            addCriterion("role_key like", value, "role_key");
            return (Criteria) this;
        }

        public Criteria andRole_keyNotLike(String value) {
            addCriterion("role_key not like", value, "role_key");
            return (Criteria) this;
        }

        public Criteria andRole_keyIn(List<String> values) {
            addCriterion("role_key in", values, "role_key");
            return (Criteria) this;
        }

        public Criteria andRole_keyNotIn(List<String> values) {
            addCriterion("role_key not in", values, "role_key");
            return (Criteria) this;
        }

        public Criteria andRole_keyBetween(String value1, String value2) {
            addCriterion("role_key between", value1, value2, "role_key");
            return (Criteria) this;
        }

        public Criteria andRole_keyNotBetween(String value1, String value2) {
            addCriterion("role_key not between", value1, value2, "role_key");
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

        public Criteria andTagIsNull() {
            addCriterion("tag is null");
            return (Criteria) this;
        }

        public Criteria andTagIsNotNull() {
            addCriterion("tag is not null");
            return (Criteria) this;
        }

        public Criteria andTagEqualTo(String value) {
            addCriterion("tag =", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotEqualTo(String value) {
            addCriterion("tag <>", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagGreaterThan(String value) {
            addCriterion("tag >", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagGreaterThanOrEqualTo(String value) {
            addCriterion("tag >=", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLessThan(String value) {
            addCriterion("tag <", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLessThanOrEqualTo(String value) {
            addCriterion("tag <=", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLike(String value) {
            addCriterion("tag like", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotLike(String value) {
            addCriterion("tag not like", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagIn(List<String> values) {
            addCriterion("tag in", values, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotIn(List<String> values) {
            addCriterion("tag not in", values, "tag");
            return (Criteria) this;
        }

        public Criteria andTagBetween(String value1, String value2) {
            addCriterion("tag between", value1, value2, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotBetween(String value1, String value2) {
            addCriterion("tag not between", value1, value2, "tag");
            return (Criteria) this;
        }

        public Criteria andUrlsIsNull() {
            addCriterion("urls is null");
            return (Criteria) this;
        }

        public Criteria andUrlsIsNotNull() {
            addCriterion("urls is not null");
            return (Criteria) this;
        }

        public Criteria andUrlsEqualTo(String value) {
            addCriterion("urls =", value, "urls");
            return (Criteria) this;
        }

        public Criteria andUrlsNotEqualTo(String value) {
            addCriterion("urls <>", value, "urls");
            return (Criteria) this;
        }

        public Criteria andUrlsGreaterThan(String value) {
            addCriterion("urls >", value, "urls");
            return (Criteria) this;
        }

        public Criteria andUrlsGreaterThanOrEqualTo(String value) {
            addCriterion("urls >=", value, "urls");
            return (Criteria) this;
        }

        public Criteria andUrlsLessThan(String value) {
            addCriterion("urls <", value, "urls");
            return (Criteria) this;
        }

        public Criteria andUrlsLessThanOrEqualTo(String value) {
            addCriterion("urls <=", value, "urls");
            return (Criteria) this;
        }

        public Criteria andUrlsLike(String value) {
            addCriterion("urls like", value, "urls");
            return (Criteria) this;
        }

        public Criteria andUrlsNotLike(String value) {
            addCriterion("urls not like", value, "urls");
            return (Criteria) this;
        }

        public Criteria andUrlsIn(List<String> values) {
            addCriterion("urls in", values, "urls");
            return (Criteria) this;
        }

        public Criteria andUrlsNotIn(List<String> values) {
            addCriterion("urls not in", values, "urls");
            return (Criteria) this;
        }

        public Criteria andUrlsBetween(String value1, String value2) {
            addCriterion("urls between", value1, value2, "urls");
            return (Criteria) this;
        }

        public Criteria andUrlsNotBetween(String value1, String value2) {
            addCriterion("urls not between", value1, value2, "urls");
            return (Criteria) this;
        }

        public Criteria andNameLikeInsensitive(String value) {
            addCriterion("upper(name) like", value.toUpperCase(), "name");
            return (Criteria) this;
        }

        public Criteria andRole_keyLikeInsensitive(String value) {
            addCriterion("upper(role_key) like", value.toUpperCase(), "role_key");
            return (Criteria) this;
        }

        public Criteria andTagLikeInsensitive(String value) {
            addCriterion("upper(tag) like", value.toUpperCase(), "tag");
            return (Criteria) this;
        }

        public Criteria andUrlsLikeInsensitive(String value) {
            addCriterion("upper(urls) like", value.toUpperCase(), "urls");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table auth_res
     *
     * @mbg.generated do_not_delete_during_merge Tue Mar 27 15:25:36 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table auth_res
     *
     * @mbg.generated Tue Mar 27 15:25:36 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	权限管理  资源
		//--------------------------------------------------
		/** 资源ID */								res_id(1,Types.INTEGER,false),
		/** 资源名称 */								name(2,Types.VARCHAR,false),
		/** 资源路径 */								role_key(2,Types.VARCHAR,false),					
													create_datetime(2,Types.TIMESTAMP,false),
		/** 0 未删除，1 已删除 [DV=>0] */			deleted(2,Types.INTEGER,false),
		/** 分组标签 */								tag(2,Types.VARCHAR,false),					
													urls(2,Types.VARCHAR,false);

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