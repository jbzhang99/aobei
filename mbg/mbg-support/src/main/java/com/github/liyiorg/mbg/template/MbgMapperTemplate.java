package com.github.liyiorg.mbg.template;

import java.util.List;

import com.github.liyiorg.mbg.bean.ModelExample;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.mapper.MbgMapper;

/**
 * 
 * @author LiYi
 *
 * @param <T>
 * 			  MbgMapper
 * @param <PrimaryKey>
 *            PrimaryKey
 * @param <Model>
 *            Model
 * @param <ModelWithBLOBs>
 *            ModelWithBLOBs
 * @param <Example>
 *            Example
 */
public interface MbgMapperTemplate<T extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example> {

	long countByExample(Example example);

	int deleteByExample(Example example);

	int deleteByPrimaryKey(PrimaryKey id);

	int insert(ModelWithBLOBs record);

	int insertSelective(ModelWithBLOBs record);

	int upsert(ModelWithBLOBs record);

	int upsertSelective(ModelWithBLOBs record);

	List<ModelWithBLOBs> selectByExampleWithBLOBs(Example example);

	List<Model> selectByExample(Example example);

	ModelWithBLOBs selectByPrimaryKey(PrimaryKey id);

	int updateByExampleSelective(Model record, Example example);

	int updateByExampleWithBLOBs(ModelWithBLOBs record, Example example);

	int updateByExample(Model record, Example example);

	int updateByPrimaryKeySelective(Model record);

	int updateByPrimaryKeyWithBLOBs(ModelWithBLOBs record);

	int updateByPrimaryKey(Model record);

	int[] batchUpdate(String statements, Object[] params);

	int[] batchDeleteByExample(Example[] example);

	int[] batchDeleteByPrimaryKey(PrimaryKey[] id);

	int[] batchInsert(ModelWithBLOBs[] record);

	int[] batchInsertSelective(ModelWithBLOBs[] record);

	int[] batchUpsert(ModelWithBLOBs[] record);

	int[] batchUpsertSelective(ModelWithBLOBs[] record);

	int[] batchUpdateByExampleSelective(ModelExample<Model, Example>[] modelExample);

	int[] batchUpdateByExample(ModelExample<Model, Example>[] modelExample);

	int[] batchUpdateByPrimaryKeySelective(Model[] record);

	int[] batchUpdateByPrimaryKey(Model[] record);

	int[] batchUpdateByExampleWithBLOBs(ModelExample<ModelWithBLOBs, Example>[] modelExample);

	int[] batchUpdateByPrimaryKeyWithBLOBs(ModelWithBLOBs[] record);

	Page<Model> selectByExample(Example example, Integer page, Integer size);

	Page<ModelWithBLOBs> selectByExampleWithBLOBs(Example example, Integer page, Integer size);

	T getMapper();
}
