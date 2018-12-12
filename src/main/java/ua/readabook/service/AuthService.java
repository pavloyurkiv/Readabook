package ua.readabook.service;

import ua.readabook.domain.SigninRequest;
import ua.readabook.domain.SignupRequest;

public interface AuthService {

	void signup(SignupRequest request);
	
	String signin(SigninRequest request);
}
