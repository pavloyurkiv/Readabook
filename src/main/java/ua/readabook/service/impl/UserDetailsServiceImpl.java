package ua.readabook.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.readabook.entity.UserEntity;
import ua.readabook.repository.UserRepository;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity entity = userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User with username [" + username + "] not found")
				);
		
		return User
					.builder()
					.username(entity.getUsername())
					.password(entity.getPassword())
					.authorities(getAuthority(entity))
					.build();
	}
	
	private Set<SimpleGrantedAuthority> getAuthority(UserEntity entity) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		entity.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())); 	// ROLE_USER ROLE_ADMIN
		});
		return authorities;
	}
	
}
