package com.aobei.trainapi.util;

import com.aobei.train.model.Category;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JacksonUtil {

    /**
     * 任意对象转json字符串
     *
     * @throws JsonProcessingException
     */
    public static String object_to_json(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(object);
    }

    /**
     * json字符串转泛型对象
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public static <T> T json_to_object(String json, Class<T> mainClass, Class... parametricClasses) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType parametricType = mapper.getTypeFactory().constructParametricType(mainClass, parametricClasses);
        T readValue = mapper.readValue(json, parametricType);
        return readValue;
    }

    public static <T> T json_to_object(String json, Class<T> objectClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, objectClass);
    }

    public static void main(String[] args) throws IOException {
        Category category = new Category();
        category.setName("nihao");
        List<Category> list = new ArrayList<>();
        list.add(category);
        list.add(category);

        String json = JacksonUtil.object_to_json(list);
        List<Category> list2 = JacksonUtil.json_to_object(json, List.class, Category.class);
        for (Category category1 : list2) {

            System.out.println(category1.getName());
        }

    }

}
