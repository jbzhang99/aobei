package com.github.liyiorg.mbg.template.factory;

import org.apache.ibatis.session.SqlSessionFactory;

import com.github.liyiorg.mbg.exceptions.MbgException;
import com.github.liyiorg.mbg.support.mapper.MbgMapper;
import com.github.liyiorg.mbg.template.DefaultMbgMapperTemplate;
import com.github.liyiorg.mbg.template.MbgMapperTemplate;

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
public class DefaultMbgMapperTemplateFactory extends MbgMapperTemplateFactory {

	private SqlSessionFactory sqlSessionFactory;

	@Override
	protected <T extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example> MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> createMbgMapperTemplate(
			Class<T> type) {
		throw new MbgException(
				"DefaultMbgMapperTemplateFactory con't support method createMbgMapperTemplate by class type.");
	}

	@Override
	protected <T extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example> MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> createMbgMapperTemplate(
			T mapper) {
		return new DefaultMbgMapperTemplate<>(mapper, sqlSessionFactory);
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

}
