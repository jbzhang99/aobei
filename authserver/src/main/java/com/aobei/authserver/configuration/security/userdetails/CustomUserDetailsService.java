package com.aobei.authserver.configuration.security.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aobei.authserver.configuration.security.LoginInterceptor;
import com.aobei.authserver.model.User;
import com.aobei.authserver.repository.UserRepository;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Bad credentials error");
		}
		LoginInterceptor.storeLoginLog(user.getUser_id());
		// 设置用户数据到密码加密层
		return new CustomUserDetails(user);
	}
}
