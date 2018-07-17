package com.aobei.trainapi.configuration.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableResourceServer
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
    @Autowired
    private JWTConfiguration jwtConfiguration;
    
    @Autowired
    private Environment env;


    
    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.tokenServices(jwtConfiguration.defaultTokenServices());
        OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        // 自定义错误处理
        oAuth2AuthenticationEntryPoint.setExceptionTranslator(new CustomWebResponseExceptionTranslator());
        config.authenticationEntryPoint(oAuth2AuthenticationEntryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
    	http
	    	.csrf().disable()
	    	.cors()
	    	.and()
	     	.authorizeRequests()
			.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
    	if(env.acceptsProfiles("pro","uat")){
    		http.authorizeRequests()
    		.antMatchers("/callback/**","/server/time","/monitor/**").permitAll()
    		.anyRequest().authenticated();
    	}else{
    		http.authorizeRequests()
    		.antMatchers("/graphiql","/callback/**","/server/time","/monitor/**","/push").permitAll()
    		.anyRequest().authenticated();
    	}

    }
    
}