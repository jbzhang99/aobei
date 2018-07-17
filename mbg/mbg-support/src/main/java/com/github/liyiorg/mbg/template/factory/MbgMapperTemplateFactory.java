package com.github.liyiorg.mbg.template.factory;

import java.util.HashMap;
import java.util.Map;

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
public abstract class MbgMapperTemplateFactory {

	private Map<Class<?>, MbgMapperTemplate<?, ?, ?, ?, ?>> map = new HashMap<>();

	protected abstract <T extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example> MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> createMbgMapperTemplate(
			Class<T> type);

	protected abstract <T extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example> MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> createMbgMapperTemplate(
			T mapper);

	@SuppressWarnings("unchecked")
	public <T extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example> MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> getMbgMapperTemplate(
			Class<T> type) {
		if (!map.containsKey(type)) {
			MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> template = createMbgMapperTemplate(type);
			map.put(type, template);
			return template;
		}
		return (MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example>) map.get(type);
	}

	@SuppressWarnings("unchecked")
	public <T extends MbgMapper<PrimaryKey, Model, ModelWithBLOBs, Example>, PrimaryKey, Model, ModelWithBLOBs, Example> MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> getMbgMapperTemplate(
			T mapper) {
		if (!map.containsKey(mapper.getClass())) {
			MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example> template = createMbgMapperTemplate(mapper);
			map.put(mapper.getClass(), template);
			return template;
		}
		return (MbgMapperTemplate<T, PrimaryKey, Model, ModelWithBLOBs, Example>) map.get(mapper);
	}

}
