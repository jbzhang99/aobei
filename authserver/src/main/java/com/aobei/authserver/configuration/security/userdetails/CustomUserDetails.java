package com.aobei.authserver.configuration.security.userdetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.aobei.authserver.model.User;

public class CustomUserDetails implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private User user;

	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(user.getRoles() == null){
			return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
		}else{
			String roles = Stream.of(user.getRoles().toUpperCase().split(","))
					.map(n -> n.startsWith("ROLE_")? n : "ROLE_" + n)
					.collect(Collectors.joining(","));
			return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
		}
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if (user.getStatus() == 1)
			return true;
		else
			return false;
	}

	public Long getUserid() {
		return user.getUser_id();
	}

}
