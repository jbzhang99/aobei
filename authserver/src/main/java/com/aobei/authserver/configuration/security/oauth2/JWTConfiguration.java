package com.aobei.authserver.configuration.security.oauth2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.aobei.authserver.configuration.CustomProperties;
import com.aobei.authserver.configuration.security.userdetails.CustomUserDetailsService;

@Configuration
public class JWTConfiguration {
    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Autowired
    private CustomProperties customProperties;

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List tokenEnhancerList = new ArrayList();
        tokenEnhancerList.add(new JWTTokenEnhancer());
        tokenEnhancerList.add(tokenEnhancer());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancerList);
        return tokenEnhancerChain;
    }

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setAccessTokenConverter(defaultAccessTokenConverter());
        jwtAccessTokenConverter.setSigningKey(customProperties.getOauth2key());
        jwtAccessTokenConverter.setVerifierKey(customProperties.getOauth2publicKey());
        return jwtAccessTokenConverter;
    }

    @Bean
    public DefaultAccessTokenConverter defaultAccessTokenConverter() {
        DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();
        DefaultUserAuthenticationConverter userConverter = new DefaultUserAuthenticationConverter();
        userConverter.setUserDetailsService(userDetailsService);
        converter.setUserTokenConverter(userConverter);
        return converter;
    }

    @Bean
    public AuthorizationServerTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain());
        // 允许刷新token
        defaultTokenServices.setSupportRefreshToken(true);
        // 刷新token 时延长过期时间
        defaultTokenServices.setReuseRefreshToken(false);
        return defaultTokenServices;
    }
}

