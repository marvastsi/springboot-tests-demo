package com.marvastsi.demo.auth;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public class Token {

	private String token;

	public String getToken() {
		return String.format("Bearer %s", token);
	}
}
