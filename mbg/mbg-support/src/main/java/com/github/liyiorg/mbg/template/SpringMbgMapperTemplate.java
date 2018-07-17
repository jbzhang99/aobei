package com.github.liyiorg.mbg.template;

import java.util.List;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.mybatis.spring.SqlSessionTemplate;

import com.github.liyiorg.mbg.exceptions.MbgBatchException;
import com.github.liyiorg.mbg.support.mapper.MbgMapper;

/**
 * 
 * @author LiYi
 * 
 * @param <T>
 *            MbgMapper
 * @param <PrimaryKey>
 *            PrimaryKey
 * @param <Model>
 *            Model
 * @param <ModelWithBLOBs>
 *            ModelWithBLOBs
 * @param <Example>
 *            Example
 */
public class SpringMbgMapperTemplate<T extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example>
		extends AbstractMbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> {

	public SpringMbgMapperTemplate() {
		super();
	}

	public SpringMbgMapperTemplate(T mapper, SqlSessionTemplate batchSqlSessionTemplate) {
		super();
		this.mapper = mapper;
		this.batchSqlSessionTemplate = batchSqlSessionTemplate;
	}

	private SqlSessionTemplate batchSqlSessionTemplate;

	@Override
	public List<BatchResult> batchExec(String[] statements, Object[] params, boolean oneResult) {
		if (batchSqlSessionTemplate == null) {
			throw new MbgBatchException("The batchSqlSessionTemplate must not null");
		}
		if (batchSqlSessionTemplate.getExecutorType() != ExecutorType.BATCH) {
			throw new MbgBatchException("The batchSqlSessionTemplate ExecutorType must BATCH");
		}

		for (int i = 0; i < statements.length; i++) {
			String mapperStatement = statements[i];
			int type = 0;
			String method;
			if (mapperStatement.indexOf(".") == -1) {
				// 补充完整mapper statement名称
				method = mapperStatement;
				mapperStatement = mapperName() + "." + mapperStatement;
			} else {
				method = mapperStatement.substring(mapperStatement.lastIndexOf(".") + 1);
			}
			if (method.matches("(\\w*(i?)insert)\\w*")) {
				type = 1;
			} else if (method.matches("(\\w*(i?)delete)\\w*")) {
				type = 2;
			} else if (method.matches("(\\w*(i?)update)\\w*")) {
				type = 3;
			} else {
				throw new MbgBatchException("The statement must insert,delete,update with '" + mapperStatement + "'");
			}

			if (statements.length == 1 && params.length > 1) {
				// 参数格式 1:N ，使用单一 statement
				for (int j = 0; j < params.length; j++) {
					switch (type) {
					case 1:
						batchSqlSessionTemplate.insert(mapperStatement, params[j]);
						break;
					case 2:
						batchSqlSessionTemplate.delete(mapperStatement, params[j]);
						break;
					case 3:
						batchSqlSessionTemplate.update(mapperStatement, params[j]);
						break;
					}
				}
			} else if (statements.length == params.length) {
				// 参数格式 N:N ， statement 与 params 1:1
				switch (type) {
				case 1:
					batchSqlSessionTemplate.insert(mapperStatement, params[i]);
					break;
				case 2:
					batchSqlSessionTemplate.delete(mapperStatement, params[i]);
					break;
				case 3:
					batchSqlSessionTemplate.update(mapperStatement, params[i]);
					break;
				}
			} else {
				throw new MbgBatchException("The statements and params length must 1:N or N:N");
			}
		}
		List<BatchResult> list = batchSqlSessionTemplate.flushStatements();
		if (oneResult && list.size() != 1) {
			throw new MbgBatchException("Batch execution returned invalid results. "
					+ "Expected 1 but number of BatchResult objects returned was " + list.size());
		}
		return list;
	}

	public SqlSessionTemplate getBatchSqlSessionTemplate() {
		return batchSqlSessionTemplate;
	}

	public void setBatchSqlSessionTemplate(SqlSessionTemplate batchSqlSessionTemplate) {
		this.batchSqlSessionTemplate = batchSqlSessionTemplate;
	}

}
