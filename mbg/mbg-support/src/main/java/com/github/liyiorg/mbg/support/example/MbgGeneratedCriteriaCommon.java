package com.github.liyiorg.mbg.support.example;

import java.sql.Types;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author LiYi
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class MbgGeneratedCriteriaCommon<Criteria extends MbgGeneratedCriteriaCommon>
		extends MbgGeneratedCriteria {

	protected <T> void addCriterion(int jdbcType, String condition, T value, String property) {
		switch (jdbcType) {
		case Types.DATE:
			addCriterionForJDBCDate(condition, (Date) value, property);
			break;
		case Types.TIME:
			addCriterionForJDBCTime(condition, (Date) value, property);
			break;
		default:
			addCriterion(condition, value, property);
		}
	}

	protected <T> void addCriterion(int jdbcType, String condition, List<T> value, String property) {
		switch (jdbcType) {
		case Types.DATE:
			addCriterionForJDBCDate(condition, (List<Date>) value, property);
			break;
		case Types.TIME:
			addCriterionForJDBCTime(condition, (List<Date>) value, property);
			break;
		default:
			addCriterion(condition, value, property);
		}
	}

	protected <T> void addCriterion(int jdbcType, String condition, T value1, T value2, String property) {
		switch (jdbcType) {
		case Types.DATE:
			addCriterionForJDBCDate(condition, (Date) value1, (Date) value2, property);
			break;
		case Types.TIME:
			addCriterionForJDBCTime(condition, (Date) value1, (Date) value2, property);
			break;
		default:
			addCriterion(condition, value1, value2, property);
		}
	}

	public <W extends WItem<?>> Criteria andIsNull(W w) {
		addCriterion(w.delimitedAliasName() + " is null");
		return (Criteria) this;
	}

	public <W extends WItem<?>> Criteria andIsNotNull(W w) {
		addCriterion(w.delimitedAliasName() + " is not null");
		return (Criteria) this;
	}

	public <W extends WItem<T>, T> Criteria andEqualTo(W w, T value) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " =", value, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<T>, T> Criteria andNotEqualTo(W w, T value) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " <>", value, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<T>, T> Criteria andGreaterThan(W w, T value) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " >", value, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<T>, T> Criteria andGreaterThanOrEqualTo(W w, T value) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " >=", value, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<T>, T> Criteria andLessThan(W w, T value) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " <", value, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<T>, T> Criteria andLessThanOrEqualTo(W w, T value) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " <=", value, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<T>, T> Criteria andIn(W w, List<T> values) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " in", values, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<T>, T> Criteria andNotIn(W w, List<T> values) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " not in", values, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<T>, T> Criteria andBetween(W w, T value1, T value2) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " between", value1, value2, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<T>, T> Criteria andNotBetween(W w, T value1, T value2) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " not between", value1, value2, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<String>> Criteria andLike(W w, String value) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " like", value, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<String>> Criteria andNotLike(W w, String value) {
		addCriterion(w.getJdbcType(), w.delimitedAliasName() + " not like", value, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<String>> Criteria andLikeInsensitive(W w, String value) {
		addCriterion(w.getJdbcType(), "upper(" + w.delimitedAliasName() + ") like", value, w.name());
		return (Criteria) this;
	}

	public <W extends WItem<String>> Criteria andNotLikeInsensitive(W w, String value) {
		addCriterion(w.getJdbcType(), "upper(" + w.delimitedAliasName() + ") not like", value, w.name());
		return (Criteria) this;
	}

}
