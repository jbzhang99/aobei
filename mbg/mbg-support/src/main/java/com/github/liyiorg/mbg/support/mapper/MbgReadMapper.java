package com.github.liyiorg.mbg.support.mapper;

import java.util.List;

/**
 * 
 * @author LiYi
 *
 * @param <PrimaryKey> PrimaryKey
 * @param <Model> Model
 * @param <ModelWithBLOBs> ModelWithBLOBs
 * @param <Example> Example
 */
public interface MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example> extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example> {

	long countByExample(Example example);

	List<Model> selectByExample(Example example);

	ModelWithBLOBs selectByPrimaryKey(PrimaryKey id);
}
