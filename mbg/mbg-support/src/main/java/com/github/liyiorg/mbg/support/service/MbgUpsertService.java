package com.github.liyiorg.mbg.support.service;

/**
 * 
 * @author LiYi
 *
 * @param <PrimaryKey> PrimaryKey
 * @param <Model> Model
 * @param <ModelWithBLOBs> ModelWithBLOBs
 * @param <Example> Example
 */
public interface MbgUpsertService<PrimaryKey, Model, ModelWithBLOBs, Example> extends MbgService<PrimaryKey, Model, ModelWithBLOBs, Example> {

	int upsert(ModelWithBLOBs record);

	int[] batchUpsert(ModelWithBLOBs[] record);

	int upsertSelective(ModelWithBLOBs record);

	int[] batchUpsertSelective(ModelWithBLOBs[] record);

}
