package com.incentro.myservice.application.config;

import com.incentro.myservice.application.config.constant.Oauth2Configurations;
import com.incentro.myservice.application.exception.CustomOauthException;
import com.incentro.myservice.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer          // to enable auth 2.0 authentication server
public class SecurityAuth2Configuration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*@Bean
    @Transactional
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }*/

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(Oauth2Configurations.JWT_SIGNING_KEY);
        return converter;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    @Primary
    @Transactional
    public DefaultTokenServices tokenServices() {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));

        //DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        OAuthTokenServices tokenService = new OAuthTokenServices();
        tokenService.setTokenStore(tokenStore());
        tokenService.setSupportRefreshToken(true);
        tokenService.setAccessTokenValiditySeconds(Oauth2Configurations.TOKEN_VALIDITY_SECONDS);
        tokenService.setRefreshTokenValiditySeconds(Oauth2Configurations.REFRESH_TOKEN_VALIDITY_SECONDS);
        tokenService.setTokenEnhancer(enhancerChain);

        return tokenService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(Oauth2Configurations.CLIENT_ID)
                .secret(passwordEncoder.encode(Oauth2Configurations.CLIENT_SECRET))
                .scopes("*")                // scope related to resource server
                .authorizedGrantTypes(Oauth2Configurations.CLIENT_AUTH_GRANT_PASSWORD, Oauth2Configurations.CLIENT_AUTH_GRANT_REFRESH_TOKEN);      // grant type
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.userDetailsService(userDetailsService);
        endpoints.tokenServices(tokenServices());
        endpoints.exceptionTranslator(e -> {
            if (e instanceof OAuth2Exception) {
                OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
                return ResponseEntity.status(oAuth2Exception.getHttpErrorCode()).body(new CustomOauthException(oAuth2Exception.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomOauthException(e.getMessage()));
            }
        });
    }

}
