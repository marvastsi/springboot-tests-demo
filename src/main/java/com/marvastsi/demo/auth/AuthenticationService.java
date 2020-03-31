package com.marvastsi.demo.auth;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.marvastsi.demo.exception.NotFoundException;
import com.marvastsi.demo.user.User;
import com.marvastsi.demo.user.UserService;

@Service
@Transactional
public class AuthenticationService {

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Autowired
	private JwtAuthenticator jwtAuthenticator;

	public Optional<Token> login(LoginDTO login) {
		String username = login.getLogin();
		Optional<User> optUser = this.findByLogin(username);
		if (optUser.isEmpty()) {
			throw new NotFoundException("Not found register with login: " + username);
		}

		User user = optUser.get(); 
		if (bcrypt.matches(login.getPassword(), user.getPassword())) {
			return Optional.of(new Token(jwtAuthenticator.encode(user.getUsername(), null)));
		}

		throw new UnauthorizedException("Unauthorized login for username: " + username);
	}

	public Optional<User> findByLogin(String login) {
		return userService.findByLogin(login);
	}

}
