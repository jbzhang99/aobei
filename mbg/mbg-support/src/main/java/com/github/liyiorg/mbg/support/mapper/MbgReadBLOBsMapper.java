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
public interface MbgReadBLOBsMapper<PrimaryKey, Model, ModelWithBLOBs, Example> extends MbgReadMapper<PrimaryKey, Model, ModelWithBLOBs, Example> {

	List<ModelWithBLOBs> selectByExampleWithBLOBs(Example example);
}
