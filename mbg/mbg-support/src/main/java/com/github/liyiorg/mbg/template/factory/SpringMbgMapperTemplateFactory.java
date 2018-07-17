package com.github.liyiorg.mbg.template.factory;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import com.github.liyiorg.mbg.support.mapper.MbgMapper;
import com.github.liyiorg.mbg.template.MbgMapperTemplate;
import com.github.liyiorg.mbg.template.SpringMbgMapperTemplate;

/**
 * 
 * @author LiYi
 *
 * @param <PrimaryKey>
 *            PrimaryKey
 * @param <Model>
 *            Model
 * @param <ModelWithBLOBs>
 *            ModelWithBLOBs
 * @param <Example>
 *            Example
 */
public class SpringMbgMapperTemplateFactory extends MbgMapperTemplateFactory {

	private SqlSessionFactory sqlSessionFactory;

	private SqlSessionTemplate sqlSessionTemplate;

	private SqlSessionTemplate batchSqlSessionTemplate;

	@Override
	protected <T extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example> MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> createMbgMapperTemplate(
			Class<T> type) {
		if (sqlSessionTemplate == null) {
			sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
			if (batchSqlSessionTemplate == null && sqlSessionTemplate.getExecutorType() == ExecutorType.BATCH) {
				batchSqlSessionTemplate = sqlSessionTemplate;
			}
		}
		if (batchSqlSessionTemplate == null) {
			batchSqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
		}
		T mapper = sqlSessionTemplate.getMapper(type);
		SpringMbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> template = new SpringMbgMapperTemplate<>();
		template.setBatchSqlSessionTemplate(batchSqlSessionTemplate);
		template.setMapper(mapper);
		return template;
	}

	@Override
	protected <T extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example> MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> createMbgMapperTemplate(
			T mapper) {
		if (batchSqlSessionTemplate == null && sqlSessionTemplate != null
				&& sqlSessionTemplate.getExecutorType() == ExecutorType.BATCH) {
			batchSqlSessionTemplate = sqlSessionTemplate;
		}
		if (batchSqlSessionTemplate == null) {
			batchSqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
		}
		SpringMbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> template = new SpringMbgMapperTemplate<>();
		template.setBatchSqlSessionTemplate(batchSqlSessionTemplate);
		template.setMapper(mapper);
		return template;
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public SqlSessionTemplate getBatchSqlSessionTemplate() {
		return batchSqlSessionTemplate;
	}

	public void setBatchSqlSessionTemplate(SqlSessionTemplate batchSqlSessionTemplate) {
		this.batchSqlSessionTemplate = batchSqlSessionTemplate;
	}

}
