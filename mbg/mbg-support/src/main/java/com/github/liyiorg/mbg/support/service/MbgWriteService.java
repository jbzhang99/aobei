package com.github.liyiorg.mbg.support.service;

import com.github.liyiorg.mbg.bean.ModelExample;

/**
 * 
 * @author LiYi
 *
 * @param <PrimaryKey> PrimaryKey
 * @param <Model> Model
 * @param <ModelWithBLOBs> ModelWithBLOBs
 * @param <Example> Example
 */
public interface MbgWriteService<PrimaryKey, Model, ModelWithBLOBs, Example> extends MbgService<PrimaryKey, Model, ModelWithBLOBs, Example> {

	int deleteByExample(Example example);

	int[] batchDeleteByExample(Example[] example);

	int deleteByPrimaryKey(PrimaryKey id);

	int[] batchDeleteByPrimaryKey(PrimaryKey[] id);

	int insert(ModelWithBLOBs record);

	int[] batchInsert(ModelWithBLOBs[] record);
	
	int insertSelective(ModelWithBLOBs record);

	int[] batchInsertSelective(ModelWithBLOBs[] record);
	
	int updateByExampleSelective(Model record, Example example);

	int[] batchUpdateByExampleSelective(ModelExample<Model, Example>[] modelExample);

	int updateByExample(Model record, Example example);

	int[] batchUpdateByExample(ModelExample<Model, Example>[] modelExample);

	int updateByPrimaryKeySelective(Model record);

	int[] batchUpdateByPrimaryKeySelective(Model[] record);

	int updateByPrimaryKey(Model record);

	int[] batchUpdateByPrimaryKey(Model[] record);

}
