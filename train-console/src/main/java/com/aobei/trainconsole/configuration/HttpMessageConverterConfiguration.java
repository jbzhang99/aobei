package com.aobei.trainconsole.configuration;

import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Configuration
public class HttpMessageConverterConfiguration {

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
}
