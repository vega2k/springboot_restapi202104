package myboot.vega2k.restapi.accounts;

import java.util.Collection;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AccountAdapter extends User {
	private Account account;

	public AccountAdapter(Account account) {
		super(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
		this.account = account;
	}

	private static Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
		// Stream<AccountRole> -> 
		// Stream<SimpleGrantedAuthority<AccountRole>> ->
		// Collection<GrantedAuthority>>		
		return roles.stream()
				    .map(role -> new SimpleGrantedAuthority(role.name()))
				    .collect(toSet());
	}

	public Account getAccount() {
		return account;
	}
}