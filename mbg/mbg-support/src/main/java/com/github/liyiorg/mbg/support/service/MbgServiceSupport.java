package com.github.liyiorg.mbg.support.service;

import java.util.List;

import com.github.liyiorg.mbg.bean.ModelExample;
import com.github.liyiorg.mbg.bean.Page;
import com.github.liyiorg.mbg.support.mapper.MbgMapper;
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
public abstract class MbgServiceSupport<Mapper extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example>
		implements MbgReadBLOBsService<PrimaryKey, Model, ModelWithBLOBs, Example>,
		MbgWriteBLOBsService<PrimaryKey, Model, ModelWithBLOBs, Example>,
		MbgUpsertService<PrimaryKey, Model, ModelWithBLOBs, Example> {

	protected MbgMapperTemplate<Mapper, PrimaryKey, Model, ModelWithBLOBs, Example> mbgMapperTemplate;

	@Override
	public long countByExample(Example example) {
		return mbgMapperTemplate.countByExample(example);
	}

	@Override
	public int deleteByExample(Example example) {
		return mbgMapperTemplate.deleteByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(PrimaryKey id) {
		return mbgMapperTemplate.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ModelWithBLOBs record) {
		return mbgMapperTemplate.insert(record);
	}

	@Override
	public int insertSelective(ModelWithBLOBs record) {
		return mbgMapperTemplate.insertSelective(record);
	}

	@Override
	public int upsert(ModelWithBLOBs record) {
		return mbgMapperTemplate.upsert(record);
	}

	@Override
	public int upsertSelective(ModelWithBLOBs record) {
		return mbgMapperTemplate.upsert(record);
	}

	@Override
	public List<ModelWithBLOBs> selectByExampleWithBLOBs(Example example) {
		return mbgMapperTemplate.selectByExampleWithBLOBs(example);
	}

	@Override
	public List<Model> selectByExample(Example example) {
		return mbgMapperTemplate.selectByExample(example);
	}

	@Override
	public ModelWithBLOBs selectByPrimaryKey(PrimaryKey id) {
		return mbgMapperTemplate.selectByPrimaryKey(id);
	}

	@Override
	public int updateByExampleSelective(Model record, Example example) {
		return mbgMapperTemplate.updateByExampleSelective(record, example);
	}

	@Override
	public int updateByExampleWithBLOBs(ModelWithBLOBs record, Example example) {
		return mbgMapperTemplate.updateByExampleWithBLOBs(record, example);
	}

	@Override
	public int updateByExample(Model record, Example example) {
		return mbgMapperTemplate.updateByExample(record, example);
	}

	@Override
	public int updateByPrimaryKeySelective(Model record) {
		return mbgMapperTemplate.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(ModelWithBLOBs record) {
		return mbgMapperTemplate.updateByPrimaryKeyWithBLOBs(record);
	}

	@Override
	public int updateByPrimaryKey(Model record) {
		return mbgMapperTemplate.updateByPrimaryKey(record);
	}

	@Override
	public int[] batchDeleteByExample(Example[] example) {
		return mbgMapperTemplate.batchDeleteByExample(example);
	}

	@Override
	public int[] batchDeleteByPrimaryKey(PrimaryKey[] id) {
		return mbgMapperTemplate.batchDeleteByPrimaryKey(id);
	}

	@Override
	public int[] batchInsert(ModelWithBLOBs[] record) {
		return mbgMapperTemplate.batchInsert(record);
	}

	@Override
	public int[] batchInsertSelective(ModelWithBLOBs[] record) {
		return mbgMapperTemplate.batchInsertSelective(record);
	}

	@Override
	public int[] batchUpsert(ModelWithBLOBs[] record) {
		return mbgMapperTemplate.batchUpsert(record);
	}

	@Override
	public int[] batchUpsertSelective(ModelWithBLOBs[] record) {
		return mbgMapperTemplate.batchUpsertSelective(record);
	}

	@Override
	public int[] batchUpdateByExampleSelective(ModelExample<Model, Example>[] modelExample) {
		return mbgMapperTemplate.batchUpdateByExampleSelective(modelExample);
	}

	@Override
	public int[] batchUpdateByExample(ModelExample<Model, Example>[] modelExample) {
		return mbgMapperTemplate.batchUpdateByExample(modelExample);
	}

	@Override
	public int[] batchUpdateByPrimaryKeySelective(Model[] record) {
		return mbgMapperTemplate.batchUpdateByPrimaryKeySelective(record);
	}

	@Override
	public int[] batchUpdateByPrimaryKey(Model[] record) {
		return mbgMapperTemplate.batchUpdateByPrimaryKey(record);
	}

	@Override
	public int[] batchUpdateByExampleWithBLOBs(ModelExample<ModelWithBLOBs, Example>[] modelExample) {
		return mbgMapperTemplate.batchUpdateByExampleWithBLOBs(modelExample);
	}

	@Override
	public int[] batchUpdateByPrimaryKeyWithBLOBs(ModelWithBLOBs[] record) {
		return mbgMapperTemplate.batchUpdateByPrimaryKeyWithBLOBs(record);
	}

	@Override
	public Page<Model> selectByExample(Example example, Integer page, Integer size) {
		return mbgMapperTemplate.selectByExample(example, page, size);
	}

	@Override
	public Page<ModelWithBLOBs> selectByExampleWithBLOBs(Example example, Integer page, Integer size) {
		return mbgMapperTemplate.selectByExampleWithBLOBs(example, page, size);
	}

	public MbgMapperTemplate<Mapper, PrimaryKey, Model, ModelWithBLOBs, Example> getMbgMapperTemplate() {
		return mbgMapperTemplate;
	}

	public void setMbgMapperTemplate(
			MbgMapperTemplate<Mapper, PrimaryKey, Model, ModelWithBLOBs, Example> mbgMapperTemplate) {
		this.mbgMapperTemplate = mbgMapperTemplate;
	}

}
