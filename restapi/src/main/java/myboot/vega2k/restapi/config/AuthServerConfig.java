package myboot.vega2k.restapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
	@Autowired
	PasswordEncoder passwordEncoder;

	// OAuth2 읶증 서버 보안(Password) 정보를 설정
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.passwordEncoder(passwordEncoder);
	}
}