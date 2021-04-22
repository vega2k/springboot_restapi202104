package myboot.vega2k.restapi.accounts;

import java.util.Collection;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {
	@Autowired
	AccountRepository accountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//orElseThrow의 아규먼트가 Supplier 타입 
		//Supplier의 추상메서드 T get()
		Account account = accountRepository.findByEmail(username)
							.orElseThrow(() -> new UsernameNotFoundException(username));
		
		return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
	}

	private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
		//Stream<AccountRole> -> Stream<SimpleGrantedAuthority<AccountRole>> -> Collection<GrantedAuthority>>
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(toSet());
	}
}