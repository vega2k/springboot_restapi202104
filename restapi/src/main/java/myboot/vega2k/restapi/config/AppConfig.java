package myboot.vega2k.restapi.config;

import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import myboot.vega2k.restapi.accounts.Account;
import myboot.vega2k.restapi.accounts.AccountRole;
import myboot.vega2k.restapi.accounts.AccountService;
import myboot.vega2k.restapi.common.AppProperties;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {
			@Autowired
			AccountService accountService;

			@Autowired
			AppProperties appProperties;
			
			@Override
			public void run(ApplicationArguments args) throws Exception {
				Account account = Account.builder()
						.email(appProperties.getUserUsername())
						.password(appProperties.getUserPassword())
						.roles(Set.of(AccountRole.USER))
						.build();
				accountService.saveAccount(account);
				
				Account accountAdmin = Account.builder()
						.email(appProperties.getAdminUsername())
						.password(appProperties.getAdminPassword())
						.roles(Set.of(AccountRole.USER, AccountRole.ADMIN))
						.build();
				accountService.saveAccount(accountAdmin);
				
			}
		};
	}
}
