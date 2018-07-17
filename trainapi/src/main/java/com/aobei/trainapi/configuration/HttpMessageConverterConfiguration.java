package com.aobei.trainapi.configuration;

import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import graphql.servlet.ObjectMapperConfigurer;

/**
 * 返回数据转换
 * 
 * @author liyi
 *
 */
@Configuration
public class HttpMessageConverterConfiguration {

	/**
	 * 配置JSON 返回Long ,long 类型为String,避免JS 中数据超长精度丢失问题
	 * 
	 * @return
	 */
	@Bean
	public HttpMessageConverters customConverters() {
		MappingJackson2HttpMessageConverter additional = new MappingJackson2HttpMessageConverter();
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		additional.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
		additional.getObjectMapper().registerModule(simpleModule);
		return new HttpMessageConverters(additional);
	}

	/**
	 * graphql 返回数据设置
	 * @return
	 */
	@Bean
	public ObjectMapperConfigurer customObjectMapperConfigurer() {
		return new ObjectMapperConfigurer() {

			@Override
			public void configure(ObjectMapper mapper) {
				SimpleModule simpleModule = new SimpleModule();
				simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
				simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
				mapper.setSerializationInclusion(Include.NON_NULL);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				mapper.registerModule(simpleModule);
			}
		};

	}
}
