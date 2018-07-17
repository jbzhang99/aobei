package com.aobei.authserver.configuration.security.oauth2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;

import com.aobei.authserver.configuration.security.LoginInterceptor;
import com.aobei.authserver.configuration.security.oauth2.tokengranter.smscode.SmsAuthorizationCodeServices;
import com.aobei.authserver.configuration.security.oauth2.tokengranter.smscode.SmsCodeTokenGranter;
import com.aobei.authserver.configuration.security.oauth2.tokengranter.wxacode.WxaAuthorizationCodeServices;
import com.aobei.authserver.configuration.security.oauth2.tokengranter.wxacode.WxaCodeTokenGranter;
import com.aobei.authserver.configuration.security.oauth2.tokengranter.wxmcode.WxmAuthorizationCodeServices;
import com.aobei.authserver.configuration.security.oauth2.tokengranter.wxmcode.WxmCodeTokenGranter;
import com.aobei.authserver.configuration.security.userdetails.CustomUserDetailsService;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
	
	@Value("#{${accessTokenValiditySeconds}}")
	private int accessTokenValiditySeconds;
	
	@Value("#{${refreshTokenValiditySeconds}}")
	public int refreshTokenValiditySeconds;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JWTConfiguration jwtConfiguration;
	
	@Autowired
	private LoginInterceptor loginInterceptor;

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private WxmAuthorizationCodeServices wxmAuthorizationCodeServices;
	
	@Autowired
	private WxaAuthorizationCodeServices wxaAuthorizationCodeServices;
	
	@Autowired
	private SmsAuthorizationCodeServices smsAuthorizationCodeServices;
	
	@Autowired
	private Environment env;
	
	private boolean testEnv;
	
	@PostConstruct
	public void initEnv(){
		testEnv = env.acceptsProfiles("test") || env.acceptsProfiles("dev");
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()")
			.allowFormAuthenticationForClients();
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//grant types["password", "authorization_code", "client_credentials", "refresh_token", "implicit"]
		String[] grantTypes = {"password", "refresh_token", "client_credentials", "wxm_code", "wxa_code", "sms_code"};
		clients.inMemory()
				.withClient("wx_m_student")
				.secret("7c08e27e-b64f-4518-9c69-579508196813")
				.authorizedGrantTypes(grantTypes)
				.scopes("read", "write", "student")
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds)
				.and()
				.withClient("wx_m_teacher")
				.secret("4891e747-2a9a-4b68-b359-56c2a7596478")
				.authorizedGrantTypes(grantTypes)
				.scopes("read", "write", "teacher")
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds)
				.and()
				.withClient("wx_m_partner")
				.secret("4xgnb74e-6a7m-br6v-ptv9-qzciok566g3w")
				.authorizedGrantTypes(grantTypes)
				.scopes("read", "write", "partner")
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds)
				.and()
				.withClient("wx_m_custom")
				.secret("4x91b74e-3b7a-bb6x-btv9-qzcio7jk6g7f")
				.authorizedGrantTypes(grantTypes)
				.scopes("read", "write", "custom")
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds)
				.and()
				.withClient("i_custom")
				.secret("daa16051-e31b-4c26-82bd-cf3aeb3b6945")
				.authorizedGrantTypes(grantTypes)
				.scopes("read", "write", "custom")
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds)
				.and()
				.withClient("a_custom")
				.secret("d9fadfeb-b248-4c3e-852f-076a8ed36354")
				.authorizedGrantTypes(grantTypes)
				.scopes("read", "write", "custom")
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds)
				.and()
				.withClient("h5_custom")
				.secret("21b425cf-14a5-fa57-b157-5efeda215e11")
				.authorizedGrantTypes(new String[]{"password", "client_credentials", "sms_code"})
				.scopes("read", "write", "custom")
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.and()
				.withClient("i_student")
				.secret("57c4a1d0-6b10-4e86-a041-2f5e1e2f0487")
				.authorizedGrantTypes(grantTypes)
				.scopes("read", "write", "student")
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds)
				.and()
				.withClient("a_student")
				.secret("f4657113-6667-4b5b-b8d8-0e05e7273921")
				.authorizedGrantTypes(grantTypes)
				.scopes("read", "write", "student")
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds)
				.and()
				.withClient("i_partner")
				.secret("53238a41-6bc5-4901-b2bc-bbd595702b16")
				.authorizedGrantTypes(grantTypes)
				.scopes("read", "write", "partner")
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds)
				.and()
				.withClient("a_partner")
				.secret("98396d5c-8bd3-4a9f-808a-08f58d383f87")
				.authorizedGrantTypes(grantTypes)
				.scopes("read", "write", "partner")
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds);
	}
	
	private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
		List<TokenGranter> granters = new ArrayList<TokenGranter>(Arrays.asList(endpoints.getTokenGranter()));
		//wxm_code
		WxmCodeTokenGranter wxmCodeTokenGranter = new WxmCodeTokenGranter(endpoints.getTokenServices(),endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());
		wxmCodeTokenGranter.setWxmAuthorizationCodeServices(wxmAuthorizationCodeServices);
		granters.add(wxmCodeTokenGranter);
		//wxa_code
		WxaCodeTokenGranter wxaCodeTokenGranter = new WxaCodeTokenGranter(endpoints.getTokenServices(),endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());
		wxaCodeTokenGranter.setWxaAuthorizationCodeServices(wxaAuthorizationCodeServices);
		granters.add(wxaCodeTokenGranter);
		//sms_code
		SmsCodeTokenGranter smsCodeTokenGranter = new SmsCodeTokenGranter(endpoints.getTokenServices(),endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());
		smsCodeTokenGranter.setSmsAuthorizationCodeServices(smsAuthorizationCodeServices);
		smsCodeTokenGranter.setTestEnv(testEnv);
		granters.add(smsCodeTokenGranter);
		
		return new CompositeTokenGranter(granters);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenServices(jwtConfiguration.defaultTokenServices())
			.userDetailsService(userDetailsService)
			.authenticationManager(authenticationManager)
			//日志拦截记录
			.addInterceptor(loginInterceptor)
			//自定义grant type
			.tokenGranter(tokenGranter(endpoints))
			//自定义错误处理
			.exceptionTranslator(new CustomWebResponseExceptionTranslator());
	}
	
}
