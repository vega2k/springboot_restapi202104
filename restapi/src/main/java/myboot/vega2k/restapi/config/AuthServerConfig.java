package myboot.vega2k.restapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import myboot.vega2k.restapi.accounts.AccountService;
import myboot.vega2k.restapi.common.AppProperties;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AppProperties appProperties;

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	AccountService accountService;
	@Autowired
	TokenStore tokenStore;

	// Authorization Server의 EndPoint 설정
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)
				 .userDetailsService(accountService)
				 .tokenStore(tokenStore);
	}

	// OAuth2 인증 서버 보안(Password) 정보를 설정
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.passwordEncoder(passwordEncoder);
	}

	/*
	 * Access Token에 대한 정보 설정 inMemory()는 클라이언트 정보를 메모리에 저장한다. withClient()는
	 * client_id 값을 설정한다. secret()은 client_secret 값을 설정한다. authorizedGrantTypes()는
	 * grant_type 값을 설정 password, refresh_token accessTokenValiditySeconds() :
	 * Access Token 만료 시간, 600초 10분 refreshTokenValiditySeconds() : Refresh Token 만료
	 * 시간 , 3600초 1시간
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient(appProperties.getClientId())
				.secret(this.passwordEncoder.encode(appProperties.getClientSecret()))
				.authorizedGrantTypes("password", "refresh_token").scopes("read", "write")
				.accessTokenValiditySeconds(10 * 60).refreshTokenValiditySeconds(6 * 10 * 60);
	}

}