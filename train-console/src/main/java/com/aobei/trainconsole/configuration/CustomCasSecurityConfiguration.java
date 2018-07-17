package com.aobei.trainconsole.configuration;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromContextPath;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aobei.train.model.AuthRes;
import com.aobei.train.service.AuthResService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.kakawait.spring.boot.security.cas.CasAuthenticationFilterConfigurer;
import com.kakawait.spring.boot.security.cas.CasAuthenticationProviderSecurityBuilder;
import com.kakawait.spring.boot.security.cas.CasSecurityConfigurerAdapter;
import com.kakawait.spring.boot.security.cas.CasSecurityProperties;
import com.kakawait.spring.boot.security.cas.CasSecurityProperties.ServiceResolutionMode;
import com.kakawait.spring.boot.security.cas.CasSingleSignOutFilterConfigurer;
import com.kakawait.spring.boot.security.cas.CasTicketValidatorBuilder;


@Configuration
class CustomCasSecurityConfiguration extends CasSecurityConfigurerAdapter {

    @Autowired
    private CasSecurityProperties casSecurityProperties;

    @Autowired
    AuthResService authResService;
    @Override
    public void configure(CasAuthenticationFilterConfigurer filter) {
        // Here you can configure CasAuthenticationFilter
    }

    @Override
    public void configure(CasSingleSignOutFilterConfigurer filter) {
        // Here you can configure SingleSignOutFilter
    }

    @Override
    public void configure(CasAuthenticationProviderSecurityBuilder provider) {
        // Here you can configure CasAuthenticationProvider
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Here you can configure Spring Security HttpSecurity object during
        // init configure

        List<AuthRes> list = authResService.selectByExample(null);


        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        for (AuthRes res : list) {
            String urls = res.getUrls();
            if(urls==null){
                continue;
            }
            String[] urlsArr = urls.split(",");
            if(urlsArr==null || urlsArr.length==0){
                continue;
            }
            if (res.getRole_key()==null||res.getRole_key()==""){
                continue;
            }
            String role_key = res.getRole_key().replace("ROLE_","");
            registry
                    .antMatchers(urlsArr)
                    .hasRole(role_key);
            System.out.println(role_key);
        }
        registry.antMatchers("/qimo/**","/fonts/**","/web/**").permitAll();
        registry.and()
                .headers().frameOptions().sameOrigin()
                .and()
                .logout()
                .logoutSuccessHandler(casLogoutSuccessHandler(casSecurityProperties, new ServiceProperties()));


        /**

        http
                .authorizeRequests()
                .antMatchers(
                        "/bannarmanager/**",
                        "/classroommanager/**",
                        "/contentmanager/**",
                        "/courseTeam/**",
                        "/customermanager/**",
                        "/planmanager/**",
                        "/questionBankManager/**",
                        "/schoolmanager/**",
                        "/scoreInfoManager/**",
                        "/teacherManager/**",
                        "/videomanager/**",
                        "/ordermanager/**"
                ).hasRole("TMANAGER")    //培训经理
                .antMatchers(
                        "/categoryParent/**",
                        "/categorySun/**",
                        "/ueditorUpload"
                ).hasRole("ZLJC")        //质量监控
                .antMatchers(
                        "/partner/**",
                        "/student/**"
                ).hasRole("PARTNER")
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .logout()
                .logoutSuccessHandler(casLogoutSuccessHandler(casSecurityProperties, new ServiceProperties()));
            **/
    }

    @Override
    public void configure(CasTicketValidatorBuilder ticketValidator) {
        // Here you can configure CasTicketValidator
    }

    @Bean
    LogoutSuccessHandler casLogoutSuccessHandler(CasSecurityProperties casSecurityProperties,
                                                 ServiceProperties serviceProperties) {
        return new CasLogoutSuccessHandler(casSecurityProperties, serviceProperties);
    }

    private static class CasLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
            implements LogoutSuccessHandler {

        private static final Logger logger = LoggerFactory.getLogger(CasLogoutSuccessHandler.class);

        private final CasSecurityProperties casSecurityProperties;

        private final ServiceProperties serviceProperties;

        public CasLogoutSuccessHandler(CasSecurityProperties casSecurityProperties,
                                       ServiceProperties serviceProperties) {
            this.casSecurityProperties = casSecurityProperties;
            this.serviceProperties = serviceProperties;
        }

        @Override
        public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    Authentication authentication) throws IOException, ServletException {
            super.handle(httpServletRequest, httpServletResponse, authentication);
        }

        @Override
        protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(casSecurityProperties.getServer().getBaseUrl())
                    .path(casSecurityProperties.getServer().getPaths().getLogout());

            String service;
            if (casSecurityProperties.getService().getResolutionMode() == ServiceResolutionMode.DYNAMIC) {
                service = fromContextPath(request).build().toUriString();
            } else {
                service = casSecurityProperties.getService().getBaseUrl().toASCIIString();
            }

            if (service != null) {
                try {
                    builder.queryParam(serviceProperties.getServiceParameter(), encode(service, UTF_8.toString()));
                } catch (UnsupportedEncodingException e) {
                    logger.warn("Unable to encode service url", e);
                }
            }
            return builder.build().toUriString();
        }
    }
}
