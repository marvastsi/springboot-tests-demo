package com.marvastsi.demo.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AuthenticationUtils
 */
@Component
public class AuthenticationUtils {

	@Autowired
	private AuthenticationService authService;

	/**
	 * Perform user authentication, based on login and password.
	 * 
	 * @param login A {@link LoginDTO} object with the user credentials
	 * @return A {@link Token} object with the user authentication JWT token.
	 */
	public Token authenticate(LoginDTO login) {
		Optional<Token> token = authService.login(login);
		if (token.isEmpty()) {
			throw new UnauthorizedException(
					String.format("Unauthorized! You don't have permission: %s", login.getLogin()));
		}
		return token.get();
	}

}