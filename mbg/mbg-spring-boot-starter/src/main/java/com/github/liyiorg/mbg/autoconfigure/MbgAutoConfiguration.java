package com.github.liyiorg.mbg.autoconfigure;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.liyiorg.mbg.autoconfigure.MbgProperties.InterceptorConfig;
import com.github.liyiorg.mbg.template.factory.MbgMapperTemplateFactory;
import com.github.liyiorg.mbg.template.factory.SpringMbgMapperTemplateFactory;

@Configuration
@EnableConfigurationProperties(MbgProperties.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MbgAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(MbgAutoConfiguration.class);

	private MbgProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public MbgMapperTemplateFactory sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		if (properties != null && properties.getInterceptors() != null) {
			for (InterceptorConfig interceptorConfig : properties.getInterceptors()) {
				try {
					if (interceptorConfig.isEnabled()) {
						Interceptor interceptor = (Interceptor) Class.forName(interceptorConfig.getClassName())
								.newInstance();
						sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
						logger.debug("Added MyBatis Interceptor {}", interceptorConfig.getClassName());
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(
							"Add MyBatis Interceptor error for className " + interceptorConfig.getClassName());
				}
			}
		}
		SpringMbgMapperTemplateFactory springMbgMapperTemplateFactory = new SpringMbgMapperTemplateFactory();
		springMbgMapperTemplateFactory.setSqlSessionFactory(sqlSessionFactory);
		return springMbgMapperTemplateFactory;
	}
}
