package ua.readabook.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.readabook.config.jwt.JwtTokenProvider;
import ua.readabook.domain.SigninRequest;
import ua.readabook.domain.SignupRequest;
import ua.readabook.entity.RoleEntity;
import ua.readabook.entity.UserEntity;
import ua.readabook.exception.AlreadyExistsException;
import ua.readabook.exception.ResourceNotFoundException;
import ua.readabook.repository.RoleRepository;
import ua.readabook.repository.UserRepository;
import ua.readabook.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public void signup(SignupRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new AlreadyExistsException("User with name " + request.getUsername() + " already exists");
		}

		UserEntity userEntity = new UserEntity();
		userEntity.setUsername(request.getUsername());
		userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

		RoleEntity role = roleRepository.findByName("USER")
				.orElseThrow(() -> new ResourceNotFoundException("Role not found"));

		Set<RoleEntity> roles = new HashSet<>();
		roles.add(role);
		userEntity.setRoles(roles);

		userRepository.save(userEntity);

	}

	@Override
	public String signin(SigninRequest request) {
		Authentication authentication = 
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(
								request.getUsername(), 
								request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtTokenProvider.generateToken(authentication);
		return token;
	}

}
