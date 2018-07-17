package com.aobei.trainconsole.util;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class JacksonUtil {

	/**
	 * 任意对象转json字符串
	 * @throws JsonProcessingException 
	 */
	public static String object_to_json(Object object) throws JsonProcessingException {
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.registerModule(simpleModule);
		
		return mapper.writeValueAsString(object);
	}
	
	/**
	 * json字符串转泛型对象
	 * @throws IOException
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static <T> T json_to_object(String json, Class<T> mainClass, Class<?>... parametricClasses) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JavaType parametricType = mapper.getTypeFactory().constructParametricType(mainClass, parametricClasses);
		T readValue = mapper.readValue(json, parametricType);
		return readValue;
	}
	
}
