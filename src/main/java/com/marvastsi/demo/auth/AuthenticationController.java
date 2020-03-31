package com.marvastsi.demo.auth;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marvastsi.demo.annotation.RestConfig;
import com.marvastsi.demo.annotation.RoleUser;
import com.marvastsi.demo.user.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestConfig
@Api(tags = "Authentication")
@RequestMapping("/authentication")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authService;

	@PostMapping("/login")
	@ApiOperation("Performs login of an user.")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO login, @ApiIgnore Errors err) {
		if (!err.hasErrors()) {
			Optional<Token> token = authService.login(login);
			if (token.isPresent()) {
				return ResponseEntity.ok(token.get());
			}
			throw new UnauthorizedException("Unauthorized! You don't have permission.");
		}
		return ResponseEntity
				.badRequest()
				.body(err
						.getAllErrors()
						.stream()
						.map(msg -> msg.getDefaultMessage())
						.collect(Collectors.joining(",")));
	}


	@RoleUser
	@GetMapping("/loggedUser")
	@ApiOperation("Retrieves the current authenticated User.")
	public ResponseEntity<?> getPrincipal() {
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<User> opt = authService.findByLogin(auth.getLogin());
		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		}
		throw new NullPointerException("Authenticated user not found.");
	}

}
