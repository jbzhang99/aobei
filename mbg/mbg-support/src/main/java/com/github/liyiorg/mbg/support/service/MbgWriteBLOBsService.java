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
public interface MbgWriteBLOBsService<PrimaryKey, Model, ModelWithBLOBs, Example>
		extends MbgWriteService<PrimaryKey, Model, ModelWithBLOBs, Example> {

	int updateByExampleWithBLOBs(ModelWithBLOBs record, Example example);

	int[] batchUpdateByExampleWithBLOBs(ModelExample<ModelWithBLOBs, Example>[] modelExample);

	int updateByPrimaryKeyWithBLOBs(ModelWithBLOBs record);

	int[] batchUpdateByPrimaryKeyWithBLOBs(ModelWithBLOBs[] record);

}
