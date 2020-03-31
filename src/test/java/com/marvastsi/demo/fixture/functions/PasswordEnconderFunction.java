package com.marvastsi.demo.fixture.functions;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.six2six.fixturefactory.function.AtomicFunction;

/**
 * PasswordEncondeFunction
 */
public class PasswordEnconderFunction implements AtomicFunction {

	private final BCryptPasswordEncoder bcrypt;
	private final String stringPassword;

	public PasswordEnconderFunction(final String stringPassword) {
		this.bcrypt = new BCryptPasswordEncoder();
		this.stringPassword = stringPassword;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T generateValue() {
		return (T) this.bcrypt.encode(this.stringPassword);
	}

}