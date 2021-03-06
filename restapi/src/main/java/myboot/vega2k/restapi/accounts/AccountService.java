package myboot.vega2k.restapi.accounts;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {
	@Autowired
	AccountRepository accountRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public Account saveAccount(Account account) {
		account.setPassword(this.passwordEncoder.encode(account.getPassword()));
		return this.accountRepository.save(account);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// orElseThrow의 아규먼트가 Supplier 타입
		// Supplier의 추상메서드 T get()
		Account account = accountRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		return new AccountAdapter(account);
	}

}