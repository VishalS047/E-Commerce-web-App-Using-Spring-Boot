package com.shopme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.User;

public class ShopifyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = this.userRepository.getUserByEmail(email);
		if (user != null) {
			return new ShopifyUserDetails(user);
		}

		throw new UsernameNotFoundException("Could not find user with email: " + email);
	}

}
