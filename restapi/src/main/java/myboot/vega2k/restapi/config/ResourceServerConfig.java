package myboot.vega2k.restapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	private static final String[] AUTH_LIST = { 
			 "/v2/api-docs", 
			 "/configuration/ui", 
			 "/swagger-resources/**", 
			 "/configuration/security", 
			 "/swagger-ui.html", 
			 "/swagger-ui/**",
			 "/webjars/**" 
	};
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId("event");
	}
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.anonymous()
				.and()
			.authorizeRequests()
				.mvcMatchers(HttpMethod.GET, "/api/**").permitAll()
				.antMatchers(AUTH_LIST).permitAll()
				.anyRequest().authenticated()
				.and()
			.exceptionHandling()
				.accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
}