package com.github.liyiorg.mbg.template;

import java.lang.reflect.Proxy;
import java.util.List;

import org.apache.ibatis.executor.BatchResult;

import com.github.liyiorg.mbg.bean.ModelExample;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.exceptions.MbgExampleException;
import com.github.liyiorg.mbg.exceptions.MbgMapperException;
import com.github.liyiorg.mbg.exceptions.MbgModelException;
import com.github.liyiorg.mbg.exceptions.MbgNoKeyException;
import com.github.liyiorg.mbg.support.example.PaginationAble;
import com.github.liyiorg.mbg.support.mapper.MbgMapper;
import com.github.liyiorg.mbg.support.mapper.MbgMapperMethods;
import com.github.liyiorg.mbg.support.mapper.MbgReadBLOBsMapper;
import com.github.liyiorg.mbg.support.mapper.MbgReadMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteBLOBsMapper;
import com.github.liyiorg.mbg.support.mapper.MbgWriteMapper;
import com.github.liyiorg.mbg.support.model.ModelUpsert;
import com.github.liyiorg.mbg.support.model.NoKey;
import com.github.liyiorg.mbg.util.GenericsUtils;

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
public abstract class AbstractMbgMapperTemplate<T extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example>
		implements MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> {

	protected T mapper;

	protected String mapperName;

	private MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example> readMapper;

	private MbgReadBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example> readBLOBsMapper;

	private MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example> writeMapper;

	private MbgWriteBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example> writeBLOBsMapper;

	private Class<?> exampleClass;

	private Boolean isNoKey;

	{
		exampleClass = GenericsUtils.getSuperClassGenricType(this.getClass(), 3);
	}

	protected boolean isNokey() {
		if (isNoKey == null) {
			isNoKey = false;
			try {
				Object model = GenericsUtils.getSuperClassGenricType(this.getClass(), 1).newInstance();
				if (model instanceof NoKey) {
					isNoKey = true;
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return isNoKey;
	}

	protected void isNoKeyCheck() {
		if (isNokey()) {
			throw new MbgNoKeyException("The table was no key or the model was implemented NoKey");
		}
	}

	protected String mapperName() {
		if (mapperName == null) {
			if (Proxy.isProxyClass(mapper.getClass())) {
				mapperName = mapper.getClass().getGenericInterfaces()[0].getTypeName();
			} else {
				mapperName = mapper.getClass().getName();
			}
		}
		return mapperName;
	}

	protected MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example> readMapper() {
		if (readMapper == null) {
			if (mapper == null) {
				throw new MbgMapperException("The mapper must not null");
			} else if (mapper instanceof MbgReadMapper) {
				readMapper = (MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper;
			} else {
				throw new MbgMapperException("The mapper is not instance of MbgReadMapper");
			}
		}
		return readMapper;
	}

	protected MbgReadBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example> readBLOBsMapper() {
		if (readBLOBsMapper == null) {
			if (mapper == null) {
				throw new MbgMapperException("The mapper must not null");
			} else if (mapper instanceof MbgReadBLOBsMapper) {
				readBLOBsMapper = (MbgReadBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper;
			} else {
				throw new MbgMapperException("The mapper is not instance of MbgReadBLOBsMapper");
			}
		}
		return readBLOBsMapper;
	}

	protected MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example> writeMapper() {
		if (writeMapper == null) {
			if (mapper == null) {
				throw new MbgMapperException("The mapper must not null");
			} else if (mapper instanceof MbgWriteMapper) {
				writeMapper = (MbgWriteMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper;
			} else {
				throw new MbgMapperException("The mapper is not instance of MbgWriteMapper");
			}
		}
		return writeMapper;
	}

	protected MbgWriteBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example> writeBLOBsMapper() {
		if (writeBLOBsMapper == null) {
			if (mapper == null) {
				throw new MbgMapperException("The mapper must not null");
			} else if (mapper instanceof MbgWriteBLOBsMapper) {
				writeBLOBsMapper = (MbgWriteBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example>) mapper;
			} else {
				throw new MbgMapperException("The mapper is not instance of MbgWriteBLOBsMapper");
			}
		}
		return writeBLOBsMapper;
	}

	@Override
	public long countByExample(Example example) {
		return readMapper().countByExample(example);
	}

	@Override
	public int deleteByExample(Example example) {
		return writeMapper().deleteByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(PrimaryKey id) {
		isNoKeyCheck();
		return writeMapper().deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ModelWithBLOBs record) {
		return writeMapper().insert(record);
	}

	@Override
	public int insertSelective(ModelWithBLOBs record) {
		return writeMapper().insertSelective(record);
	}

	@Override
	public int upsert(ModelWithBLOBs record) {
		if (record instanceof ModelUpsert) {
			ModelUpsert temp = (ModelUpsert) record;
			temp.upsert();
			return writeMapper().insert(record);
		} else {
			throw new MbgModelException("Record must ModelUpsert");
		}
	}

	@Override
	public int upsertSelective(ModelWithBLOBs record) {
		if (record instanceof ModelUpsert) {
			ModelUpsert temp = (ModelUpsert) record;
			temp.upsert();
			return writeMapper().insertSelective(record);
		} else {
			throw new MbgModelException("Record must ModelUpsert");
		}
	}

	@Override
	public List<ModelWithBLOBs> selectByExampleWithBLOBs(Example example) {
		return readBLOBsMapper().selectByExampleWithBLOBs(example);
	}

	@Override
	public List<Model> selectByExample(Example example) {
		return readMapper().selectByExample(example);
	}

	@Override
	public ModelWithBLOBs selectByPrimaryKey(PrimaryKey id) {
		isNoKeyCheck();
		return readMapper().selectByPrimaryKey(id);
	}

	@Override
	public int updateByExampleSelective(Model record, Example example) {
		return writeMapper().updateByExampleSelective(record, example);
	}

	@Override
	public int updateByExampleWithBLOBs(ModelWithBLOBs record, Example example) {
		return writeBLOBsMapper().updateByExampleWithBLOBs(record, example);
	}

	@Override
	public int updateByExample(Model record, Example example) {
		return writeMapper().updateByExample(record, example);
	}

	@Override
	public int updateByPrimaryKeySelective(Model record) {
		isNoKeyCheck();
		return writeMapper().updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(ModelWithBLOBs record) {
		isNoKeyCheck();
		return writeBLOBsMapper().updateByPrimaryKeyWithBLOBs(record);
	}

	@Override
	public int updateByPrimaryKey(Model record) {
		isNoKeyCheck();
		return writeMapper().updateByPrimaryKey(record);
	}

	/**
	 * 批量操作
	 * 
	 * @param statements
	 *            MAPPER 方法,必须包含 insert,delete,update 字样
	 * @param params
	 *            POJO,MAP
	 * @return 返回一组数据
	 */
	@Override
	public int[] batchUpdate(String statements, Object[] params) {
		List<BatchResult> list = batchExec(new String[] { statements }, params, true);
		return list.get(0).getUpdateCounts();
	}

	/**
	 * 批量操作
	 * 
	 * @param statements
	 *            MAPPER 方法,必须包含 insert,delete,update 字样
	 * @param params
	 *            POJO,MAP
	 * @return
	 */
	public List<BatchResult> batchExec(String statements, Object[] params) {
		return batchExec(new String[] { statements }, params, false);
	}

	/**
	 * 批量操作<br>
	 * The statements and params length must 1:N or N:N
	 * 
	 * @param statements
	 *            MAPPER 方法,必须包含 insert,delete,update 字样
	 * @param params
	 *            POJO,MAP
	 * @param oneResult
	 *            only one sqlStatement
	 * @return List
	 */
	public abstract List<BatchResult> batchExec(String[] statements, Object[] params, boolean oneResult);

	@Override
	public int[] batchDeleteByExample(Example[] example) {
		writeMapper();
		return batchUpdate(MbgMapperMethods.deleteByExample, example);
	}

	@Override
	public int[] batchDeleteByPrimaryKey(PrimaryKey[] id) {
		writeMapper();
		isNoKeyCheck();
		return batchUpdate(MbgMapperMethods.deleteByPrimaryKey, id);
	}

	@Override
	public int[] batchInsert(ModelWithBLOBs[] record) {
		writeMapper();
		return batchUpdate(MbgMapperMethods.insert, record);
	}

	@Override
	public int[] batchInsertSelective(ModelWithBLOBs[] record) {
		writeMapper();
		return batchUpdate(MbgMapperMethods.insertSelective, record);
	}

	@Override
	public int[] batchUpsert(ModelWithBLOBs[] record) {
		writeMapper();
		boolean first = true;
		for (ModelWithBLOBs m : record) {
			if (first) {
				first = false;
				if (!(m instanceof ModelUpsert)) {
					throw new MbgModelException("Record must ModelUpsert");
				}
			}
			ModelUpsert temp = (ModelUpsert) m;
			temp.upsert();
		}
		return batchUpdate(MbgMapperMethods.insert, record);
	}

	@Override
	public int[] batchUpsertSelective(ModelWithBLOBs[] record) {
		writeMapper();
		boolean first = true;
		for (ModelWithBLOBs m : record) {
			if (first) {
				first = false;
				if (!(m instanceof ModelUpsert)) {
					throw new MbgModelException("Record must ModelUpsert");
				}
			}
			ModelUpsert temp = (ModelUpsert) m;
			temp.upsert();
		}
		return batchUpdate(MbgMapperMethods.insertSelective, record);
	}

	@Override
	public int[] batchUpdateByExampleSelective(ModelExample<Model, Example>[] modelExample) {
		writeMapper();
		return batchUpdate(MbgMapperMethods.updateByExampleSelective, modelExample);
	}

	@Override
	public int[] batchUpdateByExample(ModelExample<Model, Example>[] modelExample) {
		writeMapper();
		return batchUpdate(MbgMapperMethods.updateByExample, modelExample);
	}

	@Override
	public int[] batchUpdateByPrimaryKeySelective(Model[] record) {
		writeMapper();
		isNoKeyCheck();
		return batchUpdate(MbgMapperMethods.updateByPrimaryKeySelective, record);
	}

	@Override
	public int[] batchUpdateByPrimaryKey(Model[] record) {
		writeMapper();
		isNoKeyCheck();
		return batchUpdate(MbgMapperMethods.updateByPrimaryKey, record);
	}

	@Override
	public int[] batchUpdateByExampleWithBLOBs(ModelExample<ModelWithBLOBs, Example>[] modelExample) {
		writeBLOBsMapper();
		return batchUpdate(MbgMapperMethods.updateByExampleWithBLOBs, modelExample);
	}

	@Override
	public int[] batchUpdateByPrimaryKeyWithBLOBs(ModelWithBLOBs[] record) {
		writeBLOBsMapper();
		isNoKeyCheck();
		return batchUpdate(MbgMapperMethods.updateByPrimaryKeyWithBLOBs, record);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Model> selectByExample(Example example, Integer page, Integer size) {
		if (example == null) {
			try {
				example = (Example) exampleClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (example instanceof PaginationAble) {
			PaginationAble temp = (PaginationAble) example;

			if ("Oracle".equalsIgnoreCase(temp.getDatabaseType())) {
				temp.setLimitStart((long) (page - 1) * size);
				temp.setLimitEnd((long) page * size);
			} else {
				temp.setLimitStart((long) (page - 1) * size);
				temp.setLimitEnd((long) size);
			}

			List<Model> list = selectByExample(example);
			temp.setLimitStart(null);
			temp.setOrderByClause(null);
			long count = countByExample(example);
			return new Page<Model>(list, count, page, size);
		} else {
			throw new MbgExampleException("Example must PaginationAble");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<ModelWithBLOBs> selectByExampleWithBLOBs(Example example, Integer page, Integer size) {
		if (example == null) {
			try {
				example = (Example) exampleClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (example instanceof PaginationAble) {
			PaginationAble temp = (PaginationAble) example;

			if ("Oracle".equalsIgnoreCase(temp.getDatabaseType())) {
				temp.setLimitStart((long) (page - 1) * size);
				temp.setLimitEnd((long) page * size);
			} else {
				temp.setLimitStart((long) (page - 1) * size);
				temp.setLimitEnd((long) size);
			}

			List<ModelWithBLOBs> list = selectByExampleWithBLOBs(example);
			temp.setLimitStart(null);
			temp.setOrderByClause(null);
			long count = countByExample(example);
			return new Page<ModelWithBLOBs>(list, count, page, size);
		} else {
			throw new MbgExampleException("Example must PaginationAble");
		}
	}

	@Override
	public T getMapper() {
		return mapper;
	}

	public void setMapper(T mapper) {
		this.mapper = mapper;
	}

}
