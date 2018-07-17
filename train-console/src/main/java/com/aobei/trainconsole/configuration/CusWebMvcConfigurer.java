package com.aobei.trainconsole.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 自定义 web 配置
 * @author liyi
 *
 */
@Configuration
public class CusWebMvcConfigurer {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Bean
	public WebMvcConfigurer configWebMvcConfigurer() {
		return new WebMvcConfigurerAdapter() {

			// 添加拦截器
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(new MtsHandlerInterceptor(redisTemplate)).addPathPatterns("/**");
			}

			// 添加 view Controller
			@Override
			public void addViewControllers(ViewControllerRegistry registry) {
				registry.addViewController("/repeat_request").setViewName("repeat_request");
			}

		};
	}

}
