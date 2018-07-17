package com.aobei.trainapi.configuration.security.oauth2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.aobei.trainapi.configuration.CustomProperties;

@Configuration
public class JWTConfiguration {
	
	@Autowired
	private CustomProperties customProperties;

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TokenEnhancerChain tokenEnhancerChain(){
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		List tokenEnhancerList = new ArrayList();
        tokenEnhancerList.add(tokenEnhancer());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancerList);
        return tokenEnhancerChain;
    }

    @Bean
    public JwtAccessTokenConverter tokenEnhancer(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setVerifierKey(customProperties.getOauth2key());
        return jwtAccessTokenConverter;
    }

    @Bean
    public ResourceServerTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain());
        return defaultTokenServices;
    }
}