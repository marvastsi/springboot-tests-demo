package com.marvastsi.demo.auth;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class Token {

	@Getter
	private String token;
}
