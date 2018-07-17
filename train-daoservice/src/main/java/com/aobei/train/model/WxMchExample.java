package com.aobei.train.model;

import com.aobei.train.model.WxMchExample.Criteria;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class WxMchExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_mch
     *
     * @mbg.generated Sun Apr 08 17:26:43 CST 2018
     */
    public WxMchExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_mch
     *
     * @mbg.generated Sun Apr 08 17:26:43 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_mch
     *
     * @mbg.generated Sun Apr 08 17:26:43 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_mch
     *
     * @mbg.generated Sun Apr 08 17:26:43 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table wx_mch
     *
     * @mbg.generated Sun Apr 08 17:26:43 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

        public Criteria andMch_idIsNull() {
            addCriterion("mch_id is null");
            return (Criteria) this;
        }

        public Criteria andMch_idIsNotNull() {
            addCriterion("mch_id is not null");
            return (Criteria) this;
        }

        public Criteria andMch_idEqualTo(String value) {
            addCriterion("mch_id =", value, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_idNotEqualTo(String value) {
            addCriterion("mch_id <>", value, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_idGreaterThan(String value) {
            addCriterion("mch_id >", value, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_idGreaterThanOrEqualTo(String value) {
            addCriterion("mch_id >=", value, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_idLessThan(String value) {
            addCriterion("mch_id <", value, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_idLessThanOrEqualTo(String value) {
            addCriterion("mch_id <=", value, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_idLike(String value) {
            addCriterion("mch_id like", value, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_idNotLike(String value) {
            addCriterion("mch_id not like", value, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_idIn(List<String> values) {
            addCriterion("mch_id in", values, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_idNotIn(List<String> values) {
            addCriterion("mch_id not in", values, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_idBetween(String value1, String value2) {
            addCriterion("mch_id between", value1, value2, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_idNotBetween(String value1, String value2) {
            addCriterion("mch_id not between", value1, value2, "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_keyIsNull() {
            addCriterion("mch_key is null");
            return (Criteria) this;
        }

        public Criteria andMch_keyIsNotNull() {
            addCriterion("mch_key is not null");
            return (Criteria) this;
        }

        public Criteria andMch_keyEqualTo(String value) {
            addCriterion("mch_key =", value, "mch_key");
            return (Criteria) this;
        }

        public Criteria andMch_keyNotEqualTo(String value) {
            addCriterion("mch_key <>", value, "mch_key");
            return (Criteria) this;
        }

        public Criteria andMch_keyGreaterThan(String value) {
            addCriterion("mch_key >", value, "mch_key");
            return (Criteria) this;
        }

        public Criteria andMch_keyGreaterThanOrEqualTo(String value) {
            addCriterion("mch_key >=", value, "mch_key");
            return (Criteria) this;
        }

        public Criteria andMch_keyLessThan(String value) {
            addCriterion("mch_key <", value, "mch_key");
            return (Criteria) this;
        }

        public Criteria andMch_keyLessThanOrEqualTo(String value) {
            addCriterion("mch_key <=", value, "mch_key");
            return (Criteria) this;
        }

        public Criteria andMch_keyLike(String value) {
            addCriterion("mch_key like", value, "mch_key");
            return (Criteria) this;
        }

        public Criteria andMch_keyNotLike(String value) {
            addCriterion("mch_key not like", value, "mch_key");
            return (Criteria) this;
        }

        public Criteria andMch_keyIn(List<String> values) {
            addCriterion("mch_key in", values, "mch_key");
            return (Criteria) this;
        }

        public Criteria andMch_keyNotIn(List<String> values) {
            addCriterion("mch_key not in", values, "mch_key");
            return (Criteria) this;
        }

        public Criteria andMch_keyBetween(String value1, String value2) {
            addCriterion("mch_key between", value1, value2, "mch_key");
            return (Criteria) this;
        }

        public Criteria andMch_keyNotBetween(String value1, String value2) {
            addCriterion("mch_key not between", value1, value2, "mch_key");
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

        public Criteria andKeyfile_pathIsNull() {
            addCriterion("keyfile_path is null");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathIsNotNull() {
            addCriterion("keyfile_path is not null");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathEqualTo(String value) {
            addCriterion("keyfile_path =", value, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathNotEqualTo(String value) {
            addCriterion("keyfile_path <>", value, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathGreaterThan(String value) {
            addCriterion("keyfile_path >", value, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathGreaterThanOrEqualTo(String value) {
            addCriterion("keyfile_path >=", value, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathLessThan(String value) {
            addCriterion("keyfile_path <", value, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathLessThanOrEqualTo(String value) {
            addCriterion("keyfile_path <=", value, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathLike(String value) {
            addCriterion("keyfile_path like", value, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathNotLike(String value) {
            addCriterion("keyfile_path not like", value, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathIn(List<String> values) {
            addCriterion("keyfile_path in", values, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathNotIn(List<String> values) {
            addCriterion("keyfile_path not in", values, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathBetween(String value1, String value2) {
            addCriterion("keyfile_path between", value1, value2, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathNotBetween(String value1, String value2) {
            addCriterion("keyfile_path not between", value1, value2, "keyfile_path");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresIsNull() {
            addCriterion("keyfile_expires is null");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresIsNotNull() {
            addCriterion("keyfile_expires is not null");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresEqualTo(Date value) {
            addCriterionForJDBCDate("keyfile_expires =", value, "keyfile_expires");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresNotEqualTo(Date value) {
            addCriterionForJDBCDate("keyfile_expires <>", value, "keyfile_expires");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresGreaterThan(Date value) {
            addCriterionForJDBCDate("keyfile_expires >", value, "keyfile_expires");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("keyfile_expires >=", value, "keyfile_expires");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresLessThan(Date value) {
            addCriterionForJDBCDate("keyfile_expires <", value, "keyfile_expires");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("keyfile_expires <=", value, "keyfile_expires");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresIn(List<Date> values) {
            addCriterionForJDBCDate("keyfile_expires in", values, "keyfile_expires");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresNotIn(List<Date> values) {
            addCriterionForJDBCDate("keyfile_expires not in", values, "keyfile_expires");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("keyfile_expires between", value1, value2, "keyfile_expires");
            return (Criteria) this;
        }

        public Criteria andKeyfile_expiresNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("keyfile_expires not between", value1, value2, "keyfile_expires");
            return (Criteria) this;
        }

        public Criteria andMch_idLikeInsensitive(String value) {
            addCriterion("upper(mch_id) like", value.toUpperCase(), "mch_id");
            return (Criteria) this;
        }

        public Criteria andMch_keyLikeInsensitive(String value) {
            addCriterion("upper(mch_key) like", value.toUpperCase(), "mch_key");
            return (Criteria) this;
        }

        public Criteria andNameLikeInsensitive(String value) {
            addCriterion("upper(name) like", value.toUpperCase(), "name");
            return (Criteria) this;
        }

        public Criteria andKeyfile_pathLikeInsensitive(String value) {
            addCriterion("upper(keyfile_path) like", value.toUpperCase(), "keyfile_path");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table wx_mch
     *
     * @mbg.generated do_not_delete_during_merge Sun Apr 08 17:26:43 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table wx_mch
     *
     * @mbg.generated Sun Apr 08 17:26:43 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	微信商户信息
		//--------------------------------------------------					
										mch_id(1,Types.VARCHAR,false),
		/** key */						mch_key(2,Types.VARCHAR,false),
		/** 名称 */						name(2,Types.VARCHAR,false),
		/** api  P12 文件路径 */		keyfile_path(2,Types.VARCHAR,false),
		/** api  文件过期时间 */		keyfile_expires(2,Types.DATE,false);

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