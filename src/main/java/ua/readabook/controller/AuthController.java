package ua.readabook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.readabook.domain.SigninRequest;
import ua.readabook.domain.SigninResponse;
import ua.readabook.domain.SignupRequest;
import ua.readabook.service.AuthService;

@RestController
@RequestMapping("auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@PostMapping("signup")
	public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
		authService.signup(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("signin")
	public ResponseEntity<?> signin(@RequestBody SigninRequest request) {
		String token = authService.signin(request);
		return new ResponseEntity<>(new SigninResponse(token), HttpStatus.OK);
	}
	
}
