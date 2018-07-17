package com.aobei.train.model;

import com.aobei.train.model.DataDownloadExample.Criteria;
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
public class DataDownloadExample extends MbgExample<Criteria> implements PaginationAble, Serializable, ColumnListAble {
    
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_download
     *
     * @mbg.generated Wed Mar 07 14:51:40 CST 2018
     */
    public DataDownloadExample() {
        databaseType = "MySQL";
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_download
     *
     * @mbg.generated Wed Mar 07 14:51:40 CST 2018
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_download
     *
     * @mbg.generated Wed Mar 07 14:51:40 CST 2018
     */
    public void includeColumns(C... cs) {
        CUtil.includeColumns(C.class, this, cs);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table data_download
     *
     * @mbg.generated Wed Mar 07 14:51:40 CST 2018
     */
    public void excludeColumns(C... cs) {
        CUtil.excludeColumns(C.class, this, cs);
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table data_download
     *
     * @mbg.generated Wed Mar 07 14:51:40 CST 2018
     */
    protected abstract static class GeneratedCriteria extends MbgGeneratedCriteria {

        public Criteria andData_download_idIsNull() {
            addCriterion("data_download_id is null");
            return (Criteria) this;
        }

        public Criteria andData_download_idIsNotNull() {
            addCriterion("data_download_id is not null");
            return (Criteria) this;
        }

        public Criteria andData_download_idEqualTo(Long value) {
            addCriterion("data_download_id =", value, "data_download_id");
            return (Criteria) this;
        }

        public Criteria andData_download_idNotEqualTo(Long value) {
            addCriterion("data_download_id <>", value, "data_download_id");
            return (Criteria) this;
        }

        public Criteria andData_download_idGreaterThan(Long value) {
            addCriterion("data_download_id >", value, "data_download_id");
            return (Criteria) this;
        }

        public Criteria andData_download_idGreaterThanOrEqualTo(Long value) {
            addCriterion("data_download_id >=", value, "data_download_id");
            return (Criteria) this;
        }

        public Criteria andData_download_idLessThan(Long value) {
            addCriterion("data_download_id <", value, "data_download_id");
            return (Criteria) this;
        }

        public Criteria andData_download_idLessThanOrEqualTo(Long value) {
            addCriterion("data_download_id <=", value, "data_download_id");
            return (Criteria) this;
        }

        public Criteria andData_download_idIn(List<Long> values) {
            addCriterion("data_download_id in", values, "data_download_id");
            return (Criteria) this;
        }

        public Criteria andData_download_idNotIn(List<Long> values) {
            addCriterion("data_download_id not in", values, "data_download_id");
            return (Criteria) this;
        }

        public Criteria andData_download_idBetween(Long value1, Long value2) {
            addCriterion("data_download_id between", value1, value2, "data_download_id");
            return (Criteria) this;
        }

        public Criteria andData_download_idNotBetween(Long value1, Long value2) {
            addCriterion("data_download_id not between", value1, value2, "data_download_id");
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

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andParamsIsNull() {
            addCriterion("params is null");
            return (Criteria) this;
        }

        public Criteria andParamsIsNotNull() {
            addCriterion("params is not null");
            return (Criteria) this;
        }

        public Criteria andParamsEqualTo(String value) {
            addCriterion("params =", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotEqualTo(String value) {
            addCriterion("params <>", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsGreaterThan(String value) {
            addCriterion("params >", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsGreaterThanOrEqualTo(String value) {
            addCriterion("params >=", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsLessThan(String value) {
            addCriterion("params <", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsLessThanOrEqualTo(String value) {
            addCriterion("params <=", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsLike(String value) {
            addCriterion("params like", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotLike(String value) {
            addCriterion("params not like", value, "params");
            return (Criteria) this;
        }

        public Criteria andParamsIn(List<String> values) {
            addCriterion("params in", values, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotIn(List<String> values) {
            addCriterion("params not in", values, "params");
            return (Criteria) this;
        }

        public Criteria andParamsBetween(String value1, String value2) {
            addCriterion("params between", value1, value2, "params");
            return (Criteria) this;
        }

        public Criteria andParamsNotBetween(String value1, String value2) {
            addCriterion("params not between", value1, value2, "params");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andUser_idIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUser_idIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUser_idEqualTo(Long value) {
            addCriterion("user_id =", value, "user_id");
            return (Criteria) this;
        }

        public Criteria andUser_idNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "user_id");
            return (Criteria) this;
        }

        public Criteria andUser_idGreaterThan(Long value) {
            addCriterion("user_id >", value, "user_id");
            return (Criteria) this;
        }

        public Criteria andUser_idGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "user_id");
            return (Criteria) this;
        }

        public Criteria andUser_idLessThan(Long value) {
            addCriterion("user_id <", value, "user_id");
            return (Criteria) this;
        }

        public Criteria andUser_idLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "user_id");
            return (Criteria) this;
        }

        public Criteria andUser_idIn(List<Long> values) {
            addCriterion("user_id in", values, "user_id");
            return (Criteria) this;
        }

        public Criteria andUser_idNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "user_id");
            return (Criteria) this;
        }

        public Criteria andUser_idBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "user_id");
            return (Criteria) this;
        }

        public Criteria andUser_idNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "user_id");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeIsNull() {
            addCriterion("finish_datetime is null");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeIsNotNull() {
            addCriterion("finish_datetime is not null");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeEqualTo(Date value) {
            addCriterion("finish_datetime =", value, "finish_datetime");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeNotEqualTo(Date value) {
            addCriterion("finish_datetime <>", value, "finish_datetime");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeGreaterThan(Date value) {
            addCriterion("finish_datetime >", value, "finish_datetime");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("finish_datetime >=", value, "finish_datetime");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeLessThan(Date value) {
            addCriterion("finish_datetime <", value, "finish_datetime");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeLessThanOrEqualTo(Date value) {
            addCriterion("finish_datetime <=", value, "finish_datetime");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeIn(List<Date> values) {
            addCriterion("finish_datetime in", values, "finish_datetime");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeNotIn(List<Date> values) {
            addCriterion("finish_datetime not in", values, "finish_datetime");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeBetween(Date value1, Date value2) {
            addCriterion("finish_datetime between", value1, value2, "finish_datetime");
            return (Criteria) this;
        }

        public Criteria andFinish_datetimeNotBetween(Date value1, Date value2) {
            addCriterion("finish_datetime not between", value1, value2, "finish_datetime");
            return (Criteria) this;
        }

        public Criteria andOss_fileIsNull() {
            addCriterion("oss_file is null");
            return (Criteria) this;
        }

        public Criteria andOss_fileIsNotNull() {
            addCriterion("oss_file is not null");
            return (Criteria) this;
        }

        public Criteria andOss_fileEqualTo(String value) {
            addCriterion("oss_file =", value, "oss_file");
            return (Criteria) this;
        }

        public Criteria andOss_fileNotEqualTo(String value) {
            addCriterion("oss_file <>", value, "oss_file");
            return (Criteria) this;
        }

        public Criteria andOss_fileGreaterThan(String value) {
            addCriterion("oss_file >", value, "oss_file");
            return (Criteria) this;
        }

        public Criteria andOss_fileGreaterThanOrEqualTo(String value) {
            addCriterion("oss_file >=", value, "oss_file");
            return (Criteria) this;
        }

        public Criteria andOss_fileLessThan(String value) {
            addCriterion("oss_file <", value, "oss_file");
            return (Criteria) this;
        }

        public Criteria andOss_fileLessThanOrEqualTo(String value) {
            addCriterion("oss_file <=", value, "oss_file");
            return (Criteria) this;
        }

        public Criteria andOss_fileLike(String value) {
            addCriterion("oss_file like", value, "oss_file");
            return (Criteria) this;
        }

        public Criteria andOss_fileNotLike(String value) {
            addCriterion("oss_file not like", value, "oss_file");
            return (Criteria) this;
        }

        public Criteria andOss_fileIn(List<String> values) {
            addCriterion("oss_file in", values, "oss_file");
            return (Criteria) this;
        }

        public Criteria andOss_fileNotIn(List<String> values) {
            addCriterion("oss_file not in", values, "oss_file");
            return (Criteria) this;
        }

        public Criteria andOss_fileBetween(String value1, String value2) {
            addCriterion("oss_file between", value1, value2, "oss_file");
            return (Criteria) this;
        }

        public Criteria andOss_fileNotBetween(String value1, String value2) {
            addCriterion("oss_file not between", value1, value2, "oss_file");
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

        public Criteria andNameLikeInsensitive(String value) {
            addCriterion("upper(name) like", value.toUpperCase(), "name");
            return (Criteria) this;
        }

        public Criteria andParamsLikeInsensitive(String value) {
            addCriterion("upper(params) like", value.toUpperCase(), "params");
            return (Criteria) this;
        }

        public Criteria andOss_fileLikeInsensitive(String value) {
            addCriterion("upper(oss_file) like", value.toUpperCase(), "oss_file");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table data_download
     *
     * @mbg.generated do_not_delete_during_merge Wed Mar 07 14:51:40 CST 2018
     */
    public static class Criteria extends GeneratedCriteria implements Serializable {
        
        private static final long serialVersionUID = 1L;

        protected Criteria() {
            super();
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table data_download
     *
     * @mbg.generated Wed Mar 07 14:51:40 CST 2018
     */
    public enum C implements CInterface {
        //--------------------------------------------------
		//[TABLE]	数据下载任务
		//--------------------------------------------------					
														data_download_id(1,Types.BIGINT,false),
		/** 下载的任务名称 */							name(2,Types.VARCHAR,false),
		/** 创建时间 */									create_datetime(2,Types.TIMESTAMP,false),
		/** 类型   1 订单数据下载 */					type(2,Types.INTEGER,false),
		/** 参数JSON数据 */								params(2,Types.VARCHAR,false),
		/** 0 待执行，1 执行中，2 执行完成，3 已取消 */	status(2,Types.INTEGER,false),
		/** 统一用户 id */								user_id(2,Types.BIGINT,false),
		/** 执行完成的时间点 */							finish_datetime(2,Types.TIMESTAMP,false),
		/** 文件存储id */								oss_file(2,Types.VARCHAR,false),
		/** 0 未删除 1 已删除 */						deleted(2,Types.INTEGER,false);

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